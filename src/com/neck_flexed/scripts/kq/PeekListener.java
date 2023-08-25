package com.neck_flexed.scripts.kq;

import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import lombok.extern.log4j.Log4j2;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Log4j2(topic = "PeekListener")
public class PeekListener implements ChatboxListener {
    private static final Predicate<String> matcher1 =
            Pattern.compile("^You peek down and see .* adventurers? inside the lair\\.").asMatchPredicate();

    private static final Predicate<String> matcher1_none =
            Pattern.compile("^You peek down and see no adventurers inside the lair\\.").asMatchPredicate();
    private static final Predicate<String> matcher2 =
            Pattern.compile("^You listen through the crevice but can hear no adventurers? inside the lair\\.").asMatchPredicate();
    private static final Predicate<String> matcher3 =
            Pattern.compile("^You listen through the crevice and can hear the cries of .* adventurers? inside the lair\\.").asMatchPredicate();
    private static Pattern number = Pattern.compile("\\d+");
    private int playerCount = -1;

    public void reset() {
        this.playerCount = -1;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        var m = messageEvent.getMessage();
        if (messageEvent.getType() == Chatbox.Message.Type.SERVER &&
                (matcher1.test(m)
                        || matcher2.test(m)
                        || matcher3.test(m)
                )) {
            var nm = number.matcher(m);
            if (nm.find()) {
                this.playerCount = Integer.parseInt(nm.group());
            } else if (matcher3.test(m)) {
                this.playerCount = 1;
            } else if (matcher1_none.test(m)) {
                this.playerCount = 0;
            } else if (matcher2.test(m)) {
                this.playerCount = 0;
            } else {
                this.playerCount = 1;
            }
            log.debug(String.format("See: %d players in boss room", this.playerCount));
        }
    }

    public int getPlayerCount() {
        return playerCount;
    }

}
