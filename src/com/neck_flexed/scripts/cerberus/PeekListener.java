package com.neck_flexed.scripts.cerberus;

import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

@Log4j2(topic = "PeekListener")
public class PeekListener implements ChatboxListener {
    // "Peek" "Iron Winch"
    // you get an interactable , "No adventurers are inside the cave."
    private static Pattern number = Pattern.compile("\\d+");
    private int playerCount = -1;

    public PeekListener() {
    }

    public void reset() {
        this.playerCount = -1;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        var str = messageEvent.getMessage();
        if (str.contains("Keeper of Keys")) {
            if (str.contains("No adventurers"))
                this.playerCount = 0;
            else
                this.playerCount = 1;
        }

    }

    public int getPlayerCount() {
        return playerCount;
    }

}
