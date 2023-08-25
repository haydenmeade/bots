package com.neck_flexed.scripts.slayer.turael;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.SlayerTask;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.traverse.GamesNecklaceTraverse;
import com.neck_flexed.scripts.common.traverse.HouseJewelleryBoxTraverse;
import com.neck_flexed.scripts.common.traverse.Traverser;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.SlayerUtil;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.NpcContact;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Log4j2()
public class TuraelTaskGetterState extends BaseState<SlayerState> {

    private static final Coordinate TURAEL_LOCATION = new Coordinate(2930, 3536, 0);
    private final SlayerBotImpl<?> bot;
    private final Traverser traverser;
    private boolean done = false;
    private String startTask;
    private boolean gotInDialogue;

    public TuraelTaskGetterState(SlayerBotImpl<?> bot, HouseConfig houseConfig) {
        super(bot, SlayerState.GET_TURAEL_TASK);
        this.bot = bot;
        this.traverser = new Traverser(
                bot,
                houseConfig,
                TURAEL_LOCATION,
                new int[]{11319, 11575, 11574,},
                null,
                new GamesNecklaceTraverse(GamesNecklaceTraverse.Destination.Burthorpe),
                new HouseJewelleryBoxTraverse(HouseJewelleryBoxTraverse.Destination.Burthorpe)
        );
    }

    @Override
    public void activate() {
        this.startTask = SlayerTask.getCurrent();
        bot.updateStatus("Getting task from turael");
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public boolean done() {
        return done;
    }

    @Override
    public void executeLoop() {
        var turael = Npcs.newQuery()
                .names("Turael")
                .actions("Assignment")
                .results()
                .first();
        if (util.canNpcContact() && turael == null) {
            this.done = SlayerUtil.npcContactSlayerTaskFrom(NpcContact.OSRS.TURAEL, startTask);
        } else {
            this.done = this.pathToTuraelSlayerTask(startTask);
        }
    }

    private boolean pathToTuraelSlayerTask(String startTask) {

        var turael = Npcs.newQuery()
                .names("Turael")
                .actions("Assignment")
                .results()
                .first();
        if (turael == null) {
            traverser.executeLoop();
            return false;
        }
        if (gotInDialogue || ChatDialog.isOpen()) {
            if (Objects.equals(SlayerTask.getCurrent(), startTask) && !ChatDialog.isOpen()) {
                this.gotInDialogue = false;
                return false;
            }
            this.gotInDialogue = true;
            return SlayerUtil.slayerTaskDialogue(startTask);
        }
        di.send(MenuAction.forNpc(turael, "Assignment"));

        Execution.delayUntil(
                () -> Monster.fromSlayerTask(bot.getOverrideTask()).isPresent()
                        || ChatDialog.isOpen(),
                util::playerMoving, 3000, 4000);
        return false;
    }

}
