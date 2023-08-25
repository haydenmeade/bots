package com.neck_flexed.scripts.common.traverse;

import com.neck_flexed.scripts.common.HouseConfig;
import com.neck_flexed.scripts.common.HouseUtil;
import com.neck_flexed.scripts.common.NeckBot;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.House;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.web.WebPathRequest;
import com.runemate.game.api.hybrid.web.vertex.objects.BasicObjectVertex;
import com.runemate.game.api.script.Execution;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Log4j2(topic = "TraverseState")
public class Traverser {
    public static final com.runemate.game.api.hybrid.location.navigation.Path.TraversalOption[] OPTS = new Path.TraversalOption[]{Path.TraversalOption.MANAGE_RUN, Path.TraversalOption.USE_DIRECT_INPUT, Path.TraversalOption.MANAGE_DISTANCE_BETWEEN_STEPS, Path.TraversalOption.MANAGE_STAMINA_ENHANCERS};
    private final @Nullable NeckBot<?, ?> bot;
    private final @Nullable HouseConfig houseConfig;
    private final Coordinate destination;
    private final @Nullable Function<Coordinate, Boolean> overrideWebPath;
    private final @Nullable TraverseMethod[] traverses;
    private final @Nullable int[] pathRegions;
    private final Coordinate startPosition;
    private final boolean useRegionPath;
    private TraverseMethod traverseMethod = null;
    private Path currentPath;
    private boolean restored = false;
    private boolean started = false;
    @Getter
    private String error = null;

    public Traverser(@Nullable NeckBot<?, ?> bot,
                     @Nullable HouseConfig houseConfig,
                     Coordinate destination,
                     int[] pathRegions,
                     @Nullable Function<Coordinate, Boolean> overrideWebPath,
                     TraverseMethod... traverses
    ) {
        this.bot = bot;
        this.houseConfig = houseConfig;
        this.destination = destination;
        this.overrideWebPath = overrideWebPath;
        this.traverses = traverses;
        var p = Players.getLocal();
        this.startPosition = p == null ? new Coordinate(0, 0, 0) : p.getServerPosition();
        this.useRegionPath = false;
        this.pathRegions = pathRegions;
    }


    public Traverser(@Nullable NeckBot<?, ?> bot,
                     @Nullable HouseConfig houseConfig,
                     Coordinate destination,
                     int[] pathRegions,
                     @Nullable Function<Coordinate, Boolean> overrideWebPath,
                     boolean useRegionPath,
                     TraverseMethod... traverses
    ) {
        this.bot = bot;
        this.houseConfig = houseConfig == null ? HouseConfig.EMPTY_OUSE : houseConfig;
        this.destination = destination;
        this.overrideWebPath = overrideWebPath;
        this.traverses = traverses;
        var p = Players.getLocal();
        this.startPosition = p == null ? new Coordinate(0, 0, 0) : p.getServerPosition();
        this.pathRegions = pathRegions;
        this.useRegionPath = useRegionPath;
    }

    public static Traverser webPathTraverser(Coordinate destination) {
        return new Traverser(null, null, destination, null, null, false);
    }

    public static boolean canRegionPath(Coordinate destination) {
        return RegionPath.buildTo(destination) != null;

    }

    public static Traverser regionPathTraverser(Coordinate destination) {
        return new Traverser(null, null, destination, null, null, true);
    }

    @NotNull
    public static Optional<@Nullable TraverseMethod> getBestTraverse(@Nullable TraverseMethod @NotNull [] traverses, HouseConfig houseConfig, Collection<SpriteItem> items) {
        return Arrays.stream(traverses).filter(t -> t != null && t.meetsRequirement(houseConfig, items)).findFirst();
    }

    @NotNull
    public static Optional<@Nullable TraverseMethod> getBestTraverse(@Nullable TraverseMethod @NotNull [] traverses, HouseConfig houseConfig) {
        return getBestTraverse(traverses, houseConfig, util.inventoryEquipmentSource());
    }

