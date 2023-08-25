package com.neck_flexed.scripts.cerberus;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.SpecialAttack;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import lombok.extern.log4j.Log4j2;

import java.util.EventListener;

@Log4j2(topic = "FightState")
public class FightState extends BaseState<CerbState> {
    private final CerbListener cerbListener;
    private final PrayerFlicker prayerFlicker;
    private final cerb bot;
    private final Loadouts loadouts;
    private final WildPieListener wildPieListener;
    private Prayer[] boostPrayers;
    private CombatStyle combatStyle;
    private Loadout spec;
    private boolean done = false;

    public FightState(CerbListener cerbListener,
                      PrayerFlicker prayerFlicker,
                      cerb bot
    ) {
        super(bot, CerbState.FIGHTING);
        this.cerbListener = cerbListener;
        this.prayerFlicker = prayerFlicker;
        this.bot = bot;
        this.loadouts = bot.loadouts;
        this.combatStyle = this.loadouts.getEquipped().getStyle();
        this.boostPrayers = util.getBoostPrayersA(combatStyle);
        this.spec = this.loadouts.getSpecLoadout();
        this.wildPieListener = new WildPieListener(bot, CerbState.RESTORING, 91);
    }

    @Override
    public EventListener[] getEventListeners() {
        if (this.bot.settings().wildPieCount() > 0)
            return new EventListener[]{wildPieListener};
        return super.getEventListeners();
    }

    @Override
    public void deactivate() {
        log.debug("deactivate");
        if (this.cerbListener.isDead()) {
            this.prayerFlicker.disable();
        } else if (this.done) {
            bot.forceState(CerbState.WALK_UNDER);
        }
    }

    @Override
    public void activate() {
        Action.set(Action.None);
        log.debug("activate");
        bot.updateStatus("Fighting");
        this.prayerFlicker.setActivePrayers(this.boostPrayers);
    }

    @Override
    public String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return this.cerbListener.isDead() || done;
    }

    @Override
    public void executeLoop() {
        var s = bot.settings();
        var target = Players.getLocal().getTarget();
        var dog = cerb.getCerb();
        if (util.otherPlayersNearby()
                && s.fightStrategy().equals(FightStrategy.Flinch)
                && (Players.getLocal().getHealthGauge() == null || !Players.getLocal().getHealthGauge().isValid())
        ) {
            this.bot.forceState(CerbState.HOPPING);
            return;
        }
        if (dog == null && !Players.getLocal().isMoving()) {
            log.debug("move to center");
            util.boostIfNeeded(this.combatStyle, bot.settings().reboost());
            util.moveTo(cerb.centerSpawnTile);
            return;
        }
        if (dog == null) return;
        if (dog.getId() == cerb.IdStartNoAttack && !Action.get().equals(Action.Move)) {
            log.debug("initiate");
            util.moveTo(dog.getArea().getCenter());
            util.boostIfNeeded(this.combatStyle, bot.settings().reboost());
            return;
        }

        // flinch method
        if (this.spec != null && SpecialAttack.getEnergy() >= this.spec.getSpecEnergy()) {
            util.activateSpec();
            if (target == null || !Action.get().equals(Action.Spec)) {
                loadouts.equip(spec);
                bot.attack();
                Action.set(Action.Spec);
            }
        } else if (target == null || Action.get() != Action.Attack) {
            loadouts.equip(loadouts.getForRole(Loadout.LoadoutRole.Combat));
            bot.attack();
        }
        done = true;
    }
}
