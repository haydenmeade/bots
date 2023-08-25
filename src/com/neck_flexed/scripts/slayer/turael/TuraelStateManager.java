package com.neck_flexed.scripts.slayer.turael;

import com.neck_flexed.scripts.common.SlayerTask;
import com.neck_flexed.scripts.common.state.StateManager;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerUtil;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.region.Players;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TuraelStateManager extends StateManager<bot, SlayerState> {
    public TuraelStateManager(bot bot) {
        super(bot);
    }

    @Override
    public SlayerState getNextState(SlayerState initiator) {
        var skipFor = bot.settings().skipFor();
        var task = SlayerTask.getCurrent();
        var monster = Monster.fromSlayerTask(bot.getOverrideTask());
        var hasTask = SlayerTask.hasTask();

        var isTuraelMonster = bot.isTuraelMonster(monster);
        bot.setLoadoutOverriders();
        switch (initiator) {
            case RESTORING:
                if (bot.breakManager.breaking() || bot.breakManager.stopBot())
                    return SlayerState.BREAKING;
                return SlayerState.RESTOCKING;
            case FIGHTING:
                if (SlayerTask.hasTask())
                    return SlayerState.FIGHTING;
                return SlayerState.PICK_UP_CANNON;
            case PICK_UP_CANNON:
                return SlayerState.GET_NEW_TASK;
            case GET_NEW_TASK:
                if (bot.settings().skipForStreak()) {
                    // Skipping for streak
                    if (hasTask && !isTuraelMonster) {
                        // Error State
                        var msg = String.format("Error State, unable to do tasks for streak with task: %s", SlayerTask.getCurrent());
                        log.error(msg);
                        bot.startPauseAndEndBotTimeout(msg);
                        return SlayerState.STARTING;
                    }

                    var streakGoal = bot.settings().skipUntil();
                    var streak = SlayerTask.getStreak();
                    if (((streak + 1) % streakGoal) == 0) {
                        // ending
                        var msg = String.format("Got Task Streak we wanted: %s", streak);
                        log.info(msg);
                        bot.updateStatus(msg);
                        ClientUI.showAlert(ClientUI.AlertLevel.INFO, msg);
                        bot.stop(msg);
                        return SlayerState.BREAKING;
                    }
                    // get turael task
                } else if (!hasTask) {
                    // get master task
                    return SlayerState.GET_MASTER_TASK;
                }
                return SlayerState.GET_TURAEL_TASK;
        }

        var skipForTurael = bot.settings().skipForTurael();
        if (!bot.settings().skipForStreak() && SlayerUtil.doesTaskMatch(task, skipForTurael, bot.settings().stopOnAnyBoss())) {
            var msg = String.format("Got Task we wanted: %s %s from Turael", SlayerTask.getCount(), task);
            log.info(msg);
            bot.updateStatus(msg);
            ClientUI.showAlert(ClientUI.AlertLevel.INFO, msg);
            bot.stop(msg);
            return SlayerState.BREAKING;
        }
        if (!bot.settings().skipForStreak() && SlayerUtil.doesTaskMatch(task, skipFor, bot.settings().stopOnAnyBoss())) {
            var msg = String.format("Got Task we wanted: %s %s from %s", SlayerTask.getCount(), task, bot.settings().master());
            log.info(msg);
            bot.updateStatus(msg);
            ClientUI.showAlert(ClientUI.AlertLevel.INFO, msg);
            bot.stop(msg);
            return SlayerState.BREAKING;
        }

        if (Bank.isOpen()) return SlayerState.RESTOCKING;

        boolean needsRestock = bot.doWeNeedToRestock();

        if (isTuraelMonster && monster.isPresent() && !needsRestock) {
            // has turael task
            if (monster.get().getLocation(bot).getArea().contains(Players.getLocal())) {
                if (util.anyOtherPlayersWithin(monster.get().getLocation(bot).getArea()))
                    return SlayerState.HOPPING;
                return SlayerState.FIGHTING;
            } else {
                return SlayerState.TRAVERSING;
            }
        }

        if (Health.isPoisoned() || Health.getCurrentPercent() < 50) {
            return SlayerState.RESTORING;
        }

        if (initiator != SlayerState.RESTOCKING && needsRestock) {
            if (util.isFullyRestored())
                return SlayerState.RESTOCKING;
            return SlayerState.RESTORING;
        }
        return SlayerState.GET_NEW_TASK;
    }
}
