package com.neck_flexed.scripts.common.breaking;

import com.runemate.ui.setting.annotation.open.Setting;
import com.runemate.ui.setting.annotation.open.SettingsGroup;
import com.runemate.ui.setting.annotation.open.Suffix;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup(group = "Breaking")
public interface BreakSettings extends Settings {

    String BREAKING = "Breaking";

    @Setting(key = "break_enabled", title = "Enable Breaking", order = 0, section = BREAKING)
    default boolean breakEnabled() {
        return false;
    }

    @Suffix(Suffix.MINUTES)
    @Setting(key = "TBB_Max", title = "Time Between Breaks (max)", order = 1, section = BREAKING)
    default int timeBetweenMax() {
        return 90;
    }

    @Suffix(Suffix.MINUTES)
    @Setting(key = "TBB_Min", title = "Time Between Breaks (min)", order = 2, section = BREAKING)
    default int timeBetweenMin() {
        return 45;
    }

    @Suffix(Suffix.MINUTES)
    @Setting(key = "Length_Max", title = "Break Length (max)", order = 3, section = BREAKING)
    default int breakLengthMax() {
        return 20;
    }

    @Suffix(Suffix.MINUTES)
    @Setting(key = "Length_Min", title = "Break Length (min)", order = 4, section = BREAKING)
    default int breakLengthMin() {
        return 10;
    }

    @Setting(key = "stopping_enabled", title = "Enable Stopping", order = 5, section = BREAKING)
    default boolean stoppingEnabled() {
        return false;
    }

    @Suffix(Suffix.MINUTES)
    @Setting(key = "StopAfter", title = "Stop After", order = 6, section = BREAKING)
    default int stopAfter() {
        return 600;
    }

}
