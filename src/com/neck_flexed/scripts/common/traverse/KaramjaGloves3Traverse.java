package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.DI;
import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.SlayerMaster;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.WaypointPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class KaramjaGloves3Traverse implements TraverseMethod {
    static final Coordinate[] path = {
            new Coordinate(2824, 2995, 0),
            new Coordinate(2828, 2991, 0),
            new Coordinate(2829, 2984, 0),
            new Coordinate(2831, 2979, 0),
            new Coordinate(2831, 2973, 0),
            new Coordinate(2832, 2968, 0),
            new Coordinate(2840, 2967, 0),
            new Coordinate(2846, 2966, 0),
            new Coordinate(2850, 2962, 0),
            new Coordinate(2857, 2960, 0),
            new Coordinate(2866, 2962, 0),
            new Coordinate(2870, 2964, 0)
    };
    private static final Pattern kg3 = Pattern.compile("^Karamja gloves 3$");
    private static final Collection<Integer> traverseRegions = List.of(11410, 11310);
    private WaypointPath waypointPath;

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        return List.of(new BaseRequirement(kg3));
    }

    @Override
    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        var g = Inventory.getItems(kg3).first();
        if (g == null) return false;
        var pos = Players.getLocal().getServerPosition();
        if (!traverseRegions.contains(pos.getContainingRegionId())) {
            DI.get().send(MenuAction.forSpriteItem(g, "Gem Mine"));
            Execution.delayUntil(() -> traverseRegions.contains(Players.getLocal().getServerPosition().getContainingRegionId()),
                    util::playerAnimatingOrMoving, 3000, 5000);
            return false;
        }
        if (pos.getContainingRegionId() == 11410) {
            DI.get().doDiInteractObstacleReachableArea(this::getLadder1, "Climb-up", p -> p.getServerPosition().getContainingRegionId() == 11310);
            return false;
        }
        if (pos.getPlane() == 0) {
            if (!DI.get().doDiInteractObstacleReachableArea(this::getLadder2, "Climb-up", p -> p.getServerPosition().getPlane() == 1)) {
                if (this.waypointPath == null) {
                    var c = new ArrayList<Coordinate>();
                    c.add(Players.getLocal().getServerPosition());
                    c.addAll(List.of(path));
                    this.waypointPath = WaypointPath.create(c.toArray(Coordinate[]::new));
                }

                waypointPath.step(Traverser.OPTS);
            }
            return false;
        } else {
            this.waypointPath = null;
            util.moveTo(SlayerMaster.DURADEL.getLocation());
            Execution.delayUntil(() -> SlayerMaster.DURADEL.getNpc() != null, util::playerMoving, 3000);
        }

        this.waypointPath = null;
        return SlayerMaster.DURADEL.getNpc() != null;
    }

    @Override
    public String toString() {
        return "Karamja Gloves 3";
    }

    GameObject getLadder1() {
        return GameObjects.newQuery()
                .on(new Coordinate(2838, 9388, 0))
                .names("Ladder")
                .actions("Climb-up")
                .results().first();
    }

    GameObject getLadder2() {
        return GameObjects.newQuery()
                .on(new Coordinate(2871, 2971, 0))
                .names("Ladder")
                .actions("Climb-up")
                .results().first();
    }
}
