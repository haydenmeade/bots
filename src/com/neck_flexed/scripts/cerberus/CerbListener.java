package com.neck_flexed.scripts.cerberus;

import com.google.common.collect.ComparisonChain;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.entities.Projectile;
import com.runemate.game.api.hybrid.entities.details.Animable;
import com.runemate.game.api.hybrid.entities.details.Identifiable;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.region.Region;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.framework.listeners.EngineListener;
import com.runemate.game.api.script.framework.listeners.NpcListener;
import com.runemate.game.api.script.framework.listeners.ProjectileListener;
import com.runemate.game.api.script.framework.listeners.SpotAnimationListener;
import com.runemate.game.api.script.framework.listeners.events.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2(topic = "CerbListener")
public class CerbListener implements NpcListener, EngineListener, SpotAnimationListener, ProjectileListener {
    private static final int ANIMATION_ID_IDLE = -1;
    private static final int ANIMATION_ID_STAND_UP = 4486;
    private static final int ANIMATION_ID_SIT_DOWN = 4487;
    private static final int ANIMATION_ID_FLINCH = 4489;
    private static final int ANIMATION_ID_RANGED = 4490;
    private static final int ANIMATION_ID_MELEE = 4491;
    private static final int ANIMATION_ID_LAVA = 4493;
    private static final int ANIMATION_ID_GHOSTS = 4494;
    private static final int ANIMATION_ID_DEATH = 4495;

    private static final int PROJECTILE_ID_MAGIC = 1242;
    private static final int PROJECTILE_ID_RANGE = 1245;

    private static final int GHOST_PROJECTILE_ID_RANGE = 34;
    private static final int GHOST_PROJECTILE_ID_MAGIC = 100;
    private static final int GHOST_PROJECTILE_ID_MELEE = 1248;

    private static final int PROJECTILE_ID_NO_FUCKING_IDEA = 15;
    private static final int PROJECTILE_ID_LAVA = 1247;

    private static final Set<Integer> REGION_IDS = Set.of(4883, 5140, 5395);
    @Getter
    private final List<CerberusAttack> upcomingAttacks = new ArrayList<>();
    private final List<Long> tickTimestamps = new ArrayList<>();
    private final cerb bot;
    private final Lock ghostLock = new ReentrantLock();
    @Getter
    private final List<Npc> ghosts = new ArrayList<>();
    @Getter
    @Nullable
    private Cerberus cerberus;
    @Getter
    private int gameTick;
    private int tickTimestampIndex;
    @Getter
    private long lastTick;
    private boolean isDead = false;
    private Area deadArea;
    @Getter
    @Nullable
    private Prayer prayer = Prayer.PROTECT_FROM_MAGIC;

    public CerbListener(cerb bot) {
        this.bot = bot;
    }

    public Prayer getBestProtectionPrayer() {
        final List<CerberusAttack> upcomingAttacks = new ArrayList<>(this.getUpcomingAttacks());

        if (upcomingAttacks.isEmpty()) {
            return Prayer.PROTECT_FROM_MAGIC;
        }

//        final int nextTick = upcomingAttacks.stream().mapToInt(CerberusAttack::getTick).min().orElse(0);
//        var nextAttacks = upcomingAttacks.stream()
//                .filter(a -> a.getTick() == nextTick)
//                .sorted(Comparator.comparingInt(a -> a.getAttack().getPriority()))
//                .collect(Collectors.toList());
//
//        if (nextAttacks.isEmpty()) return Prayer.PROTECT_FROM_MAGIC;
        //nextAttacks.forEach(a -> log.debug("tick: {}, priority: {}, name: {}", a.getTick(), a.getAttack().getPriority(), a.getAttack()));

        final CerberusAttack cerberusAttack = upcomingAttacks.get(0);

        final Prayer prayer;

        if (cerberusAttack.getAttack() == Cerberus.Attack.AUTO || cerberusAttack.getAttack() == Cerberus.Attack.LAVA) {
            prayer = this.getPrayer();
        } else {
            prayer = cerberusAttack.getAttack().getPrayer();
        }
        return prayer;
    }

