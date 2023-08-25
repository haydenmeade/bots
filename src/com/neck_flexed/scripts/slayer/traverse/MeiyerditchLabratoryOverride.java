package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.DI;
import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.Quest;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

@Log4j2
public class MeiyerditchLabratoryOverride implements TraverseOverride {
    static final Area entry = new Area.Rectangular(new Coordinate(3620, 9699, 0), new Coordinate(3641, 9681, 0));
    private static final Area VER_SINHAZA = new Area.Rectangular(new Coordinate(3640, 3235, 0), new Coordinate(3685, 3203, 0));
    private Traverser webtraverser;

    public static boolean hasReq(SlayerBotImpl<?> b) {
        if (!Quest.OSRS.SINS_OF_THE_FATHER.getStatus().equals(Quest.Status.COMPLETE)) return false;
        if (b.getItemCache() == null || b.getItemCache().isEmpty()) return true;
        return items().keySet().stream().allMatch(k -> b.getItemCache().stream().anyMatch(i -> i.getDefinition() != null && k.matcher(i.getDefinition().getName()).matches()));
    }

    public static Map<Pattern, Integer> items() {
        return NeckBank.toMap(1, "Vyre noble shoes", "Vyre noble top", "Vyre noble legs");
    }

    Callable<GameObject> getGameObject(int x, int y, String name, String action) {
        return getGameObject(new Coordinate(x, y, 0), name, action);
    }

    Callable<GameObject> getGameObject(Coordinate coordinate, String name, String action) {
        return () -> GameObjects.newQuery()
                .on(coordinate)
                .names(name)
                .actions(action)
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var di = bot.di;
        var pos = Players.getLocal().getServerPosition();
        var reg = pos.getContainingRegionId();
        if (VER_SINHAZA.contains(pos)) {
            var door = getGameObject(3647, 3218, "Door", "Enter");
            if (di.doDiInteractObstacle(door, "Enter", p -> !VER_SINHAZA.contains(p)))
                return true;
            Execution.delay(600, 650);
            return true;
        } else if (reg == 14386 || reg == 14385) {
            final var options = new String[]{"Send me to the mines.", "Send me to the mines! (Do a bit of menial work)"};
            if (!ChatDialog.isOpen()) {
                interactVyrewatch();
                Execution.delay(100, 150);
                return true;
            }

            if (ChatDialog.getContinue() != null) {
                ChatDialog.getContinue().select();
                Execution.delay(100, 150);
                return true;
            }

            var opt = ChatDialog.getOption(options);
            if (opt != null && opt.isValid()) {
                opt.select();
                Execution.delay(100, 150);
                return true;
            }
            return true;
        } else if (reg == 9544) {
            bot.loadouts.invalidateCurrent();
            items().keySet().forEach(util::equip);
            if (!Equipment.containsAllOf(items().keySet().toArray(Pattern[]::new))) {
                Execution.delay(100, 150);
                return true;
            }

            if (ChatDialog.isOpen()) {
                var cont = ChatDialog.getContinue();
                if (cont != null && cont.isValid()) {
                    cont.select();
                    Execution.delay(100, 150);
                }
                return true;
            }


            var vampyreJuve = Npcs.newQuery()
                    .names("Vampyre Juvinate")
                    .actions("Talk-to")
                    .results().nearest();
            if (vampyreJuve != null) {
                bot.di.send(MenuAction.forNpc(vampyreJuve, "Talk-to"));
                Execution.delayUntil(ChatDialog::isOpen, util::playerMoving, 2000);
                return true;
            }

            Execution.delay(100, 150);
            return true;
        } else if (reg == 14387) {
            var door1 = getGameObject(3639, 3302, "Door", "Open");
            var door2 = getGameObject(3638, 3304, "Slashed tapestry", "Walk-through");
            var door3 = getGameObject(3641, 3307, "Door", "Open");
            var stairs = getGameObject(3643, 3304, "Staircase", "Climb-down");

            if (di.doDiInteractObstacleReachable(stairs, "Climb-down", p -> p.getServerPosition().getContainingRegionId() != 14387))
                return true;

            if (di.doDiInteractObstacleReachableArea(door3, "Open", p -> !p.isMoving()))
                return true;

            if (pos.getY() <= 3304 && di.doDiInteractObstacleReachableArea(door2, "Walk-through", p -> p.getServerPosition().getY() > 3304))
                return true;
            if (di.doDiInteractObstacleReachableArea(door1, "Open", p -> !p.isMoving()))
                return true;
            Traverser.regionPathTraverser(new Coordinate(3641, 3302, 0)).executeLoop();
            return true;
        }

        var door4 = getGameObject(3629, 9680, "Door", "Open");
        var cannon = new Coordinate(3596, 9743, 0);
        if (!entry.contains(pos)) {
            if (this.webtraverser == null)
                this.webtraverser = Traverser.webPathTraverser(cannon);
            this.webtraverser.executeLoop();
            return true;
        }

        if (di.doDiInteractObstacleReachableArea(door4, "Open", p -> p.getServerPosition().getY() <= 9680))
            return true;


        return true;
    }

    private void interactVyrewatch() {
        var pos = new Coordinate(3618, 3205, 0);
        var vyre = Npcs.newQuery().names("Vyrewatch").reachable().results().nearest();
        if (vyre != null) {
            if (DI.get().send(MenuAction.forNpc(vyre, "Talk-to")))
                Execution.delayUntil(ChatDialog::isOpen, util::playerMoving, 3000, 4000);
            return;
        }
        Traverser.regionPathTraverser(pos).executeLoop();
    }
}
