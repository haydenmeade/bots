package com.neck_flexed.scripts.sire;

import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.SpecialAttack;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.NpcListener;
import com.runemate.game.api.script.framework.listeners.PlayerListener;
import com.runemate.game.api.script.framework.listeners.events.HitsplatEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Phase2Fighter")
public class Phase2Fighter implements EngineListener, PlayerListener, NpcListener {
    private final Loadouts loadouts;
    private final Loadout phase2;
    private final Loadout defSpec;
    private final boolean hasDpsSpec;
    private final sireBot bot;
    private final Sire sire;
    private boolean specHit;

    public Phase2Fighter(Loadouts loadouts, sireBot bot) {
        this.loadouts = loadouts;
        this.phase2 = u.getPhase2Loadout(loadouts);
        this.defSpec = u.getDefenseSpecLoadout(loadouts);
        this.hasDpsSpec = u.getSpecDpcLoadout(loadouts) != null;
        this.bot = bot;
        this.sire = bot.sireListener.getSire();
        this.specHit = false;
    }

    @Override
    public void onTickStart() {
        if (done()) return;
        if (this.sire == null) return;
        var sireNpc = sire.getNpc();
        if (sireNpc == null) return;
        if (sireNpc.getId() == sireBot.SireId_Phase2or3_Walk) {
            util.boostIfNeeded(phase2.getStyle(), bot.settings().reboost());
            log.debug("wait for walk");
            return;
        }
        if (sireBot.onMiasmaPool()) {
            util.moveTo(Players.getLocal().getPosition().equals(Phase2.tile1) ? Phase2.tile2 : Phase2.tile1);
        } else {
            if (defSpec != null && SpecialAttack.getEnergy() >= defSpec.getSpecEnergy() && !specHit) {
                loadouts.equip(defSpec);
                util.activateSpec();
            } else {
                loadouts.equip(phase2);
            }
            var target = Players.getLocal().getTarget();
            if (target == null || !target.equals(sire.getNpc()))
                sire.attack();
        }
    }

    @Override
    public void onNpcHitsplat(HitsplatEvent event) {
        // only want to check if no dps spec.
        if (!hasDpsSpec) return;
        if (event.getHitsplat().getDamage() == 0) return;

        if (defSpec != null && defSpec.equals(loadouts.getEquipped())) {
            this.specHit = true;
        }
    }

    public boolean done() {
        var st = this.bot.sireListener.getPhase();
        if (st != null && st != Phase.Phase2) return true;
        if (sire == null) return true;
        var s = sire.getNpc();
        return s == null || s.getId() == sireBot.SireId_Phase3_1;
    }

}
