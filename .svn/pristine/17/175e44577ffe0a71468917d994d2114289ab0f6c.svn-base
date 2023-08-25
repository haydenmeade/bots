package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.local.Varbit;
import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


@Getter
public enum Antifire {
    None(""),
    ANTIFIRE("Antifire potion"),
    EXTENDED_ANTIFIRE("Extended antifire"),
    SUPER_ANTIFIRE("Super antifire potion"),
    EXTENDED_SUPER_ANTIFIRE("Extended super antifire"),
    ;
    /**
     * Antifire timer
     * Number of game ticks remaining on antifire in intervals of 30; for a value X there are 30 * X game ticks remaining.
     * The antifire expires once this reaches 0.
     */
    private static final int VARBIT_ANTIFIRE = 3981;
    /**
     * Super Antifire timer
     * Number of game ticks remaining on super antifire in intervals of 20; for a value X there are 20 * X game ticks remaining.
     * The super antifire expires once this reaches 0.
     */
    private static final int VARBIT_SUPER_ANTIFIRE = 6101;
    private static final Varbit superAntifireProtection = Varbits.load(VARBIT_SUPER_ANTIFIRE);
    private static final Varbit antifireProtection = Varbits.load(VARBIT_ANTIFIRE);
    private final Pattern pattern;
    private final Pattern patternAnyDose;
    private final String name;

    Antifire(String name) {
        this.name = name;
        this.pattern = Pattern.compile(name + "\\(4\\)");
        this.patternAnyDose = Pattern.compile(name + ".*", Pattern.CASE_INSENSITIVE);
    }

    public static boolean hasProtection() {
        var superAntiProtection = superAntifireProtection.getValue();
        var antiProtection = antifireProtection.getValue();
        return superAntiProtection > 0 || antiProtection > 0;
    }

    public static boolean consumeIfRequired() {
        if (hasProtection()) return true;
        var f = Inventory
                .getItems(Arrays.stream(Antifire.values())
                        .filter(a -> !Objects.equals(a, None))
                        .map(Antifire::getPatternAnyDose).toArray(Pattern[]::new)).first();
        if (f == null) return false;
        util.consume(f, "Drink");
        return Execution.delayUntil(Antifire::hasProtection, 450, 650);
    }

    public static boolean hasAny() {
        return Inventory.containsAnyOf(Arrays.stream(Antifire.values()).filter(a -> !Objects.equals(a, None)).map(Antifire::getPatternAnyDose).toArray(Pattern[]::new));
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Pattern pattern() {
        return pattern;
    }

    public Pattern patternAny() {
        return pattern;
    }

    public @NotNull Map<Pattern, Integer> getBank(int amount) {
        var m = new HashMap<Pattern, Integer>();
        if (!this.equals(None))
            m.put(pattern, amount);
        return m;
    }
}
