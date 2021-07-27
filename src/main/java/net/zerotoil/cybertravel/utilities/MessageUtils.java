package net.zerotoil.cybertravel.utilities;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {

    public MessageUtils(CyberTravel main) {
        this.main = main;
    }

    private CyberTravel main;

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', main.getFileCache().getStoredFiles().get("lang").getConfig().getString( "messages.prefix") + " ");
    }

    // gets color of chat message
    public String getColor(String msg, boolean addPrefix){
        if (addPrefix) {
            return getPrefix() + ChatColor.translateAlternateColorCodes('&', msg);
        } else {
            return ChatColor.translateAlternateColorCodes('&', msg);
        }
    }

    // gets full length string from array
    public static String loopString(String args[]) {
        String input = args[4];
        for (int i = 5; i < args.length; i++){
            input = input + " " + args[i];
        }
        return input;
    }

    public void noPermission(Player sender) {
        // config: messages.no-permission
        sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do that!", sender);

    }

    public void sendMessage(String fileName, String path, String altMessage, CommandSender sender) {
        if(!main.getFileCache().getStoredFiles().get(fileName).getConfig().isSet(path)){
            sender.sendMessage(getColor(altMessage, true));
            return;
        }
        sender.sendMessage(getColor(main.getFileCache().getStoredFiles().get(fileName).getConfig().getString(path), true));
        return;
    }

    public void sendMessage(String fileName, String path, String altMessage, CommandSender sender, String placeholder, String replacement) {
        if(!main.getFileCache().getStoredFiles().get(fileName).getConfig().isSet(path)){
            sender.sendMessage(getColor(altMessage, true));
            return;
        }
        sender.sendMessage(getColor(main.getFileCache().getStoredFiles().get(fileName).getConfig().getString(path).replace("{" + placeholder + "}", replacement), true));
        return;
    }

    public void sendMessage(String fileName, String path, String altMessage, CommandSender sender, String placeholder, String replacement, boolean addPrefix) {
        if(!main.getFileCache().getStoredFiles().get(fileName).getConfig().isSet(path)){
            sender.sendMessage(getColor(altMessage, addPrefix));
            return;
        }
        sender.sendMessage(getColor(main.getFileCache().getStoredFiles().get(fileName).getConfig().getString(path).replace("{" + placeholder + "}", replacement), addPrefix));
        return;
    }

    public String formatTime(long seconds) {

        String formattedTime = "";

        // standard formatting
        String daysString = "&c{time} Day(s)";
        String hoursString = "&c{time} Hour(s)";
        String minutesString = "&c{time} Minute(s)";
        String secondsString = "&c{time} Second(s)";
        String splitter = "&c, ";

        // custom lang formatting
        if (main.getFileUtils().langFile().isConfigurationSection("time")) {

            if (main.getFileUtils().langFile().isSet("time.days")) daysString = main.getFileUtils().langFile().getString("time.days");
            if (main.getFileUtils().langFile().isSet("time.hours")) hoursString = main.getFileUtils().langFile().getString("time.hours");
            if (main.getFileUtils().langFile().isSet("time.minutes")) minutesString = main.getFileUtils().langFile().getString("time.minutes");
            if (main.getFileUtils().langFile().isSet("time.seconds")) secondsString = main.getFileUtils().langFile().getString("time.seconds");
            if (main.getFileUtils().langFile().isSet("time.splitter")) splitter = main.getFileUtils().langFile().getString("time.splitter");

        }

        long daySeconds = seconds;
        if (seconds != 0) daySeconds = seconds % 86400;
        long days = (seconds - daySeconds) / 86400;

        long hourSeconds = daySeconds;
        if (daySeconds != 0) hourSeconds = daySeconds % 3600;
        long hours = (daySeconds - hourSeconds) / 3600;

        long minuteSeconds = hourSeconds;
        if (hourSeconds != 0) minuteSeconds = hourSeconds % 60;
        long minutes = (hourSeconds - minuteSeconds) / 60;

        if (days != 0) {
            if ((hours == 0) && (minutes == 0) && (minuteSeconds == 0)) {
                formattedTime = daysString.replace("{time}", days + "");
            } else {
                formattedTime = daysString.replace("{time}", days + "") + splitter;
            }
        }

        if (hours != 0) {
            if ((minutes == 0) && (minuteSeconds == 0)) {
                formattedTime = formattedTime + hoursString.replace("{time}", hours + "");
            } else {
                formattedTime = formattedTime + hoursString.replace("{time}", hours + "") + splitter;
            }
        }

        if (minutes != 0) {
            if (minuteSeconds == 0) {
                formattedTime = formattedTime + minutesString.replace("{time}", minutes + "");
            } else {
                formattedTime = formattedTime + minutesString.replace("{time}", minutes + "") + splitter;
            }
        }

        if (minuteSeconds != 0) formattedTime = formattedTime + secondsString.replace("{time}", minuteSeconds + "");

        return formattedTime;

    }


}
