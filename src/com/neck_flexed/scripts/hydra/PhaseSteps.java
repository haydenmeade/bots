package com.neck_flexed.scripts.hydra;

import com.runemate.game.api.hybrid.location.Coordinate;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public interface PhaseSteps {
    @NotNull PhaseSteps.Step GetNextStep(int idx);

    @Getter
    class Step {
        private final Predicate<HydraListener> mustMoveWhen;
        private final int numberOfTicks;
        private final int index;
        private final Function<HydraListener, Coordinate> destination;
        private final Predicate<HydraListener> stayWhile;

        public Step(int index, Predicate<HydraListener> mustMoveWhen, Coordinate c) {
            this.index = index;
            this.mustMoveWhen = mustMoveWhen;
            this.destination = h -> c;
            this.numberOfTicks = -1;
            this.stayWhile = h -> false;
        }

        public Step(int index, Predicate<HydraListener> mustMoveWhen, Predicate<HydraListener> stayWhile, Coordinate c) {
            this.index = index;
            this.mustMoveWhen = mustMoveWhen;
            this.destination = h -> c;
            this.numberOfTicks = -1;
            this.stayWhile = stayWhile;
        }

        public Step(int index, Predicate<HydraListener> mustMoveWhen, Predicate<HydraListener> stayWhile, Function<HydraListener, Coordinate> destination) {
            this.index = index;
            this.mustMoveWhen = mustMoveWhen;
            this.destination = destination;
            this.numberOfTicks = -1;
            this.stayWhile = stayWhile;
        }

        public Step(int index, Predicate<HydraListener> mustMoveWhen, Function<HydraListener, Coordinate> destination) {
            this.index = index;
            this.mustMoveWhen = mustMoveWhen;
            this.destination = destination;
            this.numberOfTicks = -1;
            this.stayWhile = h -> false;
        }

        public Step(int index, int numberOfTicks, Function<HydraListener, Coordinate> destination) {
            this.index = index;
            this.mustMoveWhen = null;
            this.numberOfTicks = numberOfTicks;
            this.destination = destination;
            this.stayWhile = h -> false;
        }

        public Step(int index, int numberOfTicks, Coordinate c) {
            this.index = index;
            this.mustMoveWhen = null;
            this.numberOfTicks = numberOfTicks;
            this.destination = h -> c;
            this.stayWhile = h -> false;
        }

    }
}
