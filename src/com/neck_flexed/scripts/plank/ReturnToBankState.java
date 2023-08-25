package com.neck_flexed.scripts.plank;

import com.neck_flexed.scripts.common.state.BaseState;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.web.WebPath;
import com.runemate.game.api.script.Execution;
import org.jetbrains.annotations.Nullable;

public class ReturnToBankState extends BaseState<PlankState> {
    private final planker bot;
    Area neitiznot = new Area.Rectangular(new Coordinate(2329, 3811, 0), new Coordinate(2345, 3796, 0));
    private WebPath path;

    public ReturnToBankState(planker bot) {
        super(bot, PlankState.RETURNTOBANK);
        this.bot = bot;
    }

    @Override
    public void activate() {
        bot.updateStatus("Return to bank");
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return neitiznot.contains(Players.getLocal());
    }

    @Override
    public void executeLoop() {
        var lyre = Equipment.getItemIn(Equipment.Slot.WEAPON);
        if (lyre != null && lyre.getDefinition().getName().equals("Enchanted lyre(i)")) {
            if (lyre.interact("Neitiznot"))
                Execution.delayUntil(() ->
                                !planker.isNearLumberYard()
                                        && Players.getLocal() != null
                        , 4000, 6000);
            Execution.delay(600, 700);
            return;
        }
//        if (path == null) {
//            this.path = WebPath.buildTo(Landmark.BANK);
//        }
//        if (path == null || !path.step()) {
//            path = null;
//        }
    }
}
