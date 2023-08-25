package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceContainers;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

import java.awt.event.KeyEvent;

public class FossilIslandWyvernOverride implements TraverseOverride {

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        if (InterfaceContainers.getAt(608) != null) {
            Keyboard.pressKey(KeyEvent.VK_4);
            Execution.delayUntil(() -> Players.getLocal().getServerPosition().getContainingRegionId() == 14652,
                    util::playerAnimating, 1500, 2500);
            return true;
        }
        if (bot.di.doDiInteractObstacle(this::getMushTree, "Use", p -> InterfaceContainers.getAt(608) != null))
            return true;
        if (bot.di.doDiInteractObstacle(this::getTrapDoor, "Climb Down", p -> p.getServerPosition().getContainingRegionId() == 14496))
            return true;
        if (bot.di.doDiInteractObstacle(p -> isUpstairs(p.getServerPosition()), this::getStair, "Climb", p -> !isUpstairs(p.getServerPosition())))
            return true;

        Traverser.regionPathTraverser(new Coordinate(3613, 10278, 0)).executeLoop();
        return true;
    }


    private boolean isUpstairs(Coordinate p) {
        return new Area.Polygonal(
                new Coordinate(3594, 10295, 0),
                new Coordinate(3598, 10296, 0),
                new Coordinate(3599, 10295, 0),
                new Coordinate(3599, 10292, 0),
                new Coordinate(3604, 10293, 0),
                new Coordinate(3605, 10290, 0),
                new Coordinate(3601, 10288, 0),
                new Coordinate(3593, 10289, 0)
        ).contains(p);
    }

    private GameObject getStair() {
        return GameObjects.newQuery()
                .names("Stairs")
                .on(new Coordinate(3604, 10290, 0))
                .actions("Climb")
                .results().first();
    }

    private GameObject getTrapDoor() {
        return GameObjects.newQuery()
                .names("Trap Door")
                .on(new Coordinate(3677, 3853, 0))
                .actions("Climb Down")
                .results().first();
    }

    private GameObject getMushTree() {
        return GameObjects.newQuery()
                .names("Magic Mushtree")
                .on(new Coordinate(3764, 3880, 1))
                .actions("Use")
                .results().first();
    }
}
