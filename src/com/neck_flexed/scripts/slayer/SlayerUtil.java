package com.neck_flexed.scripts.slayer;

import com.neck_flexed.scripts.common.SlayerTask;
import com.neck_flexed.scripts.common.util;
import com.runemate.game.api.hybrid.local.Varbit;
import com.runemate.game.api.hybrid.local.Varbits;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.NpcContact;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;
import com.runemate.game.api.script.Execution;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

@Log4j2
public class SlayerUtil {
    private static final int SLAYER_POINTS_ID = 4068;
    private static final Varbit SLAYER_POINTS = Varbits.load(SLAYER_POINTS_ID);

    public static int getSlayerPoints() {
        return SLAYER_POINTS.getValue();
    }

    public static boolean npcContactSlayerTaskFrom(NpcContact.Contact contact, @Nullable String startTask) {
        if (!Objects.equals(SlayerTask.getCurrent(), startTask)) {
            if (!ChatDialog.isOpen()) return true;
            util.moveTo(Players.getLocal().getServerPosition());
            Execution.delayUntil(() -> !ChatDialog.isOpen(), 600, 1200);
            return false;
        }

        if (!ChatDialog.isOpen()) {
            if (!NpcContact.isOpen()) {
                Magic.Lunar.NPC_CONTACT.activate();
                Execution.delayUntil(NpcContact::isOpen,
                        util::playerAnimating,
                        5000, 7000);
                return false;
            }
            NpcContact.cast(contact);
            Execution.delayUntil(ChatDialog::isOpen,
                    util::playerAnimating,
                    5000, 7000);
            return false;
        }
        return slayerTaskDialogue(startTask);
    }

    public static boolean slayerTaskDialogue(@Nullable String startTask) {
        if (!Objects.equals(SlayerTask.getCurrent(), startTask)) {
            if (!ChatDialog.isOpen()) return true;
            util.moveTo(Players.getLocal().getServerPosition());
            Execution.delayUntil(() -> !ChatDialog.isOpen(), 600, 1200);
            return false;
        }

        var cont = ChatDialog.getContinue();
        if (cont != null && cont.isValid()) {
            if (!cont.select()) {
                log.error("Unable to continue dialague {}", cont);
            }
            Execution.delay(300, 400);
            return false;
        }
        var opt = ChatDialog.getOption(1);
        if (opt != null && opt.isValid()) {
            if (!opt.select()) {
                log.error("Unable to select option {}", opt);
            }
            Execution.delay(300, 400);
        }
        return false;
    }

    public static boolean doesTaskMatch(String task, @NotNull String skipFor, boolean stopOnAnyBoss) {
        if (SlayerTask.currentIsBoss() && stopOnAnyBoss) return true;
        var skipList = parseSlayerCsvRegexString(skipFor);
        return task != null && Arrays.stream(skipList).anyMatch(util.checkPatternArrayAgainst(task));
    }

    private static Pattern[] parseSlayerCsvRegexString(String rawString) {
        if (rawString == null || rawString.equals("")) return new Pattern[0];

        var l = Arrays
                .stream(rawString.split(",|\n"))
                .filter(s -> s != null && !s.isBlank())
                .toArray(String[]::new);

        if (l.length == 0) return new Pattern[0];
        var pList = new ArrayList<Pattern>();
        for (var s : l) {
            if (s == null || "".equals(s)) continue;

            var s2 = getExactMatch(getPluralOrNon(s));
            s = getExactMatch(s);

            try {
                pList.add(Pattern.compile(s, Pattern.CASE_INSENSITIVE));
                pList.add(Pattern.compile(s2, Pattern.CASE_INSENSITIVE));
            } catch (Exception e) {
                log.error("Pattern compile error on '{}' -- {}", s, e);
            }
        }
        return pList.toArray(new Pattern[0]);
    }

    @NotNull
    private static String getExactMatch(String s) {
        if (!s.startsWith("^"))
            s = "^" + s;
        if (!s.endsWith("$"))
            s = s + "$";
        return s;
    }

    private static String getPluralOrNon(String s) {
        if (s.endsWith("s"))
            return s.substring(0, s.length() - 1);
        return s + "s";
    }
}
