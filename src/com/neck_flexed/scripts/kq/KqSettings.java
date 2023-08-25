package com.neck_flexed.scripts.kq;

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
public interface KqSettings extends Settings, BreakSettings, HouseSettingsFairyRing, LootSettings {


    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 3)
    String breaking = "Breaking";

    @SettingsSection(title = "Consumeable", description = "Settings for stat boosts", order = 1)
    String Consumeable = "Consumeables";

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "Phase1Boost", title = "Phase 1 Boost", order = 1, section = Consumeable)
    default Boost phase1Boost() {
        return Boost.Combat;
    }


    @Setting(key = "Phase1Boost_Dose", title = "Phase 1 Boost Doses", order = 2, section = Consumeable)
    default int phase1BoostDose() {
        return 4;
    }


    @Setting(key = "reboost1", title = "Phase1: Reboost if Boost under", order = 3, section = Consumeable)
    default int reboost1() {
        return 10;
    }

    @Setting(key = "Phase2Boost", title = "Phase 2 Boost", order = 4, section = Consumeable)
    default Boost phase2Boost() {
        return Boost.Ranging;
    }

    @Setting(key = "Phase2BoostDose", title = "Phase 2 Boost Doses", order = 5, section = Consumeable)
    default int phase2BoostDoses() {
        return 4;
    }

    @Setting(key = "reboost2", title = "Phase2: Reboost if Boost under", order = 6, section = Consumeable)
    default int reboost2() {
        return 8;
    }

    @Setting(key = "Antipoison", title = "Antipoison", order = 5, section = Consumeable)
    default Antipoison antipoison() {
        return Antipoison.ANTIPOISON;
    }

    @Setting(key = "AntipoisonDose", title = "AntipoisonDoses", order = 6, section = Consumeable)
    default int antipoisonDoses() {
        return 4;
    }

    @Setting(key = "Food", title = "Food", order = 3, section = Consumeable)
    default Food food() {
        return Food.Shark;
    }

    @Setting(key = "MinFood", title = "Minimum Food for kill", order = 4, section = Consumeable)
    default int minFood() {
        return 8;
    }

    @Setting(key = "Phase1Equipment", title = "Phase 1 Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 5)
    default EquipmentLoadout phase1Equipment() {
        return null;
    }

    @Setting(key = "Phase2Equipment", title = "Phase 2 Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 7)
    default EquipmentLoadout phase2Equipment() {
        return null;
    }


    @Setting(key = "Spec", title = "Spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 8)
    default EquipmentLoadout specEquipment() {
        return null;
    }
}
