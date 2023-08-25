package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Quest;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;

import java.util.List;

public class JormungandPrisonOverride implements TraverseOverride {
    private Traverser webPath;

    public static boolean condition(SlayerBotImpl<?> bot) {
        return Quest.OSRS.THE_FREMENNIK_EXILES.getStatus().equals(Quest.Status.COMPLETE);
    }

    private static Npc getHaskell() {
        return Npcs.newQuery()
                .within(new Area.Rectangular(new Coordinate(2618, 3696, 0), new Coordinate(2622, 3682, 0)))
                .names("Haskell")
                .actions("Talk-to", "Island of Stone")
                .results().first();
    }

    private static GameObject getCaveEntry() {
        return GameObjects.newQuery()
                .on(new Coordinate(2464, 4011, 0))
                .names("Cave")
                .actions("Enter")
                .results().first();
    }

    private static Npc getJarvald() {
        return Npcs.newQuery().names("Jarvald").actions("Rellekka").results().first();
    }

    private boolean onMainland() {
        var r = Players.getLocal().getServerPosition().getContainingRegionId();
        return List.of(10552, 10808, 11064, 10809, 10553, 10297, 10810, 8253, 8509, 8252, 8508).contains(r);
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var waterbirth = 10042;
        var pos = Players.getLocal().getServerPosition();
        var r = pos.getContainingRegionId();
        var d = new Coordinate(2621, 3678, 0);
        if (onMainland()) {
            if (bot.di.doDiInteractNpcReachableArea(JormungandPrisonOverride::getHaskell, "Island of Stone", p -> p.getServerPosition().getContainingRegionId() == 9790))
                return true;
            if (this.webPath == null)
                this.webPath = Traverser.webPathTraverser(d);
            this.webPath.executeLoop();
            return true;
        } else if (r == waterbirth) {
            if (bot.di.doDiInteractNpcReachableArea(JormungandPrisonOverride::getJarvald, "Rellekka", p -> p.getServerPosition().getContainingRegionId() != waterbirth))
                return true;
        } else if (r == 9790) {
            if (bot.di.doDiInteractObstacleReachableArea(JormungandPrisonOverride::getCaveEntry, "Enter", p -> p.getServerPosition().getContainingRegionId() != 9790))
                return true;
        }
        Traverser.regionPathTraverser(destination).executeLoop();
        return true;
    }
}