    public static boolean isInPathRegions(@NotNull Coordinate pos, int[] pathRegions, Coordinate destination) {
        return pathRegions == null
                || pathRegions.length == 0
                || Arrays.stream(pathRegions).anyMatch(i -> i == pos.getContainingRegionId())
                || destination.getContainingRegionId() == pos.getContainingRegionId();
    }

    private void start() {
        this.started = true;
    }

    public boolean executeLoop() {
        var p = Players.getLocal();
        if (p == null) return false;
        var pos = p.getServerPosition();
        if (pos == null) return false;
        if (!this.started) {
            this.start();
        }

        if (houseConfig != null && this.bot != null) {
            if (House.isInside()
                    && this.houseConfig.getPool().equals(HouseConfig.Pool.Ornate)
                    && !util.isFullyRestored()
                    && !restored) {
                this.restored = HouseUtil.restoreStatsInHouse(this.bot);
                return false;
            }
        }

        return traverse(p, pos, pathRegions, traverses, destination, overrideWebPath);
    }

    private boolean traverse(@NotNull Player p,
                             @NotNull Coordinate pos,
                             int[] pathRegions,
                             @Nullable TraverseMethod[] traverses,
                             @NotNull Coordinate target,
                             @Nullable Function<Coordinate, Boolean> overrideWebPath) {
        if (target.equals(pos)) return true;
        log.debug("traverse pos {} in region {}, start {}, start region {}", pos, pos.getContainingRegionId(), startPosition, startPosition.getContainingRegionId());

        if (Traversal.getRunEnergy() < 45 && !Traversal.isStaminaEnhanced()) {
            Traversal.drinkStaminaEnhancer();
        }
        if (!Traversal.isRunEnabled() && Traversal.getRunEnergy() > 10)
            Traversal.toggleRun();

        var inPathRegions = isInPathRegions(pos, pathRegions, destination);
        if (inPathRegions) {
            log.debug("In pathing regions");
            pathWalk(pos, target, overrideWebPath);
            return false;
        }

        if (traverseMethod == null && traverses != null) {
            var bestTraverse = getBestTraverse(traverses, houseConfig);
            if (bestTraverse.isEmpty()) {
                var msg = String.format("Unable to find a valid traverse to %s", destination);
                log.error(msg);
                this.error = msg;
                return false;
            }
            this.traverseMethod = bestTraverse.get();
        }
        if (traverseMethod != null) {
            log.debug("Traverse method {}", traverseMethod);
            traverseMethod.doTraverseLoop(this.houseConfig, this.startPosition);
            return false;
        }
        log.debug("No Traverse method found");
        pathWalk(pos, target, overrideWebPath);
        return false;
    }

    private boolean pathWalk(@NotNull Coordinate pos, @NotNull Coordinate target, @Nullable Function<Coordinate, Boolean> overrideWebPath) {
        if (overrideWebPath != null) {
            var overrideResult = overrideWebPath.apply(target);
            if (overrideResult != null && overrideResult.booleanValue()) {
                this.currentPath = null;
                return false;
            }
        }
        return pathToTarget(pos, destination);
    }

    private boolean pathToTarget(Coordinate pos, Coordinate target) {
        log.debug("Path to target");
        if (this.currentPath == null) {
            log.debug("Rebuilding path to {} from {}", target, pos);
            if (useRegionPath) {
                this.currentPath = RegionPath.buildTo(target);
            } else {
                var path = WebPathRequest
                        .builder()
                        .setDestination(target)
                        .setStart(pos)
                        .setUsingTeleports(true)
                        .build();
                this.currentPath = path;
            }
        }
        if (this.currentPath != null) {
            var nodeIsGameObject = this.currentPath.getNext() instanceof BasicObjectVertex;

            if (!this.currentPath.step(OPTS) || nodeIsGameObject) {
                Execution.delayUntil(util::playerAnimatingOrMoving, 1200);
                Execution.delayWhile(util::playerAnimatingOrMoving, 3400, 3600);
                this.currentPath = null;
            }
        } else {
            log.error("Could not generate path from {} to {}", pos, target);
        }
        return false;
    }

}