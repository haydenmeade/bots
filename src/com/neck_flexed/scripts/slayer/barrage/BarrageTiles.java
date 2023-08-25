package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.runemate.game.api.hybrid.location.Coordinate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum BarrageTiles {
    DustDevil(areas.DUST_DEVIL_1, areas.DUST_DEVIL_2, Task.DUST_DEVILS),
    AbyssalDemon(areas.ABYSSAL_DEMON_1, areas.ABYSSAL_DEMON_2, Task.ABYSSAL_DEMONS),
    Nechryael(areas.NECH_1, areas.NECH_2, Task.NECHRYAEL),
    Jelly(areas.JELLY_1, areas.JELLY_2, Task.JELLIES),
    Ankou(areas.ANKOU_1, areas.ANKOU_2, Task.ANKOU),
    Smoke(new Coordinate(1), new Coordinate(1), Task.SMOKE_DEVILS),
    ;

    private static final SlayerMonster DUST_DEVIL = Monster.getByEnum(Task.DUST_DEVILS).get();
    private final Coordinate tile1;
    private final Coordinate tile2;
    private final Task task;

    public static BarrageTiles getByTask(Task task) {
        return Arrays.stream(BarrageTiles.values()).filter(f -> f.task.equals(task)).findFirst().orElse(null);
    }

    public SlayerMonster getMonster() {
        var m = Monster.getByEnum(task);
        return m.orElse(DUST_DEVIL);
    }

    @Override
    public String toString() {
        return task.getName();
    }

}
