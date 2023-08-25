package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.breaking.BreakSettings;
import com.neck_flexed.scripts.common.loot.LootSettings;
import com.neck_flexed.scripts.common.traverse.HouseSettings;
import com.runemate.game.api.hybrid.EquipmentLoadout;
import com.runemate.game.api.osrs.location.WorldRegion;
import com.runemate.ui.setting.annotation.open.*;
import com.runemate.ui.setting.open.Settings;

@SettingsGroup(group = "slayer")
public interface SlayerSettings extends Settings, BreakSettings, HouseSettings, LootSettings {


    @SettingsSection(title = "Breaking", description = "Settings for breaking and stopping", order = 99)
    String breaking = "Breaking";
    @SettingsSection(title = "Advanced", description = "Advanced", order = 98)
    String advanced = "Advanced";

    @SettingsSection(title = "Slayer", description = "Settings for slayer", order = 1)
    String slayer = "Slayer";
    @SettingsSection(title = "Consumable", description = "Settings for consumables", order = 3)
    String consumable = "Consumable";

    @SettingsSection(title = "Barrage", description = "Settings for barraging/bursting", order = 5)
    String barrage = "Barrage/Burst";

    @SettingsSection(title = "Loadouts", description = "Settings for loadouts", order = 4)
    String loadouts = "Loadouts";

    @SettingsSection(title = "Turael", description = "Settings for loadouts", order = 6)
    String turael = "Turael";

    @Setting(key = "Region", title = "World Region", order = 0)
    default WorldRegion worldRegion() {
        return WorldRegion.AUSTRALIA;
    }

    @Setting(key = "preferHouseTraverse", title = "Prefer house traverse", order = 2)
    default boolean preferHouseTraverse() {
        return true;
    }

    @Setting(key = "misc_items", title = "Bring Misc Items (e.g.Herb Sack)", order = 3, description = "Comma separated list")
    @Multiline
    default String miscItems() {
        return "";
    }

    @Setting(key = "bonecrusher", title = "Bring Bonecrusher", order = 4)
    default boolean bonecrusher() {
        return true;
    }

    @Setting(key = "ashsancifier", title = "Bring Ash Sanctifier", order = 5)
    default boolean ashSanctifier() {
        return true;
    }

    @Setting(key = "alch_items", title = "Alch Item List", order = 6, description = "Comma separated list")
    @Multiline
    default String alchItems() {
        return "Rune kiteshield\n" +
                "Rune 2h.*\n" +
                "Adamant 2h.*\n" +
                "Mithril 2h.*\n" +
                "Runite limb.*\n" +
                "Red d'hid.*\n" +
                ".*med helm\n" +
                "Rune axe\n" +
                "Adamant plate.*\n" +
                "Mithril plate.*\n" +
                "Adamant full.*\n" +
                "Mithril full.*\n" +
                "Rune full.*";
    }

    @Setting(key = "npccontact", title = "Use NPC contact to contact slayer master", order = 7)
    default boolean npcContactEnabled() {
        return false;
    }

    @Setting(key = "burybones", title = "Bury bones / Scatter ashes", order = 8)
    default boolean buryBones() {
        return false;
    }

    @Setting(key = "MeleeBoost", title = "Melee Stat Boost", order = 1, section = consumable)
    default Boost meleeBoost() {
        return Boost.Super_Set;
    }

    @Setting(key = "MogeBoost", title = "Mage Stat Boost", order = 2, section = consumable)
    default Boost mageBoost() {
        return Boost.None;
    }


    @Setting(key = "RangeBoost", title = "Range Stat Boost", order = 3, section = consumable)
    default Boost rangeBoost() {
        return Boost.Ranging;
    }

    @Setting(key = "reboostMelee", title = "(Melee) Reboost if Boost under", order = 4, section = consumable)
    default int reboostMelee() {
        return 6;
    }

    @Setting(key = "reboostMage", title = "(Mage) Reboost if Boost under", order = 5, section = consumable)
    default int reboostMage() {
        return 2;
    }

    @Setting(key = "reboostRange", title = "(Range) Reboost if Boost under", order = 6, section = consumable)
    default int reboostRange() {
        return 3;
    }

    @Setting(key = "bank_boosts", title = "Bring 1 potion every x monsters", order = 7, section = consumable)
    default int bankBoostsPer() {
        return 151;
    }

    @Setting(key = "food", title = "Food", order = 8, section = consumable)
    default Food food() {
        return Food.Shark;
    }

    @Setting(key = "antipoison", title = "Antipoison", order = 9, section = consumable)
    default Antipoison antipoison() {
        return Antipoison.ANTIDOTEPP;
    }

    @Setting(key = "antifire", title = "Antifire", order = 10, section = consumable)
    default Antifire antifire() {
        return Antifire.ANTIFIRE;
    }


    @Setting(key = "slayer_master", title = "Slayer Master", order = 1, section = slayer)
    default SlayerMaster master() {
        return SlayerMaster.DURADEL;
    }

    @Setting(key = "useCannon", title = "Use Cannon", order = 2, section = slayer)
    default boolean useCannon() {
        return false;
    }

