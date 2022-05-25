package net.zerotoil.dev.cybertravel.objects.regions.settings;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.objects.regions.Region;
import net.zerotoil.dev.cybertravel.utilities.WorldUtils;
import org.bukkit.configuration.ConfigurationSection;

public class RegionTeleport {

    private final CyberTravel main;
    @Getter private final Region region;

    @Getter private boolean enabled = false;
    @Getter private String world;
    @Getter private double[] location;


    public RegionTeleport(CyberTravel main, Region region) {
        this.main = main;
        this.region = region;

        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions." + region.getId() + ".settings.teleport");
        if (section == null) return;

        enabled = section.getBoolean("enabled", enabled);
        world = section.getString("world", WorldUtils.defaultWorld());

        if (!WorldUtils.isWorld(world)) {
            main.logger("&cInvalid teleport world for region " + region.getId() + "! Disabling teleport...");
            enabled = false;
            return;
        }

        location = WorldUtils.coordinateStringToDouble(section.getString("location"));

    }




}
