package net.zerotoil.cybertravel.utilities;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.RegionObject;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BlockUtils {

    private CyberTravel main;

    public BlockUtils(CyberTravel main) {
        this.main = main;
    }

    public void regionOutline(Player player, String region, boolean settpOnly) {

        if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") ||
                Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11") || Bukkit.getVersion().contains("1.8")) return;

        if (main.getFileUtils().dataFile().isConfigurationSection("regions." + region)) {

            if (main.getPlayerCache().getRegions().containsKey(region)) {

                double i = 0.5, d = -0.5;

                RegionObject r = main.getPlayerCache().getRegions().get(region);

                double[] corner1;
                double[] corner2;
                double[] corner3;
                double[] corner4;
                double[] corner5;
                double[] corner6;
                double[] corner7;
                double[] corner8;

                if (settpOnly) {

                    double[] tp = r.getSetTP();

                    corner1 = new double[]{tp[0] + i, tp[1] + 2, tp[2] + i};
                    corner2 = new double[]{tp[0] + d, tp[1] + 2, tp[2] + i};
                    corner3 = new double[]{tp[0] + i, tp[1] + 2, tp[2] + d};
                    corner4 = new double[]{tp[0] + d, tp[1] + 2, tp[2] + d};

                    corner5 = new double[]{tp[0] + i, tp[1], tp[2] + i};
                    corner6 = new double[]{tp[0] + d, tp[1], tp[2] + i};
                    corner7 = new double[]{tp[0] + i, tp[1], tp[2] + d};
                    corner8 = new double[]{tp[0] + d, tp[1], tp[2] + d};

                } else {

                    corner1 = new double[]{r.getPosMax(0) + i, r.getPosMax(1), r.getPosMax(2) + i};
                    corner2 = new double[]{r.getPosMin(0) + d, r.getPosMax(1), r.getPosMax(2) + i};
                    corner3 = new double[]{r.getPosMax(0) + i, r.getPosMax(1), r.getPosMin(2) + d};
                    corner4 = new double[]{r.getPosMin(0) + d, r.getPosMax(1), r.getPosMin(2) + d};

                    corner5 = new double[]{r.getPosMax(0) + i, r.getPosMin(1), r.getPosMax(2) + i};
                    corner6 = new double[]{r.getPosMin(0) + d, r.getPosMin(1), r.getPosMax(2) + i};
                    corner7 = new double[]{r.getPosMax(0) + i, r.getPosMin(1), r.getPosMin(2) + d};
                    corner8 = new double[]{r.getPosMin(0) + d, r.getPosMin(1), r.getPosMin(2) + d};

                }

                // top
                makeLine(corner1, corner2, player);
                makeLine(corner1, corner3, player);
                makeLine(corner4, corner2, player);
                makeLine(corner4, corner3, player);

                // bottom
                makeLine(corner5, corner6, player);
                makeLine(corner5, corner7, player);
                makeLine(corner8, corner6, player);
                makeLine(corner8, corner7, player);

                // connect bottom with top
                makeLine(corner1, corner5, player);
                makeLine(corner2, corner6, player);
                makeLine(corner3, corner7, player);
                makeLine(corner4, corner8, player);

            } else {

                main.getMessageUtils().sendMessage("lang", "messages.location-not-set-up", "&cThat location is not set up yet!", player);

            }

        } else {

            main.getMessageUtils().sendMessage("lang", "messages.unknown-region", "&c" + region + " is not a region!", player, "region", region);

        }


    }

    private void makeLine(double[] firstCorner, double[] secondCorner, Player player) {

        double[] lesserCorner = new double[firstCorner.length], greaterCorner = new double[secondCorner.length];
        int coordinateIndex = 0;

        // finds the lesser and greater corner
        for (int i = 0; i <= 2; i++) {

            if (firstCorner[i] > secondCorner[i]) {
                greaterCorner = firstCorner;
                lesserCorner = secondCorner;
                coordinateIndex = i;
                break;
            } else if (firstCorner[i] < secondCorner[i]) {
                lesserCorner = firstCorner;
                greaterCorner = secondCorner;
                coordinateIndex = i;
                break;
            } else {

            }

        }

        Particle.DustOptions preDustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 255), 1.0F);
        int preTimer = 10;

        if (main.getFileUtils().configFile().isSet("config.display-border.rgb")) {
            List<Integer> rgb = main.getFileUtils().configFile().getIntegerList("config.display-border.rgb");
            preDustOptions = new Particle.DustOptions(Color.fromRGB(rgb.get(0), rgb.get(1), rgb.get(2)), 1.0F);
        }
        if (main.getFileUtils().configFile().isSet("config.display-border.seconds")) {
            preTimer = main.getFileUtils().configFile().getInt("config.display-border.seconds");
        }

        final Particle.DustOptions dustOptions = preDustOptions;
        final int timer = preTimer;

        for (int i = (int)lesserCorner[coordinateIndex]; i < (int)greaterCorner[coordinateIndex]; i++) {

            Location particleLocation1;
            Location particleLocation2;

            // along the x-axis
            if (coordinateIndex == 0) {
                particleLocation1 = new Location(player.getWorld(), i, lesserCorner[1], lesserCorner[2]);
                particleLocation2 = new Location(player.getWorld(), i + 0.5, lesserCorner[1], lesserCorner[2]);

            // along the y-axis
            } else if (coordinateIndex == 1) {
                particleLocation1 = new Location(player.getWorld(), lesserCorner[0], i, lesserCorner[2]);
                particleLocation2 = new Location(player.getWorld(), lesserCorner[0], i + 0.5, lesserCorner[2]);

            // along the z-axis
            } else {
                particleLocation1 = new Location(player.getWorld(), lesserCorner[0], lesserCorner[1], i);
                particleLocation2 = new Location(player.getWorld(), lesserCorner[0], lesserCorner[1], i + 0.5);

            }

            for (int a = 0; a < timer * 2; a++) {

                (new BukkitRunnable() {

                    @Override
                    public void run() {
                        particleLocation1.getWorld().spawnParticle(Particle.REDSTONE, particleLocation1, 1, dustOptions);
                        particleLocation2.getWorld().spawnParticle(Particle.REDSTONE, particleLocation2, 1, dustOptions);
                    }

                }).runTaskLater(main, 10L * a);

            }

        }

        Location particleLocation3 = new Location(player.getWorld(), greaterCorner[0], greaterCorner[1], greaterCorner[2]);

        for (int a = 0; a < timer * 2; a++) {

            (new BukkitRunnable() {

                @Override
                public void run() {
                    particleLocation3.getWorld().spawnParticle(Particle.REDSTONE, particleLocation3, 1, dustOptions);
                }

            }).runTaskLater(main, 10L * a);

        }

    }

}
