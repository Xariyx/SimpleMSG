package com.xariyx.simplemsg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MessageCommand implements CommandExecutor {

    FileConfiguration messages;
    Main main;
    MSGLog log;

    public MessageCommand(Main main) {
        this.messages = main.getMessages();
        this.main = main;
        this.log = main.getMSGLog();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(main.color(messages.getString("msg.msgHelp")));
            return true;
        }

        Player playerToSendMessage = main.getPlayerFromName(args[0]);

        if (playerToSendMessage == null) {
            player.sendMessage(main.color(messages.getString("msg.playerOffline"))
                    .replace("%player%", args[0])
            );
            return true;
        }

        if(player == playerToSendMessage){
            player.sendMessage(main.color(messages.getString("msg.selfMessage")));
            return true;
        }

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < args.length; i++){
            sb.append(args[i]).append(" ");
        }

        String message = sb.toString();

        playerToSendMessage.sendMessage(main.color(messages.getString("msg.message"))
                .replace("%message%", message)
                .replace("%player%", player.getName())
        );

        try {
            log.addLogToFile(player, playerToSendMessage, message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (main.addReply(player, playerToSendMessage)
                && main.addReply(playerToSendMessage, player));
    }
}
