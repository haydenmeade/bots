package com.neck_flexed.scripts.sire;

import com.runemate.game.api.hybrid.location.Coordinate;

public class Phase3 {

    public static final int explosionAnim = 7098;
    private static final int xx = 2969;
    private static final int xx2 = 2971;
    private static final int yy = 4770;
    public static final Coordinate tile1S = new Coordinate(xx, yy);
    public static final Coordinate tile2S = new Coordinate(xx2, yy);
    private static final int yy2 = 4772;
    public static final Coordinate tile2 = new Coordinate(xx2, yy2);
    public static final Coordinate tile1 = new Coordinate(xx, yy2);

    public static Coordinate getMoveToTile(Coordinate current) {
        if (current.equals(Phase3.tile1) || current.equals(Phase3.tile1S))
            return Phase3.tile2S;
        return Phase3.tile1S;
    }

    public static Coordinate getMoveToTile2(Coordinate current) {
        if (current.equals(Phase3.tile1) || current.equals(Phase3.tile1S))
            return Phase3.tile2;
        return Phase3.tile1;
    }
}
