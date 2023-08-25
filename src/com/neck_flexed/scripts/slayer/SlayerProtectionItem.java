package com.neck_flexed.scripts.slayer;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Regex;
import com.runemate.game.api.osrs.local.AchievementDiary;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Getter
public enum SlayerProtectionItem {
    None(Equipment.Slot.WEAPON),
    Nosepeg(true, Equipment.Slot.HEAD, "Nose peg"),
    SpinyHelmet(true, Equipment.Slot.HEAD, "Spiny helmet"),
    ReinforcedGoggles(true, Equipment.Slot.HEAD, "Reinforced goggles"),
    Facemask(true, Equipment.Slot.HEAD, "Facemask"),
    Earmuffs(true, Equipment.Slot.HEAD, "Earmuffs"),
    Flail(Equipment.Slot.WEAPON, "Blisterwood flail"),
    MirrorShield(Equipment.Slot.SHIELD, "Mirror shield", "V's shield"),
    InsulatedBoots(Equipment.Slot.FEET, "Insulated boots"),
    BootsOfStone(Equipment.Slot.FEET, (p) -> !AchievementDiary.KOUREND_AND_KEBOS.isEliteComplete(), "Boots of stone", "Boots of brimstone"),
    ;
    private final boolean slayerHelmet;
    private final Equipment.Slot slot;
    private final Pattern items;
    private final Predicate<Player> equipWhenCondition;

    SlayerProtectionItem(Equipment.Slot slot, String... gameNames) {
        this.slot = slot;
        this.items = Regex.getPatternForExactStrings(gameNames);
        this.slayerHelmet = false;
        this.equipWhenCondition = null;
    }

    SlayerProtectionItem(Equipment.Slot slot, Predicate<Player> equipWhenCondition, String... gameNames) {
        this.slot = slot;
        this.items = Regex.getPatternForExactStrings(gameNames);
        this.slayerHelmet = false;
        this.equipWhenCondition = equipWhenCondition;
    }

    SlayerProtectionItem(boolean slayerHelmet, Equipment.Slot slot, String... gameNames) {
        this.equipWhenCondition = null;
        this.slayerHelmet = slayerHelmet;
        this.slot = slot;
        if (!slayerHelmet)
            this.items = Regex.getPatternForExactStrings(gameNames);
        else
            this.items = getSlayerHelmRegex(gameNames);
    }


    private Pattern getSlayerHelmRegex(String[] strings) {
        StringJoiner joiner = new StringJoiner("|", "^", "$");
        joiner.add(".*Slayer helmet.*");
        for (String string : strings) {
            joiner.add(string);
        }
        return Pattern.compile(joiner.toString(), Pattern.CASE_INSENSITIVE);
    }

    public @NotNull Map<Pattern, Integer> getBank() {
        var m = new HashMap<Pattern, Integer>();
        if (this.equals(None)) return m;
        if (getEquipWhenCondition() != null && !getEquipWhenCondition().test(Players.getLocal()))
            return m;
        m.put(items, 1);
        return m;

    }

}