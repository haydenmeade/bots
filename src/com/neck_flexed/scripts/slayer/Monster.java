package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.*;
import com.neck_flexed.scripts.common.traverse.*;
import com.neck_flexed.scripts.slayer.encounters.EncounterOverride;
import com.neck_flexed.scripts.slayer.encounters.NechryarchEncounter;
import com.neck_flexed.scripts.slayer.traverse.*;
import com.neck_flexed.scripts.slayer.turael.overrides.*;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.osrs.local.hud.interfaces.MinigameTeleport;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public class Monster implements SlayerMonster {

    private static Set<Monster> monsters;
    private final Task task;
    private final String monsterName;
    private final String[] superiorNames;
    private final String[] alternatives;
    private final Collection<Location> locations;
    private final @Nullable SlayerFinishItem finishItem;
    private final @Nullable SlayerProtectionItem protection;
    private final boolean isLeafBladed;
    private final boolean needsLightSource;
    private final EncounterOverride superiorEncounter;
    private final boolean isDragon;
    private final boolean isPoisonous;
    private final int food;
    private final boolean hoppingDisabled;
    private final boolean isWyvern;
    private final BoneType boneType;
    private final boolean isTuraelMonster;

    public synchronized static Set<Monster> getMonsters() {
        if (Monster.monsters == null) {
            Monster.monsters = buildMonsters();
        }
        return Monster.monsters;
    }

    public static Optional<SlayerMonster> getByEnum(Task task) {
        return getMonsters().stream()
                .filter(m -> m.getTask().equals(task))
                .map(m -> (SlayerMonster) m)
                .findFirst();
    }

    private synchronized static Set<Monster> buildMonsters() {
        return Set.of(
                new MonsterBuilder(Task.ABERRANT_SPECTRES)
                        .setMonsterName("Aberrant spectres")
                        .setSuperiorNames("Abhorrent spectre", "Deviant spectre")
                        .setAlternatives("Aberrant spectre", "Deviant spectre")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.STRONGHOLD_REGIONS)
                                        .area(areas.Spectres)
                                        .location(SlayerLocation.StrongholdSlayerDungeon)
                                        .traverseOverride(() -> new SpectreOverrideStronghold())
                                        .traverseMethods(Traverses.NIEVE_CAVE)
                                        .prayer(Prayer.PROTECT_FROM_MAGIC)
                                        .build()
                        )
                        .setProtection(SlayerProtectionItem.Nosepeg)
                        .build(),

                new MonsterBuilder(Task.ABYSSAL_DEMONS)
                        .setMonsterName("Abyssal demons")
                        .setSuperiorNames("Greater abyssal demon")
                        .setAlternatives("Abyssal demon")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.AbyssalDemons)
                                        .isBarrage(true)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .traverseToTile(new Coordinate(1674, 10097, 0))
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .setBoneType(BoneType.Ashes)
                        .build(),

                new MonsterBuilder(Task.ADAMANT_DRAGONS)
                        .setMonsterName("Adamant dragons")
                        .setAlternatives("Adamant dragon")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.LITHRIKEN_REGIONS)
                                        .location(SlayerLocation.LithkrenVault)
                                        .traverseMethods(Traverses.LITHRIKEN_TRAVERSES)
                                        .traverseOverride(() -> new LithkrenOverride(LithkrenOverride.Dragon.Adamant))
                                        .area(areas.AdamantDragons)
                                        .prayer(Prayer.PROTECT_FROM_MAGIC)
                                        .build()
                        )
                        .setFood(0)
                        .setDragon()
                        .setPoisonous()
                        .disableHopping()
                        .build(),

                new MonsterBuilder(Task.ANKOU)
                        .setMonsterName("Ankou")
                        .setAlternatives("Ankou", "Dark Ankou")
                        .setBoneType(BoneType.Bones)
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.Ankou)
                                        .traverseOverride(AnkouCatacombsOverride::new)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .isBarrage(true)
                                        .build()
                        )
                        .build(),
                //    Aviansies("Aviansies",
