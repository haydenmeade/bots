package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsGroup;
import com.runemate.ui.setting.annotation.open.SettingsSection;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup(group = "House")
public interface HouseSettings extends Settings {

    @SettingsSection(title = "House", description = "Settings for house setup", order = 2)
    String HOUSE = "House";

    @Setting(key = "HouseOrnatePool", title = "House has ornate pool", order = 1, section = HOUSE)
    default boolean hasOrnatePool() {
        return true;
    }

    @Setting(key = "HouseOrnateLewellery_E", title = "House Jewellery Box", order = 2, section = HOUSE)
    default HouseConfig.JewelleryBox jewelleryBox() {
        return HouseConfig.JewelleryBox.Ornate;
    }


    @Setting(key = "HouseAltar", title = "House Altar (Experimental)", order = 4, section = HOUSE)
    default HouseConfig.Altar altar() {
        return HouseConfig.Altar.None;
    }

    @Setting(key = "HouseFairy", title = "House has fairy ring", order = 3, section = HOUSE)
    default boolean hasHouseFairyRing() {
        return true;
    }

    @Setting(key = "HouseSpirit", title = "House has Spirit Tree", order = 4, section = HOUSE)
    default boolean hasHouseSpiritTree() {
        return false;
    }

    @Setting(key = "HouseDigsite", title = "House has mounted digsite pendant", order = 4, section = HOUSE)
    default boolean hasHouseMountedDigsite() {
        return false;
    }

    @Setting(key = "Housexeric", title = "House has mounted Xeric's talisman", order = 5, section = HOUSE)
    default boolean hasMountedXerics() {
        return false;
    }

    @Setting(key = "Housedraynor", title = "House portal: Draynor Manor", order = 6, section = HOUSE)
    default boolean hasPortalDraynor() {
        return false;
    }

    @Setting(key = "Housesenntisten", title = "House portal: Senntisten", order = 7, section = HOUSE)
    default boolean hasPortalSenntisten() {
        return false;
    }

    @Setting(key = "Houseardy", title = "House portal: Ardougne", order = 8, section = HOUSE)
    default boolean hasPortalArdougne() {
        return false;
    }

    @Setting(key = "Houselumby", title = "House portal: Lumbridge", order = 9, section = HOUSE)
    default boolean hasPortalLumbridge() {
        return false;
    }

    @Setting(key = "House10kourend", title = "House portal: KourendCastle", order = 10, section = HOUSE)
    default boolean hasPortalKourendCastle() {
        return false;
    }

    @Setting(key = "House11varrock", title = "House portal: Varrock", order = 11, section = HOUSE)
    default boolean hasPortalVarrock() {
        return false;
    }

    @Setting(key = "House11salve", title = "House portal: Salve Graveyard", order = 12, section = HOUSE)
    default boolean hasPortalSalveGraveyard() {
        return false;
    }

    @Setting(key = "HouseFalador", title = "House portal: Falador", order = 13, section = HOUSE)
    default boolean hasPortalFalador() {
        return false;
    }

    @Setting(key = "HouseLunar", title = "House portal: Lunar Isle", order = 14, section = HOUSE)
    default boolean hasPortalLunarIsle() {
        return false;
    }

    @Setting(key = "Housebattlefronte", title = "House portal: Battlefront", order = 15, section = HOUSE)
    default boolean hasHousePortalBattlefront() {
        return false;
    }

    @Setting(key = "housewaterbirthportal", title = "House portal: Waterbirth", order = 16, section = HOUSE)
    default boolean hasHousePortalWaterbirth() {
        return false;
    }

}
