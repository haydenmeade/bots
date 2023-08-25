package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

public class KrakenCoveOverride implements TraverseOverride {
    private Traverser traverser;

    private static GameObject getEntry() {
        return GameObjects.newQuery()
                .names("Cave Entrance")
                .on(new Coordinate(2277, 3611, 0))
                .actions("Enter")
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var r = Players.getLocal().getServerPosition().getContainingRegionId();
        if (r == 9116) {
            var m = new Coordinate(2282, 9989, 0);
            bot.di.sendMovement(m);
            Execution.delayUntil(() -> m.equals(Players.getLocal().getServerPosition()), util::playerMoving, 1200);
            return true;
        }
        if (bot.di.doDiInteractObstacleReachableArea(KrakenCoveOverride::getEntry, "Enter", p -> p.getServerPosition().getContainingRegionId() == 9116))
            return true;
        if (traverser == null)
            traverser = Traverser.webPathTraverser(new Coordinate(2278, 3610, 0));
        traverser.executeLoop();
        return true;
    }
}
