package com.neck_flexed.scripts.mole;

import com.runemate.game.api.hybrid.entities.details.Identifiable;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.script.framework.listeners.NpcListener;
import com.runemate.game.api.script.framework.listeners.events.AnimationEvent;
import com.runemate.game.api.script.framework.listeners.events.DeathEvent;
import com.runemate.game.api.script.framework.listeners.events.EntityEvent;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2(topic = "MoleListener")
public class MoleListener implements NpcListener {
    public static final int digAnim = 3314;
    private final Mole bot;
    private boolean isDead = false;
    private boolean isDug = false;
    private Area deadArea;

    public MoleListener(Mole bot) {
        this.bot = bot;
    }

    @Override
    public void onNpcAnimationChanged(AnimationEvent event) {
        log.debug(event);
        if (event.getEntityType() == EntityEvent.EntityType.NPC && event.getAnimationId() == digAnim) {
            log.debug("DIG");
            isDug = true;
        }
    }

    public int getId(Locatable o) {
        var identifiable = (Identifiable) o;
        return (o == null) ? 0 : identifiable.getId();
    }

    @Override
    public void onNpcDeath(@NotNull DeathEvent event) {
        try {
            log.debug(event);
            if (event.getEntityType() == EntityEvent.EntityType.NPC && getId(event.getSource()) == Mole.MoleId) {
                log.debug(String.format("MOLE DEAD on: %s", event.getSource().getPosition()));
                isDead = true;
                deadArea = event.getSource().getArea();
                bot.addKill();
            }
        } catch (Exception e) {
            log.error("NpcDeath", e);
            e.printStackTrace();
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isDug() {
        return isDug;
    }

    public void resetDig() {
        log.debug("reset dig");
        isDug = false;
    }

    public void reset() {
        log.debug("reset");
        isDead = false;
        isDug = false;
        deadArea = null;
    }

    public Area getDeadArea() {
        return deadArea;
    }
}
