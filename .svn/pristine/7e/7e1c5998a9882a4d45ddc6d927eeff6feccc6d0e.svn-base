package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Instance {
    public static Coordinate fromInstanced(Coordinate loaded) {
        if (Region.isInstanced()) {
            var chunk = Region.getTemplate(loaded);
            var chunkBase = chunk.getBase();
            var x = chunkBase.getX() + (loaded.getX() & (8 - 1));
            var y = chunkBase.getY() + (loaded.getY() & (8 - 1));
            return rotate(new Coordinate(x, y, chunkBase.getPlane()), 4 - chunk.getOrientation());
        }
        return loaded;
    }

    public static Coordinate toInstancedFirst(Coordinate instanced) {
        var r = toInstanced(instanced);
        return r.isEmpty() ? null : r.iterator().next();
    }

    public static Collection<Coordinate> toInstanced(Coordinate instanced) {
        if (Region.isInstanced()) {
            var results = new ArrayList<Coordinate>();
            var base = Region.getBase(instanced.getPlane());
            var chunks = Region.getInstanceTemplateChunks();
            for (var z = 0; z < chunks.length; z++) {
                for (int x = 0; x < chunks[z].length; x++) {
                    for (int y = 0; y < chunks[z][x].length; y++) {
                        var data = chunks[z][x][y];
                        var chunk = new Region.Template(data);
                        var plane = data >> 24 & 0x3;
                        if (plane != instanced.getPlane()) {
                            continue;
                        }
                        if (chunk.contains(instanced)) {
                            var result = rotate(
                                    new Coordinate(
                                            base.getX() + (x * 8) + (instanced.getX() & (8 - 1)),
                                            base.getY() + (y * 8) + (instanced.getY() & (8 - 1)),
                                            z
                                    ),
                                    chunk.getOrientation()
                            );
                            results.add(result);
                        }
                    }
                }
            }
            return results;
        }
        return Collections.singletonList(instanced);
    }

    private static Coordinate rotate(Coordinate point, int rotation) {
        int chunkX = point.getX() & ~(8 - 1);
        int chunkY = point.getY() & ~(8 - 1);
        int x = point.getX() & (8 - 1);
        int y = point.getY() & (8 - 1);
        switch (rotation) {
            case 1:
                return new Coordinate(chunkX + y, chunkY + (8 - 1 - x), point.getPlane());
            case 2:
                return new Coordinate(chunkX + (8 - 1 - x), chunkY + (8 - 1 - y), point.getPlane());
            case 3:
                return new Coordinate(chunkX + (8 - 1 - y), chunkY + x, point.getPlane());
        }
        return point;
    }
}
