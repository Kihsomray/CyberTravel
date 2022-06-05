package net.zerotoil.dev.cybertravel.object.region.settings;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.region.Region;
import net.zerotoil.dev.cybertravel.utility.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RegionLocation {

    private final CyberTravel main;
    private Region region;
    private boolean binded = false;

    private String world;
    @Getter private double[] upperCorner;
    @Getter private double[] lowerCorner;

    public RegionLocation(CyberTravel main) {
        this.main = main;
    }

    public RegionLocation(CyberTravel main, Region region, String world, double[] pos1, double[] pos2) {
        this.main = main;
        bind(region);
        this.world = world;
        setCorners(pos1, pos2);
    }

    public boolean setPos(@NotNull Location location, boolean pos1) {

        double[] pos = new double[]{location.getX(), location.getY(), location.getZ()};
        main.cache().regionFactory().roundCoordinates(pos);
        World world = location.getWorld();
        assert world != null;

        return (pos1 ? setPos1(pos, world) : setPos2(pos, world));
    }

    public boolean setPos1(double[] coordinates, @NotNull World world) {

        if (lowerCorner == null || !this.world.equals(world.getName())) {
            upperCorner = coordinates;

            if (this.world != null && !this.world.equals(world.getName())) {
                lowerCorner = null;
                this.world = world.getName();
                return false;
            }

            this.world = world.getName();

        } else {
            setCorners(coordinates, lowerCorner);
        }

        return true;

    }

    public boolean setPos2(double[] coordinates, @NotNull World world) {

        if (upperCorner == null || !this.world.equals(world.getName())) {
            lowerCorner = coordinates;

            if (this.world != null && !this.world.equals(world.getName())) {
                upperCorner = null;
                this.world = world.getName();
                return false;
            }

            this.world = world.getName();

        } else {
            setCorners(coordinates, upperCorner);
        }

        return true;

    }

    public boolean bind(Region region) {
        if (binded) return false;
        this.region = region;
        binded = true;
        return true;
    }

    public boolean isReady() {
        return (upperCorner != null && lowerCorner != null);
    }
    public boolean isBinded() {
        return binded;
    }

    private void setCorners(double[] pos1, double[] pos2) {
        // duplicate positions
        double[] upperCorner = Arrays.copyOf(pos1, pos1.length);
        double[] lowerCorner = Arrays.copyOf(pos2, pos2.length);

        // copy max/min
        for (int i = 0; i <= 2; i++) {
            upperCorner[i] = Math.max(pos1[i], pos2[i]);
            lowerCorner[i] = Math.min(pos1[i], pos2[i]);
        }

        // round coordinates as needed
        main.cache().regionFactory().roundCoordinates(upperCorner);
        main.cache().regionFactory().roundCoordinates(lowerCorner);

        // set the values
        this.upperCorner = upperCorner;
        this.lowerCorner = lowerCorner;

    }

    public double getUpperX() {
        return upperCorner[0];
    }
    public double getUpperY() {
        return upperCorner[1];
    }
    public double getUpperZ() {
        return upperCorner[2];
    }

    public double getLowerX() {
        return lowerCorner[0];
    }
    public double getLowerY() {
        return lowerCorner[1];
    }
    public double getLowerZ() {
        return lowerCorner[2];
    }

    public Location getUpperLocation() {
        return WorldUtils.doubleArrayToLocation(world, upperCorner);
    }
    public Location getLowerLocation() {
        return WorldUtils.doubleArrayToLocation(world, lowerCorner);
    }

    public String getUpperString() {
        return WorldUtils.coordinatesToString(upperCorner, true);
    }
    public String getLowerString() {
        return WorldUtils.coordinatesToString(lowerCorner, true);
    }

    public double[] getMidpoint() {
        return new double[]{average(getUpperX(), getLowerX()),
                average(getUpperY(), getLowerY()),
                average(getUpperZ(), getLowerZ())};
    }
    private double average(double val1, double val2) {
        return (val1 + val2) / 2;
    }

    public String getWorldName() {
        return world;
    }
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

}
