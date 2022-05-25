package net.zerotoil.dev.cybertravel.objects.regions;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.objects.regions.settings.RegionTeleport;
import net.zerotoil.dev.cybertravel.utilities.WorldUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;

public class Region {

    private final CyberTravel main;
    @Getter private final String id;

    @Getter private boolean enabled = false;
    @Getter private String world;
    @Getter private double[] upperCorner;
    @Getter private double[] lowerCorner;

    @Getter private RegionTeleport teleport;

    public Region(CyberTravel main, String id) {
        this.main = main;
        this.id = id;

        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions." + this.id);
        if (section == null) return;

        enabled = section.getBoolean("enabled", enabled);
        world = section.getString("location.world", WorldUtils.defaultWorld());

        if (!WorldUtils.isWorld(world)) throw new IllegalArgumentException();

        // temp positions
        double[] tempPos1 = WorldUtils.coordinateStringToDouble(section.getString("location.pos-1"));
        double[] tempPos2 = WorldUtils.coordinateStringToDouble(section.getString("location.pos-2"));

        // duplicate positions
        upperCorner = Arrays.copyOf(tempPos1, tempPos1.length);
        lowerCorner = Arrays.copyOf(tempPos2, tempPos2.length);

        // copy max/min
        for (int i = 0; i <= 2; i++) {
            upperCorner[i] = Math.max(tempPos1[i], tempPos2[i]);
            lowerCorner[i] = Math.min(tempPos1[i], tempPos2[i]);
        }

        teleport = new RegionTeleport(main, this);


    }



}
