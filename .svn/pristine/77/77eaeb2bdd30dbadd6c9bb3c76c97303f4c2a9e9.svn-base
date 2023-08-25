package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.DI;
import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.PortalNexusTeleport;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceContainers;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.List;

@Log4j2
public class HousePortalTraverse implements TraverseMethod {
    private final PortalNexusTeleport portal;

    public HousePortalTraverse(PortalNexusTeleport portal) {
        this.portal = portal;
    }

    public static GameObject getPortalNexus() {
        return GameObjects.newQuery()
                .names("Portal Nexus")
                .results().nearest();
    }

    public static boolean teleHousePortal(GameObject go, Coordinate startPosition) {
        if (go == null) {
            log.error("No house portal");
            return false;
        } else {
            DI.get().send(MenuAction.forGameObject(go, "Enter"));
            return true;
        }
    }

    public static GameObject getPortalLoop(PortalNexusTeleport portal) {
        return GameObjects.newQuery()
                .names(portal.getOption())
                .results().nearest();
    }

    public boolean nexusTeleTo(PortalNexusTeleport t, Coordinate startPosition) {
        if (t == null) return false;
        var n = getPortalNexus();
        var iface = InterfaceContainers.getAt(17);
        if (n == null) return false;
        var def = n.getActiveDefinition();
        if (def == null) return false;
        if (iface != null) {
            var ifaceChild = iface.getComponent(12);
            if (ifaceChild == null) return false;
            var childLabel = ifaceChild.getChild(
                    c -> {
                        if (!c.getType().equals(InterfaceComponent.Type.LABEL)) return false;
                        var text = c.getText();
                        if (text == null) return false;
                        return t.getOption().matcher(text).find();
                    }
            );
            if (childLabel == null) {
                log.error("unable to find teleport: {}", t);
                return false;
            }
            var text = childLabel.getText();
            if (text == null) return false;
            var key = text.substring(0, 1);
            Keyboard.typeKey(key);
            return true;
        } else if (def.getActions().stream().anyMatch(a -> this.portal.getOption().matcher(a).matches())) {
            var action = def.getActions().stream().filter(a -> this.portal.getOption().matcher(a).matches()).findFirst();
            if (action.isEmpty()) return false;
            DI.get().send(MenuAction.forGameObject(n, action.get()));
            return true;
        } else {
            DI.get().send(MenuAction.forGameObject(n, "Teleport Menu"));
            Execution.delayUntil(() -> InterfaceContainers.getAt(17) != null, util::playerMoving, 1200, 2400);
            return false;
        }
    }

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        return List.of(new HouseRequirement(h -> h.hasPortal(this.portal)));
    }

    @Override
    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        if (!House.isInside()) {
            util.teleToHouse();
            return false;
        }
        var sr = Players.getLocal().getServerPosition().getContainingRegionId();
        var portalLoop = getPortalLoop(this.portal);
        var res = portalLoop == null
                ? nexusTeleTo(this.portal, startPosition)
                : teleHousePortal(portalLoop, startPosition);
        if (res) {
            Execution.delayUntil(
                    () -> !House.isInside() && sr != Players.getLocal().getServerPosition().getContainingRegionId(),
                    util::playerAnimatingOrMoving,
                    3000, 4000);
        }
        return res;
    }

    @Override
    public String toString() {
        return "House Portal Traverse: " + this.portal.toString();
    }
}
