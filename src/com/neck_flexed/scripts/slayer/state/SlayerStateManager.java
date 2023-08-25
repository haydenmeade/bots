package com.neck_flexed.scripts.slayer.state;

import com.neck_flexed.scripts.common.SlayerTask;
import com.neck_flexed.scripts.common.loot.Loot;
import com.neck_flexed.scripts.common.state.StateManager;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Monster;
import com.neck_flexed.scripts.slayer.SlayerUtil;
import com.neck_flexed.scripts.slayer.bot;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SlayerStateManager extends StateManager<com.neck_flexed.scripts.slayer.bot, SlayerState> {

    public SlayerStateManager(bot bot) {
        super(bot);
    }

    @Override
    public SlayerState getNextState(SlayerState initiator) {
        var p = Players.getLocal();
        if (p == null) return SlayerState.STARTING;
        var task = SlayerTask.getCurrent();
        var monster = Monster.fromSlayerTask(bot.getOverrideTask());
        var settings = bot.settings();
        this.bot.setLoadoutOverriders();
        bot.refreshProgress();

        bot.setLoadoutOverriders();
        // ** COMBAT:
        var m = monster.orElse(bot.getPreviousTask());
        var isSuperiorPresent = m != null && m.isSuperiorPresent(bot);
        switch (initiator) {
            case BARRAGING:
            case FIGHTING:
                return SlayerState.LOOTING;
            case LOOTING:
                if (!SlayerTask.hasTask() && !isSuperiorPresent) {
                    return SlayerState.PICK_UP_CANNON;
                } else if (!isSuperiorPresent && (bot.doWeNeedToRestock(true) || bot.breakManager.breaking() || bot.breakManager.stopBot()
                        || Loot.cantLootMore(bot.getCannon().isCannonPlaced() ? 4 : 0))) {
                    return SlayerState.PICK_UP_CANNON;
                } else if (monster.isPresent() && monster.get().getLocation(bot).isBarrage() && bot.barrageTasksAvailable()) {
                    return SlayerState.LURING;
                } else {
                    return SlayerState.FIGHTING;
                }
            case PICK_UP_CANNON:
                if (isSuperiorPresent) return SlayerState.FIGHTING;
                if (!SlayerTask.hasTask() && !isSuperiorPresent) {
                    if (bot.npcContactAvailable() && !Magic.Book.LUNAR.isCurrent()) {
                        return SlayerState.SWITCH_SPELLBOOK;
                    }
                    return SlayerState.GET_NEW_TASK;
                }
                if (util.isFullyRestored() || !util.canRestoreAtFerox())
                    return SlayerState.RESTOCKING;
                return SlayerState.RESTORING;
            case STACKING:
                return SlayerState.BARRAGING;
            case LURING:
                return SlayerState.STACKING;
            case RESTORING:
                if (bot.breakManager.breaking() || bot.breakManager.stopBot())
                    return SlayerState.BREAKING;
                return SlayerState.RESTOCKING;
            case GET_NEW_TASK:
                // get master task
                return SlayerState.GET_MASTER_TASK;
        }


        if (SlayerTask.hasTask()) {
            // Unhandled task
            var wantToSkip = bot.wantToSkip(monster.orElse(null));
            if (monster.isEmpty() || wantToSkip || SlayerUtil.doesTaskMatch(task, bot.settings().skipTaskList(), bot.settings().stopOnAnyBoss())) {
                if (SlayerUtil.doesTaskMatch(task, bot.settings().stopOnTasks(), bot.settings().stopOnAnyBoss())) {
                    var msg = String.format("Got Task we wanted: %s %s from %s", SlayerTask.getCount(), task, bot.settings().master());
                    log.info(msg);
                    bot.updateStatus(msg);
                    ClientUI.showAlert(ClientUI.AlertLevel.INFO, msg);
                    Execution.delay(3000);
                    bot.stop(msg);
                    return SlayerState.BREAKING;
                } else if (wantToSkip && settings.skipTasksWithTurael()) {
                    // turael skip
                    return SlayerState.GET_TURAEL_TASK;
                } else if (wantToSkip) {
                    if (util.hasAnyTraverse(settings.master().getTraverses(), bot.getHouseConfig()))
                        return SlayerState.SKIP_TASK;
                    else if (util.canNpcContactWithSwap(bot.getHouseConfig().getAltar()) && SkipTaskState.getBestMasterTraverse(bot) != null) {
                        if (util.canNpcContact())
                            return SlayerState.SKIP_TASK;
                        return SlayerState.SWITCH_SPELLBOOK;
                    } else if (util.isFullyRestored() || !util.canRestoreAtFerox())
                        return SlayerState.RESTOCKING;
                    return SlayerState.RESTORING;
                } else {
                    var msg = String.format("Stopping due to unhandled task: %s", task);
                    bot.updateStatus(msg);
                    Execution.delay(3000);
                    bot.stop(msg);
                    return SlayerState.BREAKING;
                }
            } else {
                var monsterInstance = monster.get();
                var location = monsterInstance.getLocation(bot);
                if (monsterInstance.getLocation(bot).isBarrage() && bot.barrageTasksAvailable()) {
                    if (location.getArea().contains(p)) {
                        if (util.anyOtherPlayersWithin(location.getArea(), util.parseCsvRegexString(bot.settings().ignorePlayers())))
                            return SlayerState.HOPPING;
                        return SlayerState.LURING;
                    }
                } else if (location.getArea().contains(p)) {
                    if (!monsterInstance.isHoppingDisabled() && util.anyOtherPlayersWithin(location.getArea(), util.parseCsvRegexString(bot.settings().ignorePlayers()))
                            && Players.getLocal().getHealthGauge() == null)
                        return SlayerState.HOPPING;
                    return SlayerState.FIGHTING;
                }
            }
        }
        if (Bank.isOpen()) return SlayerState.RESTOCKING;

        // has valid task, not in area yet:
        if (monster.isPresent()) {
            // spellbook swap check
            var desiredSpellbook = bot.getDesiredSpellbook(monster.get(), bot.getHouseConfig());
            if (!desiredSpellbook.isCurrent()) {
                return SlayerState.SWITCH_SPELLBOOK;
            }
        }


        boolean needsRestock = bot.doWeNeedToRestock(false);
        if (initiator != SlayerState.RESTOCKING && needsRestock) {
            if (util.isFullyRestored() || !util.canRestoreAtFerox())
                return SlayerState.RESTOCKING;
            return SlayerState.RESTORING;
        }

        // need new task -> go to master and get a task
        if (!SlayerTask.hasTask()) {
            if (bot.npcContactAvailable() && !Magic.Book.LUNAR.isCurrent()) {
                return SlayerState.SWITCH_SPELLBOOK;
            }
            return SlayerState.GET_NEW_TASK;
        }

        return SlayerState.TRAVERSING;
    }

}
