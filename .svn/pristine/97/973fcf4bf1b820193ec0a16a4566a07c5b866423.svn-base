package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.loot.LootSettings;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.slayer.Location;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.neck_flexed.scripts.slayer.state.FightState;
import com.neck_flexed.scripts.slayer.state.LootState;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import lombok.extern.log4j.Log4j2;

import java.util.EventListener;
import java.util.regex.Pattern;

@Log4j2
public class SmokeDevilBarrageState extends BaseState<SlayerState> implements EngineListener {
    private static final SlayerMonster monster = Monster.getByEnum(Task.SMOKE_DEVILS).get();
    private final SlayerBotImpl<?> bot;
    private final BarrageListener barrageListener;
    private final PrayerFlicker prayerFlicker;
    private final NeckSpell spell;
    private final int reboost;
    private final Loadouts loadouts;
    private final Prayer[] boostPrayers;
    private final Loadout loadout;
    private final Cannon cannon;
    private final Location location;
    private final Pattern[] ignorePlayers;
    private final LootSettings lootSettings;
    private int tick = 0;
    private int lastSpellCast = -5;
    private int lootPeriod = 25;
    private boolean doingCannon;
    private boolean lootAvailable;
    private long lastCheck = 0L;

    public SmokeDevilBarrageState(SlayerBotImpl<?> bot,
                                  BarrageListener barrageListener,
                                  Loadout loadout,
                                  NeckSpell spell,
                                  int reboost,
                                  Pattern[] ignorePlayers,
                                  LootSettings lootSettings
    ) {
        super(bot, SlayerState.BARRAGING);
        this.bot = bot;
        this.cannon = bot.getCannon();
        this.loadouts = bot.loadouts;
        this.loadout = loadout;
        this.barrageListener = barrageListener;
        this.prayerFlicker = bot.prayerFlicker;
        this.spell = spell;
        this.reboost = reboost;
        this.lootSettings = lootSettings;
        this.boostPrayers = new Prayer[]{util.getMagicBoostPrayer()};
        this.location = monster.getLocation(bot);
        this.ignorePlayers = ignorePlayers;
    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{this, barrageListener};
    }

    @Override
    public void deactivate() {
        log.debug("deactivate, targeting: {}", this.barrageListener.getTargetingMe().size());
    }

    @Override
    public void activate() {
        bot.updateStatus("Barraging Smoke Devil");
        this.bot.prayerFlicker.setQuickPrayers(util.getMagicBoostPrayer(), Prayer.PROTECT_FROM_MISSILES);
        bot.setPreviousTask(monster);
        if (util.isAutoRetaliateEnabled())
            util.toggleAutoRetaliate();
    }

    @Override
    public String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        if (lootAvailable) return true;
        if (monster.isSuperiorPresent(bot)) return false;
        if (!SlayerTask.hasTask()) return true;
        if (cannon.isCannonPlaced()) return false;
        var targeting = this.barrageListener.getTargetingMe();
        if (targeting.size() == 0) return true;
        if (targeting.size() > 4) return false;
        var targetingWithHealthUnder35 = (int) targeting.stream().filter(
                n -> {
                    var hp = n.getHealthGauge();
                    if (hp == null) return true;
                    return hp.getPercent() < 45;
                }
        ).count();
        return targetingWithHealthUnder35 < 2;
    }

    private void checkLoot() {
        if (lootSettings != null && System.currentTimeMillis() - lastCheck > 2400) {
            this.lastCheck = System.currentTimeMillis();
            var l = LootState.getAvailableLoot(bot, lootSettings);
            if (l != null) {
                log.debug("Loot available {}", l);
                this.lootAvailable = true;
            }
        }
    }

    @Override
    public void onTickStart() {
        try {
            tick++;
            var p = Players.getLocal();
            if (p == null) return;
            var pos = p.getServerPosition();
            if (pos == null) return;
            if (this.doingCannon) return;
            util.boostIfNeeded(CombatStyle.Magic, reboost);

            var bestTarget = this.barrageListener.getBestTarget();
            if (bestTarget == null) return;

            this.barrageListener.decrementStackCooldown();
            var tiles = SmokeDevilStackingState.SMOKE_STACK_NS;
            var stackIdx = this.barrageListener.getStackIdx();
            if (stackIdx >= tiles.length) {
                stackIdx = 0;
                this.barrageListener.setStackIdx(0);
            }
            var currentTile = tiles[stackIdx];
            if (this.barrageListener.getStackcooldown() > 0) {
            } else {
                this.barrageListener.setStackcooldown(16);
                this.barrageListener.setStackIdx(stackIdx + 1);
                return;
            }

            loadouts.equip(loadout);

            lastSpellCast = BarrageState.castSpell(spell, bestTarget, p.getTarget(), tick, lastSpellCast, bot);
            if (lastSpellCast != tick) {
                util.moveTo(currentTile);
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }


    @Override
    public void executeLoop() {
        if (this.cannon != null && Cannon.hasCannon() && location.getCannonSpot() != null) {
            this.doingCannon = FightState.doCannonLogic(cannon, bot, location.getCannonSpot(), Players.getLocal().getServerPosition(), location.getArea(), ignorePlayers, monster.isHoppingDisabled());
        }
        checkLoot();
        if (!Traversal.isRunEnabled() && Traversal.getRunEnergy() > 2)
            Traversal.toggleRun();
    }
}
