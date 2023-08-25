package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import lombok.Getter;

public class DuelingRingTraverse extends ItemTraverse implements TraverseMethod {

    public DuelingRingTraverse(Destination dest) {
        super(items.ringOfDueling, util.join(dest.getOptions(), "Rub").toArray(new String[0]));
    }

    public enum Destination {
        PvPArena("Al Kharid PvP Arena.", "PvP Arena"),
        CastleWars("Castle Wars Arena.", "Castle Wars"),
        Ferox("Ferox Enclave.", "Ferox Enclave"),
        ;

        @Getter
        private final String[] options;

        private Destination(String... options) {
            this.options = options;
        }
    }
}
