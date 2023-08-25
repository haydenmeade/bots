package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;

public class AncientCavernOverride implements TraverseOverride {
    private final static Coordinate entrance = new Coordinate(2511, 3513, 0);
    private static final Area whirlpoolArea = new Area.Rectangular(new Coordinate(2511, 3518, 0), new Coordinate(2512, 3511, 0));
    private Traverser traverser;

    private static GameObject getWhirlpool() {
        return GameObjects.newQuery()
                .on(new Coordinate(2510, 3506, 0))
                .names("Whirlpool")
                .actions("Dive in")
                .results().first();
    }

    private static GameObject getStairsUp() {
        return GameObjects.newQuery()
                .on(new Coordinate(1778, 5344, 0))
                .names("Stairs")
                .actions("Climb-up")
                .results().first();
    }

    private static GameObject getStairsDown() {
        return GameObjects.newQuery()
                .on(new Coordinate(1769, 5365, 1))
                .names("Stairs")
                .actions("Climb-down")
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        if (isAboveGround()) {
            if (whirlpoolArea.contains(Players.getLocal().getServerPosition())
                    && bot.di.doDiInteractObstacle(AncientCavernOverride::getWhirlpool, "Dive in", p -> p.getServerPosition().getContainingRegionId() != 10038)) {
                return true;
            }

            if (this.traverser == null)
                this.traverser = Traverser.webPathTraverser(entrance);
            traverser.executeLoop();
        }
        var z = Players.getLocal().getServerPosition().getPlane();
        if (z == 1)
            if (bot.di.doDiInteractObstacleReachableArea(AncientCavernOverride::getStairsDown, "Climb-down", p -> p.getServerPosition().getPlane() != 1)) {
                return true;
            }
        if (z == 0)
            if (bot.di.doDiInteractObstacleReachableArea(AncientCavernOverride::getStairsUp, "Climb-up", p -> p.getServerPosition().getPlane() != 0)) {
                return true;
            }

        return false;
    }

    private boolean isAboveGround() {
        return Players.getLocal().getServerPosition().getContainingRegionId() != 6995;
    }
}
