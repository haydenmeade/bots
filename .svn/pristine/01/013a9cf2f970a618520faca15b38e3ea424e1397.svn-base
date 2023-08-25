package com.neck_flexed.scripts.plank;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.KeyEvent;

@RequiredArgsConstructor
public enum Plank {
    Regular("Plank", "Logs", KeyEvent.VK_1),
    Oak("Oak plank", "Oak logs", KeyEvent.VK_2),
    Teak("Teak plank", "Teak logs", KeyEvent.VK_3),
    Mahogany("Mahogany plank", "Mahogany logs", KeyEvent.VK_4);

    @Getter
    private final String gameName;
    @Getter
    private final String logName;
    @Getter
    private final int key;

    public boolean hasPlanks() {
        return Inventory.contains(gameName);
    }

    @Override
    public String toString() {
        return this.gameName;
    }
}
