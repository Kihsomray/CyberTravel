package net.zerotoil.dev.cybertravel.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Properties;

public class WorldUtils {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public static String defaultWorld() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("server.properties"));
            return props.getProperty("level-name");
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isWorld(String worldName) {

        return Bukkit.getWorld(worldName) != null;

    }

    public static Location stringToLocation(String worldName, String locationString) {
        return doubleArrayToLocation(worldName, coordinateStringToDouble(locationString));
    }

    public static Location doubleArrayToLocation(String worldName, double[] locationArray) {
        float pitch = 0, yaw = 0;
        if (locationArray.length >= 4) pitch = (float) locationArray[3];
        if (locationArray.length == 5) yaw = (float) locationArray[4];
        return new Location(Bukkit.getWorld(worldName), locationArray[0], locationArray[1], locationArray[2], yaw, pitch);
    }

    public static double[] coordinateStringToDouble(String locationString) {
        locationString = locationString.replace("[", "")
                .replace("]", "")
                .replace("<", "")
                .replace(">", "")
                .replace(", ", " ")
                .replace(",", " ");
        return Arrays.stream(locationString.split(" ")).mapToDouble(Double::parseDouble).toArray();
    }

    public static String coordinatesToString(double[] coordinates, boolean round) {
        if (round) for (int i = 0; i < coordinates.length; i++) coordinates[i] = Double.parseDouble(decimalFormat.format(coordinates[i]));

        return coordinates[0] + ", " + coordinates[1] + ", " + coordinates[2];
    }

    public static boolean areCoordinates(String string) {
        try {
            double[] coords = coordinateStringToDouble(string);
            if (coords.length < 3 || coords.length > 5) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
