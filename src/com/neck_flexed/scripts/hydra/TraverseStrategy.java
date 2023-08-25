package com.neck_flexed.scripts.hydra;

public enum TraverseStrategy {
    RadasBlessing("Rada's blessing"),
    HouseFairyRing("House Fairy Ring");
    //ArdyCloakFairyRing("Ardy cloak fairy ring");

    private final String name;

    TraverseStrategy(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
