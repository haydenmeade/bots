package com.neck_flexed.scripts.hydra;

import com.runemate.game.api.hybrid.location.Coordinate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

import static com.neck_flexed.scripts.hydra.c.*;

@Getter
@RequiredArgsConstructor
public enum HydraPhase {
    POISON(3, HYDRA_1_1, HYDRA_1_2, ProjectileID.HYDRA_POISON, 0,
            825, new Coordinate(1371, 10263, 0), Color.GREEN, Color.RED
    ),
    LIGHTNING(3, HYDRA_2_1, HYDRA_2_2, 0, HYDRA_LIGHTNING,
            550, new Coordinate(1371, 10272, 0), Color.CYAN, Color.GREEN
    ),
    FLAME(3, HYDRA_3_1, HYDRA_3_2, ProjectileID.HYDRA_FLAME, HYDRA_FIRE,
            275, new Coordinate(1362, 10272, 0), Color.RED, Color.CYAN
    ),
    ENRAGED(1, HYDRA_4_1, HYDRA_4_2, ProjectileID.HYDRA_POISON, 0,
            0, null, null, null
    );

    private final int attacksPerSwitch;
    private final int deathAnimation1;
    private final int deathAnimation2;
    private final int specialProjectileId;
    private final int specialAnimationId;
    private final int hpThreshold;

    private final Coordinate fountainWorldPoint;

    private final Color phaseColor;
    private final Color fountainColor;

}
