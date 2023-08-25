package com.neck_flexed.scripts.sire;

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

@SettingsGroup(group = "Sire")
public interface SireSettings extends Settings, BreakSettings, HouseSettingsFairyRing, LootSettings {


    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 2)
    String breaking = "Breaking";

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "Phase1Boost", title = "Phase 1 Boost", order = 1)
    default Boost phase1Boost() {
        return Boost.Ranging;
    }

    @Setting(key = "Phase2Boost", title = "Phase 2 Boost", order = 2)
    default Boost phase2Boost() {
        return Boost.Super_Set;
    }


    @Setting(key = "Antipoison2", title = "Antipoison", order = 3)
    default Antipoison antipoison() {
        return Antipoison.ANTIDOTEPP;
    }

    @Setting(key = "Food", title = "Food", order = 4)
    default Food food() {
        return Food.Shark;
    }

    @Setting(key = "MinFood", title = "Min Food for kill", order = 4)
    default int minFood() {
        return 3;
    }

    @Setting(key = "phase1Equipment", title = "Phase 1 Equipment (Ranged/Mage)", converter = EquipmentLoadout.SettingConverter.class, order = 6)
    default EquipmentLoadout phase1Equipment() {
        return null;
    }

    @Setting(key = "phase2Equipment", title = "Phase 2 Equipment (Melee)", converter = EquipmentLoadout.SettingConverter.class, order = 7)
    default EquipmentLoadout phase2Equipment() {
        return null;
    }

    @Setting(key = "defence lower spec", title = "Defence lowering spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 8)
    default EquipmentLoadout specDefenceEquipment() {
        return null;
    }

    @Setting(key = "dpsspec", title = "DPS Spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 9)
    default EquipmentLoadout specDpsEquipment() {
        return null;
    }

    @Setting(key = "LootAshes", title = "Loot Ashes", order = 11, section = LOOTING)
    default boolean lootAshes() {
        return true;
    }

    @Setting(key = "FightStrategy", title = "Fight Strategy", order = 12)
    default FightStrategy fightStrategy() {
        return FightStrategy.RestoreEveryKill;
    }

    @Setting(key = "reboost", title = "Reboost if Boost under", order = 15)
    default int reboost() {
        return 10;
    }

    @Setting(key = "initiate_with_stun", title = "Initiate fight with stun", order = 16, description = "Initiate fight with stun if you have a low range weapon")
    default boolean initiateWithStun() {
        return false;
    }
}