//            new String[]{"Aviansie","Kree'arra","Flight Kilisa","Flockleader Geerin","Wingman Skree","Reanimated aviansie"})
//            ),
                new MonsterBuilder(Task.BASILISKS)
                        .setMonsterName("Basilisks")
                        .setAlternatives("Basilisk", "Basilisk Knight")
                        .setSuperiorNames("Basilisk Sentinel", "Monstrous basilisk")
                        .setProtection(SlayerProtectionItem.MirrorShield)
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.JORMUNGANDS_PRISON_REGIONS)
                                        .area(areas.BasiliskKnight)
                                        .location(SlayerLocation.JormungandsPrison)
                                        .condition(JormungandPrisonOverride::condition)
                                        .traverseToTile(new Coordinate(2425, 10385, 0))
                                        .traverseMethods(Traverses.JORMUNGANDS_PRISON)
                                        .traverseOverride(JormungandPrisonOverride::new)
                                        .food(0)
                                        .needsFood(true)
                                        .prayer(Prayer.PROTECT_FROM_MAGIC)
                                        .build())
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                        .area(areas.Basilisk)
                                        .location(SlayerLocation.FremennikSlayerDungeon)
                                        .traverseToTile(new Coordinate(2742, 10010, 0))
                                        .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                        .traverseOverride(() -> new FremSlayerDungeonOverride(true))
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()

                        )
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.BLACK_DEMONS)
                        .setMonsterName("Black demons")
                        .setAlternatives("Black demon", "Demonic gorilla", "Balfrug Kreeyath", "Porazdir", "Skotizo")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.BlackDemon)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setBoneType(BoneType.Ashes)
                        .build(),
                new MonsterBuilder(Task.BLACK_DRAGONS)
                        .setMonsterName("Black dragons")
                        .setAlternatives("Black dragon", "Baby black dragon", "King Black Dragon", "Brutal black dragon")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.TAVERLY_DUNGEON_REGIONS)
                                        .area(areas.BabyBlackDragon)
                                        .location(SlayerLocation.TaverleyDungeon)
                                        .traverseToTile(new Coordinate(2862, 9827, 1))
                                        .traverseMethods(Traverses.TAVERLY_DUNGEON)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.BLOODVELD)
                        .setMonsterName("Bloodveld")
                        .setSuperiorNames("Insatiable Bloodveld", "Insatiable mutated Bloodveld")
                        .setAlternatives("Bloodveld", "Mutated Bloodveld", "Reanimated Bloodveld")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.Bloodveld)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{14488, 14487, 14232, 14233, 13977, 14387, 14386, 14642, 14388, 14644, 9544})
                                        .location(SlayerLocation.MeiyerditchLaboratories)
                                        .area(areas.Bloodveld_Meiye)
                                        .traverseItems(MeiyerditchLabratoryOverride.items())
                                        .traverseMethods(new TraverseMethod[]{new ItemTraverse(items.drakans, "Ver Sinhaza")})
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .traverseOverride(() -> new MeiyerditchLabratoryOverride())
                                        .cannonSpot(new Coordinate(3596, 9743, 0))
                                        .condition(MeiyerditchLabratoryOverride::hasReq)
                                        .build()
                        )
                        .setBoneType(BoneType.Ashes)
                        .build(),

                new MonsterBuilder(Task.BLUE_DRAGONS)
                        .setMonsterName("Blue dragons")
                        .setAlternatives("Blue dragon", "Baby blue dragon", "Brutal blue dragon")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.TAVERLY_DUNGEON_REGIONS)
                                        .location(SlayerLocation.TaverleyDungeon)
                                        .area(areas.BabyBlueDragon)
                                        .traverseToTile(new Coordinate(2896, 9769, 0))
                                        .traverseMethods(Traverses.TAVERLY_DUNGEON)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.CAVE_HORRORS)
                        .setMonsterName("Cave horrors")
                        .setAlternatives("Cave horror")
                        .setSuperiorNames("Cave abomination")
                        .needsLightSource()
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{15150, 15151, 14894, 14895, 14994, 14995, 15251})
                                        .area(areas.CaveHorrors)
                                        .traverseToTile(new Coordinate(3738, 9377, 0))
                                        //.cannonSpot(new Coordinate(3782, 9459, 0))
                                        .traverseMethods(new TraverseMethod[]{new MinigameTeleportTraverse(MinigameTeleport.TROUBLE_BREWING)})
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.CAVE_KRAKEN)
                        .setMonsterName("Cave kraken")
                        .setAlternatives("Kraken")
                        .location(
                                Location.builder()
                                        .area(areas.Kraken)
                                        .location(SlayerLocation.KrakenCove)
                                        .pathRegions(new int[]{9272, 9016, 9116})
                                        .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.AKQ)})
                                        .traverseOverride(KrakenCoveOverride::new)
                                        .prayer(Prayer.PROTECT_FROM_MAGIC)
                                        .build())
                        .build(),

                new MonsterBuilder(Task.COCKATRICE)
                        .setMonsterName("Cockatrice")
                        .setSuperiorNames("Cockathrice")
                        .setProtection(SlayerProtectionItem.MirrorShield)
                        .setBoneType(BoneType.Bones)
                        .location(Location.builder()
                                .traverseToTile(new Coordinate(2792, 10035, 0))
                                .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                .area(new Area.Rectangular(new Coordinate(2774, 10045, 0), new Coordinate(2813, 10022, 0)))
                                .location(SlayerLocation.FremennikSlayerDungeon)
                                .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .build())
                        .build(),

                new MonsterBuilder(Task.DAGANNOTH)
                        .setMonsterName("Dagannoth")
                        .setAlternatives("Dagannoth", "Dagannoth spawn", "Dagannoth fledgeling", "Reanimated dagannoth")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.Dagannoth)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{10040, 10140})
                                        .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.ALP)})
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .location(SlayerLocation.IntheLighthouse)
                                        .area(areas.Dagannoth_Lighthouse)
                                        .traverseOverride(() -> new LighthouseOverride())
                                        .cannonSpot(new Coordinate(2524, 10020, 0))
                                        .needsFood(true)
                                        .food(6)
                                        .build()
                        )
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.DARK_BEASTS)
                        .setMonsterName("Dark beasts")
                        .setAlternatives("Dark beast")
                        .setSuperiorNames("Night beast")
                        .location(
                                Location.builder()
                                        .area(areas.DarkBeast)
                                        .cannonSpot(new Coordinate(1992, 4655, 0))
                                        .pathRegions(new int[]{8008})
                                        .location(SlayerLocation.MournerTunnels)
                                        .traverseMethods(new TraverseMethod[]{new SlayerRingTraverse(SlayerRingDestination.DarkBeasts)})
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setBoneType(BoneType.Bones)
                        .setFood(0)
                        .build(),

                new MonsterBuilder(Task.DRAKES)
                        .setMonsterName("Drakes")
                        .setAlternatives("Drake")
                        .setSuperiorNames("Guardian Drake")
                        .location(
                                Location.builder()
                                        .area(areas.Drake)
                                        .traverseToTile(new Coordinate(1311, 3807, 0))
                                        .traverseOverride(() -> new DrakeOverride())
                                        .pathRegions(Traverses.MOUNT_KARALUUM_REGIONS)
                                        .location(SlayerLocation.KaruulmSlayerDungeon)
                                        .traverseMethods(Traverses.MOUNT_KARALUUM_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MISSILES)
                                        .build()
                        )
                        .setProtection(SlayerProtectionItem.BootsOfStone)
                        .setBoneType(BoneType.Bones)
                        .disableHopping()
                        .setFood(0)
                        .build(),

                new MonsterBuilder(Task.DUST_DEVILS)
                        .setMonsterName("Dust devils")
                        .setSuperiorNames("Choke devil")
                        .setAlternatives("Dust devil")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.DustDevil)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(com.neck_flexed.scripts.slayer.barrage.areas.DUST_DEVIL)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .isBarrage(true)
                                        .build()
                        )
                        .setProtection(SlayerProtectionItem.Facemask)
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.ELVES)
                        .setMonsterName("Elves")
                        .setAlternatives("Elf", "Elf warrior", "Reanimated elf", "Iorwerth Warrior")
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{9265})
                                        .area(areas.Elves_Llyetya)
                                        .traverseMethods(new TraverseMethod[]{
                                                new ItemTraverse("Eternal teleport crystal", "Lletya"),
                                                new ItemTraverse(Pattern.compile("Teleport crystal \\(.*\\)"), "Lletya"),
                                        })
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .build(),

                new MonsterBuilder(Task.FIRE_GIANTS)
                        .setMonsterName("Fire giants")
                        .setAlternatives("Fire giant")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.FireGiant)
                                        .traverseToTile(new Coordinate(1640, 10047, 0))
                                        .traverseOverride(() -> new GiantsDenTraverseOverride(new Coordinate(1456, 9905, 0)))
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.FOSSIL_ISLAND_WYVERNS)
                        .setMonsterName("Fossil Island Wyverns")
                        .setAlternatives("Fossil Island Wyvern", "Spitting wyvern", "Taloned wyvern", "Long-tailed wyvern", "Ancient Wyvern")
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{14908, 14652, 14496})
                                        .traverseMethods(new TraverseMethod[]{new DigsitePendantTraverse(DigsiteDestination.HouseOnHill), new HouseDigsiteTraverse(DigsiteDestination.HouseOnHill)})
                                        .location(SlayerLocation.FossilIsland)
                                        .traverseOverride(FossilIslandWyvernOverride::new)
                                        .area(areas.FossilWyverns)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setWyvern()
                        .setFood(0)
                        .disableHopping()
                        .build(),

                new MonsterBuilder(Task.GARGOYLES)
                        .setMonsterName("Gargoyles")
                        .setAlternatives("Gargoyle")
                        .setSuperiorNames("Marble gargoyle")
                        .location(
                                Location.builder()
                                        .area(areas.Gargoyle)
                                        .traverseToTile(new Coordinate(3417, 3536, 0))
                                        .pathRegions(Traverses.SLAYER_TOWER_REGIONS)
                                        .location(SlayerLocation.SlayerTower)
                                        .traverseOverride(() -> new SlayerTowerBasementTraverseOverride(new Coordinate(3430, 9945, 3)))
                                        .traverseMethods(Traverses.SLAYER_TOWER)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setFinishItem(SlayerFinishItem.RockHammer)
                        .setFood(5)
                        .build(),

                new MonsterBuilder(Task.GREATER_DEMONS)
                        .setMonsterName("Greater demons")
                        .setAlternatives("Greater demon")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.GreaterDemon)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setBoneType(BoneType.Ashes)
                        .build(),
                new MonsterBuilder(Task.HELLHOUNDS)
                        .setMonsterName("Hellhounds")
                        .setAlternatives("Hellhound", "Cerberus", "Skeleton Hellhound", "Greater Skeleton Hellhound")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(areas.Hellhound)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .setBoneType(BoneType.Ashes)
                        .build(),

                new MonsterBuilder(Task.HILL_GIANTS)
                        .setMonsterName("Hill Giants")
                        .setAlternatives("Hill giant")
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .area(new Area.Rectangular(new Coordinate(1645, 10046, 0), new Coordinate(1659, 10032, 0)))
                                        .traverseToTile(new Coordinate(1651, 10037, 0))
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build())
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.INFERNAL_MAGES)
                        .setMonsterName("Infernal Mages")
                        .setAlternatives("Infernal Mage")
                        .setSuperiorNames("Malevolent Mage")
                        .location(Location.builder()
                                .pathRegions(Traverses.SLAYER_TOWER_REGIONS)
                                .location(SlayerLocation.SlayerTower)
                                .traverseToTile(new Coordinate(3440, 3564, 1))
                                .area(new Area.Rectangular(new Coordinate(3428, 3581, 1), new Coordinate(3453, 3553, 1)))
                                .traverseMethods(Traverses.SLAYER_TOWER)
                                .build())
                        .build(),

                new MonsterBuilder(Task.IRON_DRAGONS)
                        .setMonsterName("Iron dragons")
                        .setAlternatives("Iron dragon")
                        .location(
                                Location.builder()
                                        .area(areas.IronDragons)
                                        .traverseToTile(new Coordinate(1662, 10086, 0))
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MAGIC)
                                        .build()
                        )
                        .setDragon()
                        .setFood(0)
                        .build(),

                new MonsterBuilder(Task.JELLIES)
                        .setMonsterName("Warped Jelly")
                        .setSuperiorNames("Vitreous warped Jelly")
                        .location(
                                Location.builder()
                                        .area(com.neck_flexed.scripts.slayer.barrage.areas.JELLY)
                                        .pathRegions(Traverses.CATACOMBS_REGIONS)
                                        .location(SlayerLocation.CatacombsofKourend)
                                        .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .isBarrage(true)
                                        .build())
                        .build(),

                new MonsterBuilder(Task.KALPHITE)
                        .setMonsterName("Kalphites")
                        .setAlternatives("Kalphites", "Kalphite", "Kalphite Worker", "Kalphite Soldier")
                        .setPoisonous()
                        .turael()
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{12848, 13104, 13204})
                                        .area(areas.Kalphites)
                                        .location(SlayerLocation.TaskonlyKalphiteCave)
                                        .traverseOverride(() -> new KalphiteCaveTraverseOverride())
                                        .traverseMethods(new TraverseMethod[]{new ItemTraverse(items.desertAmulet4, "Kalphite cave").withPriority(9), new FairyRingTraverse(FairyRing.BIQ)})
                                        .isInSafeArea(true)
                                        .build())
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{12848, 13104, 13204, 13205})
                                        .area(areas.Kalphite_Soldier)
                                        .location(SlayerLocation.TaskonlyKalphiteCave)
                                        .traverseOverride(KalphiteCaveTraverseOverride::new)
                                        .cannonSpot(new Coordinate(3307, 9528, 0))
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .traverseMethods(new TraverseMethod[]{new ItemTraverse(items.desertAmulet4, "Kalphite cave").withPriority(9), new FairyRingTraverse(FairyRing.BIQ)})
                                        .build())
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{KalphiteLairTraverse.LAIR_REGION_ID})
                                        .location(SlayerLocation.KalphiteLair)
                                        .traverseItems(Map.of(Pattern.compile(items.rope), 1))
                                        .area(areas.Kalphites_Lair)
                                        .traverseMethods(new TraverseMethod[]{new KalphiteLairTraverse()})
                                        .cannonSpot(new Coordinate(3501, 9522, 2))
                                        .isInSafeArea(true)
                                        .build()
                        )
                        .build(),
                new MonsterBuilder(Task.KURASK)
                        .setMonsterName("Kurask")
                        .setSuperiorNames("King kurask")
                        .setAlternatives("Kurask")
                        .setBoneType(BoneType.Bones)
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.IOWERTH_REGIONS)
                                        .area(areas.Kurask_Iowerth)
                                        .location(SlayerLocation.IorwerthDungeon)
                                        .traverseMethods(Traverses.IORWERTH_TRAVERSES)
                                        .traverseToTile(new Coordinate(3253, 12380, 0))
                                        .traverseOverride(IowerthOverride::new)
                                        .condition(IowerthOverride::condition)
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .location(
                                Location.builder()
                                        .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                        .area(areas.Kurask)
                                        .location(SlayerLocation.FremennikSlayerDungeon)
                                        .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                        .traverseOverride(() -> new FremSlayerDungeonOverride())
                                        .prayer(Prayer.PROTECT_FROM_MELEE)
                                        .build()
                        )
                        .setIsLeafBladed(true)
                        .build(),

                new MonsterBuilder(Task.LESSER_DEMONS)
                        .setMonsterName("Lesser Demons")
                        .setAlternatives("Lesser demon")
                        .location(Location.builder()
                                .pathRegions(Traverses.CATACOMBS_REGIONS)
                                .location(SlayerLocation.CatacombsofKourend)
                                .area(new Area.Rectangular(new Coordinate(1701, 10076, 0), new Coordinate(1727, 10058, 0)))
                                .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .traverseToTile(new Coordinate(1719, 10067, 0))
                                .build())
                        .setBoneType(BoneType.Ashes)
                        .build(),

                new MonsterBuilder(Task.LIZARDMEN)
                        .setMonsterName("Lizardmen")
                        .setAlternatives("Lizardmen", "Lizardman brute", "Lizardman shaman")
                        .location(
                                Location.builder()
                                        .pathRegions(new int[]{5177, 4921, 4922, 5178, 5434, 5433})
                                        .traverseMethods(new TraverseMethod[]{
                                                new ItemTraverse(Pattern.compile("Farming cape.*"), "Teleport"),
                                                new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.FarmingGuild),
                                                new ItemTraverse(items.skillsNecklace, "Farming Guild", "Rub"),
                                                new TeleportTraverse(TeleportSpellInfo.BATTLEFRONT),
                                                new HousePortalTraverse(PortalNexusTeleport.Battlefront),
                                                new FairyRingTraverse(FairyRing.CIR)
                                        })
                                        .area(areas.Lizardmen)
                                        .location(SlayerLocation.LizardmanSettlement)
                                        .prayer(Prayer.PROTECT_FROM_MISSILES)
                                        .traverseOverride(() -> new ShamanTraverseOverride())
                                        .build()
                        )
                        .setBoneType(BoneType.Bones)
                        .setPoisonous()
                        .setFood(0)
                        .build(),

                //    MinionsOfScabaras("Minions of Scabaras",
