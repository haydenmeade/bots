package com.neck_flexed.scripts.common;

import com.runemate.game.api.script.framework.core.EventDispatcher;
import com.runemate.game.api.script.framework.listeners.PlayerListener;
import com.runemate.game.api.script.framework.listeners.events.AnimationEvent;

public class RestoreListener implements PlayerListener {
    private final EventDispatcher dispatcher;
    private boolean hasRestored = false;

    public RestoreListener(EventDispatcher d) {
        this.dispatcher = d;
        d.addListener(this);
    }

    public void stop() {
        this.dispatcher.removeListener(this);
    }

    public boolean hasRestored() {
        return hasRestored;
    }

    @Override
    public void onPlayerAnimationChanged(AnimationEvent event) {
        if (event.getAnimationId() == util.RestoreAnimId)
            hasRestored = true;
    }
}
