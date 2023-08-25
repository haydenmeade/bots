package com.neck_flexed.scripts.hydra;

import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.Camera;

public class StartingState extends BaseState<HydraState> {
    private final com.neck_flexed.scripts.hydra.bot bot;
    private final Loadouts loadouts;
    private boolean started = false;

    public StartingState(com.neck_flexed.scripts.hydra.bot m, Loadouts loadouts) {
        super(m, HydraState.STARTING);
        this.bot = m;
        this.loadouts = loadouts;
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
