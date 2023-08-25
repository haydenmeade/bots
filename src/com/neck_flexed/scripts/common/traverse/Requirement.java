package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.TeleportSpellInfo;
import com.runemate.game.api.hybrid.local.Rune;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.Items;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

public interface Requirement {
    Collection<Pattern> items();

    default Collection<Rune> runes() {
        return Collections.emptyList();
    }

    boolean isHouse();

    TeleportSpellInfo spellInfo();

    default boolean meetsRequirement(HouseConfig houseConfig, Collection<SpriteItem> spriteItems) {
        if (items() == null || items().isEmpty()) return true;
        return spriteItems.stream().anyMatch(Items.getNamePredicate(items()));
    }

    default TraverseMethod.TraverseBank getBank() {
        var items = items();
        Map<Pattern, Integer> itemMap = items == null || items.isEmpty()
                ? Collections.emptyMap()
                : NeckBank.toMap(1, items.toArray(new Pattern[0]));
        var runes = runes();
        return new TraverseMethod.TraverseBank(itemMap, runes == null ? Collections.emptyList() : runes, spellInfo());
    }
}

