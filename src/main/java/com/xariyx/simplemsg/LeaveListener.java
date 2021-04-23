package com.xariyx.simplemsg;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    private final Main main;

    public LeaveListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void playerLeaveListener(PlayerQuitEvent event) {
        Player player = event.getPlayer();


        if (!(main.removeReplyKey(player) && main.removeReplyValue(player))) {
            System.err.println("Exception at removing player from reply list at player: " + player.getName());
        }

    }


}
