package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.traverse.Traverses;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Quest;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;

public class IowerthOverride implements TraverseOverride {
    private Traverser traverser;

    public static boolean condition(SlayerBotImpl<?> b) {
        var c = Quest.OSRS.SONG_OF_THE_ELVES.getStatus().equals(Quest.Status.COMPLETE);
        if (!c) return false;

        if (b.getItemCache() == null || b.getItemCache().isEmpty()) return true;
        return Traverser.getBestTraverse(Traverses.IORWERTH_TRAVERSES, b.getHouseConfig(), b.getItemCache()).isPresent();
    }

    private static GameObject getEntrance() {
        return GameObjects.newQuery()
                .on(new Coordinate(3224, 6043, 0))
                .names("Cave")
                .actions("Enter")
                .results().first();
    }

    boolean isInDungeon(Player p) {
        var r = p.getServerPosition().getContainingRegionId();
        return r == 12994 || r == 12993;
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        if (isInDungeon(Players.getLocal())) {
            if (Traverser.canRegionPath(destination)) {
                Traverser.regionPathTraverser(destination).executeLoop();
            } else {
                if (traverser == null)
                    traverser = Traverser.webPathTraverser(destination);
                traverser.executeLoop();
            }
            return true;
        }
        var c = new Coordinate(3226, 6046, 0);
        if (bot.di.doDiInteractObstacle(IowerthOverride::getEntrance, "Enter", this::isInDungeon)) {
            traverser = null;
            return true;
        }
        if (Traverser.canRegionPath(c)) {
            Traverser.regionPathTraverser(c).executeLoop();
        } else {
            if (traverser == null)
                traverser = Traverser.webPathTraverser(c);
            traverser.executeLoop();
        }
        return true;
    }
}