//            new String[]{"Minions of Scabaras","Scarab","Scarab swarm","Locust rider","Scarab mage"})
//            ),
                new MonsterBuilder(Task.MITHRIL_DRAGONS)
                        .setMonsterName("Mithril dragons")
                        .setAlternatives("Mithril dragon")
                        .setDragon()
                        .setFood(0)
                        .setBoneType(BoneType.Bones)
                        .location(
                                Location.builder()
                                        .traverseOverride(AncientCavernOverride::new)
                                        .area(areas.MithrilDragon)
                                        .pathRegions(Traverses.ANCIENT_CAVERN_REGIONS)
                                        .traverseMethods(Traverses.ANCIENT_CAVERN)
                                        .location(SlayerLocation.AncientCavern)
                                        .prayer(Prayer.PROTECT_FROM_MAGIC)
                                        .build()
                        )
                        .build(),

                new MonsterBuilder(Task.MOSS_GIANTS)
                        .setMonsterName("Moss Giants")
                        .setAlternatives("Moss giant")
                        .location(Location.builder()
                                .pathRegions(Traverses.CATACOMBS_REGIONS)
                                .location(SlayerLocation.CatacombsofKourend)
                                .area(new Area.Rectangular(new Coordinate(1679, 10053, 0), new Coordinate(1695, 10041, 0)))
                                .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .traverseToTile(new Coordinate(1687, 10048, 0))
                                .build())
                        .setBoneType(BoneType.Bones)
                        .build(),

                new MonsterBuilder(Task.MUTATED_ZYGOMITES)
                        .setMonsterName("Mutated Zygomites")
                        .setAlternatives("Mutated Zygomite", "Zygomite", "Ancient Zygomite")
                        .setFinishItem(SlayerFinishItem.Fungicide)
                        .location(
                                Location.builder()
                                        .area(areas.Zygomite)
                                        .pathRegions(new int[]{9541})
                                        .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.ZANARIS)})
                                        .location(SlayerLocation.Zanaris)
                                        .prayer(Prayer.PROTECT_FROM_MISSILES)
                                        .build()
                        )
                        .setFood(5)
                        .build(),

                new MonsterBuilder(Task.NECHRYAEL)
                        .setMonsterName("Nechryael")
                        .setSuperiorNames("Nechryarch")
                        .setAlternatives("Nechryael", "Greater Nechryael")
                        .location(Location.builder()
                                .pathRegions(Traverses.CATACOMBS_REGIONS)
                                .location(SlayerLocation.CatacombsofKourend)
                                .area(areas.Nechryael)
                                .isBarrage(true)
                                .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .build())
                        .setBoneType(BoneType.Ashes)
                        .setSuperiorEncounter(new NechryarchEncounter())
                        .setFood(1)
                        .build(),
                //    RedDragons("Red dragons",
