package com.neck_flexed.scripts.kq;

import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "KqState")
public enum KqState {
    STARTING(),
    TRAVERSING(),
    ENTERING_LAIR(),
    SEARCHING_LAIR(),
    FIGHTING(),
    LOOTING(),
    POST_LOOT_WAITING(),
    RESTORING(),
    HOPPING(),
    RESTOCKING(),
    WALK_UNDER,
    BREAKING();

}
