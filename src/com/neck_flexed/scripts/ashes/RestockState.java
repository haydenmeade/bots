package com.neck_flexed.scripts.ashes;

import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.regex.Pattern;

@Log4j2(topic = "RestockState")
public class RestockState extends com.neck_flexed.scripts.common.state.RestockState<AshSettings, AshState> {
    public RestockState(ashes bot) {
        super(bot, AshState.RESTOCKING);
    }

    @Override
    protected Map<Pattern, Integer>[] getWithdraw() {
        // withdraw
        return new Map[]{NeckBank.toMap(1, items.wildySword4),
                NeckBank.toMap(1,
                        Bank.contains(items.locatorOrb) || Inventory.contains(items.locatorOrb)
                                ? items.locatorOrb
                                : items.dwarvenRock
                ),
                NeckBank.toMap(0, bot.settings().ashes().getPattern())
        };
    }

    @Override
    protected Pattern[] getExcept() {
        return util.toPatternArray(new String[]{items.locatorOrb, items.dwarvenRock, items.wildySword4});
    }

    @Override
    public void executeLoop() {
        super.executeLoop();

        if (Bank.isOpen() &&
                !Bank.contains(bot.settings().ashes().getPattern())
                && !Inventory.contains(bot.settings().ashes().getPattern())) {
            var s = "Out of ashes";
            bot.updateStatus(s);
            bot.stop(s);
        }
    }
}
