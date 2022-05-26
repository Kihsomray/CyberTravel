package net.zerotoil.dev.cybertravel.cache;

import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.objects.regions.Region;
import net.zerotoil.dev.cybertravel.objects.regions.settings.RegionLocation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final CyberTravel main;

    private Config config;
    private Map<String, Region> regions;

    public Cache(CyberTravel main) {
        this.main = main;
        load(false);
    }

    public void reload() {
        load(true);
    }

    private void load(boolean loadCore) {

        if (loadCore) main.reloadCore();

        config = new Config(main);
        reloadRegions();

        if (loadCore) main.core().loadFinish();

    }

    /**
     * Reloads all regions in config.
     */
    public void reloadRegions() {

        long startTime = System.currentTimeMillis();
        main.logger("&cLoading region configurations...");

        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions");
        if (section == null) return;

        regions = new HashMap<>();
        for (String s : section.getKeys(false))
            regions.put(s, new Region(main, s));

        main.logger("&7Loaded &e" + regions.size() + "&7 regions in &a" + (System.currentTimeMillis() - startTime) + "ms&7.", "");

    }

    /**
     * Contains all values of the config.yml
     * saved to cache.
     *
     * @return Config containing values
     */
    public Config config() {
        return config;
    }

    /**
     * Obtains a region based on an ID.
     *
     * @param id ID of an existing region
     * @return Region binded to the ID
     */
    public Region getRegion(String id) {
        if (!isRegion(id)) return null;
        return regions.get(id);
    }

    /**
     * Checks if a region with given ID is
     * already in the config/loaded to cache.
     *
     * @param id ID of a region
     * @return If the region already exists
     */
    public boolean isRegion(String id) {
        return (regions.containsKey(id));
    }

    /**
     * Constructs a region object based on the
     * region location. Generates necessary data
     * in the regions.yml file.
     *
     * @param id New ID (must not already exist)
     * @param location Region location that is full set up
     * @return If the region was successfully created
     */
    public boolean createRegion(String id, RegionLocation location) {
        try {
            Region region = new Region(main, id, location);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
