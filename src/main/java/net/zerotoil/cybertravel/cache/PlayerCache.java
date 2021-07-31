package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.RegionObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayerCache {

    private CyberTravel main;

    private HashMap<String, List<String>> playerRegions;

    public PlayerCache(CyberTravel main) {

        this.main = main;

        playerRegions = new HashMap<>();
        initializePlayerData();

    }



    // reload player data
    public void initializePlayerData() {

        if (!playerRegions.isEmpty()) {
            playerRegions.clear();
        }

        if (!main.getFileUtils().dataFile().isConfigurationSection("players")) {
            return;
        }
        for (String i : main.getFileUtils().dataFile().getConfigurationSection("players").getKeys(false)) {
            playerRegions.put(i, main.getFileUtils().dataFile().getStringList("players." + i));
        }

    }


    public HashMap<String, List<String>> getPlayerRegions() {
        return this.playerRegions;
    }
    public boolean isDataSet(String location) {
        return main.getFileUtils().dataFile().isSet(location);
    }
    public double[] getCoords(String region, String type) {
        String stringLocation = main.getFileUtils().dataFile().getString("regions." + region + "." + type);
        return Arrays.stream(stringLocation.split(", ")).mapToDouble(Double::parseDouble).toArray();

    }


}
