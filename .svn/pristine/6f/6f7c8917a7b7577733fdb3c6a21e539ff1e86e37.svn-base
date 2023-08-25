package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.cache.configs.EnumDefinition;
import com.runemate.game.api.hybrid.cache.configs.EnumDefinitions;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Log4j2(topic = "SpecialAttackCost")
public class SpecialAttackCost {
    public final static int SPECIAL_ATTACK_COST = 906;

    private static Map<Integer, Integer> costs = new HashMap<>();
    private static boolean initialized = false;

    public static synchronized void initialize() {
        if (!initialized) {
            EnumDefinition enumDef = EnumDefinitions.load(SPECIAL_ATTACK_COST);
            if (enumDef != null) {
                initialized = true;
                for (int key : enumDef.getKeys()) {
                    Serializable value = enumDef.get(key);
                    if (value instanceof Integer) {
                        costs.put(key, (Integer) value / 10);
                    } else {
                        log.warn(String.format("Key (%d) in Special_Attack_Cost enum (%d) was not linked to an integer value (%s)", key, SPECIAL_ATTACK_COST, value));
                    }
                }
            } else {
                log.warn("null special attack cost enum def");
            }
        }
    }

    @Nullable
    public static Integer getCost(SpriteItem item) {
        return getCost(item.getId());
    }

    @Nullable
    public static Integer getCost(int weaponID) {
        if (!initialized) {
            initialize();
        }
        return costs.get(weaponID);
    }
}
