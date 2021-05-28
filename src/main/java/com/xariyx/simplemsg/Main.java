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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    Map<Player, Player> replayList = new HashMap<>();



    private FileConfiguration messages;
    private MSGLog msgLog;


    @Override
    public void onEnable() {
        createCustomConfig();
        registerCommands();
        registerListeners();
        try {
            createLogFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLogFile() throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        File dataFolder = new File(this.getDataFolder().getAbsolutePath()+"\\logs\\"+dateFormat.format(date) +".log");

        try {
            boolean isCreated = dataFolder.createNewFile();
            if(isCreated){
                System.out.println("File "+ dataFolder.getAbsolutePath() +" was created");
            }
            else{
                System.out.println("There was a problem creating Log File," +
                        " shutting down plugin, consider starting server with root permissions.");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.msgLog = new MSGLog(dataFolder);


    }


    @Override
    public void onDisable() {

    }


    public FileConfiguration getMessages() {
        return this.messages;
    }

    private void registerCommands() {
        registerCommand("msg", new MessageCommand(this));
        registerCommand("r", new ReplayCommand(this));
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

        replayList.remove(playerToRemove);

        try {
            ArrayList<Player> playersToRemove = new ArrayList<>();
            replayList.forEach((key, value) -> {
                        if (value == playerToRemove) {
                            playersToRemove.add(key);
                        }
                    }
            );

            for (Player player :
                    playersToRemove) {
                replayList.remove(player);
            }

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


    public MSGLog getMSGLog() {
        return msgLog;
    }
}
