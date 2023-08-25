package com.neck_flexed.scripts.cerberus;

import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.loadout.Loadouts;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.util.EventListener;

@Log4j2(topic = "EnterLairState")
public class EnterLairState extends BaseState<CerbState> {

    private final cerb bot;
    private final Loadouts loadouts;
    // "Turn" "Iron Winch"
    private final PeekListener peekListener;
    private final CerbListener cerbListener;

    public EnterLairState(CerbListener cerbListener, cerb bot) {
        super(bot, CerbState.ENTERING_LAIR);
        this.cerbListener = cerbListener;
        this.bot = bot;
        this.loadouts = bot.loadouts;
        this.peekListener = new PeekListener();
    }

    private static GameObject getRoomEntrance() {
        return GameObjects.newQuery()
                .names("Iron Winch")
                .within(Area.rectangular(new Coordinate(1288, 1257, 0), new Coordinate(1297, 1247, 0)))
                .actions("Turn", "Peek")
                .results().nearest();
    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{this.peekListener};
    }

    @Override
    public void activate() {
        bot.updateStatus("Entering boss room");
        this.cerbListener.reset();
    }

    @Override
    public void deactivate() {
        if (this.needHop()) {
            if (ChatDialog.isOpen()) {
                var c = ChatDialog.getContinue();
                if (c != null) {
                    c.select();
                    Execution.delay(600, 700);
                }
            }
        }
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return needHop() || cerb.isInBossRoom();
    }

    private boolean needHop() {
        return this.peekListener.getPlayerCount() > 0;
    }


    @Override
    public void executeLoop() {
        if (this.loadouts.getEquipped() == null) {
            this.loadouts.setCurrentFromEquipmentOrEquip(this.loadouts.getForRole(Loadout.LoadoutRole.Combat));
            this.loadouts.equip(this.loadouts.getEquipped(), true);
        }
        if (cerb.isInBossRoomIncludingEntrance() && !cerb.isInBossRoom()) {
            var qp = "Quick Pass";
            var flm = "Flames";
            var flames = GameObjects.newQuery().on(new Coordinate(1240, 1242, 0)).names(flm).actions(qp).results().nearest();
            var p = Players.getLocal().getServerPosition();
            if (util.otherPlayersNearby()
                    && bot.settings().fightStrategy().equals(FightStrategy.Flinch)
                    && (Players.getLocal().getHealthGauge() == null || !Players.getLocal().getHealthGauge().isValid())
            ) {
                this.bot.forceState(CerbState.HOPPING);
                return;
            }
            if (flames != null && p.getY() <= 1242) {
                log.debug("trying to pass flames");
                di.send(MenuAction.forGameObject(flames, qp));
                Execution.delayUntil(() -> Players.getLocal().getServerPosition().getY() > 1242, 5000, 5100);
            } else {
                if (!Players.getLocal().isMoving()) {
                    log.debug("move to center");
                    util.moveTo(cerb.centerSpawnTile);
                    Execution.delay(50, 60);
                }
            }
            return;
        }

        var roomEntrance = getRoomEntrance();
        if (roomEntrance == null) {
            log.debug("null winch");
            Execution.delay(600);
            return;
        }

        if (this.peekListener.getPlayerCount() == -1) {
            log.debug("peeking the winch");
            di.send(MenuAction.forGameObject(roomEntrance, "Peek"));
            Execution.delayUntil(() -> this.peekListener.getPlayerCount() != -1, 5000);
            if (ChatDialog.isOpen()) {
                var c = ChatDialog.getContinue();
                if (c != null) {
                    c.select();
                    Execution.delay(600, 700);
                }
                Keyboard.pressKey(KeyEvent.VK_SPACE);
            }
            if (this.needHop()) {
                bot.forceState(CerbState.HOPPING);
            }

        } else {
            log.debug("going in boss room");
            di.send(MenuAction.forGameObject(roomEntrance, "Turn"));
            Execution.delayUntil(() -> cerb.isInBossRoomIncludingEntrance() || needHop(), 5000, 5500);
        }
    }
}
