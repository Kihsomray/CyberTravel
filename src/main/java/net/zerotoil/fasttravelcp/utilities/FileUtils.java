package net.zerotoil.fasttravelcp.utilities;

import net.zerotoil.fasttravelcp.cache.FileCache;
import org.bukkit.configuration.Configuration;

public class FileUtils {

    public static Configuration dataFile() {
        return FileCache.storedFiles.get("data").getConfig();
    }

    public static Configuration configFile() {
        return FileCache.storedFiles.get("config").getConfig();
    }

}
