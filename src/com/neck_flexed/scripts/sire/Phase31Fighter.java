package com.neck_flexed.scripts.sire;

import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.PlayerListener;
import com.runemate.game.api.script.framework.listeners.events.AnimationEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Phase31Fighter")
public class Phase31Fighter implements EngineListener, PlayerListener {
    public static final int Phase3_2_Trigger = 1816;
    private final sireBot bot;
    private final Sire sire;
    private boolean isDone = false;

    public Phase31Fighter(sireBot bot) {
        this.bot = bot;
        this.sire = bot.sireListener.getSire();
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public void onTickStart() {
        if (done()) return;
        if (this.sire == null) return;
        var sireNpc = sire.getNpc();
        if (sireNpc == null) return;
        if (sireNpc.getId() == sireBot.SireId_Phase2or3_Walk) {
            log.debug("wait for walk");
            return;
        }
        var target = Players.getLocal().getTarget();
        if (sireBot.onMiasmaPool()) {
            log.debug("move");
            util.moveTo(Phase3.getMoveToTile2(Players.getLocal().getPosition()));
        } else if (target == null || !target.equals(sireNpc)) {
            log.debug("attack");
            sire.attack();
        }

    }

    @Override
    public void onPlayerAnimationChanged(AnimationEvent event) {
        if (event.getAnimationId() == Phase3_2_Trigger) {
            log.debug("Got phase 3.2 teleport trigger");
            setDone(true);
        }
    }

    public boolean done() {
        if (sire == null) return true;
        var s = sire.getNpc();
        return isDone()
                || s == null
                || s.getId() == sireBot.SireId_Phase3_2
                || s.getAnimationId() == Phase3.explosionAnim;
    }
}
