package com.neck_flexed.scripts.ashes;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public enum Ash {
    All("All banked ashes"),
    Fiendish("Fiendish ashes"),
    //Fiendish ashes,Vile ashes,Malicious ashes,Abyssal ashes,Infernal ashes
    Vile("Vile ashes"),
    Malicious("Malicious ashes"),
    Abyssal("Abyssal ashes"),
    Infernal("Infernal ashes");

    private static Pattern allPattern = Pattern.compile("(?:Fiendish ashes|Vile ashes|Malicious ashes|Abyssal ashes|Infernal ashes)");
    private final String gameName;

    public static boolean hasNoAshesLeft() {
        return !Inventory.contains(allPattern);
    }

    public static int ashCount() {
        return Inventory.getQuantity(allPattern);
    }

    private String getGameName() {
        return gameName;
    }

    @Override
    public String toString() {
        return this.gameName;
    }

    public Pattern getPattern() {
        return this.equals(All) ? allPattern : Pattern.compile(this.gameName);
    }
}