//            new String[]{"Red dragon","Baby red dragon","brutal red dragon"})
//            ),
                new MonsterBuilder(Task.PYREFIENDS)
                        .setMonsterName("Pyrefiends")
                        .setAlternatives("Pyrefiend")
                        .setSuperiorNames("Flaming pyrelord")
                        .setFood(0)
                        .setBoneType(BoneType.Ashes)
                        .location(Location.builder()
                                .traverseToTile(new Coordinate(2742, 10009, 0))
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                .area(new Area.Rectangular(new Coordinate(2731, 10020, 0), new Coordinate(2751, 9994, 0)))
                                .location(SlayerLocation.FremennikSlayerDungeon)
                                .traverseOverride(() -> new FremSlayerDungeonOverride(true))
                                .build())
                        .build(),

                new MonsterBuilder(Task.ROCKSLUGS)
                        .setMonsterName("Rockslugs")
                        .setAlternatives("Rockslug")
                        .setSuperiorNames("Giant rockslug")
                        .setFinishItem(SlayerFinishItem.BagOfSalt)
                        .location(Location.builder()
                                .traverseToTile(new Coordinate(2799, 10016, 0))
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                .area(new Area.Rectangular(new Coordinate(2784, 10025, 0), new Coordinate(2813, 10009, 0)))
                                .location(SlayerLocation.FremennikSlayerDungeon)
                                .build())
                        .build(),

                new MonsterBuilder(Task.RUNE_DRAGONS)
                        .setMonsterName("Rune dragons")
                        .setAlternatives("Rune dragon")
                        .location(Location.builder()
                                .pathRegions(Traverses.LITHRIKEN_REGIONS)
                                .location(SlayerLocation.LithkrenVault)
                                .traverseMethods(Traverses.LITHRIKEN_TRAVERSES)
                                .traverseOverride(() -> new LithkrenOverride(LithkrenOverride.Dragon.Rune))
                                .area(areas.RuneDragons)
                                .prayer(Prayer.PROTECT_FROM_MAGIC)
                                .build())
                        .setFood(0)
                        .setProtection(SlayerProtectionItem.InsulatedBoots)
                        .setDragon()
                        .disableHopping()
                        .build(),

                new MonsterBuilder(Task.SKELETAL_WYVERNS)
                        .setMonsterName("Skeletal Wyverns")
                        .setAlternatives("Skeletal Wyvern")
                        .location(Location.builder()
                                .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.AIQ), new HouseLocationTraverse(House.Location.RIMMINGTON), new ConstructionCapeTraverse(ConstructionCapeTraverse.Destination.Rimmington)})
                                .pathRegions(new int[]{12181, 11925, 11824, 11825, 12081, 11826})
                                .area(areas.SkeletalWyverns)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .location(SlayerLocation.AsgarnianIceDungeon)
                                .traverseOverride(() -> new SkeletalWyvernTraverseOverride())
                                .traverseToTile(new Coordinate(3007, 3151, 0))
                                .cannonSpot(new Coordinate(3031, 9549, 0))
                                .build())
                        .setWyvern()
                        .setFood(0)
                        .build(),

                new MonsterBuilder(Task.SMOKE_DEVILS)
                        .setMonsterName("Smoke devils")
                        .setAlternatives("Smoke devil")
                        .setSuperiorNames("Nuclear smoke devil")
                        .location(Location.builder()
                                .area(areas.SmokeDevil)
                                .traverseMethods(Traverses.SMOKE_DUNGEON)
                                .pathRegions(Traverses.SMOKE_DUNGEON_REGIONS)
                                .location(SlayerLocation.SmokeDevilDungeon)
                                .isBarrage(true)
                                .traverseToTile(new Coordinate(2412, 3060, 0))
                                .traverseOverride(SmokeDungeonOverride::new)
                                .prayer(Prayer.PROTECT_FROM_MISSILES)
                                .cannonSpot(new Coordinate(2398, 9444, 0))
                                .build()
                        )
                        .build(),

                //    SpiritualCreatures("Spiritual creatures",
