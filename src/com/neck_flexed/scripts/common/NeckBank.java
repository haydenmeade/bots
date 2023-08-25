package com.neck_flexed.scripts.common;

import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.input.direct.MenuAction;
import com.runemate.game.api.hybrid.local.Rune;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.Items;
import com.runemate.game.api.hybrid.util.Regex;
import com.runemate.game.api.osrs.local.AchievementDiary;
import com.runemate.game.api.script.Execution;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2(topic = "NeckBank")
@RequiredArgsConstructor
public class NeckBank {

    private static final String[] DEFAULT_STAFF_PATTERNS = new String[]{"Staff of {}", "{} battlestaff", "Mystic {} staff"};
    private static final Pattern TOME_OF_FIRE = Regex.getPatternForExactString("Tome of fire");
    private static final Pattern TOME_OF_WATER = Regex.getPatternForExactString("Tome of water");
    private static final Pattern KODAI_WAND = Regex.getPatternForExactString("Kodai wand");
    private static long lastHerbEmpty = 0;
    private final NeckBot<?, ?> bot;
    private int loopCount = 0;

    public static Map<Pattern, Integer> dramenStaffIfNoAchievementDiary() {
        return AchievementDiary.LUMBRIDGE.isEliteComplete()
                ? Collections.emptyMap()
                : toMap(1, items.dramenStaff);
    }

    public static @NotNull Map<Pattern, Integer> toMap(int amount, String... item) {
        var p = Arrays.stream(item).map(i -> Pattern.compile("^" + i + "$", Pattern.CASE_INSENSITIVE)).toArray(Pattern[]::new);
        return toMap(amount, p);
    }

    public static @NotNull Map<Pattern, Integer> toMap(int amount, Pattern... item) {
        var map = new HashMap<Pattern, Integer>();
        for (var p : item) {
            map.put(p, amount);
        }
        return map;
    }

    public static @NotNull Map<Pattern, Integer> toMap(int i, String name) {
        return toMap(i, Pattern.compile(name));
    }

    public static @NotNull Map<Pattern, Integer> getBoostsNeeded(int amount, boolean anyDose, Boost... boosts) {
        var map = new HashMap<Pattern, Integer>();
        if (amount == 0) return map;
        for (var b : boosts) {
            if (Objects.equals(b, Boost.None)) continue;
            var patterns = anyDose ? b.patternAny() : b.patternFull();
            for (var p : patterns) {
                map.put(p, amount);
            }
        }
        return map;
    }

    public static String getStringOf(Map<Pattern, Integer> bank) {
        var b = new StringBuilder();
        b.append(" ");
        for (var kv : bank.entrySet()) {
            b.append(kv.getKey().toString());
            b.append(" (");
            b.append(kv.getValue());
            b.append(");;");
        }
        return b.toString();
    }

    @NotNull
    private static Pattern[] getExceptWithWithdraw(Pattern[] except, Map<Pattern, Integer> withdraw, boolean includeNonStackableWithdrawAll) {
        var exceptWithWithdraw = new ArrayList<Pattern>(Arrays.asList(except));
        exceptWithWithdraw.add(Pattern.compile("^Lamp$"));
        for (var kv : withdraw.entrySet()) {
            if (kv.getValue() != 0) {
                exceptWithWithdraw.add(kv.getKey());
            } else if (itemStacks(kv)) {
                // add stackable withdraw alls.
                exceptWithWithdraw.add(kv.getKey());
            } else if (includeNonStackableWithdrawAll) {
                exceptWithWithdraw.add(kv.getKey());
            }
        }
        return exceptWithWithdraw.toArray(new Pattern[0]);
    }

