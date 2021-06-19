package net.zerotoil.fasttravelcp.objects;

import net.zerotoil.fasttravelcp.FastTravelCP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileObject {

    // ---- fields ----

    private FastTravelCP plugin;
    private File configFile;
    private FileConfiguration dataConfig;
    private String location;
    private String name;


    // ---- constructors ----

    // loads a new file
    public FileObject(FastTravelCP plugin, String location) {
        this.plugin = plugin;
        this.location = location;
        this.name = location.replace(".yml", "");
        saveDefaultConfig();
        dataConfig = YamlConfiguration.loadConfiguration(getFile());

    }
    private File getFile(){
        return new File(FastTravelCP.getInstance().getDataFolder(), location);
    }


    // ---- accessors ----

    public FileConfiguration getConfig() {
        return dataConfig;
    }


    // ---- mutators ----

    // saves all data
    public void saveConfig() throws IOException {
        if (!((this.dataConfig == null) || (this.configFile == null))) {
            this.getConfig().save(this.configFile);
        }
    }

    // saves the stock config
    public void saveDefaultConfig(){
        if (configFile == null) {
            configFile = getFile();
        }

        if (configFile.exists()) {
            return;
        }
        FastTravelCP.getInstance().saveResource(location, false);
    }

}
