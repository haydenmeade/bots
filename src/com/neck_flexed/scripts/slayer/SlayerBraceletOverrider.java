package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.loadout.LoadoutOverrider;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SlayerBraceletOverrider implements LoadoutOverrider {
    private final Bracelet bracelet;

    @Override
    public @Nullable Pattern getSlotOverride(Equipment.Slot slot) {
        if (slot.equals(Equipment.Slot.HANDS)) return Pattern.compile(this.bracelet.item, Pattern.CASE_INSENSITIVE);
        return null;
    }

    @RequiredArgsConstructor
    public static enum Bracelet {
        Expeditious(items.expeditiousBracelet),
        Slaughter(items.braceletOfSlaughter);
        private final String item;
    }
}
