package com.neck_flexed.scripts.hydra;

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
public interface HydraSettings extends Settings, BreakSettings, HouseSettingsFairyRing, LootSettings {

    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 2)
    String breaking = "Breaking";

    @SettingsSection(title = "Consumeable", description = "Settings for stat boosts", order = 1)
    String Consumeable = "Consumeables";

    @Setting(key = "Boost", title = "Boost", order = 0, section = Consumeable)
    default Boost boost() {
        return Boost.Ranging;
    }

    @Setting(key = "BoostAmnt", title = "Boost Amount", order = 1, section = Consumeable)
    default int boostAmount() {
        return 4;
    }

    @Setting(key = "reboost", title = "Reboost if Boost under", order = 3, section = Consumeable)
    default int reboost() {
        return 10;
    }

    @Setting(key = "Food", title = "Food", order = 4, section = Consumeable)
    default Food food() {
        return Food.Shark;
    }

    @Setting(key = "FoodAmn", title = "Food Amount", order = 5, section = Consumeable)
    default int foodAmount() {
        return 8;
    }

    @Setting(key = "minfood", title = "Min food for kill", order = 6, section = Consumeable)
    default int minFood() {
        return 1;
    }

    @Setting(key = "Antipoison", title = "Antipoison", order = 7, section = Consumeable)
    default Antipoison antipoison() {
        return Antipoison.ANTIDOTEPP;
    }

    @Setting(key = "AntipoisonAmount", title = "Antipoison Amount", order = 8, section = Consumeable)
    default int antipoisonAmount() {
        return 2;
    }

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "bosFlick", title = "Bring bracelets of slaughter to flick", order = 3)
    default int braceletsOfSlaughter() {
        return 2;
    }

    @Setting(key = "Bones", title = "Bury, Bank or ignore bones", order = 4, section = LOOTING)
    default BoneStrategy boneStrategy() {
        return BoneStrategy.Bury;
    }

    @Setting(key = "Equipment", title = "Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 6)
    default EquipmentLoadout equipment() {
        return null;
    }

    @Setting(key = "Spec", title = "Spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 7)
    default EquipmentLoadout specEquipment() {
        return null;
    }

    @Setting(key = "Housebattlefronte", title = "House has battlefront teleport", order = 4, section = HOUSE)
    default boolean hasHousePortalBattlefront() {
        return true;
    }


}
