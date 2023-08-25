package com.neck_flexed.scripts.slayer.state;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.traverse.BankTraverser;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.collections.Pair;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Log4j2
public class GetNewTaskState extends BaseState<SlayerState> {
    private static final Map<Pattern, String> infiniteTeleItemMap = Map.of(
            items.questCape, "Teleport",
            items.craftingCape, "Teleport",
            items.farmingCape, "Teleport",
            items.fishingCape, "Fishing Guild",
            items.drakans, "Ver Sinhaza",
            Pattern.compile(items.desertAmulet4), "Nardah",
            Pattern.compile(items.royalSeedPod), "Commune"
    );
    private final NeckBot<?, SlayerState> bot;
    private final SlayerMonster prevMonster;
    private final Coordinate startPos;
    private boolean done = false;
    private BankTraverser traverser;

    public GetNewTaskState(NeckBot<?, SlayerState> bot, SlayerMonster prevMonster) {
        super(bot, SlayerState.GET_NEW_TASK);
        this.bot = bot;
        this.prevMonster = prevMonster;
        bot.prayerFlicker.disable();
        this.startPos = Players.getLocal().getServerPosition();
    }

    @Override
    public void activate() {
        bot.updateStatus("Get New Task");
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public void executeLoop() {
        if (Health.isPoisoned()) {
            done = true;
            return;
        }
        if (!util.canNpcContact() || House.isInside()) {
            done = true;
            return;
        }
        // decide if in safe place or go to one.
        var p = Players.getLocal();
        if (p == null) return;
        var playerLevel = p.getCombatLevel();
        boolean isSafe = (prevMonster == null || prevMonster.getLocation(bot).isInSafeArea())
                && Npcs.newQuery().
                filter(n -> {
                    // if will agro
                    var willAggro = (n.getLevel() * 2 + 1) >= playerLevel;
                    // if any already targeting
                    var targetingMe = Objects.equals(n.getTarget(), p);
                    // filter out if none of these
                    return willAggro || targetingMe;
                })
                .results()
                .isEmpty();

        if (isSafe) {
            done = true;
            return;
        }

        var infiniteTeleItem = getInfiniteTeleItem();
        var goToHouse = util.hasHouseTeleport();
        if (goToHouse) {
            if (!House.isInside()) {
                util.teleToHouse();
                Execution.delayUntil(House::isInside, util::playerAnimating, 6000, 10000);
                Execution.delay(600, 1200);
                this.done = true;
            }
        } else if (infiniteTeleItem != null) {
            var r = p.getServerPosition().getContainingRegionId();
            infiniteTeleItem.getLeft().interact(infiniteTeleItem.getRight());
            Execution.delayUntil(
                    () -> r != Players.getLocal().getServerPosition().getContainingRegionId(), util::playerAnimating, 3000, 4000);
            Execution.delay(600, 1200);
            this.done = true;
        } else {
            if (this.traverser == null)
                this.traverser = new BankTraverser(bot, startPos);
            this.done = traverser.traverse();
        }
    }

    private @Nullable Pair<SpriteItem, String> getInfiniteTeleItem() {
        for (var kv : infiniteTeleItemMap.entrySet()) {
            var item = Inventory.contains(kv.getKey())
                    ? Inventory.getItems(kv.getKey()).first()
                    : Equipment.getItems(kv.getKey()).first();
            if (item != null) {
                return new Pair<>(item, kv.getValue());
            }
        }
        return null;
    }
}