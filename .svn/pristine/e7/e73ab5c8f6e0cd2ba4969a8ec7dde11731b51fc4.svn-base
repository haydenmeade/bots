package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;

public class KalphiteCaveTraverseOverride implements TraverseOverride {

    private Traverser traverser;

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var pos = Players.getLocal().getServerPosition();
        if (pos.getContainingRegionId() == 12848) {
            if (this.traverser == null)
                this.traverser = Traverser.regionPathTraverser(new Coordinate(3278, 3095, 0));
            traverser.executeLoop();
            return true;
        }
        return false;
    }

}
