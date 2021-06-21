package net.zerotoil.fasttravelcp.utilities;

import net.zerotoil.fasttravelcp.cache.FileCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtils {

    private static String prefix = ChatColor.translateAlternateColorCodes('&',
            FileCache.storedFiles.get("lang").getConfig().getString( "messages.prefix") + " ");

    // gets color of chat message
    public static String getColor(String msg, boolean addPrefix){
        if (addPrefix) {
            return prefix + ChatColor.translateAlternateColorCodes('&', msg);
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

    public static String noPermission() {
        // config: messages.no-permission
        return (prefix + ChatColor.translateAlternateColorCodes('&',
                FileCache.storedFiles.get("lang").getConfig().getString("messages.no-permission")));

    }

    public static void sendMessage(String fileName, String path, String altMessage, CommandSender sender) {
        if(!FileCache.storedFiles.get(fileName).getConfig().isSet(path)){
            sender.sendMessage(MessageUtils.getColor(altMessage, true));
            return;
        }
        sender.sendMessage(MessageUtils.getColor(FileCache.storedFiles.get(fileName).getConfig().getString(path), true));
        return;
    }

    public static void sendMessage(String fileName, String path, String altMessage, CommandSender sender, String placeholder, String replacement) {
        if(!FileCache.storedFiles.get(fileName).getConfig().isSet(path)){
            sender.sendMessage(MessageUtils.getColor(altMessage, true));
            return;
        }
        sender.sendMessage(MessageUtils.getColor(FileCache.storedFiles.get(fileName).getConfig().getString(path).replace("{" + placeholder + "}", replacement), true));
        return;
    }


}
