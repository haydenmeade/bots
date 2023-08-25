package com.neck_flexed.scripts.common.state;

public interface IStateManager<TState extends Enum> {
    TState getNextState(TState initiator);
}
