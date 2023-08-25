package com.neck_flexed.scripts.cerberus;

import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "CerbState")
public enum CerbState {
    STARTING(),
    BREAKING(),
    TRAVERSING(),
    ENTERING_LAIR(),
    FIGHTING(),
    LOOTING(),
    POST_LOOT_WAITING(),
    RESTORING(),
    HOPPING(),
    RESTOCKING(),
    WALK_UNDER;

}
