package com.neck_flexed.scripts.hydra;

import com.neck_flexed.scripts.common.Instance;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.region.SpotAnimations;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

@Log4j2(topic = "Phase3")
public class Phase3Steps implements PhaseSteps {

    private final Coordinate step0;
    private final Coordinate step1;
    private final Coordinate step2;
    private final Coordinate step3;
    private final Coordinate step4;
    private final Coordinate hydraMiddle;
    private boolean failedFlameSkip = false;
    private Coordinate safeTile;

    public Phase3Steps() {
        var step0 = new Coordinate(1357, 10272, 0);
        var step1 = new Coordinate(1360, 10274, 0);
        var step2 = new Coordinate(1363, 10272, 0);
        var step3 = new Coordinate(1364, 10271, 0);
        var step4 = new Coordinate(1363, 10270, 0);
        this.step0 = Instance.toInstancedFirst(step0);
        this.step1 = Instance.toInstancedFirst(step1);
        this.step2 = Instance.toInstancedFirst(step2);
        this.step3 = Instance.toInstancedFirst(step3);
        this.step4 = Instance.toInstancedFirst(step4);
        this.hydraMiddle = Instance.toInstancedFirst(new Coordinate(1364, 10265, 0));
    }

    @Override
    public @NotNull PhaseSteps.Step GetNextStep(int idx) {
        switch (idx) {
            case 0:
                return new Step(0, h -> h.getHydra().getNextSpecialRelative() < 2, HydraListener::isNotOverVent, step0);
            case 1:
                return new Step(1, 10, step1);
            case 2:
                return new Step(2,
                        h -> h.getHydra().getNpc().getServerPosition().getX() == hydraMiddle.getX() && h.playerAttacked(), step2);
            case 3:
                return new Step(3, HydraListener::isSpecialNextTick, h -> h.getHydra().getNpc().getArea().contains(Players.getLocal().getServerPosition()), step3);
            case 4:
                return new Step(4, 4, step4);
            default:
                this.failedFlameSkip = !Players.getLocal().getServerPosition().equals(step4) || isOnFlame();
                if (failedFlameSkip) {
                    if (Traversal.isRunEnabled()) {
                        Traversal.toggleRun();
                    }
                    var nullStep = new Step(5, 0, step4);
                    var c1 = Instance.toInstancedFirst(new Coordinate(1358, 10277, 0));
                    var c2 = Instance.toInstancedFirst(new Coordinate(1363, 10271, 0));
                    if (c1 == null || c2 == null) return nullStep;
                    var p = Players.getLocal();
                    if (p == null) return nullStep;
                    var pos = p.getServerPosition();
                    if (pos == null) return nullStep;
                    var area = new Area.Rectangular(c1, c2);
                    var danger = SpotAnimations.newQuery()
                            .within(area)
                            .results().stream()
                            .map(e -> e.getPosition().getArea().grow(1, 1).getCoordinates())
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    if (safeTile == null || danger.stream().anyMatch(c -> c.equals(safeTile))) {
                        // if need to refresh safeTile
                        var safeTileF = area.getCoordinates().stream()
                                .filter(c -> danger.stream().noneMatch(d -> d.equals(c))
                                        && c.distanceTo(pos) > 5
                                )
                                .findFirst();
                        this.safeTile = safeTileF.orElse(step4);
                    }
                    return new Step(5, 4,
                            h -> {
                                if (!isNearFlame()) {
                                    return Players.getLocal().getServerPosition();
                                } else {
                                    return safeTile;
                                }
                            });
                } else if (isFlameOnStep3()) {
                    return new Step(4, h -> isFlameOnStep3(), step4);
                } else {
                    // not failed flame skip
                    return new Step(3, HydraListener::isSpecialNextTick, h -> h.getHydra().getNpc().getArea().contains(Players.getLocal().getServerPosition()), step3);
                }
        }
    }

    private boolean isNearFlame() {
        return !SpotAnimations.newQuery()
                .ids(c.HYDRA_FLAME_SPOT)
                .within(Players.getLocal().getServerPosition().getArea().grow(1, 1))
                .results().isEmpty();
    }

    private boolean isFlameOnStep3() {
        return !SpotAnimations.newQuery()
                .ids(c.HYDRA_FLAME_SPOT)
                .on(step3)
                .results().isEmpty();

    }

    private boolean isOnFlame() {
        return !SpotAnimations.newQuery()
                .ids(c.HYDRA_FLAME_SPOT)
                .on(Players.getLocal().getServerPosition())
                .results().isEmpty();

    }
}
