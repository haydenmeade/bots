package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.TeleportSpellInfo;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class BaseRequirement implements Requirement {

    private final Collection<Pattern> items;

    public BaseRequirement(Pattern... items) {
        this.items = List.of(items);
    }

    public BaseRequirement(Collection<Pattern> items) {
        this.items = items;
    }

    @Override
    public Collection<Pattern> items() {
        return this.items;
    }

    @Override
    public boolean isHouse() {
        return false;
    }

    @Override
    public TeleportSpellInfo spellInfo() {
        return null;
    }


}

