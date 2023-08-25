package com.neck_flexed.scripts.slayer.state;

import com.neck_flexed.scripts.common.Cannon;
import com.neck_flexed.scripts.common.Food;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2
public class PickUpCannonState extends BaseState<SlayerState> {
    private final Cannon cannon;
    private int attempts = 0;

    public PickUpCannonState(SlayerBotImpl<?> bot) {
        super(bot, SlayerState.PICK_UP_CANNON);
        this.cannon = bot.getCannon();
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public void activate() {
        super.activate();
        if (this.cannon.isCannonPlaced()) {
            bot.updateStatus("Picking up cannon");
        }
    }

    @Override
    public boolean done() {
        if (this.attempts > 5) {
            log.error("Unable to pick up cannon");
            return true;
        }
        var go = cannon.getCannon();
        return !cannon.isCannonPlaced() || (go == null || !go.isVisible());
    }

    @Override
    public void executeLoop() {
        if (cannon.isCannonPlaced()) {
            this.attempts++;
            if (Inventory.getEmptySlots() < 4) {
                Food.dropWorst();
            }
            this.cannon.pickup();
            Execution.delay(600, 700);
        }
    }
}
