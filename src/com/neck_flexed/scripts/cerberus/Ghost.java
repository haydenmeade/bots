package com.neck_flexed.scripts.cerberus;

import com.google.common.collect.ImmutableMap;
import com.neck_flexed.scripts.common.CombatStyle;
import com.runemate.game.api.hybrid.entities.Npc;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum Ghost {
    RANGE(NpcID.SUMMONED_SOUL, CombatStyle.Ranged, Color.GREEN),
    MAGE(NpcID.SUMMONED_SOUL_5868, CombatStyle.Magic, Color.BLUE),
    MELEE(NpcID.SUMMONED_SOUL_5869, CombatStyle.Melee, Color.RED);

    private static final Map<Integer, Ghost> MAP;

    static {
        final ImmutableMap.Builder<Integer, Ghost> builder = new ImmutableMap.Builder<>();

        for (final Ghost ghost : values()) {
            builder.put(ghost.getNpcId(), ghost);
        }

        MAP = builder.build();
    }

    private final int npcId;
    private final CombatStyle type;
    private final Color color;

    /**
     * Try to identify if NPC is ghost
     *
     * @param npc npc
     * @return optional ghost
     */
    @Nullable
    public static Ghost fromNPC(final Npc npc) {
        return npc == null ? null : MAP.get(npc.getId());
    }
}
