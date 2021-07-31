package net.zerotoil.cybertravel.objects;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.IOException;

public class MessageObject {

    private CyberTravel main;

    private String message;
    private String path;
    private String prefix;
    private boolean updateConfig;

    public MessageObject(CyberTravel main, boolean updateConfig, String prefix, String path, String defaultMessage) {
        this.main = main;
        this.updateConfig = updateConfig;
        this.path = "messages." + path;
        this.prefix = prefix;

        if (isSet(path)) {

            message = ChatColor.translateAlternateColorCodes('&', getMessage(path));

        } else {

            if (updateConfig) {
                setMessage(path, defaultMessage);
            }

            message = ChatColor.translateAlternateColorCodes('&', defaultMessage);

        }

    }

    private boolean isSet(String path) {
        return main.getFileCache().getStoredFiles().get("lang").getConfig().isSet("messages." + path);
    }
    private String getMessage(String path) {
        return main.getFileCache().getStoredFiles().get("lang").getConfig().getString("messages." + path);
    }

    private void setMessage(String path, String message) {
        main.getFileCache().getStoredFiles().get("lang").getConfig().set("messages." + path, message);
        try {
            main.getFileCache().getStoredFiles().get("lang").saveConfig();
        } catch (IOException e) {
        }
    }

    // replace strings
    public String getMessage(boolean addPrefix) {
        if (addPrefix) return prefix + message;
        return message;
    }
    public String getMessage(boolean addPrefix, String placeholder, String replacement) {
        if (message.equalsIgnoreCase("")) return "";

        String message = this.message;

        message = message.replace("{" + placeholder + "}", replacement);

        if (addPrefix) message = prefix + message;
        return message;
    }
    public String getMessage(boolean addPrefix, String placeholder, String replacement, String placeholder2, String replacement2) {
        if (message.equalsIgnoreCase("")) return "";

        String message = this.message;

        message = message.replace("{" + placeholder + "}", replacement).replace("{" + placeholder2 + "}", replacement2);

        if (addPrefix) message = prefix + message;
        return message;
    }
    public String getMessage(boolean addPrefix, String placeholder, String replacement, String placeholder2, String replacement2, String placeholder3, String replacement3) {
        if (message.equalsIgnoreCase("")) return "";

        String message = this.message;

        message = message.replace("{" + placeholder + "}", replacement).replace("{" + placeholder2 + "}", replacement2).replace("{" + placeholder3 + "}", replacement3);

        if (addPrefix) message = prefix + message;
        return message;
    }
    public String getMessage(boolean addPrefix, String placeholder, String replacement, String placeholder2, String replacement2, String placeholder3, String replacement3, String placeholder4, String replacement4) {
        if (message.equalsIgnoreCase("")) return "";

        String message = this.message;

        message = message.replace("{" + placeholder + "}", replacement).replace("{" + placeholder2 + "}", replacement2)
                .replace("{" + placeholder3 + "}", replacement3).replace("{" + placeholder4 + "}", replacement4);

        if (addPrefix) message = prefix + message;
        return message;
    }

    public String getPath() {
        return this.path;
    }

}
