package net.zerotoil.fasttravelcp.cache;

import net.zerotoil.fasttravelcp.utilities.FileUtils;
import net.zerotoil.fasttravelcp.objects.RegionObject;
import org.bukkit.configuration.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayerCache {

    public static HashMap<String, List<String>> playerRegions = new HashMap<>();
    public static HashMap<String, RegionObject> regions = new HashMap<>();

    public static void refreshRegionData(boolean newRegionsOnly) {
        String rd = "regions.";
        String r = "regions";

        if ((!newRegionsOnly) && (!regions.isEmpty())) regions.clear();

        if (FileUtils.dataFile().isConfigurationSection(r)) {
            for (String i : FileUtils.dataFile().getConfigurationSection(r).getKeys(false)) {

                boolean checkKey = true;
                if (newRegionsOnly) {
                    checkKey = !regions.containsKey(i);
                }

                if (checkKey && isDataSet(rd + i + ".pos1") && isDataSet(rd + i + ".pos2") && isDataSet(rd + i + ".settp")) {

                    regions.put(i, new RegionObject(i, FileUtils.dataFile().getString("regions." + i + ".world"),
                            getCoords(i, "pos1"), getCoords(i, "pos2"), getCoords(i, "settp")));

                } else if (!checkKey) {

                } else {

                    System.out.println("You have not finished setting up region " + i + "! Please finish it before it can be used!");

                }
            }
        }
    }

    public static boolean isDataSet(String location) {
        return FileUtils.dataFile().isSet(location);
    }

    public static double[] getCoords(String region, String type) {
        String stringLocation = FileUtils.dataFile().getString("regions." + region + "." + type);
        return Arrays.stream(stringLocation.split(", ")).mapToDouble(Double::parseDouble).toArray();

    }

    public static void initializePlayerData() {

        if (!playerRegions.isEmpty()) playerRegions.clear();

        if (!FileUtils.dataFile().isConfigurationSection("players")) {
            return;
        }
        for (String i : FileUtils.dataFile().getConfigurationSection("players").getKeys(false)) {
            playerRegions.put(i, FileUtils.dataFile().getStringList("players." + i));
        }

    }




}