    @NotNull
    private static Map<Pattern, Integer> getWithdraw(Map<Pattern, Integer>[] withdrawArrays) {
        return Arrays.stream(withdrawArrays)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::max));
    }

    private static void emptyHerbSack(DI di) {
        if (System.currentTimeMillis() - lastHerbEmpty < 120000) return;
        var hs = Inventory.getItems(items.herbSack).first();

        if (hs != null) {
            lastHerbEmpty = System.currentTimeMillis();
            var def = hs.getDefinition();
            if (def != null && def.getInventoryActions().contains("Empty"))
                di.send(MenuAction.forSpriteItem(hs, "Empty"));
        }
        var gb = Inventory.getItems(items.gemBag).first();
        if (gb != null) {
            lastHerbEmpty = System.currentTimeMillis();
            var def = gb.getDefinition();
            if (def != null && def.getInventoryActions().contains("Empty"))
                di.send(MenuAction.forSpriteItem(gb, "Empty"));
        }
    }

    protected static int getQuantityOnMe(Map.Entry<Pattern, Integer> kv) {
        var rune = getRune(kv.getKey());
        if (rune != null) {
            return rune.getQuantity();
        } else {
            return Items.getQuantity(util.inventoryEquipmentSource(), kv.getKey());
        }
    }

    protected static Rune getRune(Pattern key) {
        return Arrays.stream(Rune.values()).filter(
                r -> key.matcher(r.getName()).find()
        ).findFirst().orElse(null);
    }

    private static boolean deposit(NeckBot<?, ?> bot, DiBank diBank, Pattern key, int quantity) {
        if (!Inventory.contains(key)) return true;
        var r = diBank.deposit(key, quantity);
        bot.updateStatus(String.format("Deposit %s %s, result %s", quantity, key, r));
        return r;
    }

    private static boolean withdraw(NeckBot<?, ?> bot, DiBank diBank, Pattern key, int quantity) {
        var item = Bank.getItems(key).first();
        if (item == null) return false;
        var r = diBank.withdraw(item, quantity);
        bot.updateStatus(String.format("Withdrawing %s %s, result %s", quantity, key, r));
        return r;
    }

    private static boolean itemStacks(Map.Entry<Pattern, Integer> kv) {
        if (isRune(kv)) return true;
        var item = Inventory.contains(kv.getKey())
                ? Inventory.getItems(kv.getKey()).first()
                : Equipment.contains(kv.getKey())
                ? Equipment.getItems(kv.getKey()).first()
                : Bank.getItems(kv.getKey()).first();
        if (item != null) {
            var def = item.getDefinition();
            return def != null && (def.stacks() || def.isNoted());
        }
        return false;
    }

    private static boolean isRune(Map.Entry<Pattern, Integer> kv) {
        var r = getRune(kv.getKey());
        if (r != null) return true;
        return false;
    }

    public static List<SpriteItem> getItemSourceFromBank(ArrayList<Map<Pattern, Integer>> withdrawArrays) {
        var withdraw = withdrawArrays.stream()
                .flatMap(m -> m.entrySet().stream())
                .map(Map.Entry::getKey)
                .distinct()
                .collect(Collectors.toList());

        var src = util.inventoryEquipmentBankSource();
        return withdraw.stream().map(p -> util.getItem(src, p)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static boolean hasInfiniteSource(List<SpriteItem> items, Rune r) {
        return items.stream().anyMatch(
                (item) -> {
                    ItemDefinition definition = item.getDefinition();
                    if (definition == null) {
                        return false;
                    } else {
                        String name = definition.getName();
                        if ("null".equals(name)) {
                            return false;
                        } else {

                            if (checkRuneAgainstStaff(r, name)) return true;
                            for (var replacingRune : r.getReplacingRunes()) {
                                if (checkRuneAgainstStaff(replacingRune, name)) return true;
                            }

                            if (Rune.FIRE.equals(r)) {
                                return TOME_OF_FIRE.matcher(name).find();
                            } else if (!Rune.WATER.equals(r)) {
                                return false;
                            } else {
                                return TOME_OF_WATER.matcher(name).find() || KODAI_WAND.matcher(name).find();
                            }
                        }
                    }
                }
        );
    }

    private static boolean checkRuneAgainstStaff(Rune r, String name) {
        for (int i = 0; i < DEFAULT_STAFF_PATTERNS.length; ++i) {
            String template = DEFAULT_STAFF_PATTERNS[i];
            if (name.equalsIgnoreCase(template.replace("{}", r.name()))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEm(Map<Pattern, Integer> withdraw) {
        var check = true;
        for (var kv : withdraw.entrySet()) {
            if (!check) return false;
            if (kv.getValue() != 0) {
                check = getQuantityOnMe(kv) == kv.getValue();
                if (!check)
                    log.debug("Check {} is failed, wanted {}, have {}", kv.getKey(), kv.getValue(), getQuantityOnMe(kv));
            } else if (itemStacks(kv)) {
                check = getQuantityOnMe(kv) > 0;
                if (!check)
                    log.debug("Check {} is failed, wanted {}, have {}", kv.getKey(), kv.getValue(), getQuantityOnMe(kv));
            }
        }
        return check;
    }

    public boolean checkBankCompleted(Pattern[] except, Map<Pattern, Integer>... withdrawArrays) {
        // delay a tick for DI to process.
        Execution.delay(650, 750);
        var withdraw = getWithdraw(withdrawArrays);
        var check = true;
        for (var kv : withdraw.entrySet()) {
            if (!check) return false;
            if (kv.getValue() != 0) {
                check = getQuantityOnMe(kv) == kv.getValue();
                if (!check) {
                    var msg = String.format("Check %s is failed, wanted %s, have %s", kv.getKey(), kv.getValue(), getQuantityOnMe(kv));
                    log.debug(msg);
                    if (loopCount > 15) {
                        bot.startPauseAndEndBotTimeout(msg);
                    }
                }
            } else if (itemStacks(kv)) {
                check = getQuantityOnMe(kv) > 0;
                if (!check) {
                    var msg = String.format("Check %s is failed, wanted %s, have %s", kv.getKey(), kv.getValue(), getQuantityOnMe(kv));
                    log.debug(msg);
                    if (loopCount > 15) {
                        bot.startPauseAndEndBotTimeout(msg);
                    }
                }
            } else {
                // non-stackable withdraw all
                check = Inventory.getEmptySlots() == 0 && Inventory.contains(kv.getKey());
                if (!check) {
                    var msg = String.format("Check %s is failed, wanted all, have %s", kv.getKey(), kv.getValue(), getQuantityOnMe(kv));
                    log.debug(msg);
                    if (loopCount > 15) {
                        bot.startPauseAndEndBotTimeout(msg);
                    }
                }
            }
        }
        var checkAll = getExceptWithWithdraw(except, withdraw, true);

        var unwanted = util.inventoryEquipmentSource().stream()
                .filter(t -> !Items.getNamePredicate(checkAll).test(t))
                .collect(Collectors.toList());
        if (unwanted.size() > 0) {
            check = false;
            var msg = String.format("Check unwanted is failed, have %s", unwanted.toString());
            log.debug(msg);
            if (loopCount > 15) {
                bot.startPauseAndEndBotTimeout(msg);
            }
        }
        log.debug("Check em {} ", check);
        return check;
    }

    public boolean doBankLoop(Pattern[] except, Map<Pattern, Integer>... withdrawArrays) {
        return doBankLoop(null, except, withdrawArrays);
    }

    public boolean doBankLoop(@Nullable Callable<Boolean> afterDepositCallback, Pattern[] except, Map<Pattern, Integer>... withdrawArrays) {
        if (!Bank.isOpen()) return false;
        var src = util.inventoryEquipmentBankSource();
        bot.setItemCache(src);
        var diBank = new DiBank(bot.di);
        if (!diBank.openMainTab()) return false;
        loopCount++;
        log.debug("Loop: {}", loopCount);
        if (loopCount > 25) {
            bot.startPauseAndEndBotTimeout("Unable to bank, reached loop limit of 25");
            return false;
        }

        var withdraw = getWithdraw(withdrawArrays);
        log.debug("Withdraw:  " + getStringOf(withdraw));
        log.debug("Except:  " + Arrays.toString(except));

        Pattern[] exceptWithWithdraw = getExceptWithWithdraw(except, withdraw, false);

        bot.updateStatus("Deposit all except some...");
        if (!diBank.depositAllExcept(exceptWithWithdraw))
            return false;

        if (afterDepositCallback != null) {
            try {
                var r = afterDepositCallback.call();
                if (r != null && !r.booleanValue()) return false;
            } catch (Exception e) {
                log.error(e);
            }
        }

        emptyHerbSack(bot.di);

        for (var kv : withdraw.entrySet()) {
            var q = getQuantityOnMe(kv);
            if (q < kv.getValue() || (kv.getValue() == 0 && itemStacks(kv))) {
                if (kv.getValue() != 0) {
                    var withdrawQuantity = kv.getValue() - q;
                    if (Bank.getQuantity(kv.getKey()) >= withdrawQuantity) {
                        var r = withdraw(bot, diBank, kv.getKey(), withdrawQuantity);
                        if (!r) return false;
                    } else {
                        if (Execution.delayUntil(() -> getQuantityOnMe(kv) > q, 1200, 1800))
                            return false;
                        var msg = String.format("Out of %s, wanted %s, have %s, in Bank: %s", kv.getKey(), kv.getValue(), getQuantityOnMe(kv), Bank.getQuantity(kv.getKey()));
                        log.error(msg);
                        bot.startPauseAndEndBotTimeout(msg);
                        return false;
                    }
                } else if (itemStacks(kv) && Bank.contains(kv.getKey())) {
                    var r = withdraw(bot, diBank, kv.getKey(), kv.getValue());
                    if (!r) return false;
                } else {
                    log.debug("Withdraw all delay non stackable: {}", kv.getKey());
                }
            } else if (q > kv.getValue()) {
                deposit(bot, diBank, kv.getKey(), q - kv.getValue());
                return false;
            } else {
                //log.debug("q {} k {} v {}", q, kv.getKey(), kv.getValue());
            }
        }


        // check
        boolean check = checkEm(withdraw);

        if (check) {
            for (var kv : withdraw.entrySet()) {
                // non stackable withdraw
                if (kv.getValue() == 0 && !itemStacks(kv)) {
                    if (!Bank.contains(kv.getKey()) && Inventory.getEmptySlots() > 0) {
                        var msg = String.format("Out of %s", kv.getKey());
                        bot.startPauseAndEndBotTimeout(msg);
                    }
                    return withdraw(bot, diBank, kv.getKey(), 0);
                }
            }
        }

        return check;
    }

}
