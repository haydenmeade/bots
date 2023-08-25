package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.util.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

public enum Boost {
    None("None", ""),
    Ranging("Ranging potion", "Ranging potion"),
    Ranging_Divine("Divine ranging potion", "Divine ranging potion"),
    ImbuedHeart("Imbued/Saturated heart", items.imbuedHeart.toString()),
    Combat("Super combat potion", "Super combat potion"),
    Combat_Divine("Divine super combat potion", "Divine super combat potion"),
    Super_Set("Super Set", "Super strength", "Super attack", "Super defence"),
    Super_Set_Divine("Divine Super Set", "Divine super defence potion", "Divine super attack potion", "Divine super strength potion"),
    ;

    private final Pattern[] patternAny;
    private final Pattern[] patternFull;
    private final String name;

    Boost(String name, String... patterns) {
        this.name = name;
        var patternsAny = new ArrayList<Pattern>();
        var patternsFull = new ArrayList<Pattern>();
        for (var p : patterns) {
            if (p == null || p.isEmpty()) break;
            patternsAny.add(Pattern.compile(p + ".*"));
            if (p.equals(items.imbuedHeart.toString()))
                patternsFull.add(Pattern.compile(p));
            else
                patternsFull.add(Pattern.compile(p + "\\(4\\)"));
        }
        this.patternAny = patternsAny.toArray(new Pattern[0]);
        this.patternFull = patternsFull.toArray(new Pattern[0]);
    }

    public @NotNull Map<Pattern, Integer> getBank(int amount) {
        return getBank(amount, false);
    }

    public @NotNull Map<Pattern, Integer> getBank(int amount, boolean ignoreDefence) {
        var m = new HashMap<Pattern, Integer>();
        for (var p : patternFull) {
            if (ignoreDefence && p.toString().toLowerCase().contains("defence")) continue;

            m.put(p, amount);
        }
        return m;
    }

    public boolean hasAny(boolean ignoreDefence) {
        return hasAny(Inventory.getItems(), ignoreDefence);
    }

    public boolean hasAny() {
        return hasAny(Inventory.getItems(), false);
    }

    private boolean hasAny(SpriteItemQueryResults items, boolean ignoreDefence) {
        var m = new HashSet<Pattern>();
        for (var p : patternAny) {
            if (ignoreDefence && p.toString().toLowerCase().contains("defence")) continue;

            m.add(p);
        }
        return Items.containsAnyOf(items, m.toArray(Pattern[]::new));
    }

    public Pattern[] patternFull() {
        return patternFull;
    }

    public Pattern[] patternAny() {
        return patternAny;
    }

    public int getDoses() {
        if (this.equals(None) || this.equals(ImbuedHeart)) return Integer.MAX_VALUE;
        return Inventory.getItems(this.patternAny).stream().map(item -> {
            try {
                return Integer.parseInt(item.getDefinition().getName().replaceAll("[^0-9]", ""));
            } catch (Exception e) {
                return 0;
            }
        }).reduce(Integer::sum).orElse(0);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
