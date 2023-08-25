package com.neck_flexed.scripts.sarachnis;

import com.neck_flexed.scripts.common.Boost;
import com.neck_flexed.scripts.common.Food;
import com.neck_flexed.scripts.common.breaking.BreakSettings;
import com.neck_flexed.scripts.common.loot.LootSettings;
import com.neck_flexed.scripts.common.traverse.HouseSettings;
import com.runemate.game.api.hybrid.EquipmentLoadout;
import com.runemate.game.api.osrs.location.WorldRegion;
import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsGroup;
import com.runemate.ui.setting.annotation.open.SettingsSection;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup()
public interface SarachnisSettings extends Settings, BreakSettings, LootSettings {
    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 3)
    String breaking = "Breaking";

    @SettingsSection(title = HouseSettings.HOUSE, description = "Settings for house setup", order = 2)
    String House = HouseSettings.HOUSE;

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "Boost", title = "Stat Boost", order = 1)
    default Boost boost() {
        return Boost.Combat;
    }

    @Setting(key = "BoostCount", title = "Stat Boost Amount", order = 2)
    default int boostAmount() {
        return 1;
    }

    @Setting(key = "Food", title = "Food", order = 3)
    default Food food() {
        return Food.Shark;
    }


    @Setting(key = "MinFood", title = "Min Food for kill", order = 4)
    default int minFood() {
        return 4;
    }

    @Setting(key = "Slasher", title = "Web slasher", order = 5)
    default SlashThing slashThing() {
        return SlashThing.WildySword;
    }

    @Setting(key = "FightStrategy", title = "Fight Strategy", order = 8)
    default FightStrategy fightStrategy() {
        return FightStrategy.Default;
    }


    @Setting(key = "Equipment", title = "Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 9)
    default EquipmentLoadout equipment() {
        return null;
    }

    @Setting(key = "Spec", title = "Spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 10)
    default EquipmentLoadout specEquipment() {
        return null;
    }

    @Setting(key = "reboost", title = "Reboost if Boost under", order = 12)
    default int reboost() {
        return 10;
    }

    @Setting(key = "House1", title = "House has ornate pool", order = 1, section = HouseSettings.HOUSE)
    default boolean hasOrnatePool() {
        return true;
    }

    @Setting(key = "House2", title = "House has ornate jewellery box", order = 2, section = HouseSettings.HOUSE)
    default boolean hasOrnateJewelleryBox() {
        return true;
    }

    @Setting(key = "House5", title = "House has mounted Xeric's talisman", order = 5, section = HouseSettings.HOUSE)
    default boolean hasMountedXerics() {
        return true;
    }

}
