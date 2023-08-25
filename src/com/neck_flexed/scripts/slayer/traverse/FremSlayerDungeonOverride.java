package com.neck_flexed.scripts.slayer.traverse;

import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

public class FremSlayerDungeonOverride implements TraverseOverride {
    private final boolean disableShorcut2;

    public FremSlayerDungeonOverride() {
        this.disableShorcut2 = false;
    }

    public FremSlayerDungeonOverride(boolean disableShorcut2) {
        this.disableShorcut2 = disableShorcut2;
    }

    // req: 81 agil
    private static GameObject getShortcut1() {
        return GameObjects.newQuery()
                .names("Strange floor")
                .actions("Jump-over")
                .on(new Coordinate(2774, 10003, 0))
                .results().first();
    }

    private static GameObject getShortcut1dot5() {
        return GameObjects.newQuery()
                .names("Strange floor")
                .actions("Jump-over")
                .on(new Coordinate(2769, 10002, 0))
                .results().first();
    }

    // req: 62 agil
    private static GameObject getShortcut2() {
        return GameObjects.newQuery()
                .names("Crevice")
                .actions("Squeeze-through")
                .on(new Coordinate(2734, 10008, 0))
                .results().first();
    }

    @Override
    public boolean overrideLoop(NeckBot<?, ?> bot, Coordinate destination) {
        var agil = Skill.AGILITY.getCurrentLevel();
        var pos = Players.getLocal().getServerPosition();
        var s1 = getShortcut1();
        var s15 = getShortcut1dot5();
        var s2 = getShortcut2();
        if (agil >= 81 && s1 != null && pos.getX() > 2774) {
            bot.di.send(MenuAction.forGameObject(s1, "Jump-over"));
            Execution.delayUntil(() -> Players.getLocal().getServerPosition().getX() < 2774,
                    util::playerAnimatingOrMoving, 2400);
            return true;
        } else if (agil >= 81 && s15 != null && pos.getX() > 2769) {
            bot.di.send(MenuAction.forGameObject(s15, "Jump-over"));
            Execution.delayUntil(() -> Players.getLocal().getServerPosition().getX() < 2769,
                    util::playerAnimatingOrMoving, 2400);
            return true;
        } else if (!disableShorcut2 && agil >= 62 && s2 != null && pos.getX() > 2732) {
            bot.di.send(MenuAction.forGameObject(s2, "Squeeze-through"));
            Execution.delayUntil(() -> Players.getLocal().getServerPosition().getX() <= 2730,
                    util::playerAnimatingOrMoving, 2400);
            return true;
        }
        return false;
    }
}
