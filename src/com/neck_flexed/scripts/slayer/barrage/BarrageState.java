package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.neck_flexed.scripts.slayer.state.FightState;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import lombok.extern.log4j.Log4j2;

import java.util.EventListener;
import java.util.Objects;

@Log4j2(topic = "FightState")
public class BarrageState extends BaseState<SlayerState> implements EngineListener {
    private final SlayerBotImpl<?> bot;
    private final BarrageListener barrageListener;
    private final PrayerFlicker prayerFlicker;
    private final NeckSpell spell;
    private final int reboost;
    private final Loadouts loadouts;
    private final SlayerMonster monster;
    private final Prayer[] boostPrayers;
    private final Loadout loadout;
    private int tick = 0;
    private int lastSpellCast = -5;

    public BarrageState(SlayerBotImpl<?> bot,
                        BarrageListener barrageListener,
                        PrayerFlicker prayerFlicker,
                        Loadout loadout,
                        SlayerMonster monster,
                        NeckSpell spell,
                        int reboost
    ) {
        super(bot, SlayerState.BARRAGING);
        this.bot = bot;
        this.loadouts = bot.loadouts;
        this.loadout = loadout;
        this.barrageListener = barrageListener;
        this.monster = monster;
        this.prayerFlicker = prayerFlicker;
        this.spell = spell;
        this.reboost = reboost;
        this.boostPrayers = new Prayer[]{util.getMagicBoostPrayer()};
    }

    private static Magic.Ancient getSpell(NeckSpell spell) {
        if (Health.getCurrentPercent() < 50 && util.hasBloodRunes()) {
            return util.getBloodSpell();
        }
        return spell.getSpell();
    }

    public static int castSpell(NeckSpell spell, Npc npc, Actor target, int tick, int lastSpellCast, SlayerBotImpl<?> bot) {
        var sp = getSpell(spell);
        if (sp == null) return lastSpellCast;
        var isBlood = Objects.equals(util.getBloodSpell(), sp);
        if (tick > 20 && !spell.canCast()) {
            log.debug("Can cast force to restore");
            bot.forceState(SlayerState.RESTORING);
        }
        if (sp.isAutocasting() && !isBlood) {
            if (Objects.equals(target, npc)) return lastSpellCast;
            util.attack(npc);
            return lastSpellCast;
        }

        if (tick >= (lastSpellCast + 5)) {
            log.debug("Spell {} on {}", sp, npc);
            bot.di.sendSpellCastOn(sp, npc);
            return tick;
        }
        return lastSpellCast;
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
        bot.updateStatus("Barraging " + monster);
        this.bot.prayerFlicker.setQuickPrayers(util.getMagicBoostPrayer(), Prayer.PROTECT_FROM_MELEE);
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
        if (monster.isSuperiorPresent(bot)) return false;
        if (!SlayerTask.hasTask()) return true;
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
        return monster.getTask().equals(Task.ANKOU) ? targetingWithHealthUnder35 == 0 : targetingWithHealthUnder35 < 2;
    }

    @Override
    public void onTickStart() {
        try {
            tick++;
            var p = Players.getLocal();
            if (p == null) return;
            var pos = p.getServerPosition();
            if (pos == null) return;
            util.boostIfNeeded(CombatStyle.Magic, reboost);
            if (monster.getTask().equals(Task.NECHRYAEL) && monster.isSuperiorPresent(bot)) {
                handleNechryarch();
                return;
            }
            var bestTarget = this.barrageListener.getBestTarget();
            if (bestTarget == null) return;
            loadouts.equip(loadout);
            lastSpellCast = castSpell(spell, bestTarget, p.getTarget(), tick, lastSpellCast, bot);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private void handleNechryarch() {
        var npc = FightState.setSuperiorTarget(monster.getSuperior(bot));
        if (npc == null) return;
        var p = Players.getLocal();
        if (p == null) return;
        log.debug("nechryarch {}", npc);
        loadouts.equip(loadout);

        Coordinate moveTo1 = new Coordinate(1704, 10081, 0);
        Coordinate moveTo2 = new Coordinate(1697, 10081, 0);
        if (npc.distanceTo(p) < 3 || npc.isMoving()) {
            if (p.getServerPosition().getX() > moveTo1.getX())
                util.moveTo(moveTo1);
            else
                util.moveTo(moveTo2);
            return;
        }

        lastSpellCast = castSpell(spell, npc, p.getTarget(), tick, lastSpellCast, bot);
    }

    @Override
    public void executeLoop() {
    }
}
