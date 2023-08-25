package com.neck_flexed.scripts.common.state;

import com.neck_flexed.scripts.common.NeckBot;

public abstract class StateManager<TBot extends NeckBot<?, TState>, TState extends Enum<TState>> implements IStateManager<TState> {
    protected final TBot bot;

    public StateManager(TBot bot) {
        this.bot = bot;
    }

}
