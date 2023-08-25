package com.neck_flexed.scripts.common.loot;

import com.runemate.ui.setting.annotation.open.Multiline;
import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsSection;
import com.runemate.ui.setting.open.Settings;

public interface LootSettings extends Settings {
    @SettingsSection(title = "Looting", description = "Settings for looting", order = 3)
    String LOOTING = "Looting";

    @Multiline
    @Setting(key = "always_loot", title = "Always Loot List", order = 1, description = "Comma/Newline separated Regex List (Case Insensitive), bot has defaults", section = LOOTING)
    default String alwaysLoot() {
        return "Rune .*,Abyssal.*,Dragon.*\n" +
                "Chaos rune,Blood rune,Nature rune,Law rune,Death rune,Soul rune,Dust rune,\n" +
                "Ensouled.*\n" +
                "Clue scroll.*,\n" +
                "Mist battlestaff,Dust battlestaff,Eternal gem,Imbued heart,Brimstone key,Ancient shard,Dark totem base,Dark totem middle,Dark totem top,\n" +
                "Grimy ranarr weed,Grimy avantoe,Grimy irit leaf,Grimy kwuarm,Grimy cadantine,Grimy lantadyme,Grimy dwarf weed,\n" +
                "Shield left half,Runite bar,Tooth half of key,Loop half of key,Dragonstone,\n" +
                "Torstol seed,Dwarf weed seed,Lantadyme seed,Cadantine seed,Snapdragon seed,Kwuarm seed,Avantoe seed,Irit seed,Toadflax seed,Ranarr seed,Snape grass seed,\n" +
                "Leaf-bladed sword,Mystic robe top (light),Leaf-bladed battleaxe,Long bone,Curved bone,.* battlestaff,Black d'hide vambraces,Mystic air staff,Mystic earth staff,Dragon chainbody,Granite legs,Dragon platelegs,Dragon plateskirt,Runite bolts,Adamant bolts,Runite crossbow (u),Draconic visage,Uncut diamond,Crystal shard,Battlestaff,Granite longsword,Granite boots,Runite ore,Calcite,Pyrophosphite,Volcanic ash,Unidentified.*,Wyvern visage,Granite maul,Mystic robe top (dark),Occult necklace,Dark bow,";
    }

    @Setting(key = "only_loot_noted", title = "Loot only if noted", order = 2, description = "Comma/Newline separated Regex List (Case Insensitive), bot has defaults", section = LOOTING)
    @Multiline
    default String onlyLootNoted() {
        return "Soft clay,White berries,Coconut,Adamantite bar,Mithril bar,Gold ore,Unpowered orb,Magic logs,Iron ore,Snape grass,Teak logs,\n";
    }

    @Multiline
    @Setting(key = "never_loot", title = "Never Loot List", order = 3, description = "Comma/Newline separated Regex List (Case Insensitive), bot has defaults", section = LOOTING)
    default String neverLoot() {
        return "Raw chicken,Raw beef,Bones\nSuqah.*";
    }

    @Setting(key = "minalchvaue", title = "Loot Minimum Alch Value", order = 5, section = LOOTING)
    default int minAlchValue() {
        return 0;
    }

}
