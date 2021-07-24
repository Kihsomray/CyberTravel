package net.zerotoil.cybertravel.objects;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileObject {

    // ---- fields ----

    private CyberTravel main;
    private File configFile;
    private FileConfiguration dataConfig;
    private String location;
    private String name;


    // ---- constructors ----

    // loads a new file
    public FileObject(CyberTravel main, String location) {
        this.main = main;
        this.location = location;
        this.name = location.replace(".yml", "");
        saveDefaultConfig();
        dataConfig = YamlConfiguration.loadConfiguration(getFile());

    }
    private File getFile() {
        return new File(main.getDataFolder(), location);
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
        main.saveResource(location, false);
    }

}
