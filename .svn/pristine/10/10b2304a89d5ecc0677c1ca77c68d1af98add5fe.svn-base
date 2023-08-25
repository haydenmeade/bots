package com.neck_flexed.scripts.sire;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.details.Identifiable;
import com.runemate.game.api.hybrid.entities.details.Onymous;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.NpcListener;
import com.runemate.game.api.script.framework.listeners.events.AnimationEvent;
import com.runemate.game.api.script.framework.listeners.events.DeathEvent;
import com.runemate.game.api.script.framework.listeners.events.NpcSpawnedEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.neck_flexed.scripts.sire.sireBot.*;

@Log4j2(topic = "SireListener")
@RequiredArgsConstructor
public class SireListener implements NpcListener, EngineListener {
    public static final String Name = "Abyssal Sire";
    private final sireBot sireBot;
    private Phase phase = Phase.Phase0;
    @Getter
    private Sire sire;
    private int tick = 0;
    private boolean isDead;
    private Area.Rectangular deadArea;

    private static Npc findSireNpc() {
        return Npcs.getLoaded(Name).nearest();
    }

    @Override
    public void onNpcAnimationChanged(AnimationEvent event) {
        if (Objects.equals(getName(event.getSource()), Name)
                && event.getAnimationId() == 7100 /*death anim*/) {
            isDead = true;
            deadArea = sire.getNpc().getArea();
            this.sire = null;
            sireBot.addKill();
            this.phase = Phase.Dead;
        }
    }

    public void notifyPhasing(Phase p) {
        this.phase = p;
    }

    public Phase getPhase() {
        if (this.sire == null)
            return Phase.Phase0;

        var fromNpc = getFightStateFromNpc();

        return Objects.equals(phase, fromNpc) ?
                phase :
                Objects.equals(phase, Phase.Phase3_1) &&
                        Objects.equals(fromNpc, Phase.Phase2) ?
                        Phase.Phase3_1 :
                        Objects.equals(phase, Phase.Phase3_2) &&
                                Objects.equals(fromNpc, Phase.Phase3_1) ?
                                Phase.Phase3_2 :
                                fromNpc == null ? phase : fromNpc;
    }

    private @Nullable Phase getFightStateFromNpc() {
        var s = sire.getNpc();
        if (s != null) {
            switch (s.getId()) {
                case SireId_Phase0:
                    return Phase.Phase0;
                case SireId_Phase1_Awake:
                case SireId_Phase1_Stunned:
                    return Phase.Phase1;
                case SireId_Phase2or3_Walk:
                    if (phase == Phase.Phase3_1) return Phase.Phase3_1;
                    if (s.getPosition().getY() < Phase2.yy) {
                        return Phase.Phase3_1;
                    }
                    return Phase.Phase2;
                case SireId_Phase2:
                    return Phase.Phase2;
                case SireId_Phase3_1:
                    return Phase.Phase3_1;
                case SireId_Phase3_2:
                    return Phase.Phase3_2;

                default:
                    break;
            }
        }
        return null;
    }

    public boolean isSireVisible() {
        return findSireNpc() != null;
    }

    public boolean init() {
        var h = findSireNpc();
        if (h != null) {
            log.debug("did init");
            onNpcSpawned(new NpcSpawnedEvent(h));
            return true;
        }
        return false;
    }

    @Override
    public void onTickStart() {
        try {
            this.tick++;
            if (this.sire != null && this.sire.getNpc() != null && !this.sire.getNpc().isValid()) {
                init();
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onNpcSpawned(NpcSpawnedEvent event) {
        try {
            var npc = event.getNpc();
            if (npc == null) return;
            if (!Objects.equals(npc.getName(), Name)) {
                return;
            }
            sire = new Sire(npc, sireBot);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onNpcDeath(DeathEvent event) {
        try {
            log.debug(event);
            if (getId(event.getSource()) == RespId) {
                log.debug(String.format("Respiratory system DEAD on: %s", event.getSource().getPosition()));
                this.sire.setRespiratoryKilled(this.sire.getRespiratoryKilled() + 1);
            } else if (Objects.equals(getName(event.getSource()), Name)) {
                isDead = true;
                deadArea = sire.getNpc().getArea();
                this.sire = null;
                sireBot.addKill();
            }
        } catch (Exception e) {
            log.error("NpcDeath", e);
            e.printStackTrace();
        }
    }

    public String getName(Object o) {
        if (o instanceof Onymous) {
            var identifiable = (Onymous) o;
            return identifiable.getName();
        }
        return null;
    }

    public int getId(Object o) {
        if (o instanceof Identifiable) {
            var identifiable = (Identifiable) o;
            return identifiable.getId();
        }
        return -1;
    }

    public boolean isDead() {
        return isDead;
    }

    public void reset() {
        log.debug("reset");
        isDead = false;
        deadArea = null;
        this.sire = null;
        this.phase = Phase.Phase0;
    }

    public Area getDeadArea() {
        return deadArea;
    }

}
