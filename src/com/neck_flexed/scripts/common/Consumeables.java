package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.PlayerListener;
import com.runemate.game.api.script.framework.listeners.events.HitsplatEvent;
import com.runemate.ui.setting.open.Settings;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "Consumeables")
public class Consumeables<TSettings extends Settings, TState extends Enum<TState>> implements EngineListener, PlayerListener {
    private final NeckBot<TSettings, TState> bot;
    private final TState restoreState;
    private int minEat;
    private boolean isDharok;

    public Consumeables(NeckBot<TSettings, TState> bot
            , int minEat
            , TState restoreState
    ) {
        this.bot = bot;
        this.minEat = minEat;
        this.restoreState = restoreState;
        this.isDharok = false;
    }

    public Consumeables(NeckBot<TSettings, TState> bot
            , int minEat
            , TState restoreState
            , boolean isDharok) {
        this.bot = bot;
        this.minEat = minEat;
        this.restoreState = restoreState;
        this.isDharok = isDharok;
    }

    public void setDharok(boolean isDharok) {
        this.isDharok = isDharok;
    }

    public void setMinEat(int minEat) {
        this.minEat = minEat;
    }

    private void checkAndConsume() {
        if (bot.isPaused()) return;
        if (Health.getCurrent() < minEat && !this.isDharok) {
            if (!Food.eatAny() || Food.countInventory() == 0) {
                if (Health.getCurrent() < minEat) {
                    log.debug("No food forced to restore hp: {} mineat: {}", Health.getCurrent(), minEat);
                    this.bot.forceState(restoreState);
                }
            }
        }
        if (Prayer.getPoints() < 5) {
            if (!util.restorePrayer()) {
                log.debug("No prayer forced to restore");
                this.bot.forceState(restoreState);
            }
        }
        if (Traversal.getRunEnergy() < 15) {
            Traversal.drinkStaminaEnhancer();
        }
        if (Health.isPoisoned()) {
            if (!util.curePoison())
                log.warn("No anti poison");
        }
    }


    @Override
    public void onTickStart() {
        checkAndConsume();
    }

    @Override
    public void onPlayerHitsplat(HitsplatEvent event) {
        checkAndConsume();
    }
}
