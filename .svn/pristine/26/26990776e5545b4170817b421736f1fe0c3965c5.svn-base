package com.neck_flexed.scripts.common.traverse;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.VarbitID;
import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
public enum FairyRing {

    ZANARIS(new Coordinate(2412, 4434, 0), 1),
    AIQ(new Coordinate(2996, 3114, 0), 3),
    AIR(new Coordinate(2700, 3247, 0), 2),
    AJR(new Coordinate(2780, 3613, 0), 14),
    AJS(new Coordinate(2500, 3896, 0), 13),
    AJQ(new Coordinate(2735, 5221, 0), 15),
    AKP(new Coordinate(3284, 2706, 0), 8),
    AKQ(new Coordinate(2319, 3619, 0), 11),
    AKS(new Coordinate(2571, 2956, 0), 9),
    ALP(new Coordinate(2503, 3636, 0), 4),
    ALQ(new Coordinate(3597, 3495, 0), 7),
    ALS(new Coordinate(2644, 3495, 0), 5),
    BIP(new Coordinate(3410, 3324, 0), 48),
    BIQ(new Coordinate(3251, 3095, 0), 51),
    BIS(new Coordinate(2635, 3266, 0), 49),
    BJP(new Coordinate(2267, 2976, 0), 60),
    BJS(new Coordinate(2150, 3070, 0), 61),
    BKP(new Coordinate(2385, 3035, 0), 56),
    BKR(new Coordinate(3469, 3431, 0), 58),
    BLP(new Coordinate(2437, 5126, 0), 52),
    BLR(new Coordinate(2740, 3351, 0), 54),
    BLS(new Coordinate(1295, 3493, 0), 53),
    CIP(new Coordinate(2513, 3884, 0), 32),
    CIQ(new Coordinate(2528, 3127, 0), 35),
    CIR(new Coordinate(1302, 3762, 0), 34),
    CIS(new Coordinate(1638, 3868, 0), 33),
    CJR(new Coordinate(2705, 3576, 0), 46),
    CKR(new Coordinate(2801, 3003, 0), 42),
    CKS(new Coordinate(3447, 3470, 0), 41),
    CLP(new Coordinate(3082, 3206, 0), 36),
    CLR(new Coordinate(2740, 2738, 0), 38),
    CLS(new Coordinate(2682, 3081, 0), 37),
    DIP(new Coordinate(3037, 4763, 0), 16),
    DIS(new Coordinate(3108, 3149, 0), 17),
    DJP(new Coordinate(2658, 3230, 0), 28),
    DJR(new Coordinate(1455, 3658, 0), 30),
    DKP(new Coordinate(2900, 3111, 0), 24),
    DKR(new Coordinate(3129, 3496, 0), 26),
    DKS(new Coordinate(2744, 3719, 0), 25),
    DLQ(new Coordinate(3423, 3016, 0), 23),
    DLR(new Coordinate(2213, 3099, 0), 22),
    ;

    @Getter
    private final Coordinate position;
    private final int transformId;

    /**
     * Look up the FairyRing instance represented by the provided {@code code}.
     */
    @Nullable
    public static FairyRing byCode(@NonNull String code) {
        for (var ring : values()) {
            if (code.equals(ring.name())) {
                return ring;
            }
        }
        return null;
    }

    /**
     * Presses the "Confirm" button on the combination configuration screen.
     */
    public static boolean confirm() {
        final var component = getConfirmButton();
        return component != null
                && component.interact("Confirm")
                && Execution.delayWhile(() -> getConfirmButton() != null, 600);
    }

    /**
     * Whether the combination configuration screen is open.
     */
    public static boolean isConfigurationOpen() {
        return getConfirmButton() != null;
    }

    /**
     * Returns the nearest Fairy ring GameObject, if one is available.
     */
    @Nullable
    public static GameObject getNearestObject() {
        return GameObjects.newQuery()
                .names("Fairy ring")
                .types(GameObject.Type.PRIMARY)
                .filter(g -> {
                    final var definition = g.getDefinition();
                    final var controller = definition != null ? definition.getStateVarbit() : null;
                    return controller != null && controller.getId() == VarbitID.FAIR_RING_LAST_DESTINATION.getId();
                })
                .results()
                .nearest();
    }

    private static InterfaceComponent getConfirmButton() {
        return Interfaces.newQuery()
                .containers(398)
                .actions("Confirm")
                .results()
                .first();
    }

    public boolean isPreviousDestination() {
        final var varbit = Varbits.load(VarbitID.FAIR_RING_LAST_DESTINATION.getId());
        return varbit != null && varbit.getValue() == transformId;
    }

    /**
     * @return true if the code combination is selected and the confirm button is pressed.
     */
    public boolean select() {
        if (!isConfigurationOpen()) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            var dial = Dial.values()[i];
            if (!dial.select(name().charAt(i))) {
                return false;
            }
        }

        return confirm();
    }

    @Override
    public String toString() {
        return "FairyRing(" + name() + ")";
    }


    @RequiredArgsConstructor
    public enum Dial {
        LEFT(VarbitID.FAIRY_RING_DIAL_ADCB, new Character[]{'A', 'D', 'C', 'B'}),
        MIDDLE(VarbitID.FAIRY_RING_DIAL_ILJK, new Character[]{'I', 'L', 'K', 'J'}),
        RIGHT(VarbitID.FAIRY_RING_DIAL_PSRQ, new Character[]{'P', 'S', 'R', 'Q'});

        private static final String CLOCKWISE = "Rotate clockwise";
        private static final String COUNTER_CLOCKWISE = "Rotate counter-clockwise";

        private final VarbitID varbit;
        private final Character[] values;

        public int getCurrentValue() {
            final var varbit = Varbits.load(this.varbit.getId());
            return varbit != null ? varbit.getValue() : -1;
        }

        public boolean isSelected(char character) {
            final var current = getCurrentValue();
            return current != -1 && current == indexOf(character);
        }

        public boolean select(char character) {
            if (isSelected(character)) {
                return true;
            }

            final var turns = Math.abs(getCurrentValue() - indexOf(character));
            return rotate(turns, turns <= 2) && Execution.delayUntil(() -> isSelected(character), 1200);
        }

        private int indexOf(char letter) {
            return Arrays.asList(values).indexOf(letter);
        }

        private boolean rotate(int turns, boolean clockwise) {
            final var action = clockwise ? CLOCKWISE : COUNTER_CLOCKWISE;
            final var component = getComponent(action);
            if (component == null) {
                return false;
            }

            final var start = getCurrentValue();
            for (int i = 0; i < turns; i++) {
                if (!component.interact(action)) {
                    return false;
                }
            }
            return Execution.delayUntil(() -> getCurrentValue() != start, 1200);
        }

        private InterfaceComponent getComponent(String action) {
            final var results = Interfaces.newQuery()
                    .containers(398)
                    .types(InterfaceComponent.Type.BOX)
                    .grandchildren(false)
                    .actions(action)
                    .results();

            return results.size() < 3 ? null : results.get(ordinal());
        }
    }

}
