package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.regex.Pattern;

@Log4j2(topic = "RestockState")
public class RestockState extends com.neck_flexed.scripts.common.state.RestockState<BarrageSettings, SlayerState> {
    public RestockState(NeckBot<BarrageSettings, SlayerState> bot) {
        super(bot, SlayerState.RESTOCKING);
    }

    @Override
    protected Map<Pattern, Integer>[] getWithdraw() {
        return new Map[]{
                NeckBank.getBoostsNeeded(bot.settings().boostAmount(), false, bot.settings().boost()),
                bot.settings().hasOrnatePool() && bot.settings().hasOrnatePool()
                        ? util.getHouseTeleport(Magic.Book.ANCIENT, 0).getItems()
                        : NeckBank.toMap(1, items.ringOfDueling),
        };
    }

    @Override
    protected Pattern[] getExcept() {
        return util.concatenate(
                util.toPatternArray(new String[]{"Soul rune", "Blood rune", "Water rune", "Death rune"}),
                new Pattern[]{items.imbuedHeart, items.ringOfDueling, items.runePouch, items.constructionCape, items.bonecrusher, items.herbSack, items.ashSanctifier, items.houseTab, items.xericsTalisman,},
                util.toItemList(bot.settings().equipment()),
                util.toItemList(bot.settings().lureEquipment())
        );
    }
}
