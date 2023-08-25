package com.neck_flexed.scripts.plank;

import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.regex.Pattern;

@Log4j2(topic = "RestockState")
public class RestockState extends com.neck_flexed.scripts.common.state.RestockState<PlankSettings, PlankState> {
    public RestockState(planker bot) {
        super(bot, PlankState.TRAVERSING);
    }

    @Override
    protected Map<Pattern, Integer>[] getWithdraw() {
        // withdraw
        return new Map[]{
                NeckBank.toMap(1, items.staminaPotions),
                NeckBank.toMap(0, bot.settings().plank().getLogName())
        };
    }

    @Override
    protected Pattern[] getExcept() {
        return util.concatenate(util.toPatternArray(new String[]{
                "Coins"
        }), new Pattern[]{items.staminaPotions});
    }

    @Override
    public void executeLoop() {
        super.executeLoop();

        if (Bank.isOpen() &&
                !Bank.contains(bot.settings().plank().getLogName())
                && !Inventory.contains(bot.settings().plank().getLogName())) {
            var s = "Out of logs";
            bot.updateStatus(s);
            bot.stop(s);
        }
    }
}
