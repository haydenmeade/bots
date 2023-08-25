package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.PlayerListener;
import com.runemate.ui.setting.open.Settings;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "WildPieListener")
public class WildPieListener<TSettings extends Settings, TState extends Enum<TState>> implements EngineListener, PlayerListener {
    private final NeckBot<TSettings, TState> bot;
    private final TState restoreState;
    private final int maintainSlayerLevel;

    public WildPieListener(NeckBot<TSettings, TState> bot
            , TState restoreState
            , int maintainSlayerLevel
    ) {
        this.bot = bot;
        this.restoreState = restoreState;
        this.maintainSlayerLevel = maintainSlayerLevel;
    }

    private boolean needsSlayerBoost() {
        return Skill.SLAYER.getCurrentLevel() < maintainSlayerLevel;
    }

    private void checkAndConsume() {
        if (bot.isPaused()) return;
        if (maintainSlayerLevel != -1) {
            if (needsSlayerBoost()) {
                var wildPieHalf = Inventory.getItems(items.wildPieHalf).first();
                var wildPie = Inventory.getItems(items.wildPie).first();
                if (wildPieHalf != null) {
                    util.consume(wildPieHalf, "Eat");
                    Execution.delayWhile(this::needsSlayerBoost, 1200, 3600);
                } else if (wildPie != null) {
                    util.consume(wildPie, "Eat");
                    Execution.delayWhile(this::needsSlayerBoost, 1200, 3600);
                } else {
                    bot.updateStatus("Out of wild pies");
                    bot.forceState(this.restoreState);
                }
            }
        }
    }

    @Override
    public void onTickStart() {
        checkAndConsume();
    }
}
