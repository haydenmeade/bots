package com.neck_flexed.scripts.dk;

public enum FightStrategy {
    Tribrid("Fight all the kings"),
    PrayFlickOnly("Only Prayer Flick the Kings");

    private final String name;

    FightStrategy(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
