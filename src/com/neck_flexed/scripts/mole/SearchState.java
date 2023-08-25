package com.neck_flexed.scripts.mole;

import com.neck_flexed.scripts.common.Action;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.hud.HintArrow;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.queries.HintArrowQueryBuilder;
import com.runemate.game.api.hybrid.region.Players;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2(topic = "SearchState")
public class SearchState extends BaseState<MoleState> {
    private final Mole bot;
    private Path pathToCentre;

    public SearchState(Mole bot) {
        super(bot, MoleState.SEARCHING);
        this.bot = bot;
    }

    public static @Nullable HintArrow getMoleHint() {
        return new HintArrowQueryBuilder().results().first();
    }

    public static Path getPathTo(Coordinate c) {
        return BresenhamPath.buildTo(c);
    }

    @Override
    public void activate() {
        bot.updateStatus("Searching");
    }

    @Override
    public void deactivate() {
    }

    @Override
    public String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return Mole.getMole() != null || util.isCannonNearby() || !Mole.isInLair();
    }

    @Override
    public void executeLoop() {
        var arr = getMoleHint();
        if (arr == null || arr.getPosition() == null) {
            //log.debug("No hint, going to centre");
            if (!Players.getLocal().getPosition().equals(Mole.centreLair)) {
                if (pathToCentre == null)
                    pathToCentre = getPathTo(Mole.centreLair);
                if (pathToCentre != null) {
                    pathToCentre.step();
                    Action.set(Action.Move);
                } else {
                    log.debug("Could not generate path in Mole.search no arrow");
                }
            }
        } else {
            var pos = arr.getPosition();
            if (pos == null) return;
            var path = getPathTo(pos);
            if (path != null) {
                path.step();
                Action.set(Action.Move);
            } else {
                log.debug("Could not generate path in Mole.search");
            }
        }
    }
}

