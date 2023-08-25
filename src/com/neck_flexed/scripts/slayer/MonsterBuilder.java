package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.slayer.encounters.EncounterOverride;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class MonsterBuilder {
    private final Task task;
    private String monsterName;
    private String[] alternatives;
    private String[] superiorNames = new String[0];
    private @Nullable SlayerFinishItem finishItem = null;
    private @Nullable SlayerProtectionItem protection = null;
    private boolean isLeafBladed = false;
    private boolean isDragon = false;
    private boolean isPoisonous = false;
    private EncounterOverride superiorEncounter = null;
    private boolean disableHopping = false;
    private int food = -1;
    private boolean isWyvern = false;
    private Collection<Location> locations = new ArrayList<>();
    private BoneType boneType = BoneType.None;
    private boolean isTurael = false;
    private boolean needsLightSource = false;

    public MonsterBuilder(Task task) {
        this.task = task;
    }

    public MonsterBuilder setPoisonous() {
        this.isPoisonous = true;
        return this;
    }

    public MonsterBuilder disableHopping() {
        this.disableHopping = true;
        return this;
    }

    public MonsterBuilder setWyvern() {
        this.isWyvern = true;
        return setBoneType(BoneType.Bones);
    }

    public MonsterBuilder setDragon() {
        this.isDragon = true;
        return setBoneType(BoneType.Bones);
    }

    public MonsterBuilder setFood(int food) {
        this.food = food;
        return this;
    }

    public MonsterBuilder location(Location location) {
        this.locations.add(location);
        return this;
    }


    public MonsterBuilder setMonsterName(String monsterName) {
        this.monsterName = monsterName;
        return this;
    }

    public MonsterBuilder turael() {
        this.isTurael = true;
        return this;
    }

    public MonsterBuilder setAlternatives(String... alternatives) {
        this.alternatives = alternatives;
        return this;
    }

    public MonsterBuilder setSuperiorNames(String... superiorNames) {
        this.superiorNames = superiorNames;
        return this;
    }

    public MonsterBuilder setFinishItem(@Nullable SlayerFinishItem finishItem) {
        this.finishItem = finishItem;
        return this;
    }

    public MonsterBuilder needsLightSource() {
        this.needsLightSource = true;
        return this;
    }

    public MonsterBuilder setProtection(@Nullable SlayerProtectionItem protection) {
        this.protection = protection;
        return this;
    }

    public MonsterBuilder setIsLeafBladed(boolean isLeafBladed) {
        this.isLeafBladed = isLeafBladed;
        return this;
    }

    public MonsterBuilder setBoneType(BoneType boneType) {
        this.boneType = boneType;
        return this;
    }

    public MonsterBuilder setSuperiorEncounter(EncounterOverride encounter) {
        this.superiorEncounter = encounter;
        return this;
    }

    public Monster build() {
        return new Monster(
                task,
                monsterName,
                superiorNames,
                alternatives,
                locations,
                finishItem,
                protection,
                isLeafBladed,
                needsLightSource,
                superiorEncounter,
                isDragon,
                isPoisonous,
                food,
                disableHopping,
                isWyvern,
                boneType,
                isTurael
        );
    }
}