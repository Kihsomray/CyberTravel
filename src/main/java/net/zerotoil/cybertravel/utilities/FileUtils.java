package net.zerotoil.cybertravel.utilities;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.configuration.Configuration;

public class FileUtils {

    public FileUtils(CyberTravel main) {
        this.main = main;
    }

    private CyberTravel main;


    public Configuration dataFile() {
        return main.getFileCache().getStoredFiles().get("data").getConfig();
    }

    public Configuration configFile() {
        return main.getFileCache().getStoredFiles().get("config").getConfig();
    }

    public Configuration langFile() {
        return main.getFileCache().getStoredFiles().get("lang").getConfig();
    }

    public String getPermission(String permissionName, String defaultPermission) {
        if (langFile().isSet("permissions." + permissionName)){
            return langFile().getString("permissions." + permissionName);
        }
        return defaultPermission;
    }

}
