package net.zerotoil.dev.cybertravel.object.region;

import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.region.settings.RegionLocation;
import net.zerotoil.dev.cybertravel.utility.WorldUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

public class RegionFactory {

    private final CyberTravel main;
    private final HashMap<Player, RegionLocation> playerSetPositions = new HashMap<>();

    private DecimalFormat decimalFormat;

    /**
     * Allows for the creation of new regions.
     *
     * @param main Main instance
     */
    public RegionFactory(CyberTravel main) {
        this.main = main;
        decimalFormat();
    }

    private void decimalFormat() {
        ConfigurationSection section = main.core().files().getConfig("config").getConfigurationSection("config");
        if (section == null) return;
        if (section.getBoolean("round-coordinates.enabled", true)) {
            decimalFormat = WorldUtils.formatDecimals((short) section.getInt("round-coordinates.amount", 0));
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        }
    }

    /**
     * Set the first location of a new region.
     * Null player will consider this as if it
     * was being set from the console.
     *
     * @param player Player in question
     * @param location Position 1 location
     */
    public void setPos1(@Nullable Player player, @NotNull Location location) {
        setPos(player, location, true);
    }

    /**
     * Set the second location of a new region.
     * Null player will consider this as if it
     * was being set from the console.
     *
     * @param player Player in question
     * @param location Position 2 location
     */
    public void setPos2(@Nullable Player player, @NotNull Location location) {
        setPos(player, location, false);
    }

    // sets position of player
    private void setPos(Player player, @NotNull Location location, boolean pos1) {
        playerSetPositions.putIfAbsent(player, new RegionLocation(main));
        RegionLocation regionLocation = playerSetPositions.get(player);
        boolean bool = regionLocation.setPos(location, pos1);

        // added position message
        main.sendMessage(player, "added-position", new String[]{
                    "positionNumber",
                    "positionLocation"
                },
                pos1 ? "Position 1" : "Position 2",
                pos1 ? regionLocation.getUpperString() : regionLocation.getLowerString()
        );

        // warns player if other position in another world
        if (!bool) main.sendMessage(player, "different-world");

    }

    /**
     * Creates a region with the two locations
     * set with setPos1() and setPos2(). Null
     * player will consider this as if it was
     * being set from the console.
     *
     * @param player Player in question
     * @param id Region ID number
     */
    public void createRegion(@Nullable Player player, @NotNull String id) {
        RegionLocation location = playerSetPositions.get(player);
        if (location == null || !location.isReady()) {
            main.sendMessage(player, "insufficient-positions");
            return;
        }

        if (main.cache().isRegion(id)) {
            main.sendMessage(player, "region-exists", new String[]{"regionID"}, id);
            return;
        }

        main.sendMessage(player, "region-created", new String[]{"regionID"}, id);
        main.cache().createRegion(id, playerSetPositions.get(player));

    }

    public void roundCoordinates(double[] coordinates) {

        if (decimalFormat == null) return;
        for (int i = 0; i < coordinates.length; i++)
            coordinates[i] = Long.parseLong(decimalFormat.format(coordinates[i]));

    }

}
