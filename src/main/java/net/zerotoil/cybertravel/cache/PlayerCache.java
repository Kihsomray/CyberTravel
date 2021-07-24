package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.RegionObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayerCache {

    private CyberTravel main;

    public PlayerCache(CyberTravel main) {
        this.main = main;
    }

    private HashMap<String, List<String>> playerRegions = new HashMap<>();
    private HashMap<String, RegionObject> regions = new HashMap<>();

    public HashMap<String, List<String>> getPlayerRegions() {
        return this.playerRegions;
    }
    public HashMap<String, RegionObject> getRegions() {
        return this.regions;
    }

    public boolean isDataSet(String location) {
        return main.getFileUtils().dataFile().isSet(location);
    }
    public double[] getCoords(String region, String type) {
        String stringLocation = main.getFileUtils().dataFile().getString("regions." + region + "." + type);
        return Arrays.stream(stringLocation.split(", ")).mapToDouble(Double::parseDouble).toArray();

    }

    public void refreshRegionData(boolean newRegionsOnly) {
        String rd = "regions.";
        String r = "regions";

        if ((!newRegionsOnly) && (!regions.isEmpty())) regions.clear();

        if (main.getFileUtils().dataFile().isConfigurationSection(r)) {
            for (String i : main.getFileUtils().dataFile().getConfigurationSection(r).getKeys(false)) {

                boolean checkKey = true;
                if (newRegionsOnly) {
                    checkKey = !regions.containsKey(i);
                }

                if (checkKey && isDataSet(rd + i + ".pos1") && isDataSet(rd + i + ".pos2") && isDataSet(rd + i + ".settp")) {

                    regions.put(i, new RegionObject(i, main.getFileUtils().dataFile().getString("regions." + i + ".world"),
                            getCoords(i, "pos1"), getCoords(i, "pos2"), getCoords(i, "settp")));

                } else if (!checkKey) {

                } else {

                    System.out.println("You have not finished setting up region " + i + "! Please finish it before it can be used!");

                }
            }
        }
    }
    public void initializePlayerData() {

        if (!playerRegions.isEmpty()) playerRegions.clear();

        if (!main.getFileUtils().dataFile().isConfigurationSection("players")) {
            return;
        }
        for (String i : main.getFileUtils().dataFile().getConfigurationSection("players").getKeys(false)) {
            playerRegions.put(i, main.getFileUtils().dataFile().getStringList("players." + i));
        }

    }




}
