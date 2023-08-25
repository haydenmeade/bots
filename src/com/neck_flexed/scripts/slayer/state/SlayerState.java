package com.neck_flexed.scripts.slayer.state;

public enum SlayerState {
    STARTING(),
    BREAKING(),
    TRAVERSING(),

    FIGHTING(),
    LOOTING(),

    LURING(),
    STACKING(),
    BARRAGING(),

    RESTORING(),
    HOPPING(),
    RESTOCKING(),

    GET_NEW_TASK(),
    SKIP_TASK(),
    GET_TURAEL_TASK(),
    GET_MASTER_TASK(),

    SWITCH_SPELLBOOK(),
    PICK_UP_CANNON;

}
