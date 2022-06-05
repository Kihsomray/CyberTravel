package net.zerotoil.dev.cybertravel.utility;

import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.region.Region;

import java.util.HashMap;
import java.util.Map;

public class PlayerUtils {

    /**
     * Convert an array string from config to a list of
     * existing regions.
     *
     * @param main Main instance
     * @param string Array string from config
     * @return List of regions from the array string
     */
    public static Map<String, Region> stringToRegionMap(CyberTravel main, String string) {
        Map<String, Region> list = new HashMap<>();

        // remove the surrounding brackets.
        string = string.substring(1, string.length() - 1);
        for(String s : string.split(", ")) {
            if (!main.cache().regions().isRegion(s)) continue;
            list.put(s, main.cache().regions().getRegion(s));
        }

        return list;

    }


}
