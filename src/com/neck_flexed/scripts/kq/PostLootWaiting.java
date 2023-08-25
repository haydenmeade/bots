package com.neck_flexed.scripts.kq;

import com.neck_flexed.scripts.common.Food;
import com.neck_flexed.scripts.common.PrayerFlicker;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import org.jetbrains.annotations.Nullable;

public class PostLootWaiting extends BaseState<KqState> {
    private final kq bot;
    private final PrayerFlicker prayerFlicker;

    public PostLootWaiting(kq bot, PrayerFlicker prayerFlicker) {
        super(bot, KqState.POST_LOOT_WAITING);
        this.bot = bot;
        this.prayerFlicker = prayerFlicker;
    }

    @Override
    public void activate() {
        bot.updateStatus("Waiting for respawn");
        this.prayerFlicker.setActivePrayers(Prayer.PROTECT_FROM_MELEE);
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return kq.getKq() != null;
    }

    @Override
    public void executeLoop() {
        util.eatIfHpAllows(Food.getAny());
        bot.loadouts.equip(bot.loadouts.getForName(KqPhase.Phase1));
    }
}
