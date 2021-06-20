package net.zerotoil.fasttravelcp;

import net.zerotoil.fasttravelcp.cache.FileCache;
import org.bukkit.configuration.Configuration;

public class FileUtils {

    public static Configuration dataFile = FileCache.storedFiles.get("data").getConfig();
    public static Configuration configFile = FileCache.storedFiles.get("config").getConfig();
    public static Configuration langFile = FileCache.storedFiles.get("lang").getConfig();

}
