package com.xariyx.simplemsg;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MSGLog {

    FileWriter writer;

    public MSGLog(File logFile) throws IOException {
        writer = new FileWriter(logFile);
    }

    public void addLogToFile(Player sender, Player receiver, String message) throws IOException {

        writer.write(sender.getName() +" > "+ receiver.getName()+" : "+ message);
        writer.flush();
        writer.close();

    }







}
