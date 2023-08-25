package com.neck_flexed.scripts.cerberus;

import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.Camera;

public class StartingState extends BaseState<CerbState> {
    private final cerb bot;
    private boolean started = false;

    public StartingState(cerb m, Loadouts loadouts) {
        super(m, CerbState.STARTING);
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
        Camera.setZoom(0.1, 0.1);
        if (util.isAutoRetaliateEnabled())
            util.toggleAutoRetaliate();
        started = true;
    }
}
