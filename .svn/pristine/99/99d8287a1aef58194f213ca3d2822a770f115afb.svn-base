package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import org.jetbrains.annotations.Nullable;

public class SmokeDevilStackingState extends BaseState<SlayerState> {
    public static Coordinate[] SMOKE_STACK_NS = new Coordinate[]{
            new Coordinate(2398, 9434, 0),
            new Coordinate(2398, 9452, 0),
            new Coordinate(2392, 9443, 0),
    };
    private final SlayerBotImpl<?> bot;

    public SmokeDevilStackingState(SlayerBotImpl<?> bot) {
        super(bot, SlayerState.STACKING);
        this.bot = bot;
    }

    @Override
    public void activate() {
        bot.updateStatus("Stacking Smoke Devil");
        this.bot.prayerFlicker.setQuickPrayers(util.getMagicBoostPrayer(), Prayer.PROTECT_FROM_MISSILES);
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public void executeLoop() {
        if (!Traversal.isRunEnabled() && Traversal.getRunEnergy() > 2)
            Traversal.toggleRun();
    }
}
