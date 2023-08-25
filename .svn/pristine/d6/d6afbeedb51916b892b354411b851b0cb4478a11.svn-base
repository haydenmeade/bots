package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.common.Cannon;
import com.neck_flexed.scripts.common.Task;
import com.neck_flexed.scripts.common.loadout.Loadout;
import com.neck_flexed.scripts.common.state.BaseState;
import com.neck_flexed.scripts.common.util;
import com.neck_flexed.scripts.slayer.Location;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.neck_flexed.scripts.slayer.state.FightState;
import com.neck_flexed.scripts.slayer.state.SlayerState;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.NpcListener;
import com.runemate.game.api.script.framework.listeners.events.HitsplatEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public class LuringState extends BaseState<SlayerState> implements EngineListener, NpcListener {

    private final SlayerBotImpl<?> bot;
    private final BarrageListener listener;
    private final SlayerMonster monster;
    private final Loadout loadout;
    private final Cannon cannon;
    private final Location location;
    private final Pattern[] ignorePlayers;
    private final List<Npc> lured = new ArrayList<>();
    private boolean started;
    private int tick = 0;
    private int check = -1;
    private Npc attacked;
    private boolean doingCannon;

    public LuringState(SlayerBotImpl<?> bot,
                       BarrageListener listener,
                       SlayerMonster monster,
                       Loadout lureLoadout,
                       Pattern[] ignorePlayers
    ) {
        super(bot, SlayerState.LURING);
        this.bot = bot;
        this.listener = listener;
        this.monster = monster;
        this.loadout = lureLoadout;
        this.cannon = bot.getCannon();
        this.location = monster.getLocation(bot);
        this.ignorePlayers = ignorePlayers;
    }

    @Override
    public void onNpcHitsplat(HitsplatEvent event) {
        if (Objects.equals(event.getSource(), attacked)) {
            started = true;
        }
    }

    @Override
    public EventListener[] getEventListeners() {
        return new EventListener[]{this, listener};
    }

    @Override
    public void activate() {
        bot.updateStatus("Luring " + monster);
        this.bot.prayerFlicker.setQuickPrayers(util.getMagicBoostPrayer(), location.getPrayer());
    }

    @Override
    public @Nullable String fatalError() {
        return null;
    }

    @Override
    public void onTickStart() {
        tick++;
        if (doingCannon) return;
        if (tick % 2 == 0) return;
        var p = Players.getLocal();
        if (p == null) return;
        // after hopping for some reason
        if (lured.size() > 0 && !started) {
            if (attacked != null) {
                attacked.interact("Attack");
                if (!attacked.isValid()) {
                    lured.clear();
                }
            } else {
                lured.clear();
            }
            return;
        }
        if (check != -1 && check <= tick) {
            lured.clear();
            check = -1;
        }
        var monsters = new ArrayList<Npc>(monster.getNpcsWithin(bot));
        var notTargetingMe = monsters.stream()
                .filter(m -> !Objects.equals(m.getTarget(), p) && !lured.contains(m))
                .min(Comparator.comparingDouble(n -> n.distanceTo(p)));
        if (notTargetingMe.isEmpty()) return;
        var bestTarget = notTargetingMe.get();

        this.bot.loadouts.equip(loadout);
        if (!Objects.equals(p.getTarget(), bestTarget)) {
            util.attack(bestTarget);
            lured.add(bestTarget);
            attacked = bestTarget;
            check = tick + 3;
        }
    }

    @Override
    public boolean done() {
        var npcs = monster.getNpcsWithin(bot);
        return (listener.allTargetingMe(npcs.asList()) && listener.getTargetingMe(npcs).size() > 3)
                ||
                (monster.getTask().equals(Task.NECHRYAEL) && monster.isSuperiorPresent(bot))
                ||
                (cannon.isCannonPlaced() && listener.getTargetingMe(npcs).size() > 5);
    }

    @Override
    public void executeLoop() {
        if (!Traversal.isRunEnabled() && Traversal.getRunEnergy() > 20)
            Traversal.toggleRun();
        if (this.cannon != null && Cannon.hasCannon() && location.getCannonSpot() != null) {
            this.doingCannon = FightState.doCannonLogic(cannon, bot, location.getCannonSpot(), Players.getLocal().getServerPosition(), location.getArea(), ignorePlayers, monster.isHoppingDisabled());
        }
    }
}
