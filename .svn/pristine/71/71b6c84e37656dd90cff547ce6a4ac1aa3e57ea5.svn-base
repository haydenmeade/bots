package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.TeleportSpellInfo;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.Rune;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.Execution;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

@Log4j2
public class TeleportTraverse implements TraverseMethod {
    @Getter
    private final TeleportSpellInfo info;

    public TeleportTraverse(TeleportSpellInfo info) {
        this.info = info;
    }

    private static String getSpellbookSwapName(Magic.Book book) {
        switch (book) {
            case STANDARD:
                return "Standard";
            case ANCIENT:
                return "Ancient";
            case ARCEUUS:
                return "Arceuus";
        }
        return null;
    }

    @Override
    public String toString() {
        return "Teleport Traverse: " + info.toString();
    }

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        var l = new ArrayList<Requirement>();
        l.add(getSpellRequirement());
        return l;
    }

    public SpellRequirement getSpellRequirement() {
        return new SpellRequirement(info);
    }

    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        var currReg = Players.getLocal().getServerPosition().getContainingRegionId();
        if (currReg != startPosition.getContainingRegionId()) return true;
        var tabName = info.getTeleportTab();
        if (Inventory.contains(tabName)) {
            var tab = Inventory.getItems(tabName).first();
            if (tab == null || !tab.interact("Break")) {
                log.error("Teleport tab error {}", info);
                return false;
            }
            return Execution.delayUntil(() -> Players.getLocal().getServerPosition().getContainingRegionId() != startPosition.getContainingRegionId(),
                    util::playerAnimating, 1200, 2400);
        }
        var spell = info.getSpell();
        var spellBookActive = spell.getSpellBook().isCurrent();
        var canSwap = TeleportSpellInfo.canSwap();
        if (!spellBookActive && canSwap) {
            if (!Magic.Lunar.SPELLBOOK_SWAP.activate(getSpellbookSwapName(info.getBook()))) {
                log.error("Unable to spellbook swap to {} for {}", spell.getSpellBook(), spell);
                return false;
            }
            Execution.delayUntil(() -> spell.getSpellBook().isCurrent(),
                    util::playerAnimating, 1200, 2400);
            return false;
        }
        var reg = Players.getLocal().getServerPosition().getContainingRegionId();
        if (!spell.activate()) {
            log.error("Unable to activate spell {}", spell);
            return false;
        }
        return Execution.delayUntil(() -> Players.getLocal().getServerPosition().getContainingRegionId() != reg,
                util::playerAnimating, 3200, 4400);
    }

    @RequiredArgsConstructor
    public static class SpellRequirement implements Requirement {
        @Getter
        private final TeleportSpellInfo info;

        @Override
        public Collection<Pattern> items() {
            return Collections.emptyList();
        }

        @Override
        public Collection<Rune> runes() {
            return Collections.emptyList();
        }

        @Override
        public boolean isHouse() {
            return false;
        }

        @Override
        public TeleportSpellInfo spellInfo() {
            return info;
        }

        @Override
        public TraverseBank getBank() {
            return new TraverseBank(
                    Collections.emptyMap(),
                    Collections.emptyList(), info
            );
        }

        @Override
        public boolean meetsRequirement(HouseConfig houseConfig, Collection<SpriteItem> spriteItems) {
            return info.hasTeleportTab(spriteItems) || info.canCast(spriteItems);
        }
    }
}