    @Override
    public void onNpcSpawned(NpcSpawnedEvent event) {
        try {
            final Npc npc = event.getNpc();

            if (cerberus == null && npc != null && npc.getName() != null && npc.getName().toLowerCase().contains("cerberus")) {
                log.debug("onNpcSpawned name={}, id={}", npc.getName(), npc.getId());

                cerberus = new Cerberus(npc);

                gameTick = 0;
                tickTimestampIndex = 0;
                lastTick = System.currentTimeMillis();

                upcomingAttacks.clear();
                tickTimestamps.clear();
                try {
                    ghostLock.lock();
                    ghosts.clear();
                } finally {
                    ghostLock.unlock();
                }
            }

            if (cerberus == null) {
                return;
            }

            final Ghost ghost = Ghost.fromNPC(npc);

            if (ghost != null) {
                if (ghosts.size() == 3) {
                    log.error("double spawn event {}", event);
                    manuallyGetGhosts();
                } else {
                    log.debug("Ghost spawned {}", ghost);
                    try {
                        ghostLock.lock();
                        ghosts.add(npc);
                    } finally {
                        ghostLock.unlock();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private void manuallyGetGhosts() {
        try {
            ghostLock.lock();
            var ghostsNpcs = Npcs.newQuery()
                    .ids(Ghost.MAGE.getNpcId(), Ghost.MELEE.getNpcId(), Ghost.RANGE.getNpcId())
                    .results();
            ghosts.clear();
            ghosts.addAll(ghostsNpcs);
        } finally {
            ghostLock.unlock();
        }
    }

    @Override
    public void onSpotAnimationSpawned(SpotAnimationSpawnEvent event) {
        if (!cerb.isInBossRoom()) return;
        var anim = event.getSpotAnimation();
        if (anim == null) return;
        log.debug(event);
        if (anim.getId() == 1246)
            log.debug("LAVA");
    }

    @Override
    public void onProjectileLaunched(ProjectileLaunchEvent event) {
        if (cerberus == null) {
            return;
        }
        try {

            final Projectile projectile = event.getProjectile();

            final int hp = cerberus.getHp();

            final Phase expectedAttack = cerberus.getNextAttackPhase(1, hp);

            switch (projectile.getSpotAnimationId()) {
                case PROJECTILE_ID_MAGIC:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MAGIC");

                    if (expectedAttack != Phase.TRIPLE) {
                        cerberus.nextPhase(Phase.AUTO);
                    } else {
                        cerberus.setLastTripleAttack(Cerberus.Attack.MAGIC);
                    }

                    cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.MAGIC);
                    break;
                case PROJECTILE_ID_RANGE:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "RANGED");

                    if (expectedAttack != Phase.TRIPLE) {
                        cerberus.nextPhase(Phase.AUTO);
                    } else {
                        cerberus.setLastTripleAttack(Cerberus.Attack.RANGED);
                    }

                    cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.RANGED);
                    break;
                case GHOST_PROJECTILE_ID_RANGE:
                    if (!ghosts.isEmpty()) {
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "RANGED");
                    }
                    break;
                case GHOST_PROJECTILE_ID_MAGIC:
                    if (!ghosts.isEmpty()) {
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MAGIC");
                    }
                    break;
                case GHOST_PROJECTILE_ID_MELEE:
                    if (!ghosts.isEmpty()) {
                        log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MELEE");
                    }
                    break;
                case PROJECTILE_ID_NO_FUCKING_IDEA:
                case PROJECTILE_ID_LAVA: //Lava
                default:
                    break;

            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onTickStart() {
        try {
            var c = cerberus;
            if (c == null) {
                return;
            }

            if (tickTimestamps.size() <= tickTimestampIndex) {
                tickTimestamps.add(System.currentTimeMillis());
            } else {
                tickTimestamps.set(tickTimestampIndex, System.currentTimeMillis());
            }

            long min = 0;

            for (int i = 0; i < tickTimestamps.size(); ++i) {
                if (min == 0) {
                    min = tickTimestamps.get(i) + 600 * ((tickTimestampIndex - i + 5) % 5);
                } else {
                    min = Math.min(min, tickTimestamps.get(i) + 600 * ((tickTimestampIndex - i + 5) % 5));
                }
            }

            tickTimestampIndex = (tickTimestampIndex + 1) % 5;

            lastTick = min;

            ++gameTick;

            if (gameTick % 10 == 3) {
                setAutoAttackPrayer();
            }

            if (gameTick - c.getLastGhostYellTick() == 20 && ghosts.size() > 0) {
                try {
                    ghostLock.lock();
                    ghosts.clear();
                } finally {
                    ghostLock.unlock();
                }
            }

            calculateUpcomingAttacks();
            log.debug("Cerb HP: {}", c.getHp());
            log.debug("Best prayer: {}", this.getBestProtectionPrayer());
            log.debug("Current prayer: {}", this.bot.prayerFlicker.getActiveProtectionPrayer());
            log.debug("Ghosts: {}", this.ghosts.size());
            log.debug("Upcoming attacks for at {}", gameTick);
            for (int i = 0; i < upcomingAttacks.size(); i++) {
                var a = upcomingAttacks.get(i);
                log.debug("i:{} t: {}, attk: {}", i, a.getTick(), a.getAttack());
            }


        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onNpcAnimationChanged(AnimationEvent event) {
        try {
            var cerberus = this.cerberus;
            if (cerberus == null) {
                return;
            }

            final Animable actor = event.getSource();

            final Npc npc = cerberus.getNpc();

            if (npc == null || getId(actor) != npc.getId()) {
                log.debug("Other: {}", event);
                return;
            }

            final int animationId = npc.getAnimationId();

            final int hp = cerberus.getHp();

            final Phase expectedAttack = cerberus.getNextAttackPhase(1, hp);

            switch (animationId) {
                case ANIMATION_ID_MELEE:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MELEE");

                    cerberus.setLastTripleAttack(null);
                    cerberus.nextPhase(expectedAttack);
                    cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.MELEE);
                    break;
                case ANIMATION_ID_LAVA:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "LAVA");

                    cerberus.nextPhase(Phase.LAVA);
                    cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.LAVA);
                    break;
                case ANIMATION_ID_GHOSTS:
                    log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "GHOSTS");

                    cerberus.nextPhase(Phase.GHOSTS);
                    cerberus.setLastGhostYellTick(gameTick);
                    cerberus.setLastGhostYellTime(System.currentTimeMillis());
                    cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.GHOSTS);
                    break;
                case ANIMATION_ID_SIT_DOWN:
                case ANIMATION_ID_STAND_UP:
                    this.cerberus = new Cerberus(cerberus.getNpc());
                    gameTick = 0;
                    lastTick = System.currentTimeMillis();
                    upcomingAttacks.clear();
                    tickTimestamps.clear();
                    tickTimestampIndex = 0;
                    cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.SPAWN);
                    break;
                case ANIMATION_ID_IDLE:
                case ANIMATION_ID_FLINCH:
                case ANIMATION_ID_RANGED:
                    break;
                case ANIMATION_ID_DEATH:
                    log.debug("dead on {}", gameTick);
                    deadArea = npc.getArea();
                    this.cerberus = null;
                    try {
                        ghostLock.lock();
                        ghosts.clear();
                    } finally {
                        ghostLock.unlock();
                    }
                    isDead = true;
                    gameTick = 0;
                    lastTick = System.currentTimeMillis();
                    upcomingAttacks.clear();
                    tickTimestamps.clear();
                    tickTimestampIndex = 0;
                    bot.addKill();
                    break;
                default:
                    log.debug("gameTick={}, animationId={} (UNKNOWN)", gameTick, animationId);
                    break;
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public int getId(Object o) {
        if (o instanceof Identifiable) {
            var identifiable = (Identifiable) o;
            return identifiable.getId();
        }
        return -1;
    }

    @Override
    public void onNpcDeath(@NotNull DeathEvent event) {
        if (!cerb.isInBossRoom()) return;
        final Npc npc = (Npc) event.getSource();
        try {
            ghostLock.lock();
            ghosts.remove(npc);
        } finally {
            ghostLock.unlock();
        }
    }

    private void sortGhosts() {
        final var c = cerberus;
        if (c == null) return;
        if (c.getLastGhostYellTick() < this.gameTick - 11) return;
        try {
            ghostLock.lock();
            if (ghosts.size() > 1) {
                /*
                 * First, sort by the southernmost ghost (e.g with lowest y).
                 * Then, sort by the westernmost ghost (e.g with lowest x).
                 * This will give use the current wave and order of the ghosts based on what ghost will attack first.
                 */
                ghosts.sort((a, b) -> ComparisonChain.start()
                        .compare(a != null && a.getServerPosition() != null ? a.getServerPosition().getY() : 0,
                                b != null && b.getServerPosition() != null ? b.getServerPosition().getY() : 0)
                        .compare(a != null && a.getServerPosition() != null ? a.getServerPosition().getX() : 0,
                                b != null && b.getServerPosition() != null ? b.getServerPosition().getX() : 0)
                        .result());
            }
        } finally {
            ghostLock.unlock();
        }
    }

    private void calculateUpcomingAttacks() {
        var cerberus = this.cerberus;
        if (cerberus == null) return;
        upcomingAttacks.clear();

        final Cerberus.Attack lastCerberusAttack = cerberus.getLastAttack();

        if (lastCerberusAttack == null) {
            return;
        }

        final int lastCerberusAttackTick = cerberus.getLastAttackTick();

        final int hp = cerberus.getHp();

        final Phase expectedPhase = cerberus.getNextAttackPhase(1, hp);
        final Phase lastPhase = cerberus.getLastAttackPhase();

        int tickDelay = 0;

        if (lastPhase != null) {
            tickDelay = lastPhase.getTickDelay();
        }
        sortGhosts();

        for (int tick = gameTick + 1; tick <= gameTick + 10; ++tick) {
            if (ghosts.size() == 3) {
                final Ghost ghost;

                if (cerberus.getLastGhostYellTick() == tick - 13) {
                    ghost = Ghost.fromNPC(ghosts.get(0));
                } else if (cerberus.getLastGhostYellTick() == tick - 15) {
                    ghost = Ghost.fromNPC(ghosts.get(1));
                } else if (cerberus.getLastGhostYellTick() == tick - 17) {
                    ghost = Ghost.fromNPC(ghosts.get(2));
                } else {
                    ghost = null;
                }

                if (ghost != null) {
                    switch (ghost.getType()) {
                        case Melee:
                            upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_MELEE));
                            break;
                        case Ranged:
                            upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_RANGED));
                            break;
                        case Magic:
                            upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_MAGIC));
                            break;
                    }

                    continue;
                }
            }

            if (expectedPhase == Phase.TRIPLE) {
                if (cerberus.getLastTripleAttack() == Cerberus.Attack.MAGIC) {
                    if (lastCerberusAttackTick + 4 == tick) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
                    } else if (lastCerberusAttackTick + 7 == tick) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
                    }
                } else if (cerberus.getLastTripleAttack() == Cerberus.Attack.RANGED) {
                    if (lastCerberusAttackTick + 4 == tick) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
                    }
                } else if (cerberus.getLastTripleAttack() == null) {
                    if (lastCerberusAttackTick + tickDelay + 2 == tick) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MAGIC));
                    } else if (lastCerberusAttackTick + tickDelay + 5 == tick) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
                    }
                }
            } else if (expectedPhase == Phase.AUTO) {
                if (lastCerberusAttackTick + tickDelay + 1 == tick) {
                    if (prayer == Prayer.PROTECT_FROM_MAGIC) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MAGIC));
                    } else if (prayer == Prayer.PROTECT_FROM_MISSILES) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
                    } else if (prayer == Prayer.PROTECT_FROM_MELEE) {
                        upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
                    }
                }
            }
        }
    }

    private void setAutoAttackPrayer() {
        if (cerberus == null) return;
        var npc = cerberus.getNpc();
        if (npc == null) return;
        int defenseStab = bot.loadouts.getEquipped().getDefenseStab();
        int defenseMagic = bot.loadouts.getEquipped().getDefenceMagic();
        int defenseRange = bot.loadouts.getEquipped().getDefenceRange();

        final int magicLvl = Skill.MAGIC.getCurrentLevel();
        final int defenseLvl = Skill.DEFENCE.getCurrentLevel();

        final int magicDefenseTotal = (int) (((double) magicLvl) * 0.7 + ((double) defenseLvl) * 0.3) + defenseMagic;
        final int rangeDefenseTotal = defenseLvl + defenseRange;

        int meleeDefenseTotal = defenseLvl + defenseStab;

        final Player player = Players.getLocal();

        if (player != null) {
            final Coordinate worldPointPlayer = player.getServerPosition();
            final Coordinate worldPointCerberus = npc.getServerPosition();

            if (worldPointPlayer.getX() < worldPointCerberus.getX() - 1
                    || worldPointPlayer.getX() > worldPointCerberus.getX() + 5
                    || worldPointPlayer.getY() < worldPointCerberus.getY() - 1
                    || worldPointPlayer.getY() > worldPointCerberus.getY() + 5) {
                meleeDefenseTotal = Integer.MAX_VALUE;
            }
        }

        if (magicDefenseTotal <= rangeDefenseTotal && magicDefenseTotal <= meleeDefenseTotal) {
            prayer = Prayer.PROTECT_FROM_MAGIC;
        } else if (rangeDefenseTotal <= meleeDefenseTotal) {
            prayer = Prayer.PROTECT_FROM_MISSILES;
        } else {
            prayer = Prayer.PROTECT_FROM_MELEE;
        }
    }

    private boolean inCerberusRegion() {
        for (final int regionId : Region.getLoadedRegionIds()) {
            if (REGION_IDS.contains(regionId)) {
                return true;
            }
        }

        return false;
    }

    public boolean isDead() {
        return isDead;
    }

    public void reset() {
        log.debug("reset");
        isDead = false;
        deadArea = null;
        this.cerberus = null;
        try {
            ghostLock.lock();
            ghosts.clear();
        } finally {
            ghostLock.unlock();
        }
        gameTick = 0;
        lastTick = System.currentTimeMillis();
        upcomingAttacks.clear();
        tickTimestamps.clear();
        tickTimestampIndex = 0;
    }

    @Override
    public void onNpcHitsplat(HitsplatEvent event) {
        if (cerberus == null || cerberus.getNpc() == null) return;
        try {
            if (getId(event.getSource()) == cerberus.getNpc().getId())
                cerberus.onHitsplat(event.getHitsplat());
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public Area getDeadArea() {
        return deadArea;
    }

    public void init() {
        if (cerberus == null) {
            var n = cerb.getCerb();
            onNpcSpawned(new NpcSpawnedEvent(n));
        }
    }
}
