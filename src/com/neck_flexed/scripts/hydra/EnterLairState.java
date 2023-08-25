package com.neck_flexed.scripts.hydra;

import com.neck_flexed.scripts.common.PrayerFlicker;
import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2(topic = "EnterLairState")
public class EnterLairState extends BaseState<HydraState> {

    private final Loadouts loadouts;
    private final com.neck_flexed.scripts.hydra.bot bot;
    private HydraListener hydraListener;
    private PrayerFlicker prayerFlicker;

    public EnterLairState(HydraListener hydraListener, com.neck_flexed.scripts.hydra.bot bot, PrayerFlicker prayerFlicker) {
        super(bot, HydraState.ENTERING_LAIR);
        this.hydraListener = hydraListener;
        this.bot = bot;
        this.prayerFlicker = prayerFlicker;
        this.loadouts = bot.loadouts;
    }


    @Override
    public void activate() {
        bot.updateStatus("Entering boss room");
        this.hydraListener.reset();
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return com.neck_flexed.scripts.hydra.bot.inBossRoom();
    }

    @Override
    public void executeLoop() {
        if (this.loadouts.getEquipped() == null) {
            this.loadouts.setCurrentFromEquipmentOrEquip(this.loadouts.getForRole(Loadout.LoadoutRole.Combat));
            this.loadouts.equip(this.loadouts.getEquipped(), true);
        }
        var d = com.neck_flexed.scripts.hydra.bot.getDoor();
        if (d != null) {
            hydraListener.init(null);
            Execution.delayUntil(() -> hydraListener.getHydra() != null, 1200, 1800);
            di.send(MenuAction.forGameObject(d, "Quick-open"));
            Execution.delayUntil(com.neck_flexed.scripts.hydra.bot::inBossRoom, util::playerMoving, 1200, 1800);
        }
    }
}
