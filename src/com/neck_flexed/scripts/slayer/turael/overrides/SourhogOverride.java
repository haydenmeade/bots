package com.neck_flexed.scripts.slayer.turael.overrides;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.traverse.TraverseOverride;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public class SourhogOverride implements TraverseOverride {
    private Traverser overrideTraverser;

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        log.debug("Override web path sourhog");
        var monster = Monster.getByEnum(Task.SOURHOGS).get();
        var loc = monster.getLocation(bot);
        var pos = Players.getLocal().getServerPosition();
        var sourhogRegion = 12695;
        if (pos.getContainingRegionId() == sourhogRegion) {
            if (pos.getY() <= 9704) {
                util.moveTo(loc.getCannonSpot());
                Execution.delayUntil(() -> Objects.equals(Players.getLocal().getServerPosition(), loc.getCannonSpot()),
                        util::playerMoving, 1000, 2000);
                return true;
            }
            var blockage = GameObjects.newQuery()
                    .on(new Coordinate(3156, 9704, 0))
                    .names("Blockage")
                    .actions("Climb-over")
                    .results()
                    .nearest();
            if (blockage != null) {
                bot.di.send(MenuAction.forGameObject(blockage, "Climb-over"));
                Execution.delayUntil(() -> loc.getArea().contains(Players.getLocal().getServerPosition()),
                        util::playerAnimatingOrMoving, 3000, 4000);
            }

        } else {
            var hole = GameObjects.newQuery()
                    .within(new Coordinate(3150, 3347, 0).getArea().grow(2, 2))
                    .names("Strange hole")
                    .results()
                    .nearest();
            if (hole == null) {
                if (this.overrideTraverser == null)
                    this.overrideTraverser = Traverser.webPathTraverser(new Coordinate(3149, 3347, 0));
                overrideTraverser.executeLoop();
                return true;
            }
            bot.di.send(MenuAction.forGameObject(hole, "Climb-down"));
            Execution.delayUntil(() ->
                            Players.getLocal().getServerPosition().getContainingRegionId() == sourhogRegion,
                    util::playerAnimatingOrMoving, 3000, 4000);
        }
        return true;
    }
}
