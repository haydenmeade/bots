package com.neck_flexed.scripts.kq;

import com.neck_flexed.scripts.common.PrayerFlicker;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2(topic = "SearchingLairState")
public class SearchingLairState extends BaseState<KqState> {
    private final kq bot;
    private final PrayerFlicker prayerFlicker;

    public SearchingLairState(kq bot, PrayerFlicker prayerFlicker) {
        super(bot, KqState.SEARCHING_LAIR);
        this.bot = bot;
        this.prayerFlicker = prayerFlicker;
    }

    @Override
    public void activate() {
        bot.updateStatus("Looking for the bug");
        this.prayerFlicker.setActivePrayers(Prayer.PROTECT_FROM_MAGIC);
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        var k = kq.getKq();
        return k != null && k.getArea() != null && k.getArea().contains(Players.getLocal()) || !kq.isInLair();
    }

    @Override
    public void executeLoop() {
        var k = kq.getKq();
        util.boostIfNeeded(this.bot.loadouts.getEquipped().getStyle());
        log.debug("CCV:{}", util.canCastVengeance());
        if (util.canCastVengeance()) {
            di.send(MenuAction.forInterfaceComponent(
                    Magic.Lunar.VENGEANCE.getComponent(), 0));
            Execution.delayUntil(() -> !util.canCastVengeance(), util::playerAnimating, 1200, 1400);
        }
        if (k != null) {
            if (k.getPosition() == null || k.getArea() == null) {
                log.debug("null");
                log.debug(k);
                return;
            }
            var p = k.getArea().getCenter();
            util.moveTo(p);
            Execution.delayUntil(() -> p.distanceTo(k.getArea().getCenter()) > 2 || this.done(), util::playerMoving, 1200, 1600);
        } else {
            var n = Npcs.getLoaded().furthest();
            if (n != null) {
                util.moveTo(n.getPosition());
                Execution.delayUntil(() -> kq.getKq() != null, util::playerMoving, 5000, 6000);
            }
        }
    }
}
