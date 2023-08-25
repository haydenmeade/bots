package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

public class SmokeDungeonOverride implements TraverseOverride {
    GameObject getSmokyCave() {
        return GameObjects.newQuery()
                .names("Smoky cave")
                .actions("Enter")
                .on(new Coordinate(2411, 3061, 0))
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var inCave = 9619;
        var p = Players.getLocal().getServerPosition();
        if (p.getContainingRegionId() == inCave) {
            Traverser.regionPathTraverser(new Coordinate(2398, 9444, 0)).executeLoop();
            return true;
        } else {
            var c = getSmokyCave();
            if (c != null) {
                bot.di.send(MenuAction.forGameObject(c, "Enter"));
                Execution.delayUntil(() -> Players.getLocal().getServerPosition().getContainingRegionId() == inCave,
                        util::playerAnimatingOrMoving, 1000, 2000);
                return true;
            }
        }
        return false;
    }
}
