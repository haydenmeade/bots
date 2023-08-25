package com.neck_flexed.scripts.common.state;

import com.neck_flexed.scripts.common.DI;
import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.PrayerFlicker;
import com.runemate.ui.setting.open.Settings;
import lombok.extern.log4j.Log4j2;

import java.util.EventListener;

@Log4j2
public abstract class BaseState<T extends Enum<T>> implements LoopState<T> {
    protected final T stateEnum;
    protected final NeckBot<?, T> bot;
    protected final PrayerFlicker prayerFlicker;
    protected final DI di;
    protected final Settings settings;

    public BaseState(NeckBot<?, T> bot, T state) {
        this.bot = bot;
        this.stateEnum = state;
        this.prayerFlicker = bot.prayerFlicker;
        this.di = bot.di;
        this.settings = bot.settings();
    }

    @Override
    public final T get() {
        return stateEnum;
    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[0];
    }

    @Override
    public void deactivate() {
        log.debug("Deactivate {}", this.get());
    }

    @Override
    public void activate() {
        log.debug("Activate {}", this.get());
    }
}
