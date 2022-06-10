package net.zerotoil.dev.cybertravel.utility;

import com.sun.javafx.geom.Vec3d;
import net.zerotoil.dev.cybertravel.cache.Config;
import net.zerotoil.dev.cybertravel.object.region.Region;
import net.zerotoil.dev.cybertravel.object.region.settings.RegionLocation;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

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

    public static DecimalFormat formatDecimals(short amount) {

        String s = "#";
        if (amount > 0) s = s + ".";
        for (short i = 0; i < amount; i++) s = s + "#";

        return new DecimalFormat(s);

    }

    public static void generateCube(Region region) {
        if (!region.getMain().cache().config().isDisplayBorderEnabled()) return;
        Config config = region.getMain().cache().config();
        generateCube(
                region.getMain(),
                region.getLocation().getWorld(),
                region.getLocation().getUpperCorner(),
                region.getLocation().getLowerCorner(),
                config.getDisplayBorderSeconds(),
                config.getDisplayBorderInterval(),
                config.getDisplayBorderDust(),
                config.getRoundDecimalsAmount()
        );
    }

    public static void generateCube(JavaPlugin plugin,
                                    World world,
                                    double[] upperCorner,
                                    double[] lowerCorner,
                                    long displayBorderSeconds,
                                    double displayBorderInterval,
                                    @NotNull Particle.DustOptions dustOptions,
                                    short roundCoordinatesAmount) {

        if (roundCoordinatesAmount == 0) {
            for (int i = 0; i < upperCorner.length; i++) upperCorner[i] += 1;
        }

        double[] underUpper = new double[]{upperCorner[0], lowerCorner[1], upperCorner[2]};
        double[] aboveLower = new double[]{lowerCorner[0], upperCorner[1], lowerCorner[2]};

        double[] besideUpper = new double[]{lowerCorner[0], upperCorner[1], upperCorner[2]};
        double[] besideLower = new double[]{upperCorner[0], lowerCorner[1], lowerCorner[2]};

        for (int i = 0; i < Math.max(displayBorderSeconds, 1) * 2; i ++) {
            (new BukkitRunnable() {
                @Override
                public void run() {

                    // corner 1
                    generateLine(world, upperCorner, lowerCorner[0], 0, displayBorderInterval, dustOptions);
                    generateLine(world, upperCorner, lowerCorner[1], 1, displayBorderInterval, dustOptions);
                    generateLine(world, upperCorner, lowerCorner[2], 2, displayBorderInterval, dustOptions);

                    // corner 2
                    generateLine(world, lowerCorner, upperCorner[0], 0, displayBorderInterval, dustOptions);
                    generateLine(world, lowerCorner, upperCorner[1], 1, displayBorderInterval, dustOptions);
                    generateLine(world, lowerCorner, upperCorner[2], 2, displayBorderInterval, dustOptions);

                    // under corner 1
                    generateLine(world, underUpper, lowerCorner[0], 0, displayBorderInterval, dustOptions);
                    generateLine(world, underUpper, lowerCorner[2], 2, displayBorderInterval, dustOptions);

                    // above corner 2
                    generateLine(world, aboveLower, upperCorner[0], 0, displayBorderInterval, dustOptions);
                    generateLine(world, aboveLower, upperCorner[2], 2, displayBorderInterval, dustOptions);

                    // beside corner 1
                    generateLine(world, besideUpper, lowerCorner[1], 1, displayBorderInterval, dustOptions);

                    // beside corner 2
                    generateLine(world, besideLower, upperCorner[1], 1, displayBorderInterval, dustOptions);

                }
            }).runTaskLater(plugin, 10L * i);
        }

    }


    public static void generateLine(@NotNull World world, double[] start, double endCoordinate, int type, double interval, @NotNull Particle.DustOptions dustOptions) {

        double[] next = Arrays.copyOf(start, start.length);
        next[type] = Math.min(start[type], endCoordinate);
        endCoordinate = Math.max(start[type], endCoordinate);

        while (next[type] < endCoordinate) {
            world.spawnParticle(Particle.REDSTONE, next[0], next[1], next[2], 1, dustOptions);
            next[type] += interval;
        }
        next[type] = endCoordinate;
        world.spawnParticle(Particle.REDSTONE, next[0], next[1], next[2], 1, dustOptions);

    }



}
