package com.neck_flexed.scripts.mole;

import com.neck_flexed.scripts.common.PortalNexusTeleport;
import com.neck_flexed.scripts.common.TeleportSpellInfo;
import com.neck_flexed.scripts.common.items;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.traverse.*;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.GameObjectQueryBuilder;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

import java.util.EventListener;

@Log4j2(topic = "TraverseState")
public class TraverseState extends BaseState<MoleState> {
    private final Mole bot;
    private final MoleHillListener moleHillListener;
    private final Traverser traverser;
    private boolean needToHop = false;
    private String error;

    public TraverseState(Mole bot) {
        super(bot, MoleState.TRAVERSING);
        this.bot = bot;
        this.traverser = new Traverser(
                bot, bot.getHouseConfig(), Mole.entryTile,
                new int[]{11828, 12084,},
                null,
                new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.FaladorPark),
                new ItemTraverse(items.ringOfWealth, "Rub", "Falador Park"),
                new TeleportTraverse(TeleportSpellInfo.FALADOR),
                new HousePortalTraverse(PortalNexusTeleport.Falador)
        );
        this.moleHillListener = new MoleHillListener();
    }

    private static GameObject getMoleHill() {
        return new GameObjectQueryBuilder().on(Mole.entryTile).ids(Mole.MoleHillId).results().first();
    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{moleHillListener};
    }

    private int lookInsideMoleHill() {
        if (moleHillListener.getPlayerCount() > -1) return moleHillListener.getPlayerCount();
        log.debug("looking inside mole hill");
        var mh = getMoleHill();
        if (mh == null) return -1;
        if (!mh.interact("Look-inside")) return -1;
        Execution.delayUntil(() -> moleHillListener.getPlayerCount() > -1 || bot.isPaused(), 5000);
        return moleHillListener.getPlayerCount();
    }

    @Override
    public void activate() {
        bot.updateStatus("Traversing");
    }

    @Override
    public void deactivate() {
        super.deactivate();
        if (needToHop)
            bot.forceState(MoleState.HOPPING);
    }

    @Override
    public String fatalError() {
        return error;
    }

    @Override
    public boolean done() {
        return needToHop || Mole.isInLair();
    }

    private void enterLair() {
        log.debug("entering lair");
        var spade = Inventory.getItems(items.spade).first();
        if (spade == null) {
            this.error = "no spade found";
            return;
        }
        spade.interact("Dig");
        Execution.delayUntil(() -> Mole.isInLair() || bot.isPaused(), 5000);
    }

    @Override
    public void executeLoop() {

        if (Mole.inFaladorPark() && !Players.getLocal().getPosition().equals(Mole.entryTile)) {
            if (!Players.getLocal().getPosition().equals(Mole.entryTile)) {
                util.moveTo(Mole.entryTile);
                Execution.delay(600, 750);
            }
            return;
        } else if (!Mole.inFaladorPark() && !Mole.isInLair()) {
            traverser.executeLoop();
            return;
        }

        if (Players.getLocal().getPosition().equals(Mole.entryTile)) {
            var pc = lookInsideMoleHill();
            if (pc > 0) {
                // check if players inside.
                needToHop = true;
            } else if (pc == 0) {
                if (this.bot.loadouts != null)
                    this.bot.loadouts.setCurrentFromEquipmentOrEquip(this.bot.loadouts.getAnyCombat());
                enterLair();
            }
        }

    }
}
