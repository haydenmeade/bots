package com.neck_flexed.scripts.mole;

import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

@Log4j2(topic = "RestockState")
public class RestockState extends com.neck_flexed.scripts.common.state.RestockState<MoleSettings, MoleState> {
    public RestockState(Mole bot) {
        super(bot, MoleState.RESTOCKING);
    }

    @Override
    protected Map<Pattern, Integer>[] getWithdraw() {
        return new Map[]{
                NeckBank.getBoostsNeeded(bot.settings().boostAmount(), false, bot.settings().boost()),
                NeckBank.toMap(bot.settings().staminaAmount(), items.stamina4Dose),
                NeckBank.toMap(1
                        , items.spadeP
                        , Mole.falShield
                        , bot.settings().light().getPattern()
                ),
                bot.settings().hasOrnatePool() && bot.settings().hasOrnateJewelleryBox()
                        ? Collections.emptyMap()
                        : NeckBank.toMap(1, items.ringOfDueling),
                NeckBank.toMap(2, bot.settings().food().gameName())
        };
    }

    @Override
    protected Pattern[] getExcept() {
        return util.concatenate(
                util.toPatternArray(new String[]{"Dust rune", "Law rune", "Earth rune", "Air rune", "Water rune", "Fire rune", "Nature rune", "Mist rune", "Mud rune", "Smoke rune", "Steam rune", "Lava rune",
                        items.spade, items.dwarvenRock, items.locatorOrb,
                }),
                new Pattern[]{Mole.falShield, bot.settings().light().getPattern(), items.imbuedHeart, items.ringOfDueling, items.runePouch, items.constructionCape, items.houseTab,},
                util.toItemList(bot.settings().equipment()),
                util.toItemList(bot.settings().specEquipment())
        );
    }
}
