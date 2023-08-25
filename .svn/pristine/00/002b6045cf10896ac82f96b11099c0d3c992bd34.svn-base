package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.local.Rune;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

public enum NeckSpell {

    IceBarrage(Magic.Ancient.ICE_BARRAGE,
            () -> Rune.DEATH.getQuantity() >= 4 && Rune.BLOOD.getQuantity() >= 2 && Rune.WATER.getQuantity() >= 6,
            Rune.DEATH, Rune.BLOOD, Rune.WATER
    ),
    IceBurst(Magic.Ancient.ICE_BURST,
            () -> Rune.DEATH.getQuantity() >= 2 && Rune.CHAOS.getQuantity() >= 4 && Rune.WATER.getQuantity() >= 4,
            Rune.DEATH, Rune.CHAOS, Rune.WATER
    ),
    ShadowBarrage(Magic.Ancient.SHADOW_BARRAGE,
            () -> Rune.AIR.getQuantity() >= 4 && Rune.BLOOD.getQuantity() >= 2 && Rune.DEATH.getQuantity() >= 4 && Rune.SOUL.getQuantity() >= 3,
            Rune.AIR, Rune.BLOOD, Rune.DEATH, Rune.SOUL
    ),
    ShadowBurst(Magic.Ancient.SHADOW_BURST,
            () -> Rune.AIR.getQuantity() >= 1 && Rune.CHAOS.getQuantity() >= 4 && Rune.DEATH.getQuantity() >= 2 && Rune.SOUL.getQuantity() >= 2,
            Rune.AIR, Rune.CHAOS, Rune.DEATH, Rune.SOUL
    ),
    BloodBarrage(Magic.Ancient.BLOOD_BARRAGE,
            () -> Rune.BLOOD.getQuantity() >= 4 && Rune.DEATH.getQuantity() >= 4 && Rune.SOUL.getQuantity() >= 1,
            Rune.BLOOD, Rune.DEATH, Rune.SOUL
    ),
    BloodBurst(Magic.Ancient.BLOOD_BURST,
            () -> Rune.BLOOD.getQuantity() >= 2 && Rune.DEATH.getQuantity() >= 2 && Rune.CHAOS.getQuantity() >= 4,
            Rune.BLOOD, Rune.DEATH, Rune.CHAOS
    ),
    ;

    @Getter
    private final Magic.Ancient spell;
    @Getter
    private final Collection<Rune> runes;
    private final BooleanSupplier hasRunes;

    private NeckSpell(Magic.Ancient spell, BooleanSupplier hasRunes, Rune... runes) {
        this.spell = spell;
        this.hasRunes = hasRunes;
        this.runes = List.of(runes);
    }

    public static NeckSpell getBestBlood() {
        var magic = Skill.MAGIC.getCurrentLevel();
        if (magic >= 92)
            return BloodBarrage;
        if (magic >= 68)
            return BloodBurst;
        return null;
    }

    public boolean canCast() {
        return hasRunes.getAsBoolean();
    }

    @Override
    public String toString() {
        return spell.name();
    }
}
