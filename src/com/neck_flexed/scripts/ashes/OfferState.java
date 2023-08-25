package com.neck_flexed.scripts.ashes;

import com.neck_flexed.scripts.common.state.BaseState;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

@Log4j2(topic = "OfferState")
public class OfferState extends BaseState<AshState> implements EngineListener {
    private final com.neck_flexed.scripts.ashes.ashes bot;
    private boolean done;
    private int tick = 0;
    private int offertick = -100;
    private Magic.Arceuus spell = Magic.Arceuus.DEMONIC_OFFERING;

    public OfferState(ashes ashes) {
        super(ashes, AshState.OFFERING);
        this.bot = ashes;
    }


    @Override
    public void activate() {
        this.bot.updateStatus("Offering");
    }

    @Override
    public void deactivate() {

    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{this};
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return this.done || this.bot.isDead() || !ashes.atFountain();
    }

    @Override
    public void executeLoop() {
        if (Ash.hasNoAshesLeft()) {
            this.done = true;
        }
    }

    @Override
    public void onTickStart() {
        this.tick++;
        if (tick - offertick >= 10)
            if (spell.activate()) {
                offertick = tick;
            }
    }
}
