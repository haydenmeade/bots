package com.neck_flexed.scripts.common.traverse;


import com.neck_flexed.scripts.common.HouseConfig;
import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsGroup;
import com.runemate.ui.setting.annotation.open.SettingsSection;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup(group = "House")
public interface HouseSettingsFairyRing extends Settings {
    @SettingsSection(title = "House", description = "Settings for house setup", order = 2)
    String HOUSE = "House";

    @Setting(key = "HouseOrnPool", title = "House has ornate pool", order = 1, section = HOUSE)
    default boolean hasOrnatePool() {
        return true;
    }

    @Setting(key = "HouseOrnateLewellery_E", title = "House Jewellery Box", order = 2, section = HOUSE)
    default HouseConfig.JewelleryBox jewelleryBox() {
        return HouseConfig.JewelleryBox.Ornate;
    }

    @Setting(key = "HouseFairyRing", title = "House has fairy ring", order = 3, section = HOUSE)
    default boolean hasHouseFairyRing() {
        return true;
    }

}
