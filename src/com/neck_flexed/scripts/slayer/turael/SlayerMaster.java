package com.neck_flexed.scripts.slayer.turael;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.TeleportSpellInfo;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.traverse.*;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.NpcContact;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import lombok.Getter;

import java.util.regex.Pattern;

public enum SlayerMaster implements NpcContact.Contact {
    DURADEL("Duradel", new Coordinate(1)),
    KONAR("Konar.*", new Coordinate(1308, 3786, 0)),
    NIEVE_STEVE("Nieve|Steve", new Coordinate(2434, 3423, 0)),
    KRYSTILIA("Krystilia", new Coordinate(3109, 3514, 0)),
    TURAEL("Turael", new Coordinate(2930, 3536, 0)),
    CHAELDAR("Chaeldar", new Coordinate(2444, 4431, 0)),
    VANNAKA("Vannaka", new Coordinate(3146, 9914, 0)),
    ;

    private final Pattern name;
    @Getter
    private final Coordinate location;

    private SlayerMaster(String name, Coordinate location) {
        this.name = Pattern.compile("^" + name + "$");
        this.location = location;
    }

    public static SlayerMaster fromSlayerMaster(com.neck_flexed.scripts.slayer.SlayerMaster slayerMaster) {
        switch (slayerMaster) {
            case DURADEL:
                return DURADEL;
            case NIEVE_STEVE:
                return NIEVE_STEVE;
            case TURAEL:
                return TURAEL;
            case CHAELDAR:
                return CHAELDAR;
            case VANNAKA:
                return VANNAKA;
        }
        return null;
    }

    public boolean canTraverseOrContact(HouseConfig houseConfig) {
        if (Magic.Book.LUNAR.isCurrent() && util.canNpcContact())
            return true;
        return util.hasAnyTraverse(this.getTraverses(), houseConfig);
    }

    public InterfaceComponent getComponent() {
        return Interfaces.newQuery().actions(this.getAction()).containers(this.getContainerId()).types(InterfaceComponent.Type.CONTAINER).results().first();
    }

    public int getContainerId() {
        return 75;
    }

    public Pattern getAction() {
        return this.name;
    }

    public String toString() {
        return this.name();
    }

    public TraverseMethod[] getTraverses() {
        switch (this) {
            case DURADEL:
                return Traverses.duradaddy();
            case KONAR:
                return new TraverseMethod[]{
                        new ItemTraverse("Rada's blessing 4", "Mount Karuulm"),
                        new FairyRingTraverse(FairyRing.CIR)
                };
            case NIEVE_STEVE:
                return Traverses.NIEVE_CAVE;
            case KRYSTILIA:
                return new TraverseMethod[]{
                        new ItemTraverse("Amulet of glory.*", "Rub", "Edgeville"),
                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Edgeville),
                        new FairyRingTraverse(FairyRing.DKR)
                };
            case TURAEL:
                return new TraverseMethod[]{
                        new GamesNecklaceTraverse(GamesNecklaceTraverse.Destination.Burthorpe),
                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Burthorpe),
                        new HouseLocationTraverse(House.Location.TAVERLEY),
                        new ConstructionCapeTraverse(ConstructionCapeTraverse.Destination.Taverley)
                };
            case CHAELDAR:
                return new TraverseMethod[]{new FairyRingTraverse(FairyRing.ZANARIS)};
            case VANNAKA:
                return new TraverseMethod[]{
                        new TeleportTraverse(TeleportSpellInfo.PADDEWWA),
                        new ItemTraverse(items.amuletOfGlory, "Rub", "Edgeville"),
                        new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Edgeville),
                        new FairyRingTraverse(FairyRing.DKR)
                };

        }
        return new TraverseMethod[0];
    }

    public Npc getNpc() {
        return Npcs.newQuery()
                .names(this.name)
                .actions("Assignment", "Rewards")
                .results()
                .first();
    }

    public int[] getPathRegions() {
        switch (this) {
            case DURADEL:
                return Traverses.DURADADDY_REGIONS;
            case CHAELDAR:
                return new int[]{9797, 9541};
            case KONAR:
                return new int[]{5178, 5179};
            case NIEVE_STEVE:
                return new int[]{9782, 9781, 9525, 9526};
            case KRYSTILIA:
                return new int[]{12342, 12598, 12597, 12853, 12341};
            case TURAEL:
                return new int[]{11319, 11575, 11574,};
            case VANNAKA:
                return new int[]{12442, 12698, 12342};
        }
        return new int[0];
    }
}
