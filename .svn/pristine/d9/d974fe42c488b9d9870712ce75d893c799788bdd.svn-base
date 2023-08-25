package com.neck_flexed.scripts.cerberus;

import com.runemate.game.api.hybrid.location.Coordinate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@Getter
@RequiredArgsConstructor
public enum Arena {
    WEST(1231, 1249, 1243, 1257),
    NORTH(1295, 1313, 1307, 1321),
    EAST(1359, 1377, 1243, 1257);

    private final int x1, x2, y1, y2;

    @Nullable
    public static Arena getArena(final Coordinate c) {
        for (final Arena arena : Arena.values()) {
            if (c.getX() >= arena.getX1() && c.getX() <= arena.getX2() &&
                    c.getY() >= arena.getY1() && c.getY() <= arena.getY2()) {
                return arena;
            }
        }

        return null;
    }

    public Coordinate getGhostTile(final int ghostIndex) {
        if (ghostIndex > 2 || ghostIndex < 0) {
            return null;
        }

        return new Coordinate(x1 + 8 + ghostIndex, y1 + 13, 0);
    }
}
