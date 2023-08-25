package com.neck_flexed.scripts.dk;

import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.EventListener;

@Log4j2(topic = "OffTickState")
public class OffTickState extends BaseState<DkState> implements EngineListener {
    private final DkListener dkListener;
    private final dk bot;
    private boolean done = false;
    private King inProgress;
    private Coordinate moveInProgress;

    public OffTickState(DkListener dkListener,
                        dk bot
    ) {
        super(bot, DkState.OFF_TICK);
        this.dkListener = dkListener;
        this.bot = bot;
    }


    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{this};
    }

    @Override
    public void activate() {
        bot.updateStatus(String.format("Offticking"));
    }

    @Override
    public String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public void onTickStart() {
        var toOffTick = dkListener.needToOffTick();
        if (toOffTick.isEmpty()) return;

        var p = Players.getLocal();
        if (p == null || p.getPosition() == null) return;
        if (this.inProgress == null || !toOffTick.contains(inProgress)) {
            this.inProgress = toOffTick.stream()
                    .min(Comparator.comparingDouble(k -> k.getNpc().getServerPosition().distanceTo(p))).get();
            moveInProgress = null;
        }

        if (inProgress.getNpc() != null
                && inProgress.getNpc().getArea() != null
                && inProgress.getNpc().getArea().contains(p)) {
            this.dkListener.notifyDidOffTick(this.inProgress);
            bot.updateStatus(String.format("Offticking done: %s", inProgress.getName()));
        }

        if (moveInProgress == null
                || !p.getPosition().equals(moveInProgress)
                || (inProgress.getNpc() != null && !inProgress.getNpc().getArea().contains(p))) {
            var npc = inProgress.getNpc();
            bot.updateStatus(String.format("Offticking: %s", inProgress.getName()));
            this.moveInProgress = npc.getPosition();
            util.moveTo(moveInProgress);
        }
    }

    @Override
    public void executeLoop() {
        done = dkListener.needToOffTick().isEmpty();
    }
}
