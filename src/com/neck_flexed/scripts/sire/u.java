package com.neck_flexed.scripts.sire;


import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.SpecialAttack;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "util")
public class u {

    public static Loadout getPhase1Loadout(Loadouts l) {
        return l.getForName(Phase.Phase1);
    }

    public static Loadout getPhase2Loadout(Loadouts l) {
        return l.getForName(Phase.Phase2);
    }

    public static Loadout getSpecDpcLoadout(Loadouts l) {
        return l.getForRole(Loadout.LoadoutRole.SpecDps);
    }

    public static Loadout getDefenseSpecLoadout(Loadouts l) {
        return l.getForRole(Loadout.LoadoutRole.SpecDefenseReduction);
    }

    public static boolean hasSpec() {
        return SpecialAttack.getEnergy() >= 90;
    }

    public static boolean isRestored() {
        return !Health.isPoisoned() &&
                Health.getCurrentPercent() >= 90 &&
                Prayer.getPoints() > 40 &&
                hasSpec();
    }

    public static boolean needToHop() {
        return Players.getLoaded().size() > 1;
    }

}
