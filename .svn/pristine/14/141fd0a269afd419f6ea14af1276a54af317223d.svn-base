package com.neck_flexed.scripts.slayer.barrage;

import com.neck_flexed.scripts.slayer.Location;
import com.neck_flexed.scripts.slayer.SlayerBotImpl;
import com.neck_flexed.scripts.slayer.SlayerMonster;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.collections.Pair;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.NpcListener;
import com.runemate.game.api.script.framework.listeners.events.DeathEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2(topic = "BarrageListener")
public class BarrageListener implements NpcListener, EngineListener {
    private final SlayerBotImpl<?> bot;
    private final Location location;
    @Getter
    private final SlayerMonster monster;
    private int tick = 0;
    private boolean isInArea = false;
    @Getter
    private int stackcooldown = 0;
    @Getter
    @Setter
    private int stackIdx = 0;

    public BarrageListener(SlayerBotImpl<?> bot, SlayerMonster monster) {
        this.bot = bot;
        this.monster = monster;
        this.location = monster.getLocation(bot);
    }

    public void setStackcooldown(int v) {
        stackcooldown = v;
    }

    public void decrementStackCooldown() {
        stackcooldown--;
    }

    @Override
    public void onTickStart() {
        try {
            this.tick++;
            log.debug("tick: {}, monsters alive {}", tick, getTargetingMe().size());
            var p = Players.getLocal();
            if (p == null) return;
            if (monster == null) return;
            this.isInArea = location.getArea().contains(p);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public Npc getNearest() {
        return monster.getNpcsWithin(bot).nearest();
    }

    public Pair<Npc, Integer> getBestTargetWithCount() {
        var npcs = monster.getNpcsQuery(bot).results();
        if (npcs.isEmpty()) return new Pair<>(null, 0);

        var best = npcs.stream()
                .map(n -> new Pair<>(n, getTargetsAround(npcs, n)))
                .max(Comparator.comparingInt(Pair::getRight))
                .orElse(null);
        if (best == null) {
            log.debug("nearest");
            return new Pair<>(npcs.nearest(), 1);
        }
        log.debug("best: {}, stack size: {}", best.getLeft(), best.getRight());
        return best;
    }

    public Npc getBestTarget() {
        return getBestTargetWithCount().getLeft();
    }


    public LocatableEntityQueryResults<Npc> getAround(Locatable l, int withinSize) {
        var names = monster.getAllNames();
        return Npcs.newQuery()
                .names(names)
                .within(l.getArea().toRectangular().grow(withinSize, withinSize))
                .filter(n -> n.getTarget() == null || Objects.equals(n.getTarget(), Players.getLocal()))
                .results();

    }

    private int getTargetsAround(LocatableEntityQueryResults<Npc> query, Npc n) {
        var area = n.getServerPosition().getArea().grow(1, 1);
        return (int) query.stream().filter(area::contains).count();
    }

    @Override
    public void onNpcDeath(DeathEvent event) {
        if (!isInArea) return;
        var source = event.getSource();
        if (source == null) return;
        if (!(source instanceof Npc)) return;
        var npc = (Npc) source;
        if (!Objects.equals(npc.getTarget(), Players.getLocal())) return;
        log.debug("{} dead: {} - {}", monster, tick, event);
        if (bot != null) {
            bot.handleNpcDeath(monster, npc, (Npc) Players.getLocal().getTarget());
        }
    }


    public List<Npc> getTargetingMe() {
        return getTargetingMe(monster.getNpcsWithin(bot));
    }

    public boolean allTargetingMe(List<Npc> npcs) {
        var p = Players.getLocal();
        if (p == null) return false;
        return npcs.stream().allMatch(n -> Objects.equals(n.getTarget(), p));
    }

    public List<Npc> getTargetingMe(LocatableEntityQueryResults<Npc> npcs) {
        var p = Players.getLocal();
        if (p == null) return new ArrayList<>();
        return npcs.stream().filter(n -> n != null && n.isValid() && Objects.equals(n.getTarget(), p)).collect(Collectors.toList());
    }
}
