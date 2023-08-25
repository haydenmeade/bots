package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.items;

public class SlayerRingTraverse extends ItemTraverse implements TraverseMethod {
    private final SlayerRingDestination dest;


    public SlayerRingTraverse(SlayerRingDestination dest) {
        super(items.slayerRing, "Rub", "Teleport", dest.getDestination());
        this.dest = dest;
    }
}