//            new String[]{"Spiritual creature","Spiritual ranger","Spiritual warrior","Spiritual mage"})
//            ),

                new MonsterBuilder(Task.STEEL_DRAGONS)
                        .setMonsterName("Steel dragons")
                        .setAlternatives("Steel dragon")
                        .location(Location.builder()
                                .area(areas.SteelDragons)
                                .traverseToTile(new Coordinate(1608, 10055, 0))
                                .pathRegions(Traverses.CATACOMBS_REGIONS)
                                .location(SlayerLocation.CatacombsofKourend)
                                .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                .traverseOverride(() -> new SteelDragonTraverseOverride())
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .build())
                        .setDragon()
                        .setFood(0)
                        .build(),

                new MonsterBuilder(Task.SUQAHS)
                        .setMonsterName("Suqahs")
                        .setAlternatives("Suqah")
                        .location(Location.builder()
                                .pathRegions(new int[]{8253, 8509, 8252, 8508})
                                .area(areas.Suqah)
                                .traverseMethods(new TraverseMethod[]{new HousePortalTraverse(PortalNexusTeleport.LunarIsle), new TeleportTraverse(TeleportSpellInfo.MOONCLAN)})
                                .prayer(Prayer.PROTECT_FROM_MAGIC)
                                .cannonSpot(new Coordinate(2119, 3943, 0))
                                .build())
                        .setFood(0)
                        .build(),
                new MonsterBuilder(Task.TROLLS)
                        .location(Location
                                .builder()
                                .cannonSpot(new Coordinate(1242, 3517, 0))
                                .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.BLS)})
                                .area(areas.Troll)
                                .location(SlayerLocation.SouthofMountQuidamortem)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .pathRegions(new int[]{4918, 5174, 4919})
                                .build()
                        )
                        .setMonsterName("Trolls")
                        .setAlternatives("Troll", "Mountain troll", "Ice troll", "Ice troll female", "Ice troll runt", "Ice troll male", "Reanimated troll")
                        .build(),
