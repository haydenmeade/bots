package com.neck_flexed.scripts.dk;

import com.neck_flexed.scripts.common.CombatStyle;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;

import java.util.List;
import java.util.stream.Collectors;

public enum King {
    Prime("Dagannoth Prime", dk.PrimeId, dk.PrimeAttackId, Prayer.PROTECT_FROM_MAGIC, CombatStyle.Ranged, 1),
    Supreme("Dagannoth Supreme", dk.SupremeId, dk.SupremeAttackId, Prayer.PROTECT_FROM_MISSILES, CombatStyle.Melee, 2),
    Rex("Dagannoth Rex", dk.RexId, dk.RexAttackId, Prayer.PROTECT_FROM_MELEE, CombatStyle.Magic, 3);

    private final String name;
    private final int id;
    private final int attackId;
    private final Prayer protect;
    private final CombatStyle attackWith;
    private final int prayPriority;

    King(String name, int id, int attackId, Prayer protect, CombatStyle attackWith, int prayPriority) {
        this.name = name;
        this.id = id;
        this.attackId = attackId;
        this.protect = protect;
        this.attackWith = attackWith;
        this.prayPriority = prayPriority;
    }

    public static King fromNpc(Npc npc) {
        if (npc == null) return null;
        return fromId(npc.getId());
    }

    public static King fromId(int id) {
        switch (id) {
            case dk.SupremeId:
                return King.Supreme;
            case dk.PrimeId:
                return King.Prime;
            case dk.RexId:
                return King.Rex;
            default:
                return null;
        }
    }

    public static List<King> getAllKings() {
        return Npcs.newQuery().ids(King.Supreme.id, King.Rex.id, King.Prime.id)
                .filter(n -> n.getAnimationId() != dk.DeathAnimation)
                .results()
                .stream()
                .map(King::fromNpc)
                .collect(Collectors.toList());
    }

    public static List<King> getAttackingKings() {
        return Npcs.newQuery().ids(King.Supreme.id, King.Rex.id, King.Prime.id)
                .filter(n -> n.getAnimationId() != dk.DeathAnimation
                        && n.getTarget() != null
                        && n.getTarget().equals(Players.getLocal()))
                .results()
                .stream()
                .map(King::fromNpc)
                .collect(Collectors.toList());
    }

    public static King getNearestKing() {
        return fromNpc(Npcs.newQuery().ids(King.Supreme.id, King.Rex.id, King.Prime.id)
                .filter(n -> n.getAnimationId() != dk.DeathAnimation)
                .results()
                .nearest());
    }

    public double distanceTo() {
        var p = Players.getLocal();
        if (p == null) return 0;
        var npc = getNpc();
        if (npc == null) return 0;
        return p.distanceTo(npc);
    }

    public CombatStyle getAttackWith() {
        return attackWith;
    }

    public Prayer getProtect() {
        return protect;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Npc getNpc() {
        return Npcs.newQuery().ids(this.id).names(this.name).results().first();
    }

    public int getAttackId() {
        return attackId;
    }

    public int getPrayPriority() {
        return prayPriority;
    }
}
