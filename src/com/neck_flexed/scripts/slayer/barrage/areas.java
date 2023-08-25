package com.neck_flexed.scripts.slayer.barrage;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

public class areas {
    public static Area DUST_DEVIL = new Area.Rectangular(new Coordinate(1702, 10023, 0), new Coordinate(1720, 10009, 0));
    public static Coordinate DUST_DEVIL_1 = new Coordinate(1708, 10019, 0);
    public static Coordinate DUST_DEVIL_2 = new Coordinate(1707, 10018, 0);
    public static Area ABYSSAL_DEMON = new Area.Polygonal(
            new Coordinate(1668, 10093, 0),
            new Coordinate(1668, 10102, 0),
            new Coordinate(1681, 10102, 0),
            new Coordinate(1681, 10090, 0),
            new Coordinate(1678, 10086, 0),
            new Coordinate(1677, 10083, 0),
            new Coordinate(1670, 10083, 0),
            new Coordinate(1665, 10088, 0)
    );
    public static Coordinate ABYSSAL_DEMON_1 = new Coordinate(1674, 10092, 0);
    public static Coordinate ABYSSAL_DEMON_2 = new Coordinate(1674, 10090, 0);
    public static Area JELLY = new Area.Rectangular(new Coordinate(1681, 10006, 0), new Coordinate(1695, 9991, 0));
    public static Coordinate JELLY_1 = new Coordinate(1686, 10002, 0);
    public static Coordinate JELLY_2 = new Coordinate(1685, 10001, 0);
    public static Coordinate ANKOU_1 = new Coordinate(1641, 9998, 0);
    public static Coordinate ANKOU_2 = new Coordinate(1642, 9999, 0);
    public static Area NECH = new Area.Rectangular(new Coordinate(1700, 10087, 0), new Coordinate(1711, 10076, 0));
    public static Coordinate NECH_1 = new Coordinate(1709, 10085, 0);
    public static Coordinate NECH_2 = new Coordinate(1708, 10086, 0);

}
