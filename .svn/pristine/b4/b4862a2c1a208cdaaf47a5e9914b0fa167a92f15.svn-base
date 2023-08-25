package com.neck_flexed.scripts.common.state;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.traverse.DuelingRingTraverse;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2(topic = "BaseRestoreState")
public class RestoreState<TState extends Enum<TState>> extends BaseState<TState> {
    private final NeckBot<?, TState> bot;
    private final HouseConfig houseConfig;
    private final boolean endOnSlayerTaskFinished;
    private final DuelingRingTraverse rodTraverse;
    private final Coordinate start;
    private final boolean teleToHouse;
    private String error;
    private boolean done;

    public RestoreState(NeckBot<?, TState> bot,
                        TState restoreState,
                        HouseConfig houseConfig,
                        boolean endOnSlayerTaskFinished
    ) {
        super(bot, restoreState);
        this.bot = bot;
        this.houseConfig = houseConfig;
        this.endOnSlayerTaskFinished = endOnSlayerTaskFinished;
        this.rodTraverse = new DuelingRingTraverse(DuelingRingTraverse.Destination.Ferox);
        this.start = Players.getLocal().getServerPosition();
        this.teleToHouse = Objects.equals(houseConfig.getPool(), HouseConfig.Pool.Ornate) && util.hasHouseTeleport();
    }

    public RestoreState(NeckBot<?, TState> bot,
                        TState restoreState,
                        HouseConfig houseConfig
    ) {
        this(bot, restoreState, houseConfig, false);
    }


    @Override
    public void activate() {
        bot.updateStatus("Restoring at " + (teleToHouse ? "House" : "Ferox"));
        bot.prayerFlicker.disable();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        if (this.endOnSlayerTaskFinished && !SlayerTask.hasTask()) {
            bot.updateStatus("Finished endOnSlayerTaskFinished task");
            Execution.delay(1000);
            bot.stop("Finished endOnSlayerTaskFinished task");
        }
    }

    @Override
    public String fatalError() {
        return this.error;
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public void executeLoop() {
        if (this.teleToHouse) {
            if (!House.isInside()) {
                util.teleToHouse();
                return;
            }

            if (!HouseUtil.restoreStatsInHouse(bot)) {
                return;
            }

            if (!Traversal.isRunEnabled())
                Traversal.toggleRun();

            this.done = true;
        } else {
            if (!util.ferox.contains(Players.getLocal())) {
                if (!util.canRestoreAtFerox()) {
                    error = "Unable to restore without ring of dueling";
                    return;
                }
                rodTraverse.doTraverseLoop(bot.getHouseConfig(), start);
                return;
            } else if (!util.restoreAtFerox(bot)) {
                return;
            }
            this.done = true;
        }
    }
}
