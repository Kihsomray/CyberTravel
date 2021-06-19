package net.zerotoil.fasttravelcp.cache;

import net.zerotoil.fasttravelcp.MessageUtils;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayerCache {

    public static HashMap<String, List<String>> playerRegions = new HashMap<>();
    public static List<double[]> pos1 = new ArrayList<>();
    public static List<double[]> pos2 = new ArrayList<>();
    public static List<String> regions = new ArrayList<>();
    public static List<String> worlds = new ArrayList<>();


    public static void initializePlayerData() {

        if (!playerRegions.isEmpty()) playerRegions.clear();

        Configuration dataConfig = FileCache.storedFiles.get("data").getConfig();

        if (!dataConfig.isConfigurationSection("players")) {
            return;
        }
        for (String i : dataConfig.getConfigurationSection("players").getKeys(false)) {
            playerRegions.put(i, dataConfig.getStringList("players." + i));
        }

    }

    public static void initializeRegionData() {

        if (!pos1.isEmpty()) pos1.clear();
        if (!pos2.isEmpty()) pos2.clear();
        if (!regions.isEmpty()) regions.clear();
        if (!worlds.isEmpty()) worlds.clear();


        Configuration dataConfig = FileCache.storedFiles.get("data").getConfig();

        if (dataConfig.isConfigurationSection("regions")) {
            for (String i : dataConfig.getConfigurationSection("regions").getKeys(false)) {

                if (dataConfig.isSet("regions." + i + ".pos1") && dataConfig.isSet("regions." + i + ".pos2") && dataConfig.isSet("regions." + i + ".settp")) {
                    regions.add(i);

                    String stringLocation1 = dataConfig.getString("regions." + i + ".pos1");
                    double[] arrLocation1 = Arrays.stream(stringLocation1.split(", ")).mapToDouble(Double::parseDouble).toArray();
                    pos1.add(arrLocation1);

                    String stringLocation2 = dataConfig.getString("regions." + i + ".pos2");
                    double[] arrLocation2 = Arrays.stream(stringLocation2.split(", ")).mapToDouble(Double::parseDouble).toArray();
                    pos2.add(arrLocation2);

                    worlds.add(dataConfig.getString("regions." + i + ".world"));

                } else {
                    System.out.println("&cYou have not finished setting up region " + i + "! Please finish it before it can be used!");
                }

            }


        }
    }

}
