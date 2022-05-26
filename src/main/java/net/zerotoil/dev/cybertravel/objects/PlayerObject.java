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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PlayerObject {

    private final CyberTravel main;
    @Getter private final Player player;

    private List<Region> regions;

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
                scanner.nextLine();
                regions = PlayerUtils.stringToRegionList(main, scanner.nextLine());
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
            String content = player.getName() + "\n" + Arrays.toString(regions.toArray());
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


}
