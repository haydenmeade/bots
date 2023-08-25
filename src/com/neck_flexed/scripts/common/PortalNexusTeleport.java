package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.util.Regex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public enum PortalNexusTeleport {
    ArceuusLibrary("Arceuus Library"),
    DraynorManor("Draynor Manor"),
    Battlefront("Battlefront"),
    Varrock("Varrock"),
    GrandExchange("Grand Exchange"),
    MindAltar("Mind Altar"),
    Lumbridge("Lumbridge"),
    Falador("Falador"),
    SalveGraveyard("Salve Graveyard"),
    Camelot("Camelot"),
    SeersVillage("Seers' Village"),
    FenkenstrainsCastle("Fenken' Castle", "Fenkenstrain"),
    Ardougne("Ardougne"),
    Watchtower("Watchtower"),
    Yanille("Yanille"),
    Senntisten("Senntisten"),
    WestArdougne("West Ardougne"),
    Marim("Marim"),
    HarmonyIsland("Harmony Island"),
    Kharyrll("Kharyrll"),
    LunarIsle("Lunar Isle"),
    KourendCastle("Kourend Castle", "Kourend"),
    TheForgottenCemetery("Cemetery"),
    WaterbirthIsland("Waterbirth Island", "Waterbirth"),
    Barrows("Barrows"),
    Carrallangar("Carrallangar"),
    FishingGuild("Fishing Guild"),
    Catherby("Catherby"),
    Annakarl("Annakarl"),
    ApeAtollDungeon("Ape Atoll Dungeon"),
    Ghorrock("Ghorrock"),
    TrollStronghold("Troll Stronghold"),
    Weiss("Weiss");

    private final Pattern option;

    PortalNexusTeleport(String... options) {
        this.option = Regex.getPatternContainingOneOf(options);
    }

    @Override
    public String toString() {
        return this.option.toString();
    }
}
