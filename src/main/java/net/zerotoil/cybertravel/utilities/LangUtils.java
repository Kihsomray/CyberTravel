package net.zerotoil.cybertravel.utilities;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LangUtils {

    private CyberTravel main;

    public LangUtils(CyberTravel main) {
        this.main = main;
    }

    // gets color of chat message
    public String getColor(String msg, boolean addPrefix){
        if (addPrefix) return main.getLangCache().getPrefix() + ChatColor.translateAlternateColorCodes('&', msg);
        return ChatColor.translateAlternateColorCodes('&', msg);
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
        sender.sendMessage(main.getLangCache().getMessages().get("no-permission").getMessage(true));

    }

    public String formatTime(long seconds) {

        String formattedTime = "";

        String daysString = main.getLangCache().getTimeDaysFormat();
        String hoursString = main.getLangCache().getTimeHoursFormat();
        String minutesString = main.getLangCache().getTimeMinutesFormat();
        String secondsString = main.getLangCache().getTimeSecondsFormat();
        String splitter = main.getLangCache().getTimeSplitterFormat();

        if (seconds == 0) return main.getLangUtils().getColor(secondsString.replace("(", "").replace(")", "").replace("{time}", "0"), false);

        long daySeconds = seconds;
        if (seconds != 0) daySeconds = seconds % 86400;
        long days = (seconds - daySeconds) / 86400;

        long hourSeconds = daySeconds;
        if (daySeconds != 0) hourSeconds = daySeconds % 3600;
        long hours = (daySeconds - hourSeconds) / 3600;

        long minuteSeconds = hourSeconds;
        if (hourSeconds != 0) minuteSeconds = hourSeconds % 60;
        long minutes = (hourSeconds - minuteSeconds) / 60;

        // day formatting
        daysString = daysString.replace("{time}", days + "");
        if (days == 1) {
            daysString = daysString.replaceAll("\\s*\\([^\\)]*\\)\\s*", "");
        } else {
            daysString = daysString.replace("(", "").replace(")", "");
        }
        if (days != 0) {
            if ((hours == 0) && (minutes == 0) && (minuteSeconds == 0)) {
                formattedTime = daysString;
            } else {
                formattedTime = daysString + splitter;
            }
        }


        // hour formatting
        hoursString = hoursString.replace("{time}", hours + "");
        if (hours == 1) {
            hoursString = hoursString.replaceAll("\\s*\\([^\\)]*\\)\\s*", "");
        } else {
            hoursString = hoursString.replace("(", "").replace(")", "");
        }
        if (hours != 0) {
            if ((minutes == 0) && (minuteSeconds == 0)) {
                formattedTime = formattedTime + hoursString;
            } else {
                formattedTime = formattedTime + hoursString + splitter;
            }
        }


        // minute formatting
        minutesString = minutesString.replace("{time}", minutes + "");
        if (minutes == 1) {
            minutesString = minutesString.replaceAll("\\s*\\([^\\)]*\\)\\s*", "");
        } else {
            minutesString = minutesString.replace("(", "").replace(")", "");
        }
        if (minutes != 0) {
            if (minuteSeconds == 0) {
                formattedTime = formattedTime + minutesString;
            } else {
                formattedTime = formattedTime + minutesString + splitter;
            }
        }


        // second formatting
        secondsString = secondsString.replace("{time}", minuteSeconds + "");
        if (minuteSeconds == 1) {
            secondsString = secondsString.replaceAll("\\s*\\([^\\)]*\\)\\s*", "");
        } else {
            secondsString = secondsString.replace("(", "").replace(")", "");
        }
        if (minuteSeconds != 0) formattedTime = formattedTime + secondsString;

        return main.getLangUtils().getColor(formattedTime, false);

    }
    public String getPermission(String permissionName) {
        return main.getLangCache().getPermissions().get(permissionName).getPermission();
    }
    public String getMessage(String messageName, boolean addPrefix) {
        return main.getLangCache().getMessages().get(messageName).getMessage(addPrefix);
    }

}
