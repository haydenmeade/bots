package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.*;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Log4j2
public class ItemTraverse implements TraverseMethod {
    private final Pattern item;
    private final String[] options;
    private int priority = 0;

    public ItemTraverse(Pattern item, String... options) {
        this.item = item;
        this.options = options;
    }

    public ItemTraverse(String item, String... options) {
        this.item = Pattern.compile(item);
        this.options = options;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public TraverseMethod withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public String toString() {
        return item.toString() + " [" + priority + "] Traverse Method: " + Arrays.toString(options);
    }

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        return List.of(new BaseRequirement(this.item));
    }

    @Override
    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        var reg = Players.getLocal().getServerPosition().getContainingRegionId();
        if (util.isInCombat() && !Equipment.contains(item)) {
            var invItem = Inventory.getItems(item).first();
            if (util.isEquippable(invItem)) {
                util.equip(invItem);
                Execution.delayUntil(() -> Equipment.contains(item), 560, 720);
                return false;
            }
        } else if (util.isInCombat() && isSlayerRing(item) && Equipment.contains(item)) {
            slayerRingCombatTele();
            return Players.getLocal().getServerPosition().getContainingRegionId() != reg;
        }
        var item = Equipment.contains(this.item)
                ? Equipment.getItems(this.item).first()
                : Inventory.getItems(this.item).first();

        if (item == null) {
            log.error("Unable to find {}", this.item);
            return false;
        }
        if (Objects.equals(item.getOrigin(), SpriteItem.Origin.EQUIPMENT) && !ControlPanelTab.EQUIPMENT.isOpen()) {
            ControlPanelTab.EQUIPMENT.open();
            return false;
        }
        if (this.options.length == 0) {
            log.error("no options {}", this.item);
            return false;
        }

        if (!ChatDialog.isOpen()) {
            var sprite = DiBank.getSpriteItemComponent(item);
            if (sprite == null) {
                log.error("No sprite {}", item);
                return false;
            }
            var actions = sprite.getRawActions();
            if (actions == null) return false;
            var l = Arrays.asList(this.options);
            var firstOpt = Arrays.stream(actions)
                    .filter(o -> o != null && l.contains(o))
                    .findFirst();
            if (firstOpt.isEmpty()) {
                log.error("Unable to get first option {}", this.item);
                return false;
            }

            if (!DI.get().send(MenuAction.forSpriteItem(item, firstOpt.get()))) {
                log.error("Unable to option: {} on {}", firstOpt.get(), this.item);
            }
            if (firstOpt.get().toLowerCase().contains("slayer"))
                Execution.delayUntil(() -> ChatDialog.isOpen() || Players.getLocal().getServerPosition().getContainingRegionId() != reg,
                        1000, 1400);
            else
                Execution.delayUntil(() -> ChatDialog.isOpen() || Players.getLocal().getServerPosition().getContainingRegionId() != reg,
                        util::playerAnimating,
                        1000, 1400);
            return false;
        }
        var option = ChatDialog.getOption(this.options);
        if (option != null && option.isValid()) {
            if (!DI.get().interact(option.getComponent(), 0)) {
                log.error("Unable to select: {} on {}, options: {}", option, this.item, options);
                return false;
            }
            var r = Execution.delayUntil(
                    () -> Players.getLocal().getServerPosition().getContainingRegionId() != reg,
                    util::playerAnimating,
                    600, 1200);
            if (!ChatDialog.isOpen() && !r) {
                Execution.delayUntil(
                        () -> Players.getLocal().getServerPosition().getContainingRegionId() != reg,
                        util::playerAnimating,
                        2200, 3200);
            }
            return Players.getLocal().getServerPosition().getContainingRegionId() != reg;
        } else {
            util.moveTo(Players.getLocal().getServerPosition());
        }
        Execution.delay(600, 700);
        return Players.getLocal().getServerPosition().getContainingRegionId() != reg;
    }

    private void slayerRingCombatTele() {
        log.debug("Slayer ring combat teleport");
        var item = Equipment.getItems(this.item).first();
        var p = Players.getLocal();
        var reg = p.getServerPosition().getContainingRegionId();
        if (p.getAnimationId() == AnimationID.TELEPORT || item == null) return;
        var dest = Arrays.stream(SlayerRingDestination.values()).filter(d -> Arrays.stream(this.options).anyMatch(o -> o.equals(d.getDestination()))).findFirst().orElse(
                SlayerRingDestination.StrongholdSlayerCave
        );

        if (Objects.equals(item.getOrigin(), SpriteItem.Origin.EQUIPMENT) && !ControlPanelTab.EQUIPMENT.isOpen()) {
            ControlPanelTab.EQUIPMENT.open();
            return;
        }

        var sprite = DiBank.getSpriteItemComponent(item);
        if (sprite == null) {
            log.error("No sprite {}", item);
            return;
        }

        try {
            Keyboard.pressKey(dest.getKey());
            if (!ChatDialog.isOpen()) {
                if (!DI.get().send(MenuAction.forSpriteItem(item, "Teleport"))) {
                    log.error("Unable to option: {} on {}", "Teleport", this.item);
                }
            }
            Execution.delayUntil(
                    () -> Players.getLocal().getServerPosition().getContainingRegionId() != reg,
                    () -> Players.getLocal().getAnimationId() == AnimationID.TELEPORT,
                    300, 350);
            Keyboard.pressKey(dest.getKey());

        } finally {
            Keyboard.releaseKey(dest.getKey());
        }
    }

    private boolean isSlayerRing(Pattern item) {
        return item.toString().toLowerCase().contains("slayer");
    }
}
