package com.neck_flexed.scripts.mole;

import com.neck_flexed.scripts.common.state.BaseState;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.osrs.local.AchievementDiary;

public class StartingState extends BaseState<MoleState> {
    private final Mole bot;
    private boolean started = false;

    public StartingState(Mole m) {
        super(m, MoleState.STARTING);
        this.bot = m;
    }

    @Override
    public void activate() {
        bot.updateStatus("Starting");
    }

    @Override
    public String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return started;
    }

    @Override
    public void executeLoop() {
        Camera.setZoom(0.0, 0.1);
        started = true;
        if (!AchievementDiary.FALADOR.isHardComplete()) {
            ClientUI.showAlert(ClientUI.AlertLevel.WARN, "Requires Falador Hard Diary to work correctly.");
        }
    }
}
