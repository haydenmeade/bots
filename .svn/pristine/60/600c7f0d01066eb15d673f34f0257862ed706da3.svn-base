package com.neck_flexed.scripts.slayer.state;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Location;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.neck_flexed.scripts.slayer.traverse.TraverseOverride;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

@Log4j2
public class TraverseState extends BaseState<SlayerState> {
    private final SlayerBotImpl<?> bot;
    private final SlayerMonster monster;
    private final Traverser traverser;
    private final Location location;
    private final Pattern[] ignorePlayers;
    private boolean done = false;
    private boolean setQuickPrayers = false;

    public TraverseState(SlayerBotImpl<?> bot, @NotNull SlayerMonster monster, Pattern[] ignorePlayers) {
        super(bot, SlayerState.TRAVERSING);
        this.bot = bot;
        this.monster = monster;
        this.ignorePlayers = ignorePlayers;
        HouseConfig houseConfig = bot.getHouseConfig();
        this.location = monster.getLocation(bot);
        var overrideFunc = location.getTraverseOverride();
        TraverseOverride tempoverride = null;
        try {
            tempoverride = overrideFunc != null ? overrideFunc.call() : null;
        } catch (Exception e) {
            log.error(e);
            tempoverride = null;
        }
        var override = tempoverride;
        bot.prayerFlicker.disable();
        this.traverser = new Traverser(
                bot,
                houseConfig,
                location.getTraverseToTile(),
                location.getPathRegions(),
                override == null ? null : destination -> override.overrideLoop(bot, destination),
                location.getTraverseMethods()
        );
    }

    @Override
    public void activate() {
        super.activate();
        var s = String.format("Traversing to %s", monster);
        var location = monster.getLocation(bot);
        var sl = location.getLocation();
        if (sl != null)
            s += " at " + sl.getName();
        bot.updateStatus(s);
        this.bot.setLoadoutOverriders();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        bot.loadouts.invalidateCurrent();
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return this.done || location.getArea().contains(Players.getLocal().getServerPosition());
    }

    @Override
    public void executeLoop() {
        this.done = traverser.executeLoop();
        if (traverser.getError() != null && !traverser.getError().isEmpty()) {
            bot.updateStatus(String.format("Force restock since missing traverse to %s", monster));
            bot.forceState(SlayerState.RESTOCKING);
            return;
        }
        if (!setQuickPrayers)
            this.setQuickPrayers = Prayer.setQuickPrayers(util.joinPrayers(bot.loadouts.getEquipped().getBoostPrayers(), location.getPrayer()));
        if (!monster.isHoppingDisabled()
                && util.anyOtherPlayersWithin(location.getArea(), ignorePlayers)
                && Players.getLocal().getHealthGauge() == null) {
            log.debug("Force hopping from traverse");
            this.bot.forceState(SlayerState.HOPPING);
        }
    }
}
