package net.zerotoil.dev.cybertravel.utilities;

import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.objects.regions.Region;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

    /**
     * Convert an array string from config to a list of
     * existing regions.
     *
     * @param main Main instance
     * @param string Array string from config
     * @return List of regions from the array string
     */
    public static List<Region> stringToRegionList(CyberTravel main, String string) {
        List<Region> list = new ArrayList<>();

        // remove the surrounding brackets.
        string = string.substring(1, string.length() - 1);
        for(String s : string.split(", ")) {
            if (!main.cache().isRegion(s)) continue;
            list.add(main.cache().getRegion(s));
        }

        return list;

    }


}
