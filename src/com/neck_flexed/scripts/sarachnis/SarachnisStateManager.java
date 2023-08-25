package com.neck_flexed.scripts.sarachnis;

import com.neck_flexed.scripts.common.state.StateManager;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;

public class SarachnisStateManager extends StateManager<bot, SarachnisState> {
    private final SarachnisListener sarachnisListener;

    public SarachnisStateManager(bot bot, SarachnisListener sarachnisListener) {
        super(bot);
        this.sarachnisListener = sarachnisListener;
    }

    @Override
    public SarachnisState getNextState(SarachnisState initiator) {
        switch (initiator) {
            case POST_LOOT:
                return SarachnisState.FIGHTING;
            case LOOTING:
                if (bot.doWeNeedToRestock() || bot.breakManager.breaking() || bot.breakManager.stopBot()) {
                    return SarachnisState.RESTORING;
                } else {
                    return SarachnisState.POST_LOOT;
                }
            case FIGHTING:
                if (this.sarachnisListener.isDead() || areas.isInBossRoom())
                    return SarachnisState.LOOTING;
            case RESTORING:
                return SarachnisState.RESTOCKING;
        }
        if (areas.isInBossRoom()) {
            return SarachnisState.FIGHTING;
        }
        if (bot.breakManager.breaking() || bot.breakManager.stopBot())
            return SarachnisState.BREAKING;

        if (Bank.isOpen()) return SarachnisState.RESTOCKING;
        boolean needsRestock = bot.doWeNeedToRestock();
        if (initiator != SarachnisState.RESTOCKING && needsRestock) {
            if (util.isFullyRestored())
                return SarachnisState.RESTOCKING;
            return SarachnisState.RESTORING;
        }
        return SarachnisState.TRAVERSING;
    }
}
