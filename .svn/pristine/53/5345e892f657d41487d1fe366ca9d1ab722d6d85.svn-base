package com.neck_flexed.scripts.slayer;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public interface SlayerBot {

    SlayerMonster getPreviousTask();

    void setPreviousTask(SlayerMonster previousTask);

    Map<Coordinate, Long> getDeathTiles();

    Collection<Npc> getSuperiorBlacklist();

    default void timeCheckDeathTiles() {
        for (var p :
                getDeathTiles().entrySet()) {
            if (p.getValue() - System.currentTimeMillis() > 15000)
                getDeathTiles().remove(p.getKey(), p.getValue());
        }
    }

    default void handleNpcDeath(SlayerMonster monster, Npc npc, Npc currentTarget, Coordinate lootPosition) {
        if (!monster.isMonster(npc)) return;
        if (Objects.equals(npc, currentTarget)
                || npc.getTarget() == null
                || Objects.equals(Players.getLocal(), npc.getTarget())) {
            addKill();
            if (lootPosition.isReachable())
                getDeathTiles().put(lootPosition, System.currentTimeMillis());
        }
    }

    default void handleNpcDeath(SlayerMonster monster, Npc npc, Npc currentTarget) {
        handleNpcDeath(monster, npc, currentTarget, npc.getServerPosition());
    }

    void taskCompleted();

    void addKill();
}
