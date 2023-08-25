package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;

import java.util.List;

public class SpectreOverrideStronghold implements TraverseOverride {
    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var reg = Players.getLocal().getServerPosition().getContainingRegionId();
        if (List.of(9881, 9880, 9624, 9625).contains(reg)) {
            Traverser.regionPathTraverser(destination).executeLoop();
            return true;
        }
        return false;
    }
}
