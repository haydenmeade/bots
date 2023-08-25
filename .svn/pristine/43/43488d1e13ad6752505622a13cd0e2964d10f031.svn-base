package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.hydra.KaraluumDungeonOverrider;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;

public class DrakeOverride implements TraverseOverride {
    private BresenhamPath currentPath;

    private static GameObject getRocks() {
        return GameObjects.newQuery()
                .on(new Coordinate(1321, 10205, 0))
                .names("Rocks")
                .actions("Climb")
                .results().first();
    }

    private static GameObject getSteps() {
        return GameObjects.newQuery()
                .on(new Coordinate(1330, 10205, 0))
                .names("Steps")
                .actions("Climb")
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        if (new KaraluumDungeonOverrider().overrideLoop(bot, destination)) return true;
        if (Players.getLocal().getServerPosition().getPlane() == 1) {
            if (this.currentPath == null)
                this.currentPath = BresenhamPath.buildTo(new Coordinate(1310, 10240, 1));
            if (this.currentPath != null)
                currentPath.step(Traverser.OPTS);
            return true;
        }
        currentPath = null;
        if (bot.di.doDiInteractObstacleReachable(DrakeOverride::getSteps, "Climb", p -> p.getServerPosition().getPlane() != 0))
            return true;
        if (bot.di.doDiInteractObstacleReachable(DrakeOverride::getRocks, "Climb", p -> p.getServerPosition().getX() > 1321))
            return true;
        return false;
    }
}
