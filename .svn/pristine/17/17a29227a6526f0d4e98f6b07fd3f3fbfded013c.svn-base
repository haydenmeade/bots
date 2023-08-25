package com.neck_flexed.scripts.slayer.encounters;

import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;

import java.util.Objects;

public class NechryarchEncounter implements EncounterOverride {
    private static final SlayerMonster monster = Monster.getByEnum(Task.NECHRYAEL).get();
    private Traverser traverser;

    @Override
    public boolean executeLoop(Npc superior) {
        var p = Players.getLocal();
        var anyDeathSpawnsTargetingMe = Npcs.newQuery()
                .names("Chaotic death spawn")
                .filter(n -> Objects.equals(n.getTarget(), p))
                .results().size() > 0;

        if (anyDeathSpawnsTargetingMe && p.getServerPosition().getContainingRegionId() != 6557) {
            // run away
            if (traverser == null)
                this.traverser = Traverser.regionPathTraverser(new Coordinate(1662, 10071, 0));
            traverser.executeLoop();
            return true;
        } else {
            Traverser.regionPathTraverser(monster.getLocation(null).getTraverseToTile()).executeLoop();
        }
        return false;
    }
}
