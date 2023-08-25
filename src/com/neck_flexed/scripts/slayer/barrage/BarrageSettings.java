package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.Boost;
import com.neck_flexed.scripts.common.NeckSpell;
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
public interface BarrageSettings extends Settings, BreakSettings, HouseSettings, LootSettings {

    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 2)
    String breaking = "Breaking";
    @SettingsSection(title = "Advanced", description = "Advanced", order = 98)
    String advanced = "Advanced";

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "Boost", title = "Stat Boost", order = 1)
    default Boost boost() {
        return Boost.None;
    }

    @Setting(key = "BoostCount", title = "Stat Boost Amount", order = 2)
    default int boostAmount() {
        return 1;
    }

    @Setting(key = "mon_new", title = "BarrageMonster", order = 3)
    default BarrageTiles monster() {
        return BarrageTiles.DustDevil;
    }

    @Setting(key = "spell", title = "Spell", order = 4)
    default NeckSpell spell() {
        return NeckSpell.IceBarrage;
    }

    @Setting(key = "Equipment", title = "Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 8)
    default EquipmentLoadout equipment() {
        return null;
    }

    @Setting(key = "Lure", title = "Lure Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 9)
    default EquipmentLoadout lureEquipment() {
        return null;
    }

    @Setting(key = "reboost", title = "Reboost if Boost under", order = 10)
    default int reboost() {
        return 2;
    }

    @Setting(key = "ignore_players", title = "Ignore players hop checking", order = 1, description = "Comma separated list (case insensitive)", section = advanced)
    default String ignorePlayers() {
        return "";
    }
}
