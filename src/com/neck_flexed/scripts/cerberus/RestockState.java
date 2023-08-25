package com.neck_flexed.scripts.cerberus;

import com.neck_flexed.scripts.common.NeckBank;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.util;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

@Log4j2(topic = "RestockState")
public class RestockState extends com.neck_flexed.scripts.common.state.RestockState<CerbSettings, CerbState> {


    public RestockState(cerb bot) {
        super(bot, CerbState.RESTOCKING);
    }

    @Override
    protected Map<Pattern, Integer>[] getWithdraw() {
        return new Map[]{
                NeckBank.getBoostsNeeded(bot.settings().boostAmount(),
                        false,
                        bot.settings().boost()
                ),
                //NeckBank.toMap(1, items.staminaPotions),
                NeckBank.toMap(bot.settings().prayerAmount(), items.prayer4Dose),
                bot.settings().wildPieCount() > 0
                        ? NeckBank.toMap(bot.settings().wildPieCount(), items.wildPie)
                        : Collections.emptyMap(),
                bot.settings().traverseStrategy().equals(TraverseStrategy.KeyMasterTeleport)
                        ? NeckBank.toMap(1, items.keyMasterTeleport)
                        : Collections.emptyMap(),
                bot.settings().hasOrnatePool() && bot.settings().hasOrnateJewelleryBox()
                        ? Collections.emptyMap()
                        : NeckBank.toMap(1, items.ringOfDueling),
                NeckBank.toMap(bot.settings().fightStrategy().equals(FightStrategy.Flinch
                ) ? 2 : 0, bot.settings().food().gameName())
        };
    }

    @Override
    protected Pattern[] getExcept() {
        return util.concatenate(
                util.toPatternArray(new String[]{"Dust rune", "Law rune", "Earth rune", "Air rune", "Water rune", "Fire rune", "Nature rune", "Mist rune", "Mud rune", "Smoke rune", "Steam rune", "Lava rune",
                        items.spade,
                }),
                new Pattern[]{items.runePouch, items.imbuedHeart, items.constructionCape, items.ashSanctifier, items.houseTab,},
                util.toItemList(bot.settings().equipment()),
                util.toItemList(bot.settings().ghostEquipment()),
                util.toItemList(bot.settings().specEquipment())
        );
    }
}