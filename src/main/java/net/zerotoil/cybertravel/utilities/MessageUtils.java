package net.zerotoil.cybertravel.utilities;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.cache.FileCache;
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


}
