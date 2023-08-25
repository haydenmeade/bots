package com.neck_flexed.scripts.cerberus;

import com.runemate.game.api.script.framework.listeners.PlayerListener;
import com.runemate.game.api.script.framework.listeners.events.AnimationEvent;

public class PlayerAnimationListener implements PlayerListener {

    private final int animationId;
    private boolean done = false;

    public PlayerAnimationListener(int animationId) {
        this.animationId = animationId;
    }

    @Override
    public void onPlayerAnimationChanged(AnimationEvent event) {
        if (event.getAnimationId() != animationId)
            this.done = true;
    }

    public boolean isDone() {
        return done;
    }
}
