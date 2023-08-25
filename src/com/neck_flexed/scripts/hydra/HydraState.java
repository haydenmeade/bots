package com.neck_flexed.scripts.hydra;

import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "HydraState")
public enum HydraState {
    STARTING(),
    BREAKING(),
    TRAVERSING(),
    ENTERING_LAIR(),
    FIGHTING(),
    LOOTING(),
    POST_LOOT(),
    RESTORING(),
    RESTOCKING();

}
