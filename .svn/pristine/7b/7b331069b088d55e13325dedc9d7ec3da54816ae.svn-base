package com.neck_flexed.scripts.dk;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class dkAreas {
    static Coordinate dungeonEntrance = new Coordinate(2519, 3738, 0);
    static Coordinate dropRockTile = new Coordinate(2490, 10162, 0);
    static Coordinate standRockTile = new Coordinate(2490, 10164, 0);
    // "Destroy" "Door-support"
    // "Climb-down" "Iron ladder"
    static Coordinate throwAxeTile = new Coordinate(2545, 10146, 0);
    static Area pastDoor = new Area.Rectangular(new Coordinate(2491, 10172, 0), new Coordinate(2550, 10138, 0));

    static Area waterbirth =
            Area.rectangular(new Coordinate(2491, 3773, 0), new Coordinate(2563, 3709, 0));
    static Area frem =
            new Area.Rectangular(new Coordinate(2590, 3730, 0), new Coordinate(2747, 3619, 0));
    static Area lunar = new Area.Rectangular(new Coordinate(2039, 3967, 0), new Coordinate(2189, 3832, 0));
    static Area dungeon1 =
            Area.rectangular(new Coordinate(2430, 10177, 0), new Coordinate(2560, 10110, 0));
    static Area bossRoomStandard = Area.rectangular(
            new Coordinate(2879, 4474, 0), new Coordinate(2944, 4422, 0)
    );

    // dungeon: 1698, 4465, 2012, 4279
    static Area bossRoomSlayer = Area.rectangular(
            new Coordinate(2873, 4409, 0), new Coordinate(2947, 4354, 0)
    );
    static Coordinate safespotTile = new Coordinate(1917, 4363, 0);
    static Area peekArea = new Area.Rectangular(new Coordinate(1916, 4366, 0), new Coordinate(1919, 4361, 0));
    static Area bottomArea = new Area.Rectangular(new Coordinate(1872, 4414, 0), new Coordinate(1966, 4351, 0));

    static Area getCurrentBossArea() {
        return bossRoomSlayer.contains(Players.getLocal())
                ? bossRoomSlayer
                : bossRoomStandard;
    }

    static boolean inPeekArea() {
        return peekArea.contains(Players.getLocal());
    }

    static boolean inDungeon2() {
        var r = Players.getLocal().getPosition().getContainingRegionId();
        return r == 7236 || r == 7492 || r == 7748;
    }

    static boolean inBossRoom() {
        return bossRoomStandard.contains(Players.getLocal()) || bossRoomSlayer.contains(Players.getLocal());
    }

    public static boolean onSafespot() {
        var p = Players.getLocal();
        if (p == null) return false;
        return safespotTile.equals(p.getServerPosition());
    }

    public static boolean inDungeon1() {
        return dungeon1.contains(Players.getLocal());
    }
}
