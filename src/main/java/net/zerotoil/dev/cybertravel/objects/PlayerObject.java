package net.zerotoil.dev.cybertravel.objects;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.objects.regions.Region;
import net.zerotoil.dev.cybertravel.utilities.PlayerUtils;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PlayerObject {

    private final CyberTravel main;
    @Getter private final Player player;

    private final String[] placeholders = new String[]{"player", "playerDisplayName", "playerUUID", "regions", "cooldown", "currentRegion"};

    private Map<String, Region> regions = new HashMap<>();

    /**
     * Stores data about a player and the regions
     * they have discovered.
     *
     * @param main Main instance
     * @param player Player in question
     */
    public PlayerObject(CyberTravel main, Player player) {
        this.main = main;
        this.player = player;
        loadData();
    }

    /**
     * Add a region to a player's discovered
     * regions. Must be undiscovered.
     *
     * @param region Region to add
     * @return False if already discovered
     */
    public boolean addRegion(Region region) {
        return addRegion(region.getId());
    }

    /**
     * Add a region to a player's discovered
     * regions. Must be undiscovered.
     *
     * @param region Region to add
     * @return False if already discovered
     */
    public boolean addRegion(String region) {
        if (isDiscovered(region)) return false;
        if (!main.cache().isRegion(region)) return false;
        regions.put(region, main.cache().getRegion(region));

        regions.get(region).getMessage().sendMessage(player);
        return true;
    }

    /**
     * Checks if the region is discovered.
     *
     * @param region Region in question
     * @return If region is discovered
     */
    public boolean isDiscovered(String region) {
        return regions.containsKey(region);
    }

    /**
     * Checks if the region is discovered.
     *
     * @param region Region in question
     * @return If region is discovered
     */
    public boolean isDiscovered(Region region) {
        return regions.containsValue(region);
    }

    /**
     * Loads data for the player. Creates a player
     * data file if one does not already exist.
     */
    private void loadData() {
        File playerFile = getPlayerFile();
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
                saveData(true);
            } else {
                Scanner scanner = new Scanner(playerFile);
                if (!scanner.hasNextLine()) {
                    saveData(true);
                    scanner = new Scanner(playerFile);
                }
                scanner.nextLine();
                regions = PlayerUtils.stringToRegionMap(main, scanner.nextLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
            main.logger("&cFailed to make file for " + player.getName() + ".");
        }
    }

    /**
     * Save the player's data to file.
     */
    public void saveData() {
        saveData(false);
    }

    // Saves the player's data to their file.
    private void saveData(boolean initial) {
        try {
            String content = player.getName() + "\n" + Arrays.toString(regions.keySet().toArray());
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(getPlayerFile().getAbsolutePath()));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            if (initial) throw new IllegalArgumentException();
            else main.logger("&cFailed to save data for " + player.getName() + ".");
        }
    }

    // Returns the player's file.
    private File getPlayerFile() {
        return new File(main.getDataFolder() + File.separator + "player_data", player.getUniqueId() + ".ctr");
    }

    /**
     * Get the player's placeholders.
     *
     * @return Array of placeholders
     */
    public String[] getPlaceholders() {
        return placeholders;
    }

    /**
     * Get the player's placeholder replacements.
     *
     * @return Array of placeholder replacements
     */
    public String[] getReplacements() {
        String[] replacements = new String[placeholders.length];

        // player placeholders
        replacements[0] = player.getName();
        replacements[1] = player.getDisplayName();
        replacements[2] = player.getUniqueId().toString();

        // list of region placeholders
        replacements[3] = Arrays.toString(regions.values().toArray())
                .replace("[", "").replace( "]", "");

        replacements[4] = ""; // TODO add this
        replacements[5] = getCurrentRegionString();

        return replacements;
    }

    /**
     * Returns a region in which the player is
     * located. Will return null if it is not
     * within a region.
     *
     * @return Region the location is in
     */
    public Region getCurrentRegion() {
        return main.cache().getRegionAt(player.getLocation());
    }

    /**
     * Returns a region in which the player is
     * located. Will return "None" if it is not
     * within a region.
     *
     * @return Region the location is in
     */
    public String getCurrentRegionString() {
        Region region = getCurrentRegion();
        if (region == null) return main.core().getLangValue(player, "lang", "placeholders.null-region");
        return region.getDisplayName();
    }

}
