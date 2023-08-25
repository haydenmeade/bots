package com.neck_flexed.scripts.slayer.turael;

import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.regex.Pattern;

@Log4j2(topic = "RestockState")
public class RestockState extends com.neck_flexed.scripts.common.state.RestockState<TuraelSettings, SlayerState> {
    private final bot tBot;

    public RestockState(com.neck_flexed.scripts.slayer.turael.bot bot) {
        super(bot, SlayerState.RESTOCKING);
        this.tBot = bot;
    }

    @Override
    public void deactivate() {
        var inv = NeckInventory.createInventory();
        log.debug("Initializing inventory: " + inv);
        tBot.setInventory(inv);
    }

    @Override
    protected Map<Pattern, Integer>[] getWithdraw() {
        var inv = tBot.getInventory();
        if (inv != null) {
            var map = inv.getMap();
            map.putAll(NeckBank.getBoostsNeeded(bot.settings().boostAmount(), false, bot.settings().boost()));
            return new Map[]{map};
        }
        return new Map[]{
                NeckBank.getBoostsNeeded(bot.settings().boostAmount(), false, bot.settings().boost()),
                NeckBank.toMap(Bank.getQuantity(items.iceCooler) + Inventory.getQuantity(items.iceCooler), items.iceCooler),
        };
    }

    @Override
    protected Pattern[] getExcept() {
        var inv = tBot.getInventory();
        if (inv != null) {
            return inv.getMap().keySet().toArray(new Pattern[0]);
        }
        return util.toPatternArray(new String[]{".*"});
    }
}
