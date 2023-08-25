package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceContainers;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.queries.InterfaceComponentQueryBuilder;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

@Log4j2
public class GliderTraverse implements TraverseMethod {
    private final Destination dest;
    private Traverser traverser;

    public GliderTraverse(Destination dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "Gnome Glider (Seed pod, Ring of Dueling): " + dest;
    }

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        var l = new ArrayList<Requirement>();
        l.add(new BaseRequirement(util.toPatternArray(items.royalSeedPod)));
        l.add(new BaseRequirement(util.toPatternArray(items.grandSeedPod)));
        l.add(new BaseRequirement(items.ringOfDueling));
        l.add(new HouseRequirement(HouseConfig::hasBaseJewelleryBox));
        return l;
    }

    @Override
    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        var startRegion = startPosition.getContainingRegionId();
        var gliderInterface = InterfaceContainers.getAt(138);
        if (gliderInterface != null) {
            var component = new InterfaceComponentQueryBuilder().actions(dest.getLabel()).results().first();
            if (component != null) {
                if (!component.interact(dest.getLabel())) {
                    log.error("Unable to interact with {}", dest.getLabel());
                }
                Execution.delayUntil(
                        () -> startRegion != Players.getLocal().getServerPosition().getContainingRegionId(),
                        util::playerAnimating,
                        5000, 6050);
            }
            return startRegion != Players.getLocal().getServerPosition().getContainingRegionId();
        }

        var captain = Npcs.newQuery().names("Captain Errdo", "Captain Dalbur").actions("Glider").results().nearest();
        if (captain != null) {
            if (!captain.interact("Glider")) {
                log.error("Unable to interact with captain");
            }
            Execution.delayUntil(
                    () -> InterfaceContainers.getAt(138) != null,
                    5000, 6050);
            return false;

        }
        var ladder = GameObjects.newQuery().names("Ladder").actions("Climb-up").results().nearest();
        if (ladder != null && Players.getLocal().getServerPosition().getContainingRegionId() == 9782) {
            var plane = Players.getLocal().getServerPosition().getPlane();
            if (!ladder.interact("Climb-up")) {
                log.error("Unable to interact with ladder {}", ladder);
            }
            Execution.delayUntil(
                    () -> Players.getLocal().getServerPosition().getPlane() != plane,
                    5000, 6050);
            return false;
        }

        var pod = Inventory.getItems(items.royalSeedPod, items.grandSeedPod).first();
        if (pod == null) {
            if (Players.getLocal().getServerPosition().getContainingRegionId() == 13106) {
                // traverse to captain
                if (this.traverser == null)
                    this.traverser = Traverser.webPathTraverser(new Coordinate(3285, 3214, 0));
                this.traverser.executeLoop();
            } else {
                // TODO duelling ring/house duelling ring
            }
            return false;
        }
        if (!pod.interact(Pattern.compile("^Commune|Squash$"))) {
            log.error("Unable to interact with seed pod");
        }
        return false;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Destination {
        TaQuirPriw("Ta Quir Priw"),
        Sindarpos("Sindarpos"),
        LemantoAndra("Lemanto Andra"),
        KarHewo("Kar-Hewo"),
        LemantollyUndri("Lemantolly Undri"),
        OokookollyUndri("Ookookolly Undri"),
        Gandius("Gandius");
        private final String label;
    }
}
