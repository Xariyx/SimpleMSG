package com.xariyx.simplemsg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ReplayCommand implements CommandExecutor {

    FileConfiguration messages;
    Main main;

    public ReplayCommand(Main main) {
        this.messages = main.getMessages();
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(main.color(messages.getString("msg.help")));
            return true;
        }

        Player playerToSendMessage = main.replayList.get(player);

        if (player == playerToSendMessage) {
            player.sendMessage(main.color(messages.getString("msg.selfMessage")));
            return true;
        }

        if (playerToSendMessage == null){
            player.sendMessage(main.color(messages.getString("msg.noReply")));
            return true;
        }

        StringBuilder sb = new StringBuilder();

        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        String message = sb.toString();

        playerToSendMessage.sendMessage(main.color(messages.getString("msg.message"))
                .replace("%message%", message)
                .replace("%player%", player.getName())
        );

        return (main.addReply(player, playerToSendMessage)
                && main.addReply(playerToSendMessage, player));


    }
}
