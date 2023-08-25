package com.neck_flexed.scripts.common.overlay.render;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class RenderTarget {

    private final Object target;
    private final Paint paint;

    public RenderTarget(final Object target, final Paint paint) {
        this.target = target;
        this.paint = paint;
    }

    public RenderTarget(final Object target) {
        this.target = target;
        this.paint = defaultPaint(target);
    }

    private static Paint defaultPaint(Object target) {
        if (target instanceof LocatableEntity) {
            if (target instanceof Npc) {
                return Color.DODGERBLUE;
            }

            if (target instanceof Player) {
                return Color.BLUE;
            }

            if (target instanceof GroundItem) {
                return Color.GREEN;
            }

            return Color.RED;
        }

        if (target instanceof InterfaceComponent) {
            return Color.HOTPINK;
        }

        if (target instanceof SpriteItem) {
            return Color.YELLOWGREEN;
        }

        if (target instanceof Coordinate) {
            return Color.ORANGE;
        }

        if (target instanceof Path) {
            return Color.LIGHTPINK;
        }

        if (target instanceof Area) {
            return Color.FIREBRICK;
        }

        return Color.BLACK;
    }
}
