package net.zerotoil.fasttravelcp.objects;

import java.util.ArrayList;
import java.util.List;

public class RegionObject {


    private double[] pos1;
    private double[] pos2;
    private double[] setTP;
    private String region;
    private String world;

    public RegionObject() {

    }

    public RegionObject(String region, String world, double[] pos1, double[] pos2, double[] setTP) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.setTP = setTP;
        this.region = region;
        this.world = world;
    }


    public double[] getPos1() {
        return pos1;
    }

    public double[] getPos2() {
        return pos2;
    }

    public double[] getSetTP() {
        return setTP;
    }

    public String getRegion() {
        return region;
    }

    public String getWorld() {
        return world;
    }

    public double getPosMin(int arrayNumber) {
        return Math.min(this.pos1[arrayNumber], this.pos2[arrayNumber]);
    }

    public double getPosMax(int arrayNumber) {
        return Math.max(this.pos1[arrayNumber], this.pos2[arrayNumber]);
    }


    public void setPos1(double[] pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(double[] pos2) {
        this.pos2 = pos2;
    }

    public void setSetTP(double[] setTP) {
        this.setTP = setTP;
    }

    public void setRegions(String region) {
        this.region = region;
    }

    public void setWorlds(String world) {
        this.world = world;
    }
}
