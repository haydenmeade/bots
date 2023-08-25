package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.Cannon;
import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.SlayerTask;
import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.slayer.encounters.ShamanEncounter;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import com.runemate.ui.setting.open.Settings;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public abstract class SlayerBotImpl<TSettings extends Settings> extends NeckBot<TSettings, SlayerState> implements SlayerBot {
    protected final Text progressLog = new Text("No progress yet");
    @Getter
    private final Cannon cannon = new Cannon(di);
    @Getter
    private final Map<Coordinate, Long> deathTiles = new ConcurrentHashMap<>();
    @Getter
    private final Collection<Npc> superiorBlacklist = new ArrayList<>();
    private final Collection<Task> skippedTasks = new ArrayList<>();
    @Getter
    @Setter
    protected SlayerMonster previousTask;
    protected Collection<Task> tasksCompleted = new ArrayList<>();
    @Getter
    protected ShamanEncounter shamanEncounter = new ShamanEncounter();

    public abstract @NotNull Location selectLocation(SlayerMonster monster);

    @Override
    public void onMessageReceived(MessageEvent event) {
        super.onMessageReceived(event);
        if (isPaused() || !started) return;
        if (!Objects.equals(event.getType(), Chatbox.Message.Type.SERVER)) return;
        var msg = event.getMessage();
        if (msg == null) return;
        if (msg.equals("That is not your superior creature.")) {
            log.debug("Blacklist superior {}", event);
            this.blacklistNearestSuperior();
        }
    }

    private void blacklistNearestSuperior() {
        var m = Monster.fromSlayerTask(this.getOverrideTask());
        if (m.isPresent()) {
            var s = m.get().getSuperior(this);
            if (s == null) return;
            this.getSuperiorBlacklist().add(s.nearest());
        }
    }

    public abstract Task getOverrideTask();

    public abstract boolean barrageTasksAvailable();

    public void refreshProgress() {
        var currentTask = SlayerTask.getCurrentT();
        var sb = new StringBuilder();
        sb.append("# Current Task: ");
        sb.append(currentTask == null ? "None" : currentTask.getName());
        sb.append("\n");

        if (skippedTasks.size() > 0) {
            sb.append("# Skipped Tasks: ");
            sb.append("\n");
            skippedTasks.forEach(t -> {
                sb.append(t.getName());
                sb.append("\n");
            });
            sb.append("\n");
        }
        if (tasksCompleted.size() > 0) {
            sb.append("# Completed Tasks: ");
            sb.append("\n");
            tasksCompleted.forEach(t -> {
                sb.append(t.getName());
                sb.append("\n");
            });
        }
        this.progressLog.setText(sb.toString());
    }

    public void skippedTask(Task task) {
        this.skippedTasks.add(task);
        refreshProgress();
    }

    @Override
    public void taskCompleted() {
        if (previousTask == null) {
            log.error("Unable to find previous task in task completed");
            this.tasksCompleted.add(Task.KILLERWATTS);
        } else {
            this.tasksCompleted.add(previousTask.getTask());
        }
        this.getSuperiorBlacklist().clear();
        refreshProgress();
    }

    public abstract void setLoadoutOverriders();
}
