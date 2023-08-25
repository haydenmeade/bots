package com.neck_flexed.scripts.slayer.state;

import com.neck_flexed.scripts.common.state.BaseState;
import com.runemate.game.api.hybrid.local.Camera;

public class StartingState extends BaseState<SlayerState> {
    private final com.neck_flexed.scripts.slayer.bot bot;
    private boolean started = false;

    public StartingState(com.neck_flexed.scripts.slayer.bot m) {
        super(m, SlayerState.STARTING);
        this.bot = m;
    }

    @Override
    public void activate() {
        bot.updateStatus("Starting");

    }

    @Override
    public String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return started;
    }

    @Override
    public void executeLoop() {
        bot.refreshProgress();
        Camera.setZoom(0.0, 0.1);
        started = true;
    }
}
