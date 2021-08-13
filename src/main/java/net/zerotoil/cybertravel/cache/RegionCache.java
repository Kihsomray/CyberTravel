package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.RegionObject;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class RegionCache {

    private CyberTravel main;
    private Configuration config;

    private HashMap<String, RegionObject> regions = new HashMap<>();

    public RegionCache(CyberTravel main) {

        this.main = main;
        this.loadRegionData();

    }

    // load in new region data
    public void loadRegionData() {

        config = main.getFileCache().getStoredFiles().get("regions").getConfig();

        if (!regions.isEmpty()) regions.clear();

        // imports old data into new regions.yml
        if ((main.getFileCache().getStoredFiles().get("data").getConfig().isConfigurationSection("regions")) &&
                (main.getFileCache().getStoredFiles().get("data").getConfig().get("regions") != null)) {

            config.set("regions", main.getFileCache().getStoredFiles().get("data").getConfig().getConfigurationSection("regions"));

            try {

                main.getFileCache().getStoredFiles().get("regions").saveConfig();
                main.getFileCache().getStoredFiles().get("data").getConfig().set("regions", null);

                try {

                    main.getFileCache().getStoredFiles().get("data").saveConfig();

                } catch (Exception e) {}

            } catch (Exception e) {}

            for (String i : config.getConfigurationSection("regions").getKeys(false)) {

                ConfigurationSection configSection = config.getConfigurationSection("regions." + i);
                set(i , null);
                set(i + ".location", configSection);

            }

        }

        if (!config.isConfigurationSection("regions")) return;
        for (String i : config.getConfigurationSection("regions").getKeys(false)) {

            String subPath = "regions." + i + ".";
            String location = subPath + "location.";
            String settings = subPath + "settings.";

            regions.put(i, new RegionObject(i, config.getString(location + "world")));

            if (config.isSet(location + "pos1")) regions.get(i).setPos1(getCoordinates(i, "pos1"));
            if (config.isSet(location + "pos2")) regions.get(i).setPos2(getCoordinates(i, "pos2"));
            if (config.isSet(location + "settp")) regions.get(i).setSetTP(getCoordinates(i, "settp"));
            if (config.isSet(settings + "display-name")) regions.get(i).setDisplayName(config.getString(settings + "display-name"));
            if (config.isSet(settings + "price")) regions.get(i).setPrice(config.getDouble(settings + "price"));
            if (config.isSet(settings + "cooldown")) regions.get(i).setCooldown(config.getLong(settings + "cooldown"));
            if (config.isSet(settings + "command")) {

                if (config.isList(settings + "command")) {

                    regions.get(i).setCommands(config.getStringList(settings + "command"));

                } else {

                    regions.get(i).addCommand(config.getString(settings + "command"));

                }
            }
            if (config.isSet(settings + "commands")) {

                if (config.isList(settings + "commands")) {

                    regions.get(i).setCommands(config.getStringList(settings + "commands"));

                } else {

                    regions.get(i).addCommand(config.getString(settings + "commands"));

                }
            }

            if (main.getConfigCache().isAutoEnableRegions()) {

                if ((regions.get(i).getPos1() != null) && (regions.get(i).getPos2() != null) && (regions.get(i).getSetTP() != null)) {

                    regions.get(i).setEnabled(true);
                    set(i + ".enabled", true);

                } else {

                    regions.get(i).setEnabled(false);
                    set(i + ".enabled", false);

                }

            } else if (config.isSet(subPath + "enabled")) {

                regions.get(i).setEnabled(config.getBoolean(subPath + "enabled"));

            } else {

                set(i + ".enabled", false);
                regions.get(i).setEnabled(false);

            }

        }


    }


    private double[] getCoordinates(String region, String type) {
        String stringLocation = config.getString("regions." + region + ".location." + type);
        return Arrays.stream(stringLocation.split(", ")).mapToDouble(Double::parseDouble).toArray();

    }

    private void set(String path, Object object) {
        config.set("regions." + path, object);
        try {
            main.getFileCache().getStoredFiles().get("regions").saveConfig();
        } catch (IOException e) {}
    }

    public HashMap<String, RegionObject> getRegions() {
        return regions;
    }
}
