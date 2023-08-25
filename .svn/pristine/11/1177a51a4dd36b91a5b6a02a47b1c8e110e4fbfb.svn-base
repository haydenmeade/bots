package com.neck_flexed.scripts.common;

import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.script.framework.listeners.SkillListener;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2(topic = "Inactivity")
public class Inactivity implements SkillListener {
    private static final String defaultReason = "Haven't gained experience in 15 minutes, stopping bot";
    private final NeckBot<?, ?> bot;
    private final Lock inactivityLock = new ReentrantLock();
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> inactivityFuture;
    private boolean started;
    private boolean enabled = true;
    private int timeoutInMinutes = 15;
    private String reason = defaultReason;

    public Inactivity(NeckBot<?, ?> bot) {
        this.bot = bot;
        this.executor = new ScheduledThreadPoolExecutor(1);
    }

    public void disable() {
        this.enabled = false;
    }

    private void inactivityTimeout() {
        if (!enabled) return;
        if (!started) return;
        ClientUI.showAlert(ClientUI.AlertLevel.ERROR, this.reason);
        bot.updateStatus(this.reason);
        this.bot.stop(this.reason);
    }

    public void stop() {
        if (!enabled) return;
        this.started = false;
        inactivityTimerStop();
        if (bot.getEventDispatcher().getListeners().contains(this)) {
            bot.getEventDispatcher().removeListener(this);
        }
    }

    public void start() {
        startTimeout(15, defaultReason);
    }

    public void startTimeout(int minutes, String reason) {
        this.timeoutInMinutes = minutes;
        this.reason = reason;
        if (!enabled) return;
        this.started = true;
        if (!bot.getEventDispatcher().getListeners().contains(this)) {
            bot.getEventDispatcher().addListener(this);
        }
        inactivityTimerStart();
    }

    @Override
    public void onExperienceGained(SkillEvent event) {
        if (!enabled) return;
        if (!this.started) return;
        inactivityTimerStart();
    }

    private void inactivityTimerStop() {
        if (!enabled) return;
        try {
            this.inactivityLock.lock();
            if (this.inactivityFuture != null) {
                inactivityFuture.cancel(true);
            }
        } catch (Exception e) {
            log.error("inactivity timer stop", e);
            e.printStackTrace();
        } finally {
            this.inactivityLock.unlock();
        }
    }

    private void inactivityTimerStart() {
        if (!enabled) return;
        try {
            this.inactivityLock.lock();
            if (this.inactivityFuture != null) {
                inactivityFuture.cancel(true);
            }
            this.inactivityFuture = executor.schedule(this::inactivityTimeout, this.timeoutInMinutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("inactvity timer start", e);
            e.printStackTrace();
        } finally {
            this.inactivityLock.unlock();
        }
    }
}
