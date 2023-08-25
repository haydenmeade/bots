package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Landmark;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.web.WebPath;
import com.runemate.game.api.hybrid.web.vertex.objects.BasicObjectVertex;
import com.runemate.game.api.script.Execution;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
public class BankTraverser {
    private final NeckBot<?, ?> bot;
    private final Coordinate start;
    private WebPath webPath;
    private String error;

    @Nullable
    private static LocatableEntity getVisibleBank() {
        var booth = Banks.getLoadedBankBooths().nearest();
        var chest = Banks.getLoadedBankChests().nearest();
        var b = booth != null
                ? booth
                : chest != null
                ? chest
                : Banks.getLoaded().nearest();
        if (b == null) return null;
        var pos = b.getPosition();
        if (pos == null) return null;
        if (pos.isReachable() && b.isValid()) return b;
        return null;
    }


    // ret false if not finished
    public boolean traverse() {
        var b = getVisibleBank();
        if (b != null) return true;
        var traverses = Traverses.BANK;
        var config = bot.getHouseConfig();
        if (House.isInside()) {
            var src = util.inventoryEquipmentSource();
            var houseTraverse = Arrays.stream(traverses).filter(t -> t.isHouse(config) && t.meetsRequirement(config, src)).findFirst();
            if (houseTraverse.isPresent()) {
                traverses = new TraverseMethod[]{houseTraverse.get()};
            }
        }
        var bestTraverse = Traverser.getBestTraverse(traverses, config);

        if (bestTraverse.isEmpty()) {
            log.debug("web to bank");
            this.webPathToBank();
        } else {
            log.debug("Traverse using {} to bank", bestTraverse.get());
            bestTraverse.get().doTraverseLoop(config, start);
        }
        return false;
    }

    public void traverseAndOpen() {
        if (!traverse()) return;
        var b = getVisibleBank();

        // NO BANK
        if (b == null) {
            return;
        }

        // FOUND BANK
        var pos = b.getPosition();
        if (pos.distanceTo(Players.getLocal().getServerPosition()) > 3) {
            bot.di.sendMovement(b.getPosition());
            Execution.delayUntil(() -> pos.distanceTo(Players.getLocal().getServerPosition()) <= 3, util::playerMoving, 1200);
            return;
        }
        if (!Execution.delayUntil(() -> Bank.open(b), util::playerMoving, 3000, 4000)) {
            webPathToBank();
        }
    }

    private void webPathToBank() {

        if (webPath == null) {
            this.webPath = WebPath.buildTo(Landmark.BANK);
        }
        if (webPath != null) {
            var nodeIsGameObject = this.webPath.getNext() instanceof BasicObjectVertex;
            if (!webPath.step(Traverser.OPTS) || nodeIsGameObject) {
                if (Players.getLocal().isMoving()) {
                    Execution.delayUntil(util::isInCombat, util::playerAnimatingOrMoving, 3400, 3600);
                }
                this.webPath = null;
            }
        } else {
            log.error("Could not generate path to bank");
        }
    }
}
