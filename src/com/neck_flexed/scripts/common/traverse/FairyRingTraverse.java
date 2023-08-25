package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.*;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.cognizant.WaypointPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Items;
import com.runemate.game.api.osrs.local.AchievementDiary;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

@Log4j2
public class FairyRingTraverse implements TraverseMethod {
    public static final Area ardyCloakTele = Area.rectangular(new Coordinate(2594, 3246, 0), new Coordinate(2664, 3204, 0));
    private static final int MAX_QUEST_POINTS = 298;
    private static final Area MISCELLANIA = new Area.Rectangular(new Coordinate(2496, 3902, 0), new Coordinate(2558, 3840, 0));
    private final FairyRing fairyRing;
    private Path path;

    public FairyRingTraverse(FairyRing fairyRing) {
        this.fairyRing = fairyRing;
    }

    public static GameObject getFairyRing() {
        var p = Players.getLocal().getServerPosition();
        if (ardyCloakTele.contains(p))
            return GameObjects
                    .newQuery()
                    .on(new Coordinate(2658, 3230, 0))
                    .types(GameObject.Type.PRIMARY)
                    .names("Fairy ring")
                    .results().nearest();

        var f1 = com.runemate.game.api.hybrid.local.hud.interfaces.FairyRing.getNearestObject();
        if (f1 != null && f1.getPosition() != null && f1.getPosition().isReachable()) return f1;
        var fr = GameObjects.newQuery().types(
                GameObject.Type.PRIMARY).names("Fairy ring").results().nearest();
        if (fr != null && fr.getPosition() != null && fr.getPosition().isReachable()) return fr;
        var f2 = GameObjects.newQuery().types(
                GameObject.Type.PRIMARY).names("Spiritual Fairy Tree").results().nearest();
        if (f2 != null) return f2;
        var f3 = GameObjects.newQuery().types(
                GameObject.Type.PRIMARY).actions(Pattern.compile(".*Zanaris.*")).results().nearest();
        if (f3 != null && f3.getPosition() != null && f3.getPosition().isReachable()) return f3;
        return null;
    }

    public static boolean ardyCloakTele() {
        var cl = Inventory.contains(items.argdougneCloak)
                ? Inventory.getItems(items.argdougneCloak).first()
                : Equipment.getItems(items.argdougneCloak).first();
        if (cl == null) {
            log.error("no ardy cloak");
            return false;
        }
        if (cl.interact("Monastery Teleport"))
            return Execution.delayUntil(() -> ardyCloakTele.contains(Players.getLocal()), util::playerAnimating, 2000, 3000);
        return false;
    }

    private Pattern[] dramen() {
        return NeckBank.dramenStaffIfNoAchievementDiary().keySet().toArray(new Pattern[0]);
    }

