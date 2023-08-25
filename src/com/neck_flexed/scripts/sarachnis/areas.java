package com.neck_flexed.scripts.sarachnis;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;

public class areas {
    public static Area bossRoom = new Area.Rectangular(new Coordinate(1830, 9911, 0), new Coordinate(1852, 9891, 0));
    private static Area forthosDungeon = new Area.Rectangular(new Coordinate(1778, 9994, 0), new Coordinate(1869, 9887, 0));

    public static boolean isInBossRoom() {
        return bossRoom.contains(Players.getLocal());
    }

    public static boolean isInForthosDungeon() {
        return forthosDungeon.contains(Players.getLocal());
    }
}
