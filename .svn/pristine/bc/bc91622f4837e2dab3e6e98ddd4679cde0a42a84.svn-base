package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.queries.results.InteractableQueryResults;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.CachingDistanceComparator;
import com.runemate.game.api.hybrid.util.Sort;
import com.runemate.game.api.hybrid.util.calculations.CommonMath;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class CoordinateQueryResults extends InteractableQueryResults<Coordinate, CoordinateQueryResults> {
    public CoordinateQueryResults(final Collection<Coordinate> results) {
        super(results);
    }

    public CoordinateQueryResults(
            final Collection<Coordinate> results,
            ConcurrentMap<String, Object> cache
    ) {
        super(results, cache);
    }

    @Override
    protected CoordinateQueryResults get() {
        return this;
    }

    /**
     * Calls sort(Comparator) with a Comparator that will sort the list by distance from the local player (nearest first)
     */
    public final CoordinateQueryResults sortByDistance() {
        return sortByDistanceFrom(Players.getLocal());
    }

    public final CoordinateQueryResults sortByDistance(Distance.Algorithm algorithm) {
        return sortByDistanceFrom(Players.getLocal(), algorithm);
    }

    /**
     * Calls sort(Comparator) with a Comparator that will sort the list by distance from the local player (nearest first)
     */
    public final CoordinateQueryResults sortByDistanceFrom(Locatable center) {
        return sort(new CachingDistanceComparator(center, Distance.Algorithm.MANHATTAN));
    }

    public final CoordinateQueryResults sortByDistanceFrom(
            Locatable center,
            Distance.Algorithm algorithm
    ) {
        return sort(new CachingDistanceComparator(center, algorithm));
    }

    /**
     * Gets the nearest entity to the local player.
     * If two or more entities are tied for the place of nearest then one will be selected according to the active player sense profile.
     */
    @Nullable
    public final Coordinate nearest() {
        return nearest(Distance.Algorithm.MANHATTAN);
    }

    @Nullable
    public final Coordinate nearest(Distance.Algorithm algorithm) {
        if (size() <= 1) {
            return first();
        }
        return nearestTo(Players.getLocal(), algorithm);
    }

    /**
     * Gets the nearest entity to the given locatable.
     * If two or more entities are tied for the place of nearest then one will be selected according to the active player sense profile.
     */
    @Nullable
    public final Coordinate nearestTo(final Locatable locatable) {
        return nearestTo(locatable, Distance.Algorithm.MANHATTAN);
    }

    @Nullable
    public final Coordinate nearestTo(final Locatable locatable, Distance.Algorithm algorithm) {
        if (size() <= 1) {
            return first();
        }
        if (locatable == null) {
            return null;
        }
        var sorted = Sort.byDistanceFrom(locatable, this.backingList, algorithm);
        var equidistant = new ArrayList<Coordinate>(3);
        double minimum = -1;
        HashMap<String, Object> cache = new HashMap<>(4);
        for (Coordinate entity : sorted) {
            double distance = Distance.between(locatable, entity, algorithm, cache);
            if (minimum == -1) {
                minimum = distance;
            } else if (minimum != distance) {
                break;
            }
            equidistant.add(entity);
        }
        Coordinate preferred = null;
        minimum = -1;
        int tiebreaker = PlayerSense.getAsInteger(PlayerSense.Key.DISTANCE_ANGLE_TIE_BREAKER);
        for (Coordinate entity : equidistant) {
            int distance = CommonMath.getDistanceBetweenAngles(
                    tiebreaker,
                    CommonMath.getAngleOf(locatable, entity)
            );
            if (minimum == -1 || distance < minimum) {
                minimum = distance;
                preferred = entity;
            }
        }
        return preferred;
    }

    /**
     * Gets the furthest entity to the local player.
     * If two or more entities are tied for the place of furthest then one will be selected according to the active player sense profile.
     */
    @Nullable
    public final Coordinate furthest() {
        if (size() <= 1) {
            return last();
        }
        return furthestFrom(Players.getLocal());
    }

    @Nullable
    public final Coordinate furthestFrom(final Locatable locatable) {
        return furthestFrom(locatable, Distance.Algorithm.MANHATTAN);
    }

    @Nullable
    public final Coordinate furthestFrom(final Locatable locatable, Distance.Algorithm algorithm) {
        if (size() <= 1) {
            return last();
        }
        if (locatable == null) {
            return null;
        }
        var sorted = Sort.byDistanceFrom(locatable, this.backingList);
        Collections.reverse(sorted);
        var equidistant = new ArrayList<Coordinate>(3);
        double maximum = -1;
        HashMap<String, Object> cache = new HashMap<>();
        for (Coordinate entity : sorted) {
            double distance = Distance.between(locatable, entity, algorithm, cache);
            if (maximum == -1) {
                maximum = distance;
            } else if (maximum != distance) {
                break;
            }
            equidistant.add(entity);
        }
        int tiebreaker;
        if (equidistant.size() > 1 && locatable.equals(Players.getLocal())) {
            var visible = new ArrayList<Coordinate>(3);
            tiebreaker = PlayerSense.getAsInteger(PlayerSense.Key.DISTANCE_VISIBILITY_TIE_BREAKER);
            for (Coordinate entity : equidistant) {
                if (entity.getVisibility() >= tiebreaker) {
                    visible.add(entity);
                }
            }
            if (!visible.isEmpty()) {
                equidistant = visible;
            }
        }
        tiebreaker = PlayerSense.getAsInteger(PlayerSense.Key.DISTANCE_ANGLE_TIE_BREAKER);
        Coordinate preferred = null;
        double minimum = -1;
        for (Coordinate entity : equidistant) {
            int distance = CommonMath.getDistanceBetweenAngles(
                    tiebreaker,
                    CommonMath.getAngleOf(locatable, entity)
            );
            if (minimum == -1 || distance < minimum) {
                minimum = distance;
                preferred = entity;
            }
        }
        return preferred;
    }
}
