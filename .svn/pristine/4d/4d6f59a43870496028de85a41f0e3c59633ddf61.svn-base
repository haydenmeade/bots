package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.TeleportSpellInfo;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.local.Varp;
import com.runemate.game.api.hybrid.local.VarpID;
import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.osrs.local.hud.interfaces.MinigameTeleport;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MinigameTeleportTraverse implements TraverseMethod {
    private static final Varp VARP = Varps.getAt(VarpID.LAST_MINIGAME_TELEPORT.getId());
    private static final Duration DURATION = Duration.of(20, ChronoUnit.MINUTES);
    private final MinigameTeleport teleport;

    public static boolean canTeleport() {
        long lastTeleportSeconds = (long) VARP.getValue() * 60;
        Instant teleportExpireInstant = Instant.ofEpochSecond(lastTeleportSeconds).plus(DURATION);
        Duration remainingTime = Duration.between(Instant.now(), teleportExpireInstant);
        return remainingTime.getSeconds() <= 0;
    }

    @Override
    public String toString() {
        return "Minigame teleport to: " + teleport;
    }

    @Override
    public Collection<Requirement> requirements(HouseConfig houseConfig) {
        return List.of(new MinigameRequirement());
    }

    @Override
    public boolean doTraverseLoop(HouseConfig houseConfig, Coordinate startPosition) {
        if (!canTeleport()) return false;
        if (House.isInside()) {
            new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.CastleWars).doTraverseLoop(houseConfig, startPosition);
            return false;
        }
        return teleport.activate();
    }

    private static class MinigameRequirement implements Requirement {
        public MinigameRequirement() {
        }

        @Override
        public Collection<Pattern> items() {
            return Collections.emptyList();
        }

        @Override
        public boolean isHouse() {
            return false;
        }

        @Override
        public boolean meetsRequirement(HouseConfig houseConfig, Collection<SpriteItem> spriteItems) {
            return canTeleport();
        }

        @Override
        public TeleportSpellInfo spellInfo() {
            return null;
        }
    }
}