    @Setting(key = "doBarrage", title = "Barrage / burst tasks (Requires Ancient Spellbook Active)", order = 3, section = slayer)
    default boolean doBarrageTasks() {
        return false;
    }

    @Setting(key = "doskip", title = "Skip tasks", order = 4, section = slayer)
    default boolean skipTasksEnabled() {
        return true;
    }

    @DependsOn(group = "slayer", key = "doskip", value = "true")
    @Setting(key = "doskip_unhandler", title = "Skip unhandled tasks", order = 5, section = slayer)
    default boolean skipUnhandled() {
        return true;
    }


    @DependsOn(group = "slayer", key = "doskip", value = "true")
    @Setting(key = "doskip_turael", title = "Turael skip unhandled tasks", order = 6, section = slayer)
    default boolean skipTasksWithTurael() {
        return false;
    }

    @Setting(key = "skip_taskslist", title = "Skip other tasks (comma separated regex list)", order = 7, section = slayer)
    @DependsOn(group = "slayer", key = "doskip", value = "true")
    default String skipTaskList() {
        return "Iron dragons";
    }


    @Setting(key = "stop_on_tasks", title = "Stop on tasks (comma separated regex list)", order = 8, section = slayer)
    default String stopOnTasks() {
        return "Hydras,Alchemical Hydra,Vorkath";
    }

    @Setting(key = "SkipBoss", title = "Stop on any boss task", order = 9, section = slayer)
    default boolean stopOnAnyBoss() {
        return false;
    }

    @Setting(key = "SlaughterTasks", title = "Tasks to use slaughter bracelets on", order = 10, section = slayer)
    @Multiline
    default String slaughterTasks() {
        return "";
    }

    @Setting(key = "ExpeditiousTasks", title = "Tasks to use expeditious bracelets on", order = 11, section = slayer)
    @Multiline
    default String expeditiousTasks() {
        return "";
    }

    @Setting(key = "Melee Equipment", title = "Melee Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 1, section = loadouts)
    default EquipmentLoadout meleeEquipment() {
        return null;
    }

    @Setting(key = "MeleeSpec Equipment", title = "Melee Spec Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 2, section = loadouts)
    default EquipmentLoadout meleeSpecEquipment() {
        return null;
    }

    @Setting(key = "Leaf Bladed Equipment", title = "Leaf Bladed Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 3, section = loadouts)
    default EquipmentLoadout leafEquipment() {
        return null;
    }

    @Setting(key = "Dragon Equipment", title = "Dragon Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 4, section = loadouts)
    default EquipmentLoadout dragonEquipment() {
        return null;
    }

    @Setting(key = "Wyvern Equipment", title = "Wyvern Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 5, section = loadouts)
    default EquipmentLoadout wyvernEquipment() {
        return null;
    }

    @Setting(key = "WyrmDrake Equipment", title = "Wyrm/Drake Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 6, section = loadouts)
    default EquipmentLoadout wyrmDrakeEquipment() {
        return null;
    }

    @Setting(key = "LizardShaman Equipment", title = "Lizardman Shaman Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 7, section = loadouts)
    default EquipmentLoadout lizardmanShaman() {
        return null;
    }

    @Setting(key = "Waterfiend Equipment", title = "Waterfiend Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 8, section = loadouts)
    default EquipmentLoadout waterfiendEquipment() {
        return null;
    }

    @Setting(key = "Kraken Equipment", title = "Kraken Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 9, section = loadouts)
    default EquipmentLoadout krakenEquipment() {
        return null;
    }

    @DependsOn(group = "slayer", key = "doBarrage", value = "true")
    @Setting(key = "spell", title = "Spell", order = 4, section = barrage)
    default NeckSpell spell() {
        return NeckSpell.IceBarrage;
    }

    @DependsOn(group = "slayer", key = "doBarrage", value = "true")
    @Setting(key = "Barrage Equipment", title = "Barrage Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 8, section = barrage)
    default EquipmentLoadout barrageEquipment() {
        return null;
    }

    @DependsOn(group = "slayer", key = "doBarrage", value = "true")
    @Setting(key = "BarrageLure", title = "Lure Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 9, section = barrage)
    default EquipmentLoadout barrageLureEquipment() {
        return null;
    }


    @Setting(key = "slayer_toggle", title = "Slayer Mode", order = 98)
    default boolean slayerMode() {
        return true;
    }

    @Setting(key = "dmonster2", title = "Manually Set Monster", order = 99)
    @DependsOn(key = "slayer_toggle", value = "false", group = "slayer")
    default Task debugMonster() {
        return Task.ABYSSAL_DEMONS;
    }

    @Setting(key = "EquipmentTurael", title = "Turael Skip Equipment", converter = EquipmentLoadout.SettingConverter.class, order = 8, section = turael)
    @DependsOn(group = "slayer", key = "doskip_turael", value = "true")
    default EquipmentLoadout turaelSkipEquipment() {
        return null;
    }

    @Setting(key = "ignore_players", title = "Ignore players hop checking", order = 1, description = "Comma separated list (case insensitive)", section = advanced)
    default String ignorePlayers() {
        return "";
    }

}
