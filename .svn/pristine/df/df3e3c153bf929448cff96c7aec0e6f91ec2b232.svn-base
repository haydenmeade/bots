package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.TeleportSpellInfo;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

@Log4j2
public class RequirementWith implements Requirement {

    private final Collection<Pattern> items;
    private final Callable<Boolean> callable;

    public RequirementWith(Callable<Boolean> requirement, Pattern... items) {
        this.items = List.of(items);
        this.callable = requirement;
    }

    public RequirementWith(Collection<Pattern> items, Callable<Boolean> requirement) {
        this.items = items;
        this.callable = requirement;
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

    @Override
    public boolean meetsRequirement(HouseConfig houseConfig, Collection<SpriteItem> spriteItems) {
        Boolean b = null;
        try {
            b = this.callable.call();
        } catch (Exception e) {
            log.error(e);
        }
        return b != null && b && Requirement.super.meetsRequirement(houseConfig, spriteItems);
    }
}
