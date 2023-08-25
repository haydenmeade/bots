package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.Execution;

public class LighthouseOverride implements TraverseOverride {
    private static final Monster monster = (Monster) Monster.getByEnum(Task.DAGANNOTH).get();

    private GameObject getLadderToFight() {
        return GameObjects.newQuery()
                .on(new Coordinate(2515, 10006, 0))
                .names("Iron ladder")
                .actions("Climb", "Peek")
                .results().first();
    }

    private GameObject getLadder() {
        return GameObjects.newQuery()
                .on(new Coordinate(2509, 3644, 0))
                .names("Iron ladder")
                .actions("Climb")
                .results().first();
    }

    private GameObject getDoor1() {
        return GameObjects.newQuery()
                .on(new Coordinate(2509, 3636, 0))
                .names("Doorway")
                .actions("Walk-through")
                .results().first();
    }

    private GameObject getDoor() {
        return GameObjects.newQuery()
                .on(new Coordinate(2516, 10003, 0))
                .names("Strange wall")
                .actions("Open")
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var slayerBot = (SlayerBotImpl<?>) bot;
        var reg = Players.getLocal().getServerPosition().getContainingRegionId();
        if (reg == 10040) {
            if (bot.di.doDiInteractObstacleReachable(this::getLadder, "Climb", p -> p.getServerPosition().getContainingRegionId() != 10040))
                return true;
            if (bot.di.doDiInteractObstacle(this::getDoor1, "Walk-through", p -> !util.playerAnimatingOrMoving()))
                return true;
            return false;
        }
        var loc = monster.getLocation(slayerBot);
        if (loc.getCannonSpot().isReachable()) {
            bot.prayerFlicker.setActivePrayers(Prayer.PROTECT_FROM_MISSILES);
            bot.di.sendMovement(loc.getCannonSpot());
            Execution.delayUntil(() -> loc.getArea().contains(Players.getLocal().getServerPosition()), util::playerMoving, 1200, 2400);
            return true;
        }

        var txt = ChatDialog.getText();
        if (ChatDialog.isOpen() && txt != null && txt.contains("there are no adventurers")) {
            bot.di.doDiInteractObstacleReachable(this::getLadderToFight, "Climb", p -> p.getAnimationId() != -1);
            return true;
        } else if (txt != null) {
            slayerBot.forceState(SlayerState.HOPPING);
            return true;
        }
        if (bot.di.doDiInteractObstacleReachable(this::getLadderToFight, "Peek", p -> ChatDialog.isOpen()))
            return true;
        if (bot.di.doDiInteractObstacle(this::getDoor, "Open", p -> !p.isMoving()))
            return true;
        return false;
    }
}
