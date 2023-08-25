package com.neck_flexed.scripts.dk;

import com.neck_flexed.scripts.common.Antipoison;
import com.neck_flexed.scripts.common.Boost;
import com.neck_flexed.scripts.common.Food;
import com.neck_flexed.scripts.common.breaking.BreakSettings;
import com.neck_flexed.scripts.common.loot.LootSettings;
import com.neck_flexed.scripts.common.traverse.HouseSettingsFairyRing;
import com.runemate.game.api.hybrid.EquipmentLoadout;
import com.runemate.game.api.osrs.location.WorldRegion;
import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsGroup;
import com.runemate.ui.setting.annotation.open.SettingsSection;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup()
public interface DkSettings extends Settings, BreakSettings, HouseSettingsFairyRing, LootSettings {

    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 2)
    String breaking = "Breaking";

    @SettingsSection(title = "Equipment", description = "Settings for Equipment", order = 1)
    String equipment = "Equipment";

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "BoostRanged", title = "Ranged Boost", order = 1)
    default Boost boostRanged() {
        return Boost.Ranging;
    }

    @Setting(key = "BoostMagic", title = "Magic Boost", order = 2)
    default Boost boostMagic() {
        return Boost.None;
    }

    @Setting(key = "BoostMelee", title = "Melee Boost", order = 3)
    default Boost boostMelee() {
        return Boost.Combat;
    }

    @Setting(key = "BoostCount", title = "Stat Boost Amount", order = 4)
    default int boostAmount() {
        return 1;
    }

    @Setting(key = "Food", title = "Food", order = 5)
    default Food food() {
        return Food.Shark;
    }

    @Setting(key = "Antipoison", title = "Antipoison", order = 6)
    default Antipoison antipoison() {
        return Antipoison.ANTIDOTEPP;
    }

    @Setting(key = "AntipoisonAmount", title = "Antipoison Amount", order = 7)
    default int antipoisonAmount() {
        return 2;
    }

    @Setting(key = "Cave", title = "Use Slayer Cave?", order = 8)
    default boolean useSlayerCave() {
        return false;
    }


    @Setting(key = "TraverseStrategy", title = "TraverseStrategy", order = 10)
    default TraverseStrategy traverseStrategy() {
        return TraverseStrategy.FremennikBoots4;
    }

    @Setting(key = "FightStrategy", title = "Fight Strategy", order = 11)
    default FightStrategy fightStrategy() {
        return FightStrategy.Tribrid;
    }

    @Setting(key = "MeleeEquipment", title = "Melee Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 12, section = equipment)
    default EquipmentLoadout meleeEquipment() {
        return null;
    }

    @Setting(key = "RangedEquipment", title = "Ranged Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 13, section = equipment)
    default EquipmentLoadout rangedEquipment() {
        return null;
    }

    @Setting(key = "MagicEquipment", title = "Magic Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 14, section = equipment)
    default EquipmentLoadout magicEquipment() {
        return null;
    }

    @Setting(key = "Spec", title = "Spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 15, section = equipment)
    default EquipmentLoadout specEquipment() {
        return null;
    }

    @Setting(key = "Bury Bones", title = "Bury Bones (No elite frem)", order = 16, section = LOOTING)
    default boolean buryBones() {
        return false;
    }


    @Setting(key = "reboost", title = "Reboost if Boost under", order = 18)
    default int reboost() {
        return 10;
    }


    @Setting(key = "HouseWB", title = "House portal: Waterbirth Island", order = 11, section = HOUSE)
    default boolean hasPortalWaterbirth() {
        return true;
    }

    @Setting(key = "HouseL", title = "House portal: Lunar Isle", order = 12, section = HOUSE)
    default boolean hasPortalLunar() {
        return true;
    }
}
