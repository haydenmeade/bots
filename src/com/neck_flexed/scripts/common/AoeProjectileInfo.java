package com.neck_flexed.scripts.common;

import java.util.HashMap;
import java.util.Map;

public enum AoeProjectileInfo {

    /**
     * the AOEs of Grotesque Guardians
     */
    DAWN_FREEZE(ProjectileID.DAWN_FREEZE, 3),
    DUSK_CEILING(ProjectileID.DUSK_CEILING, 3),


    /**
     * Aoe of Addy Drags
     */
    ADDY_DRAG_POISON(ProjectileID.ADDY_DRAG_POISON, 3),

    /**
     * the Breath of the Drake
     */
    DRAKE_BREATH(ProjectileID.DRAKE_BREATH, 1),

    /**
     * Cerbs fire
     */
    CERB_FIRE(ProjectileID.CERB_FIRE, 2),


    /**
     * Marble gargoyle (Superior Gargoyle)
     */
    MARBLE_GARGOYLE_AOE(ProjectileID.MARBLE_GARGOYLE_AOE, 1);

    private static final Map<Integer, AoeProjectileInfo> map = new HashMap<>();

    static {
        for (AoeProjectileInfo aoe : values()) {
            map.put(aoe.id, aoe);
        }
    }

    /**
     * The id of the projectile to trigger this AoE warning
     */
    private final int id;
    /**
     * How long the indicator should last for this AoE warning This might
     * need to be a bit longer than the projectile actually takes to land as
     * there is a fade effect on the warning
     */
    private final int aoeSize;

    AoeProjectileInfo(int id, int aoeSize) {
        this.id = id;
        this.aoeSize = aoeSize;
    }

    public static AoeProjectileInfo getById(int id) {
        return map.get(id);
    }

    public int getId() {
        return id;
    }

    public int getAoeSize() {
        return aoeSize;
    }
}
