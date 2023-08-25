package com.neck_flexed.scripts.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CombatStyle {
    Melee(10),
    Ranged(5),
    Magic(5);
    private final int defaultBoostThreshold;

}
