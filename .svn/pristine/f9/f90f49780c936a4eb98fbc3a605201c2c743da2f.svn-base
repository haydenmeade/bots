package com.neck_flexed.scripts.ashes;

import com.neck_flexed.scripts.common.breaking.BreakSettings;
import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsGroup;
import com.runemate.ui.setting.annotation.open.SettingsSection;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup()
public interface AshSettings extends Settings, BreakSettings {

    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 2)
    String breaking = "Breaking";

    @Setting(key = "Ashes", title = "Ashes To Offer", order = 0)
    default Ash ashes() {
        return Ash.All;
    }
}
