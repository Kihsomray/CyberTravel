package net.zerotoil.dev.cybertravel.object.region;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.region.settings.RegionCommands;
import net.zerotoil.dev.cybertravel.object.region.settings.RegionLocation;
import net.zerotoil.dev.cybertravel.object.region.settings.RegionMessage;
import net.zerotoil.dev.cybertravel.object.region.settings.RegionTeleport;
import net.zerotoil.dev.cybertravel.utility.WorldUtils;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;

public class Region {

    private final CyberTravel main;
    @Getter private final String id;

    @Getter private boolean enabled = false;
    @Getter private String displayName;

    @Getter private RegionLocation location;

    @Getter private RegionTeleport teleport;
    @Getter private RegionCommands commands;
    @Getter private RegionMessage message;

    private static final String[] placeholders = new String[]{
            "regionID",
            "regionDisplayName",
            "regionUpperCoordinates",
            "regionLowerCoordinates",
            "regionWorld",
            "regionEnabled"
    };

    /**
     * Constructs a region object based on data
     * saved to the regions.yml file.
     *
     * @param main Main instance
     * @param id Config ID of region
     */
    public Region(CyberTravel main, String id) {
        this.main = main;
        this.id = id;
        reload();

    }

    /**
     * Constructs a region object based on the
     * region location. Generates necessary data
     * in the regions.yml file.
     *
     * @param main Main instance
     * @param id New ID (must not already exist)
     * @param location Region location that is fully set up
     * @throws IllegalArgumentException If region already exists
     */
    public Region(CyberTravel main, String id, RegionLocation location) {
        this.main = main;
        this.id = id;

        if (main.cache().isRegion(id)) throw new IllegalArgumentException();

        Configuration config = main.core().files().getConfig("regions");

        String pre = "regions." + id + ".";

        config.set(pre + "enabled", false);
        config.set(pre + "display-name", id.replace("_", "").replace("-", ""));

        String loc = pre + "location.";
        setRegionLocation(location);
        config.set(loc + "world", location.getWorldName());
        config.set(loc + "pos-1", location.getUpperString());
        config.set(loc + "pos-2", location.getLowerString());

        String tp = pre + "settings.teleport.";
        config.set(tp + "enabled", false);
        config.set(tp + "world", location.getWorldName());
        config.set(tp + "location", WorldUtils.coordinatesToString(location.getMidpoint(), false));

        config.set(pre + "settings.commands", null);

        String msg = pre + "settings.message.";
        config.set(msg + "header", true);
        config.set(msg + "footer", true);
        config.set(msg + "content", Arrays.toString(new String[]{
                "&7► <G:EE0099>No need to walk back here again!</G:EE00EE>",
                "&7► <G:EE0099>Teleport to this Region using &l/ctp tp {id}</G:EE00EE>"
        }));

        try {
            main.core().files().get("regions").saveConfig();
        } catch (Exception e) {
            // nothing
        }

    }

    /**
     * Reloads the region based on data
     * inside the region.yml file.
     */
    public void reload() {
        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions." + this.id);
        if (section == null) return;

        enabled = section.getBoolean("enabled", enabled);
        displayName = section.getString("display-name", id);

        // location of the world
        String world = section.getString("location.world", WorldUtils.defaultWorld());

        if (!WorldUtils.isWorld(world)) throw new IllegalArgumentException();

        // get the positions
        double[] pos1 = WorldUtils.coordinateStringToDouble(section.getString("location.pos-1"));
        double[] pos2 = WorldUtils.coordinateStringToDouble(section.getString("location.pos-2"));

        // location coordinates from config
        location = new RegionLocation(main, this, world, pos1, pos2);

        // initialize all other region data
        teleport = new RegionTeleport(main, this);
        commands = new RegionCommands(main, this);
        message = new RegionMessage(main, this);
    }

    /**
     * Unloads data from the region.
     */
    public void unload() {
        teleport.unloadCooldowns();
    }

    /**
     * Check if the location is within the region.
     *
     * @param location Location to check
     * @return If location is in the region
     */
    public boolean inRegion(Location location) {
        if (!enabled) return false;
        if (location.getWorld() == null) return false;
        RegionLocation rl = this.location;
        if (!location.getWorld().getName().equals(rl.getWorldName())) return false;
        if (location.getX() > rl.getUpperX() || location.getX() < rl.getLowerX()) return false;
        if (location.getY() > rl.getUpperY() || location.getY() < rl.getLowerY()) return false;
        return !(location.getZ() > rl.getUpperZ()) && !(location.getZ() < rl.getLowerZ());
    }

    /**
     * Set a new region location. Must not
     * be binded to another region.
     *
     * @param location New region location that is fully set up
     * @return False if passed location is already binded
     */
    public boolean setRegionLocation(RegionLocation location) {
        if (!location.isReady()) return false;
        this.location = location;
        return location.bind(this);

    }

    /**
     * Get the region's placeholders.
     *
     * @return Array of placeholders
     */
    public String[] getPlaceholders() {
        return placeholders;
    }

    /**
     * Get the region's placeholder replacements.
     *
     * @return Array of placeholder replacements
     */
    public String[] getReplacements() {
        String[] replacements = new String[placeholders.length];

        replacements[0] = id;
        replacements[1] = displayName;
        replacements[2] = location.getUpperString();
        replacements[3] = location.getLowerString();
        replacements[4] = location.getWorldName();
        replacements[5] = enabled + "";

        return replacements;
    }



}
