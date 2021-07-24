package net.zerotoil.cybertravel.utilities;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.cache.FileCache;
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

}
