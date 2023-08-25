package com.neck_flexed.scripts.common.loadout;

import com.neck_flexed.scripts.common.CombatStyle;
import com.neck_flexed.scripts.common.NeckBot;
import com.runemate.game.api.hybrid.EquipmentLoadout;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2(topic = "Loadouts")
public class Loadouts {
    private final List<Loadout> loadouts;
    @Getter
    private final Collection<LoadoutOverrider> loadoutOverriders = ConcurrentHashMap.newKeySet();
    private Loadout current;
    private Loadout spec;
    @Getter
    private boolean currentIsDirty;

    public Loadouts(List<Loadout> loadouts) {
        this.loadouts = loadouts;
        this.spec = getSpec();
        currentIsDirty = true;
    }

    @SafeVarargs
    public static Loadouts fromEquipmentLoadouts(NeckBot<?, ?> bot, Triple<EquipmentLoadout, Loadout.LoadoutRole, String>... _loadouts) {
        if (_loadouts == null) return null;
        return new Loadouts(Arrays.stream(_loadouts)
                .parallel()
                .filter(kv -> kv.getLeft() != null && !kv.getLeft().isEmpty())
                .map(kv -> {
                    try {
                        return bot.getPlatform().invokeAndWait(() -> Loadout.fromEquipmentLoadout(kv.getLeft(), kv.getMiddle(), kv.getRight()));
                    } catch (ExecutionException | InterruptedException e) {
                        log.error(e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public void addLoadoutOverrider(LoadoutOverrider loadoutOverrider) {
        this.loadoutOverriders.add(loadoutOverrider);
        this.currentIsDirty = true;
    }

    public void clearLoadoutOverrider() {
        this.loadoutOverriders.clear();
    }

    public Loadout getSpecLoadout() {
        return spec;
    }

    public List<Loadout> get() {
        return loadouts;
    }

    private Loadout getSpec() {
        return loadouts.stream().filter(Loadout::isSpecLoadout).findFirst().orElse(null);
    }


    public Loadout getForName(Enum name) {
        return loadouts.stream().filter(l -> l.getName().equals(name.toString())).findFirst().orElse(null);
    }

    public Loadout getDharoks() {
        return loadouts.stream().filter(Loadout::isDharoks).findFirst().orElse(null);
    }

    public Loadout getForName(String name) {
        return loadouts.stream().filter(l -> l.getName().equals(name)).findFirst().orElse(null);
    }

    public void invalidateCurrent() {
        currentIsDirty = true;
    }

    public void equip(Loadout l) {
        this.equip(l, false);
    }

    public void equip(Loadout l, boolean force) {
        if (l == null) return;
        var res = l.equip(current, force || currentIsDirty, this.loadoutOverriders);
        if (res != null) {
            current = res;
            currentIsDirty = false;
        } else {
            currentIsDirty = true;
        }
    }

    public void equipStyle(CombatStyle style) {
        equip(getForStyle(style));
    }

    public void equipStyle(CombatStyle style, boolean force) {
        equip(getForStyle(style), force);
    }

    public synchronized Loadout getEquipped() {
        if (this.current == null) {
            // happens if loadouts are refreshed without pausing
            setCurrentFromEquipmentOrEquip(getAnyCombat());
        }
        return this.current;
    }

    public Loadout getAnyCombat() {
        return getForRole(Loadout.LoadoutRole.Combat);
    }

    public Loadout getForStyle(CombatStyle style) {
        return loadouts.stream()
                .filter(l -> l.getStyle().equals(style) && !l.isSpecLoadout())
                .findFirst().orElse(null);
    }

    public Loadout getForRole(Loadout.LoadoutRole role) {
        return loadouts.stream()
                .filter(l -> l.getRole().equals(role))
                .findFirst().orElse(null);
    }

    public synchronized void setCurrentFromEquipmentOrEquip(Loadout defaultTo) {
        current = null;
        for (var l : loadouts) {
            var isLoadout = l.getLoadout().loadout();
            boolean check = true;
            for (var s : Equipment.Slot.values()) {
                var item = Equipment.getItemIn(s);
                var d = item == null ? null : item.getDefinition();
                if (!isLoadout.test(d)) {
                    check = false;
                    break;
                }
            }
            if (check) {
                log.debug("Found loadout, equiping: " + l.getStyle());
                current = l;
                break;
            }
        }
        if (current == null && defaultTo != null) {
            current = defaultTo;
            defaultTo.equip(null, true, this.loadoutOverriders);
            log.debug("No loadout, defaulting to: " + defaultTo.getStyle());
        }
        if (current == null) {
            log.error("No current loadout able to be set");
            if (this.loadouts.size() > 0) {
                this.loadouts.get(0).equip(null, true, this.loadoutOverriders);
                log.debug("Equipping 1st loadout");
            }
        }
    }

    public Pattern[] getItemFilter() {
        return this.loadouts.stream().map(l -> l.getBank(
                this.loadoutOverriders
        ).keySet()).flatMap(Collection::stream).toArray(Pattern[]::new);
    }
}
