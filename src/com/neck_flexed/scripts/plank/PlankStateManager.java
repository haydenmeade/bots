package com.neck_flexed.scripts.plank;

import com.neck_flexed.scripts.common.state.StateManager;

public class PlankStateManager extends StateManager<planker, PlankState> {
    public PlankStateManager(planker planker) {
        super(planker);
    }

    @Override
    public PlankState getNextState(PlankState initiator) {
        switch (initiator) {
            case RETURNTOBANK:
                return PlankState.RESTOCKING;
        }

        if (bot.breakManager != null)
            if (bot.breakManager.breaking() || bot.breakManager.stopBot())
                return PlankState.BREAKING;

        if (bot.doWeNeedToRestock()) {
            if (planker.isNearLumberYard()) {
                return PlankState.RETURNTOBANK;
            }
            return PlankState.RESTOCKING;
        }
        if (planker.getOperator() != null) {
            return PlankState.PLANKING;
        }
        return PlankState.TRAVERSING;
    }
}
