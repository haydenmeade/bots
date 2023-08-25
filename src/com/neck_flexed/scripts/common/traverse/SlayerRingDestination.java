package com.neck_flexed.scripts.common.traverse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.KeyEvent;

@RequiredArgsConstructor
@Getter
public enum SlayerRingDestination {
    StrongholdSlayerCave("Teleport to the Stronghold Slayer Cave", KeyEvent.VK_1),
    SlayerTower("Teleport to the Morytania Slayer Tower", KeyEvent.VK_2),
    FremennikSlayerDungeon("Teleport to the Rellekka Slayer Caves", KeyEvent.VK_3),
    TarnsLair("Teleport to Tarn's Lair", KeyEvent.VK_4),
    DarkBeasts("Teleport to Dark Beasts", KeyEvent.VK_5);
    private final String destination;
    private final int key;
}
