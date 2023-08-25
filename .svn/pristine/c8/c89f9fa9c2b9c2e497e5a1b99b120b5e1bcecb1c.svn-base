package com.neck_flexed.scripts.sire;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.region.Npcs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;

@Getter
@Log4j2(topic = "Sire")
@RequiredArgsConstructor
public class Sire {
    public static final int MAX_HIT = 66;
    public static final int MAX_HP = 400;
    @Nullable
    private final Npc npc;
    private final sireBot bot;
    private Timer currentStunTimer;
    private boolean isStunned;
    private int respiratoryKilled;

    public boolean areTentaclesAwake() {
        return !Npcs.newQuery().ids(5912).results().isEmpty();
    }

    public boolean isStunned() {
        if (areTentaclesAwake()) return false;
        var s = this.npc;
        if (s != null && s.getId() == sireBot.SireId_Phase1_Awake) {
            if (currentStunTimer != null)
                currentStunTimer.cancel();
            isStunned = false;
        }
        return isStunned;
    }

    public void resetStun() {
        isStunned = false;
        if (currentStunTimer != null)
            currentStunTimer.cancel();
    }

    public int getRespiratoryKilled() {
        return respiratoryKilled;
    }

    public void setRespiratoryKilled(int respiratoryKilled) {
        this.respiratoryKilled = respiratoryKilled;
    }


    public void startStunTimer() {
        if (currentStunTimer != null)
            currentStunTimer.cancel();
        isStunned = true;
        currentStunTimer = new Timer();
        currentStunTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isStunned = false;
            }
        }, 26000);
    }

    public void attack() {
        if (npc == null) return;
        bot.di.send(MenuAction.forNpc(npc, "Attack"));
    }

    private int getHp() {
        if (npc == null) return MAX_HP;
        var h = npc.getHealthGauge();
        if (h == null) return MAX_HP;
        return h.getPercent() / 100 * MAX_HP;
    }

}
