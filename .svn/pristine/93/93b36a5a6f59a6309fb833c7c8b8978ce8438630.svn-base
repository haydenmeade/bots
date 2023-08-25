package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import lombok.Getter;

public class GamesNecklaceTraverse extends ItemTraverse implements TraverseMethod {

    public GamesNecklaceTraverse(Destination dest) {
        super(items.gamesNecklace, util.join(dest.getOptions(), "Rub").toArray(new String[0]));
    }

    public enum Destination {
        Burthorpe("Burthorpe.", "Burthorpe"),
        BarbarianOutpost("Barbarian Outpost.", "Barbarian Outpost"),
        CorpBeast("Corporeal Beast.", "Corporeal Beast"),
        TearsOfGuthix("Tears of Guthix.", "Tears of Guthix"),
        Wintertodt("Wintertodt Camp.", "Wintertodt Camp"),
        ;

        @Getter
        private final String[] options;

        private Destination(String... options) {
            this.options = options;
        }
    }
}
