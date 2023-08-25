package com.neck_flexed.scripts.common.state;

import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

public interface LoopState<T extends Enum> {

    void activate();

    void deactivate();

    EventListener[] getEventListeners();

    T get();

    @Nullable String fatalError();

    boolean done();

    void executeLoop();

}
