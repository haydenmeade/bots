package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.util.Items;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum LightSource {
    None("", "None"),
    KandarinHeadgear("Kandarin headgear.*", "Kandarin headgear"),
    FiremakingSkillcape("Firemaking cape.*", "Firemaking cape"),
    BrumaTorch("Bruma torch", "Bruma torch"),
    BullseyeLantern("Bullseye lantern", "Bullseye lantern"),
    OilLantern("Oil Lantern", "Oil Lantern"),
    MiningHelmet("Mining Helmet", "Mining Helmet"),
    AbyssalLantern("Abyssal lantern.*", "Abyssal lantern");

    private final Pattern pattern;
    private String name;

    LightSource(String s, String name) {
        this.pattern = Pattern.compile(s);
        this.name = name;
    }

    public static boolean hasAny() {
        var pttns = Arrays.stream(LightSource.values()).filter(l -> !l.equals(None)).map(LightSource::getPattern).toArray(Pattern[]::new);
        return Items.containsAnyOf(util.inventoryEquipmentSource(), pttns);
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
