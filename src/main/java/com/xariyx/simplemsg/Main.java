package com.xariyx.simplemsg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    Map<Player, Player> replayList = new HashMap<>();
    private FileConfiguration messages;


    @Override
    public void onEnable() {
        createCustomConfig();
        registerCommands();
        registerListeners();
    }



    @Override
    public void onDisable() {

    }


    public FileConfiguration getMessages() {
        return this.messages;
    }

    private void registerCommands() {
        registerCommand("message", new MessageCommand(this));
        registerCommand("replay", new ReplayCommand(this));
    }


    private void registerCommand(String commandName, CommandExecutor commandExecutor) {
        PluginCommand party = this.getCommand(commandName);
        if (party != null) {
            party.setExecutor(commandExecutor);
        }
    }

    private void registerListeners() {
        registerListener(new LeaveListener(this));
    }


    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }


    private void createCustomConfig() {
        File customConfigFile = new File(getDataFolder(), "messages.yml");
        if (!customConfigFile.exists()) {
            if (customConfigFile.getParentFile().mkdirs()) {
                System.out.println(customConfigFile + " was created!");
            }
            saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();
        try {
            messages.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public boolean addReply(Player player, Player playerToPut) {
        try {
            replayList.put(player, playerToPut);
            return true;
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    public boolean removeReplyKey(Player playerToRemove) {
        try {
            replayList.remove(playerToRemove);
            return true;
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    public boolean removeReplyValue(Player playerToRemove) {
        try {
            replayList.forEach((key, value) -> {
                        if (value == playerToRemove) {
                            replayList.remove(key);
                        }
                    }
            );
        } catch (
                NullPointerException ignored) {
            return false;
        }
        return true;
    }

    public Player getPlayerFromName(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public String color(String toColor){
        return ChatColor.translateAlternateColorCodes('&', toColor);
    }


}
