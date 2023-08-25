package com.neck_flexed.scripts.kq;

import com.neck_flexed.scripts.common.Food;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

@Log4j2(topic = "WalkUnderState")
public class WalkUnderState extends BaseState<KqState> implements EngineListener {

    private final kq bot;
    private boolean done;
    private boolean donePrevCycle = false;
    private int eatCycle = -100;
    private int tick = 0;

    public WalkUnderState(kq bot) {
        super(bot, KqState.WALK_UNDER);
        this.bot = bot;
    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{this};
    }

    @Override
    public void activate() {
        bot.updateStatus("Walk under");
        if (!Traversal.isRunEnabled())
            Traversal.toggleRun();
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return kq.getKq() == null || done;
    }

    @Override
    public void onTickStart() {
        try {
            tick++;
            Food food = Food.getAny();
            if (food == null && eatCycle < 0) done = true;
            if (tick - eatCycle >= 3) {
                if (donePrevCycle) {
                    log.debug("done walk and eat under");
                    done = true;
                } else {
                    eatCycle = tick;
                    if (food != null && Inventory.contains(food.gameName())) {
                        donePrevCycle = Health.getMaximum() - Health.getCurrent() < food.getHeals();
                        if (!donePrevCycle) {
                            util.eatIfHpAllows(food, food.getHeals());
                            log.debug(String.format("tried to eat and: %s", donePrevCycle));
                        }
                    } else {
                        log.debug("no more food");
                        donePrevCycle = true;
                    }
                }
            }

            var queen = kq.getKq();
            if (queen == null || queen.getPosition() == null || queen.getArea() == null) {
                log.debug("null");
                log.debug(queen);
                return;
            }
            var center = queen.getArea().getCenter();
            if (!center.equals(Players.getLocal().getPosition())) {
                util.moveTo(center);
                log.debug(String.format("Walking under move"));
            }
        } catch (Exception e) {
            log.error("error", e);
            e.printStackTrace();
        }
        if (util.canCastVengeance()) {
            di.send(MenuAction.forInterfaceComponent(
                    Magic.Lunar.VENGEANCE.getComponent(), 0));
        }
    }

    @Override
    public void executeLoop() {
    }
}
