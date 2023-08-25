package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public enum Antipoison {
    None("", ""),
    ANTIDOTEPP("Antidote\\+\\+\\(4\\)", "Antidote++"),
    ANTIPOISON("Antipoison\\(4\\)", "Antipoison"),
    SUPER_ANTIPOISON("Superantipoison\\(4\\)", "Superantipoison"),
    ;
    private final Pattern pattern;
    private final String name;
    private final Pattern patternAnyDose;

    Antipoison(String pattern, String name) {
        this.pattern = Pattern.compile(pattern);
        this.patternAnyDose = Pattern.compile(name.replace("+", ".") + ".*");
        this.name = name;
    }

    public static boolean hasAny() {
        return Inventory.containsAnyOf(Arrays.stream(Antipoison.values()).filter(a -> !Objects.equals(a, None)).map(Antipoison::getPatternAnyDose).toArray(Pattern[]::new));
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Pattern pattern() {
        return pattern;
    }

    public @NotNull Map<Pattern, Integer> getBank(int amount) {
        var m = new HashMap<Pattern, Integer>();
        if (!this.equals(None))
            m.put(pattern, amount);
        return m;
    }
}
