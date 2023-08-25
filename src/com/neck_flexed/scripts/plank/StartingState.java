package com.neck_flexed.scripts.plank;

import com.neck_flexed.scripts.common.state.BaseState;
import com.runemate.game.api.hybrid.local.Camera;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "StartingState")
public class StartingState extends BaseState<PlankState> {
    private final planker bot;
    private boolean started = false;

    public StartingState(planker m) {
        super(m, PlankState.STARTING);
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
        Camera.setZoom(0.0, 0.1);
        started = true;
    }
}
