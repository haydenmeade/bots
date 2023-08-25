package com.neck_flexed.scripts.hydra;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Projectile;
import com.runemate.game.api.hybrid.entities.SpotAnimation;
import com.runemate.game.api.hybrid.region.SpotAnimations;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;

@Getter
@Log4j2(topic = "Hydra")
@RequiredArgsConstructor
public class Hydra {
    private static final int MAX_HP = 1100;
    @Nullable
    private final Npc npc;
    private HydraPhase phase = HydraPhase.POISON;
    private AttackStyle nextAttack = AttackStyle.MAGIC;
    private AttackStyle lastAttack = AttackStyle.MAGIC;

    @Setter
    private boolean immunity = true;
    @Setter
    private Projectile flameProjectile;
    private int nextSpecial = 3;
    private int attackCount;
    private int nextSwitch = phase.getAttacksPerSwitch();

    public boolean getImmunity() {
        return immunity;
    }

    public boolean isSpecialAttack() {
        return getNextSpecialRelative() == 0;
    }

    public SpotAnimation getNearestLightning() {
        final HydraPhase phase = getPhase();
        if (!phase.equals(HydraPhase.LIGHTNING)) return null;
        return SpotAnimations.newQuery()
                .ids(c.LIGHTNING_ID)
                .results()
                .nearest();
    }

    public void setNextSpecial() {
        nextSpecial += 9;
    }

    public int getNextSpecialRelative() {
        return nextSpecial - attackCount;
    }

    public void changePhase(final HydraPhase hydraPhase) {
        phase = hydraPhase;
        nextSpecial = 3;
        attackCount = 0;
        immunity = true;
        log.debug(String.format("Phase change to %s", hydraPhase));

        if (hydraPhase == HydraPhase.ENRAGED) {
            immunity = false;
            switchStyles();
            nextSwitch = phase.getAttacksPerSwitch();
        }
    }

    public void handleProjectile(final int projectileId) {
        var a = AttackStyle.fromProjectileId(projectileId);
        log.debug(String.format("handleProjectile %s", a));
        if (projectileId != nextAttack.getProjectileID()) {
            if (projectileId == lastAttack.getProjectileID()) {
                // If the current attack isn't what was expected and we accidentally counted 1 too much
                return;
            }

            // If the current attack isn't what was expected and we should have switched prayers
            switchStyles();

            nextSwitch = phase.getAttacksPerSwitch() - 1;
        } else {
            nextSwitch--;
        }

        lastAttack = nextAttack;
        attackCount++;

        if (nextSwitch <= 0) {
            switchStyles();
            nextSwitch = phase.getAttacksPerSwitch();
        }
    }

    public int getHpUntilPhaseChange() {
        return Math.max(0, getHp() - phase.getHpThreshold());
    }

    private void switchStyles() {
        nextAttack = lastAttack == Hydra.AttackStyle.MAGIC
                ? Hydra.AttackStyle.RANGED
                : Hydra.AttackStyle.MAGIC;
        log.debug(String.format("Switch styles to %s", nextAttack));
    }

    public int getHp() {
        if (npc == null) return MAX_HP;
        var h = npc.getHealthGauge();
        if (h == null) return MAX_HP;
        return (int) ((double) (h.getPercent()) / 100.0 * MAX_HP);
    }

    @Getter
    @RequiredArgsConstructor
    public enum AttackStyle {
        MAGIC(ProjectileID.HYDRA_MAGIC, Prayer.PROTECT_FROM_MAGIC),
        RANGED(ProjectileID.HYDRA_RANGED, Prayer.PROTECT_FROM_MISSILES);

        private final int projectileID;
        private final Prayer prayer;

        public static AttackStyle fromProjectileId(int id) {
            if (id == ProjectileID.HYDRA_MAGIC)
                return MAGIC;
            if (id == ProjectileID.HYDRA_RANGED)
                return RANGED;
            return null;
        }

    }
}
