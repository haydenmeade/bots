package com.neck_flexed.scripts.kq;

import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.Camera;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "StartingState")
public class StartingState extends BaseState<KqState> {
    private final kq bot;
    private boolean started = false;
    private Loadouts loadouts;

    public StartingState(kq m) {
        super(m, KqState.STARTING);
        this.bot = m;
        this.loadouts = bot.loadouts;
    }

    @Override
    public void activate() {
        bot.updateStatus("Starting");

    }

    @Override
    public void deactivate() {
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
