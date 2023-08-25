package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.Skills;
import com.runemate.game.api.hybrid.local.WorldOverview;
import com.runemate.game.api.hybrid.local.WorldType;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.osrs.location.WorldRegion;
import com.runemate.game.api.script.Execution;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;

@Log4j2(topic = "Hopping")
public class Hopping {

    private static final int CONTAINER_INDEX = 69;
    private static final List<WorldType> EXCLUDE = List.of(
            WorldType.QUEST_SPEEDRUNNING,
            WorldType.TOURNAMENT_WORLD,
            WorldType.PVP,
            WorldType.BOUNTY,
            WorldType.PVP_ARENA,
            WorldType.QUEST_SPEEDRUNNING,
            WorldType.LAST_MAN_STANDING,
            WorldType.BETA,
            WorldType.NOSAVE_MODE,
            WorldType.TOURNAMENT_WORLD,
            WorldType.FRESH_START_WORLD,
            WorldType.DEADMAN,
            WorldType.HIGH_RISK,
            WorldType.SEASONAL
    );

    public static boolean isOpen() {
        return ControlPanelTab.WORLD_HOP.isOpen();
    }

    public static boolean open() {
        return ControlPanelTab.WORLD_HOP.open();
    }

    public static boolean close() {
        final InterfaceComponent component = Interfaces.newQuery()
                .containers(69)
                .actions("Close")
                .sprites(535)
                .heights(23)
                .widths(26)
                .types(InterfaceComponent.Type.SPRITE)
                .results()
                .first();
        return component != null && component.interact("Close");
    }

    private static boolean to(int targetWorld) {
        log.info("Hopping to world {}", targetWorld);
        if (targetWorld < 0) {
            log.warn("Invalid request to hop to world {}", targetWorld);
            return false;
        }
        int initialWorld = Worlds.getCurrent();
        if (targetWorld == initialWorld) {
            log.info("The target world of {} is already loaded.", targetWorld);
            return true;
        }
        log.debug("Attempting to hop to world {} from {}", targetWorld, initialWorld);
        if (open()) {
            final InterfaceComponent wc = getWorldComponent(targetWorld);
            if (wc == null) {
                log.warn("Couldn't find world component");
                return false;
            }
            final InterfaceComponent cc = getContainerComponent();
            if (cc == null) {
                log.warn("Couldn't find container component");
                return false;
            }
            DI.get().send(MenuAction.forInterfaceComponent(wc, 0));
            Execution.delayUntil(() -> Worlds.getCurrent() == targetWorld || getHopWarningDialog() != null, 6000, 9000);
            ChatDialog.Option confirm = getHopWarningDialog();
            if (confirm != null && confirm.select()) {
                Execution.delayUntil(() -> Worlds.getCurrent() == targetWorld, 6000, 9000);
            }
        }
        return targetWorld == Worlds.getCurrent();
    }

    private static ChatDialog.Option getHopWarningDialog() {
        return ChatDialog.getOption("Yes. In future, only warn about dangerous worlds.",
                "Switch world",
                "Switch World"
        );
    }

    private static InterfaceComponent getContainerComponent() {
        return Interfaces.newQuery()
                .containers(CONTAINER_INDEX)
                .types(InterfaceComponent.Type.CONTAINER)
                .grandchildren(false)
                .widths(174)
                .heights(193)
                .results()
                .first();
    }

    private static InterfaceComponent getWorldComponent(int worldId) {
        return Interfaces.newQuery()
                .containers(CONTAINER_INDEX)
                .types(InterfaceComponent.Type.SPRITE)
                .grandchildren(true)
                .names(Integer.toString(worldId))
                .visible()
                .results()
                .first();
    }

    private static boolean isSafeAndUsable(@NonNull WorldOverview destination, int current) {
        int totalLevel = IntStream.of(Skills.getBaseLevels()).sum();
        EnumSet<WorldType> types = destination.getWorldTypes();
        return destination.getId() != current
                && destination.isMembersOnly() &&
                types.stream().noneMatch(EXCLUDE::contains)
                && !WorldType.isPvp(destination.getWorldTypes())
                && canTotalLevel(destination, totalLevel);
    }

    private static boolean canTotalLevel(@NotNull WorldOverview destination, int totalLevel) {
        return (!destination.isSkillTotal2200() || totalLevel >= 2200) && (!destination.isSkillTotal2000() || totalLevel >= 2000) && (!destination.isSkillTotal1750() || totalLevel >= 1750) && (!destination.isSkillTotal1500() || totalLevel >= 1500) && (!destination.isSkillTotal1250() || totalLevel >= 1250) && (!destination.isSkillTotal750() || totalLevel >= 750) && (!destination.isSkillTotal500() || totalLevel >= 500);
    }

    public static boolean hop(int world) {
        log.debug("hopping");
        if (!isOpen() && !open()) {
            Execution.delay(1200, 1400);
            log.error("Unable to open world hopper");
            return false;
        }
        var current = Worlds.getCurrent();
        log.debug("current {}", current);
        if (!to(world)) {
            return false;
        }
        return Execution.delayUntil(() -> current != Worlds.getCurrent() && Players.getLocal() != null, 9000, 12000);
    }

    public static boolean hop(WorldRegion region) {
        log.debug("hopping");
        if (!isOpen() && !open()) {
            Execution.delay(1200, 1400);
            log.error("Unable to open world hopper");
            return false;
        }
        var current = Worlds.getCurrent();
        log.debug("current {}", current);
        var newWorldWithRegion = Worlds.newQuery()
                .filter(w -> isSafeAndUsable(w, current) && w.getRegion().getAlpha2().equals(region.getAlpha2()))
                .results();
        var newWorldWithNoRegion = Worlds.newQuery().filter(w -> isSafeAndUsable(w, current)).results();
        var newWorld = newWorldWithRegion.isEmpty() ? newWorldWithNoRegion.random() : newWorldWithRegion.random();
        if (newWorld == null) return false;
        if (!to(newWorld.getId())) {
            return false;
        }
        Execution.delayUntil(() -> current != Worlds.getCurrent() && Players.getLocal() != null, 9000, 12000);
        Execution.delay(5000, 6000);
        return current != Worlds.getCurrent();
    }
}
