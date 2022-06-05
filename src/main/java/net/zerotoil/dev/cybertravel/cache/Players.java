package net.zerotoil.dev.cybertravel.cache;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Players {

    private final CyberTravel main;
    private final Cache cache;

    @Getter private Map<String, PlayerData> players;

    public Players(Cache cache) {
        this.main = cache.main;
        this.cache = cache;
    }

    /**
     * Load all online players.
     */
    public void loadPlayers() {
        players = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers())
            loadPlayer(player);
    }

    /**
     * Unload all players in cache.
     */
    public void unloadPlayers() {
        for (Player player : Bukkit.getOnlinePlayers())
            savePlayer(player);
        players = new HashMap<>();
    }

    /**
     * Loads a certain player's data into cache.
     * If the player does not have any data yet,
     * then it will automatically generate.
     *
     * @param player Player in question
     * @return False if player is already in cache
     */
    public boolean loadPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (players.containsKey(uuid)) return false;
        players.put(uuid, new PlayerData(main, player));
        return true;
    }

    /**
     * Saves a certain player's data to file
     * and removes them from cache.
     *
     * @param player Player in question
     * @return False if player is not in cache
     */
    public boolean unloadPlayer(Player player) {
        boolean bool = savePlayer(player);
        players.remove(player.getUniqueId().toString());
        return bool;
    }

    /**
     * Saves a certain player's data to file.
     *
     * @param player Player in question
     * @return False if player is not in cache
     */
    public boolean savePlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!players.containsKey(uuid)) return false;
        players.get(uuid).saveData();
        return true;
    }

    /**
     * Gets a certain player's data based on
     * the bukkit player object.
     *
     * @param player Player in question
     * @return Player data saved to cache
     */
    public PlayerData getPlayer(Player player) {
        return player != null ? getPlayer(player.getUniqueId().toString()) : null;
    }

    /**
     * Gets a certain player's data based on
     * that player's UUID.
     *
     * @param uuid UUID of player in question
     * @return Player data saved to cache
     */
    public PlayerData getPlayer(String uuid) {
        return players.get(uuid);
    }


}
