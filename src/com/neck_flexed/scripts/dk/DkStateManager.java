package com.neck_flexed.scripts.dk;

import com.neck_flexed.scripts.common.state.StateManager;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.osrs.local.SpecialAttack;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DkStateManager extends StateManager<dk, DkState> {
    private final DkListener dkListener;

    public DkStateManager(dk dk, DkListener dkListener) {
        super(dk);
        this.dkListener = dkListener;
    }

    @Override
    public DkState getNextState(DkState initiator) {
        switch (initiator) {
            case OFF_TICK:
                return DkState.FIGHTING;
            case FIGHTING:
                if (bot.isSlayerTaskDone()) {
                    log.debug("deactivate finished slayer task");
                    return DkState.LOOTING;
                }
                if (bot.breakManager.breaking() || bot.breakManager.stopBot()) {
                    log.debug("deactivate breaking");
                    return DkState.LOOTING;
                }
                if (!dkListener.needToOffTick().isEmpty()) {
                    log.debug("deactivate offtick");
                    return DkState.OFF_TICK;
                }
                if (King.getNearestKing() == null) {
                    log.debug("deactivate no king");
                    return DkState.LOOTING;
                }
                log.debug("deactivate other");
                break;
            case LOOTING:
                if (bot.isSlayerTaskDone()) {
                    log.debug("deactivate no slayer task");
                    return DkState.RESTORING;
                }
                if (bot.breakManager.breaking() || bot.breakManager.stopBot()) {
                    log.debug("deactivate breaking");
                    return DkState.RESTORING;
                }
                return DkState.FIGHTING;
            case RESTORING:
                log.debug("restore");
                return DkState.RESTOCKING;
        }


        if (dkAreas.inBossRoom()) {
            return DkState.FIGHTING;
        }
        if (dkAreas.inPeekArea())
            return DkState.ENTERING_LAIR;

        if (bot.isSlayerTaskDone()) {
            bot.updateStatus("Finished slayer task");
            bot.stop("Finished slayer task");
        }
        if (bot.breakManager.breaking() || bot.breakManager.stopBot()) {
            return DkState.BREAKING;
        }
        if (Bank.isOpen()) {
            log.debug("Bank open");
            return DkState.RESTOCKING;
        }
        boolean needsRestock = bot.doWeNeedToRestock();
        if (initiator != DkState.RESTOCKING && needsRestock) {
            log.debug("Need restock");
            if (Health.getCurrentPercent() == 100 && Prayer.getPoints() > 20 && SpecialAttack.getEnergy() >= 80)
                return DkState.RESTOCKING;
            return DkState.RESTORING;
        }
        return DkState.TRAVERSING;
    }
}
