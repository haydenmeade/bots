package com.neck_flexed.scripts.slayer;

import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.VarpID;
import com.runemate.game.api.hybrid.local.Varps;

public enum SlayerUnlock {
    // Copied from enum 834 in the cache
    // enum 854 contains if you can disable the unlock
    GARGOYLE_SMASHER(0),
    SLUG_SALTER(1),
    REPTILE_FREEZER(2),
    SHROOM_SPRAYER(3),
    DARK_BEAST_EXTEND(4),
    SLAYER_HELMET(5),
    SLAYER_RINGS(6),
    BROADER_FLETCHING(7),
    ANKOU_EXTEND(8),
    SUQAH_EXTEND(9),
    BLACK_DRAGON_EXTEND(10),
    METAL_DRAGON_EXTEND(11),
    SPIRITUAL_MAGE_EXTEND(12),
    ABYSSAL_DEMON_EXTEND(13),
    BLACK_DEMON_EXTEND(14),
    GREATER_DEMON_EXTEND(15),
    MITHRIL_DRAGON_UNLOCK(16),
    AVIANSIES_ENABLE(17),
    TZHAAR_ENABLE(18),
    BOSS_ENABLE(19),
    BLOODVELD_EXTEND(20),
    ABERRANT_SPECTRE_EXTEND(21),
    AVIANSIES_EXTEND(22),
    MITHRIL_DRAGON_EXTEND(23),
    CAVE_HORROR_EXTEND(24),
    DUST_DEVIL_EXTEND(25),
    SKELETAL_WYVERN_EXTEND(26),
    GARGOYLE_EXTEND(27),
    NECHRYAEL_EXTEND(28),
    CAVE_KRAKEN_EXTEND(29),
    LIZARDMEN_ENABLE(30),
    KBD_SLAYER_HELM(31),
    KALPHITE_QUEEN_SLAYER_HELM(32),
    ABYSSAL_DEMON_SAYER_HELM(33),
    RED_DRAGON_ENABLE(34),
    SUPERIOR_ENABLE(35, 5362),
    SCABARITE_EXTEND(36),
    MITHRIL_DRAGON_NOTES(37),
    SKOTIZO_SLAYER_HELM(38),
    FOSSIL_ISLAND_WYVERN_EXTEND(39),
    ADAMANT_DRAGON_EXTEND(40),
    RUNE_DRAGON_EXTEND(41),
    VORKATH_SLAYER_HELM(42),
    FOSSIL_ISLAND_WYVERN_DISABLE(43, 6251),
    GROTESQUE_GUARDIAN_DOUBLE_COUNT(44),
    HYDRA_SLAYER_HELM(45),
    BASILISK_EXTEND(46),
    BASILISK_UNLOCK(47),
    OLM_SLAYER_HELM(48),
    VAMPYRE_EXTEND(49),
    VAMPYRE_UNLOCK(50);

    private final int toggleVarbit;

    SlayerUnlock(int index) {
        assert index == ordinal();
        this.toggleVarbit = -1;
    }

    SlayerUnlock(int index, int varbitid) {
        assert index == ordinal();
        this.toggleVarbit = varbitid;
    }

    /**
     * @return true if this unlock is bought
     */
    public boolean isOwned() {
        var varpid = ordinal() > 32 ? VarpID.SLAYER_UNLOCK_2 : VarpID.SLAYER_UNLOCK_1;
        var varp = Varps.getAt(varpid.getId()).getValue();
        return (varp & (1 << (ordinal() % 32))) != 0;
    }

    /**
     * @return true if this unlock is bought and enabled
     */
    public boolean isEnabled() {
        if (isOwned()) {
            if (toggleVarbit == -1) {
                return true;
            }
            var varbit = Varbits.load(toggleVarbit);
            if (varbit == null) return true;
            return varbit.getValue() == 0;
        }
        return false;
    }
}