    private Collection<Pattern> withDramen(Pattern item) {
        var l = new ArrayList<Pattern>();
        l.add(item);
        if (!AchievementDiary.LUMBRIDGE.isEliteComplete())
            l.add(items.dramenStaff);
        return l;
    }

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        var l = new ArrayList<Requirement>();
//        l.add(new RequirementWith(
//                withDramen(items.questCape),
//                () -> Quests.getQuestPoints() >= MAX_QUEST_POINTS));
        l.add(new HouseRequirement(HouseConfig::isHasFairyRing, dramen()));
        l.add(new HouseRequirement(h -> h.hasPortal(PortalNexusTeleport.SalveGraveyard), dramen()));
        l.add(new BaseRequirement(util.join(dramen(), items.argdougneCloak)));
        l.add(new BaseRequirement(util.join(dramen(), items.ringOfWealth)));
        l.add(new HouseRequirement(HouseConfig::hasOrnateJewelleryBox, dramen()));
        return l;
    }

    @Override
    public String toString() {
        return "Fairy Ring (House / Ardy Cloak / Salve Portal / Ring of Wealth)" + this.fairyRing.toString();
    }

    @Override
    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        var fr = getFairyRing();
        if (fr != null) {
            return fairyRingTo(fr, fairyRing, startPosition);
        }
        if (Inventory.contains(items.questCape) || Equipment.contains(items.questCape)) {
            util.questCapeTele();
        } else if (houseConfig.isHasFairyRing() && util.hasHouseTeleport()) {
            util.teleToHouse();
        } else if (houseConfig.hasPortal(PortalNexusTeleport.SalveGraveyard) && util.hasHouseTeleport()) {
            new HousePortalTraverse(PortalNexusTeleport.SalveGraveyard).doTraverseLoop(houseConfig, startPosition);
        } else if (MISCELLANIA.contains(Players.getLocal().getServerPosition())) {
            Traverser.regionPathTraverser(new Coordinate(2515, 3884, 0)).executeLoop();
        } else if (Items.contains(util.inventoryEquipmentSource(), items.ringOfWealth)) {
            new ItemTraverse(items.ringOfWealth, "Rub", "Miscellania").doTraverseLoop(houseConfig, startPosition);
        } else if (houseConfig.hasOrnateJewelleryBox() && util.hasHouseTeleport()) {
            new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Miscellania).doTraverseLoop(houseConfig, startPosition);
        } else if (Inventory.contains(items.argdougneCloak) || Equipment.contains(items.argdougneCloak)) {
            var pos = Players.getLocal().getServerPosition();
            if (ardyCloakTele.contains(pos)) {
                if (this.path == null) {
                    this.path = WaypointPath.create(new Coordinate[]{
                            pos,
                            new Coordinate(2613, 3223, 0),
                            new Coordinate(2632, 3225, 0),
                            new Coordinate(2649, 3232, 0),
                            new Coordinate(2655, 3231, 0)
                    });
                }

                if (this.path != null) {
                    if (!this.path.step())
                        this.path = null;
                } else {
                    log.error("Could not generate path from {} to ardy ring", pos);
                }

            } else {
                ardyCloakTele();
            }
        }
        return false;
    }

    private boolean fairyRingTo(GameObject fairyRing, FairyRing destination, Coordinate startPosition) {
        if (Players.getLocal() == null) return false;
        if (fairyRing == null) {
            log.debug("null fairy ring");
            return false;
        }
        if (!AchievementDiary.LUMBRIDGE.isEliteComplete()) {
            if (Equipment.getItems(items.dramenStaff).isEmpty()) {
                util.equipDramen();
                return false;
            }
        }
        var cp = Players.getLocal().getServerPosition().getContainingRegionId();
        Callable<Boolean> check = () -> cp != Players.getLocal().getServerPosition().getContainingRegionId();
        if (FairyRing.ZANARIS.equals(destination)) {
            DI.get().send(MenuAction.forGameObject(fairyRing, Pattern.compile(".*Zanaris", Pattern.CASE_INSENSITIVE)));
            return Execution.delayUntil(check,
                    util::playerAnimatingOrMoving,
                    3500, 4500);
        } else if (destination.isPreviousDestination()) {
            log.debug("fairy ring to last destination");
            var patternLastDest = Pattern.compile(".*ast-destination \\(" + destination.name() + "\\)");
            DI.get().send(MenuAction.forGameObject(fairyRing, patternLastDest));
            return Execution.delayUntil(check,
                    util::playerAnimatingOrMoving,
                    3500, 4500);
        } else if (FairyRing.isConfigurationOpen()) {
            if (!destination.select()) {
                Execution.delay(1000, 2400);
                return false;
            }
            return Execution.delayUntil(check,
                    util::playerAnimatingOrMoving,
                    3500, 4500);
        } else {
            log.debug("fairy ring configure");
            if (fairyRing.getActiveDefinition().getActions().contains("Configure"))
                DI.get().send(MenuAction.forGameObject(fairyRing, "Configure"));
            else
                DI.get().send(MenuAction.forGameObject(fairyRing, "Ring-configure"));
            Execution.delayUntil(FairyRing::isConfigurationOpen, 2000, 3000);
        }
        return false;
    }
}
