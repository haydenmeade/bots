package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.loadout.LoadoutOverrider;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.region.Players;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MonsterLoadoutOverrider implements LoadoutOverrider {
    private final SlayerMonster monster;

    @Override
    public @Nullable Pattern getSlotOverride(Equipment.Slot slot) {
        if (monster == null) return null;
        var protection = monster.getProtection();
        if (protection == null) return null;
        if (!protection.getSlot().equals(slot)) return null;
        if (protection.getEquipWhenCondition() != null && !protection.getEquipWhenCondition().test(Players.getLocal()))
            return null;
        return protection.getItems();
    }
}
