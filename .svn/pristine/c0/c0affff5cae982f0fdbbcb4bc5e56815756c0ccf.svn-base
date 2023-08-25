package com.neck_flexed.scripts.cerberus;

import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "RestoreState")
public class RestoreState extends com.neck_flexed.scripts.common.state.RestoreState<CerbState> {
    private CerbListener cerbListener;

    public RestoreState(cerb bot, CerbListener cerbListener) {
        super(bot, CerbState.RESTORING, bot.getHouseConfig(), true);
        this.cerbListener = cerbListener;
    }

    @Override
    public void activate() {
        super.activate();
        this.cerbListener.reset();
    }
}
