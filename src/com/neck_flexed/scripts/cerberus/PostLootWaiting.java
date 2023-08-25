package com.neck_flexed.scripts.cerberus;

import com.neck_flexed.scripts.common.Food;
import com.neck_flexed.scripts.common.PrayerFlicker;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import org.jetbrains.annotations.Nullable;

public class PostLootWaiting extends BaseState<CerbState> {
    private final cerb bot;
    private final PrayerFlicker prayerFlicker;
    private final CerbListener cerbListener;


    public PostLootWaiting(cerb bot, PrayerFlicker prayerFlicker, CerbListener cerbListener) {
        super(bot, CerbState.POST_LOOT_WAITING);
        this.bot = bot;
        this.prayerFlicker = prayerFlicker;
        this.cerbListener = cerbListener;
    }

    @Override
    public void activate() {
        prayerFlicker.disable();
        bot.updateStatus("Waiting for respawn");
    }

    @Override
    public void deactivate() {
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return cerb.getCerb() != null;
    }

    @Override
    public void executeLoop() {
        util.eatIfHpAllows(Food.getAny());
        if (!cerb.centerSpawnTile.equals(Players.getLocal().getServerPosition()) && !Players.getLocal().isMoving()) {
            util.moveTo(cerb.centerSpawnTile);
            Execution.delay(100, 200);
        }
    }
}