//                new MonsterBuilder(Task.Trolls_Ice)
//                        .setMonsterName("Trolls")
//                        .setAlternatives("Troll", "Mountain troll", "Ice troll", "Ice troll female", "Ice troll runt", "Ice troll male", "Reanimated troll")
//                        .pathRegions(9531, 9532, 10553, 10297, 10809, 10552, 10808, 11064)
//                        .area(areas.IceTroll)
//                        .traverseMethods(
//                                // TODO
//                                //new ItemTraverse("Enchanted Lyre", "TODO"),
//                                //new ItemTraverse("Frem Boots", "TODO"),
//                                new ConstructionCapeTraverse(ConstructionCapeTraverse.DigsiteDestination.Rellekka),
//                                new HouseLocationTraverse(House.Location.RELLEKKA),
//                                new FairyRingTraverse(FairyRing.AJR)
//                        )
//                        .cannonSpot(new Coordinate(0, 0, 0))
//                        .prayer(Prayer.PROTECT_FROM_MISSILES)
//                        .build(),
                new MonsterBuilder(Task.TUROTH)
                        .setMonsterName("Turoth")
                        .setSuperiorNames("Spiked Turoth")
                        .setAlternatives("Turoth")
                        .location(Location.builder()
                                .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                .area(areas.Turoth)
                                .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                .location(SlayerLocation.FremennikSlayerDungeon)
                                .traverseOverride(FremSlayerDungeonOverride::new)
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .build())
                        .setIsLeafBladed(true)
                        .build(),
                //    TzHaar("TzHaar",
