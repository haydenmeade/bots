package com.neck_flexed.scripts.dk;

import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "DkState")
public enum DkState {
    STARTING(),
    OFF_TICK(),
    BREAKING(),
    TRAVERSING(),
    ENTERING_LAIR(),
    FIGHTING(),
    LOOTING(),
    RESTORING(),
    HOPPING(),
    RESTOCKING();

}
