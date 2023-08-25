package com.neck_flexed.scripts.ashes;

import com.neck_flexed.scripts.common.state.IStateManager;
import com.neck_flexed.scripts.common.state.StateManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AshStateManager extends StateManager<ashes, AshState> implements IStateManager<AshState> {
    public AshStateManager(ashes ashes) {
        super(ashes);
    }

    @Override
    public AshState getNextState(AshState initiator) {
        log.debug(Ash.hasNoAshesLeft());
        log.debug(Ash.ashCount());
        if (!ashes.inWildy()) {
            if (bot.breakManager != null)
                if (bot.breakManager.breaking() || bot.breakManager.stopBot())
                    return AshState.BREAKING;
        }
        if (bot.doWeNeedToRestock()) {
            if (ashes.inWildy()) {
                return AshState.SELFDEATH;
            }
            return AshState.RESTOCKING;
        }
        if (ashes.atFountain()) {
            return AshState.OFFERING;
        }
        return AshState.TRAVERSING;
    }
}