//            new String[]{"TzHaar"})
//            ),
                new MonsterBuilder(Task.VAMPYRES)
                        .setMonsterName("Vampyres")
                        .setAlternatives("Vampyre", "Feral Vampyre", "Vampyre Juvinate", "Vyrewatch", "Vyrewatch Sentinel")
                        .setProtection(SlayerProtectionItem.Flail)
                        .disableHopping()
                        .location(Location.builder()
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .location(SlayerLocation.Darkmeyer)
                                .traverseMethods(new TraverseMethod[]{new ItemTraverse(items.drakans, "Darkmeyer")})
                                .pathRegions(new int[]{14388})
                                .traverseToTile(new Coordinate(3598, 3342, 0))
                                .area(areas.Vampyre)
                                .build())
                        .build(),

                new MonsterBuilder(Task.WATERFIENDS)
                        .setMonsterName("Waterfiends")
                        .setAlternatives("Waterfiend")
                        .setFood(0)
                        .setBoneType(BoneType.Ashes)
                        .location(
                                Location.builder()
                                        .area(areas.Waterfiend)
                                        .location(SlayerLocation.KrakenCove)
                                        .pathRegions(new int[]{9272, 9016, 9116})
                                        .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.AKQ)})
                                        .traverseOverride(KrakenCoveOverride::new)
                                        .prayer(Prayer.PROTECT_FROM_MISSILES)
                                        .build()
                        )
                        .build(),

                new MonsterBuilder(Task.WYRMS)
                        .setMonsterName("Wyrms")
                        .setAlternatives("Wyrm")
                        .setSuperiorNames("Shadow Wyrm")
                        .location(Location.builder()
                                .area(areas.Wyrm)
                                .traverseToTile(new Coordinate(1311, 3807, 0))
                                .traverseOverride(() -> new WyrmTraverseOverride())
                                .pathRegions(Traverses.MOUNT_KARALUUM_REGIONS)
                                .location(SlayerLocation.KaruulmSlayerDungeon)
                                .traverseMethods(Traverses.MOUNT_KARALUUM_TRAVERSES)
                                .prayer(Prayer.PROTECT_FROM_MAGIC)
                                .build())
                        .setProtection(SlayerProtectionItem.BootsOfStone)
                        .setBoneType(BoneType.Bones)
                        .disableHopping()
                        .setFood(5)
                        .build(),

                new MonsterBuilder(Task.BANSHEES).setMonsterName("Banshees")
                        .setAlternatives(new String[]{"Banshee", "Screaming banshee", "Screaming twisted banshee", "Twisted Banshee"})
                        .turael()
                        .setProtection(SlayerProtectionItem.Earmuffs)
                        .location(Location.builder()
                                .pathRegions(Traverses.SLAYER_TOWER_REGIONS)
                                .location(SlayerLocation.SlayerTower)
                                .traverseToTile(new Coordinate(3441, 3542, 0))
                                .area(areas.Banshees)
                                .traverseMethods(Traverses.SLAYER_TOWER)
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.BATS).setMonsterName("Bats")
                        .setAlternatives(new String[]{"Bat", "Giant bat", "Deathwing"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{13110, 13109, 13365, 13364, 13108, 133366})
                                .area(areas.Bats)
                                .traverseMethods(new TraverseMethod[]{
                                        new ItemTraverse(items.ringOfTheElements, "Rub", "Earth Altar"),
                                        new DigsitePendantTraverse(DigsiteDestination.Digsite),
                                        new HouseDigsiteTraverse(DigsiteDestination.Digsite),
                                        new HousePortalTraverse(PortalNexusTeleport.Senntisten)
                                })
                                .cannonSpot(new Coordinate(3352, 3490, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.BEARS)
                        .setMonsterName("Bears")
                        .setAlternatives(new String[]{"Bear", "Grizzly bear cub", "Bear cub", "Grizzly bear"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{10804, 10547, 10803, 10548})
                                .area(areas.Bears)
                                .traverseMethods(new TraverseMethod[]{
                                        new ItemTraverse(items.questCape, "Teleport"),
                                        new FairyRingTraverse(FairyRing.BLR),
                                        new HousePortalTraverse(PortalNexusTeleport.Ardougne),
                                        new TeleportTraverse(TeleportSpellInfo.ARDOUGNE),
                                })
                                .cannonSpot(new Coordinate(2701, 3331, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.BIRDS).setMonsterName("Birds")
                        .setAlternatives(new String[]{"Bird", "Duck", "Chicken", "Undead chicken", "Rooster", "Seagull", "Duckling"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{12596, 12852, 12853, 12597})
                                .area(areas.Birds)
                                .traverseMethods(new TraverseMethod[]{
                                        new ItemTraverse("Chronicle", "Teleport"),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.ChampionsGuild),
                                        new TeleportTraverse(TeleportSpellInfo.VARROCK),
                                })
                                .cannonSpot(new Coordinate(3176, 3360, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.CAVE_BUGS).setMonsterName("Cave bugs")
                        .setAlternatives(new String[]{"Cave bug"})
                        .turael()
                        .needsLightSource()
                        .location(Location.builder()
                                .pathRegions(new int[]{10833})
                                .area(areas.CaveBugs)
                                .traverseMethods(new TraverseMethod[]{
                                        new FairyRingTraverse(FairyRing.AJQ)
                                })
                                .cannonSpot(new Coordinate(2714, 5235, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.CAVE_CRAWLERS).setMonsterName("Cave crawlers")
                        .setAlternatives(new String[]{"Cave crawler", "Chasm Crawler"})
                        .setSuperiorNames("Chasm Crawler")
                        .turael()
                        .location(Location.builder()
                                .pathRegions(Traverses.FREM_SLAYER_REGIONS)
                                .area(areas.CaveCrawlers)
                                .traverseMethods(Traverses.FREM_SLAYER_TRAVERSES)
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.CAVE_SLIMES).setMonsterName("Cave slimes")
                        .setAlternatives(new String[]{"Cave slime"})
                        .turael()
                        .setPoisonous()
                        .needsLightSource()
                        .location(Location.builder()
                                .pathRegions(new int[]{10833})
                                .area(areas.CaveSlime)
                                .traverseMethods(new TraverseMethod[]{new FairyRingTraverse(FairyRing.AJQ)})
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.COWS).setMonsterName("Cows")
                        .setAlternatives(new String[]{"Cow", "Cow calf"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{12850, 12851, 13106, 13107})
                                .area(areas.Cows)
                                .traverseMethods(new TraverseMethod[]{
                                        new HousePortalTraverse(PortalNexusTeleport.Lumbridge),
                                        new TeleportTraverse(TeleportSpellInfo.LUMBRIDGE)
                                })
                                .traverseOverride(() -> new CowOverride())
                                .cannonSpot(new Coordinate(3258, 3279, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.CRAWLING_HANDS).setMonsterName("Crawling Hands")
                        .setAlternatives(new String[]{"Crawling Hand", "Crushing hand"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(Traverses.SLAYER_TOWER_REGIONS)
                                .location(SlayerLocation.SlayerTower)
                                .area(areas.CrawlingHands)
                                .traverseMethods(Traverses.SLAYER_TOWER)
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.DOGS).setMonsterName("Dogs")
                        .setAlternatives(new String[]{"Dog", "Jackal", "Guard dog", "Wild dog", "Reanimated dog"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{13613, 13357, 13614, 13358, 13615})
                                .area(areas.Dogs)
                                .traverseMethods(new TraverseMethod[]{
                                        new ItemTraverse(items.desertAmulet4, "Nardah"),
                                        new FairyRingTraverse(FairyRing.DLQ),
                                })
                                .cannonSpot(new Coordinate(3387, 2905, 0))
                                .build())
                        .build(),
                new MonsterBuilder(Task.DWARVES).setMonsterName("Dwarves")
                        .setAlternatives(new String[]{"Dwarf", "Black Guard", "Chaos Dwarf", "Dwarf gang member"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{11316, 11575, 11318, 11574, 11418, 11829, 11572, 11828, 11573})
                                .area(areas.Dwarves)
                                .traverseMethods(new TraverseMethod[]{
                                        new ConstructionCapeTraverse(ConstructionCapeTraverse.Destination.Taverley),
                                        new ItemTraverse("Taverly teleport", "Break"),
                                        new HouseLocationTraverse(House.Location.TAVERLEY),
                                        new GamesNecklaceTraverse(GamesNecklaceTraverse.Destination.Burthorpe),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Burthorpe),
                                        new TeleportTraverse(TeleportSpellInfo.FALADOR)
                                })
                                .cannonSpot(new Coordinate(2865, 9877, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.GHOSTS).setMonsterName("Ghosts")
                        .setAlternatives(new String[]{"Ghost"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(Traverses.CATACOMBS_REGIONS)
                                .location(SlayerLocation.CatacombsofKourend)
                                .area(areas.Ghosts)
                                .traverseMethods(Traverses.CATACOMBS_TRAVERSES)
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.GOBLINS).setMonsterName("Goblins")
                        .setAlternatives(new String[]{"Goblin"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{12340, 12339, 12595, 12596, 12338, 12594, 12850, 12851})
                                .area(areas.Goblins)
                                .traverseMethods(new TraverseMethod[]{
                                        new HousePortalTraverse(PortalNexusTeleport.DraynorManor),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.DraynorVillage),
                                        new TeleportTraverse(TeleportSpellInfo.LUMBRIDGE)
                                })
                                .cannonSpot(new Coordinate(3145, 3302, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.ICEFIENDS).setMonsterName("Icefiends")
                        .setAlternatives(new String[]{"Icefiend"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{11830, 12086, 12342, 12598, 12341, 12085, 12343, 12087})
                                .area(areas.Icefiends)
                                .traverseMethods(new TraverseMethod[]{
                                        new TeleportTraverse(TeleportSpellInfo.LASSAR),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.EdgevilleMonastery),
                                        new FairyRingTraverse(FairyRing.DKR)
                                })
                                .cannonSpot(new Coordinate(3007, 3476, 0))
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.LIZARDS).setMonsterName("Lizards")
                        .setAlternatives(new String[]{"Lizard", "Desert Lizard", "Small Lizard", "Sulphur Lizard"})
                        .turael()
                        .setFinishItem(SlayerFinishItem.IceCooler)
                        .location(Location.builder()
                                .pathRegions(new int[]{13615})
                                .area(areas.Lizards)
                                .traverseMethods(new TraverseMethod[]{
                                        new FairyRingTraverse(FairyRing.DLQ),
                                })
                                .cannonSpot(new Coordinate(3411, 3033, 0))
                                .build())
                        .build(),
                new MonsterBuilder(Task.MINOTAURS).setMonsterName("Minotaurs")
                        .setAlternatives(new String[]{})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{12341, 12342, 7505})
                                .area(areas.Minotaurs)
                                .location(SlayerLocation.StrongholdofSecurity)
                                .traverseMethods(new TraverseMethod[]{
                                        new ItemTraverse(items.skullSceptre, "Invoke"),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Edgeville),
                                        new FairyRingTraverse(FairyRing.DKR)
                                })
                                .traverseOverride(() -> new MinotaurOverride())
                                .cannonSpot(new Coordinate(1876, 5217, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.MONKEYS).setMonsterName("Monkeys")
                        .setAlternatives(new String[]{"Monkey", "Karamjan Monkey"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{11054})
                                .area(areas.Monkeys)
                                .traverseMethods(new TraverseMethod[]{
                                        new FairyRingTraverse(FairyRing.CKR)
                                })
                                .cannonSpot(new Coordinate(2793, 2994, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.RATS).setMonsterName("Rats")
                        .setAlternatives(new String[]{"Rat", "Giant rat", "Dungeon rat"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{12853, 12854, 12954})
                                .area(areas.Rats)
                                .traverseMethods(new TraverseMethod[]{
                                        new HousePortalTraverse(PortalNexusTeleport.Varrock),
                                        new TeleportTraverse(TeleportSpellInfo.VARROCK)
                                })
                                .traverseOverride(() -> new RatOverride())
                                .cannonSpot(new Coordinate(3237, 9866, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.SCORPIONS).setMonsterName("Scorpions")
                        .setAlternatives(new String[]{"Scorpion"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{13106, 13107, 12850})
                                .area(areas.Scorpions)
                                .traverseMethods(new TraverseMethod[]{
                                        new DuelingRingTraverse(DuelingRingTraverse.Destination.PvPArena),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.PvpArena),
                                        new TeleportTraverse(TeleportSpellInfo.LUMBRIDGE)

                                })
                                .cannonSpot(new Coordinate(3298, 3299, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.SKELETONS).setMonsterName("Skeletons")
                        .setAlternatives(new String[]{"Skeleton"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{13365, 13364, 13109, 13108})
                                .area(areas.Skeletons)
                                .traverseMethods(new TraverseMethod[]{
                                        new HousePortalTraverse(PortalNexusTeleport.Senntisten),
                                        new DigsitePendantTraverse(DigsiteDestination.Digsite),
                                        new HouseDigsiteTraverse(DigsiteDestination.Digsite),
                                })
                                .traverseItems(Map.of(Pattern.compile(items.rope), 1))
                                .traverseOverride(() -> new SkeletonOverride())
                                .cannonSpot(new Coordinate(3376, 9749, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.SOURHOGS).setMonsterName("Sourhogs")
                        .setAlternatives(new String[]{"Sourhog"})
                        .turael()
                        .setProtection(SlayerProtectionItem.ReinforcedGoggles)
                        .location(Location.builder()
                                .pathRegions(new int[]{12340, 12339, 12338, 12594, 12595, 12596, 12695, 12850, 12851})
                                .area(areas.Sourhogs)
                                .traverseMethods(new TraverseMethod[]{
                                        new HousePortalTraverse(PortalNexusTeleport.DraynorManor),
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.DraynorVillage),
                                        new TeleportTraverse(TeleportSpellInfo.LUMBRIDGE)
                                })
                                .traverseOverride(() -> new SourhogOverride())
                                .cannonSpot(new Coordinate(3167, 9695, 0))
                                .prayer(Prayer.PROTECT_FROM_MISSILES)
                                .build())
                        .build(),
                new MonsterBuilder(Task.SPIDERS).setMonsterName("Spiders")
                        .setAlternatives(new String[]{"Spider", "Giant spider"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{12338, 12339, 12594, 12850})
                                .area(areas.Spiders)
                                .traverseMethods(new TraverseMethod[]{
                                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.DraynorVillage),
                                        new HousePortalTraverse(PortalNexusTeleport.Lumbridge),
                                        new TeleportTraverse(TeleportSpellInfo.LUMBRIDGE)
                                })
                                .cannonSpot(new Coordinate(3168, 3244, 0))
                                .isInSafeArea(true)
                                .build())
                        .build(),
                new MonsterBuilder(Task.WOLVES).setMonsterName("Wolves")
                        .setAlternatives(new String[]{"Wolf", "Big Wolf", "Desert Wolf", "Ice wolf", "Jungle wolf", "White wolf"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{11318})
                                .area(areas.Wolves)
                                .traverseMethods(new TraverseMethod[]{
                                        new GliderTraverse(GliderTraverse.Destination.Sindarpos),
                                })
                                .cannonSpot(new Coordinate(2845, 3498, 0))
                                .prayer(Prayer.PROTECT_FROM_MELEE)
                                .build())
                        .build(),
                new MonsterBuilder(Task.ZOMBIES).setMonsterName("Zombies")
                        .setAlternatives(new String[]{"Zombie", "Undead chicken", "Undead cow"})
                        .turael()
                        .location(Location.builder()
                                .pathRegions(new int[]{14647, 14646, 14391, 14390})
                                .area(areas.Zombies)
                                .traverseMethods(new TraverseMethod[]{
                                        new ItemTraverse("Ectophial", "Empty"),
                                        new FairyRingTraverse(FairyRing.ALQ)
                                })
                                .cannonSpot(new Coordinate(3621, 3529, 0))
                                .isInSafeArea(true)
                                .build())
                        .build()
        );
    }

    public static Optional<SlayerMonster> fromSlayerTask(@Nullable Task override) {
        var task = override == null ? SlayerTask.getCurrentT() : override;
        if (task == null) return Optional.empty();
        return getByEnum(task);
    }

    public int getFood(NeckBot<?, ?> bot) {
        return food > 0 ? food : getLocation(bot).getFood();
    }

    public boolean needsFood(NeckBot<?, ?> bot) {
        return this.food >= 0 || getLocation(bot).isNeedsFood();
    }

    @Override
    public boolean needsLightSource() {
        return this.needsLightSource;
    }

    @Override
    public String toString() {
        return this.monsterName;
    }

}
