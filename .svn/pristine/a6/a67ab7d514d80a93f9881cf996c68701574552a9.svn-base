package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.WaypointPath;
import com.runemate.game.api.hybrid.region.Players;

import java.util.List;

public class SteelDragonTraverseOverride implements TraverseOverride {
    static final List<Locatable> pathCoords = List.of(
            new Coordinate(1672, 10037, 0),
            new Coordinate(1670, 10030, 0),
            new Coordinate(1674, 10023, 0),
            new Coordinate(1666, 10010, 0),
            new Coordinate(1647, 10004, 0),
            new Coordinate(1629, 10010, 0),
            new Coordinate(1620, 10033, 0),
            new Coordinate(1623, 10046, 0),
            new Coordinate(1608, 10054, 0)
    );
    private WaypointPath path;

    public static boolean inCatacomds(Coordinate c) {
        return List.of(6557, 6556, 6812, 6813).contains(c.getContainingRegionId());
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var p = Players.getLocal().getServerPosition();
        if (!inCatacomds(p)) return false;
        if (this.path == null)
            this.path = WaypointPath.create(pathCoords);
        path.step(Traverser.OPTS);
        return true;
    }
}
