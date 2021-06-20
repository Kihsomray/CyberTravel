package net.zerotoil.fasttravelcp;

import net.zerotoil.fasttravelcp.cache.FileCache;
import net.zerotoil.fasttravelcp.cache.PlayerCache;
import net.zerotoil.fasttravelcp.listeners.MovementListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FastTravelCP extends JavaPlugin {

    private static FastTravelCP instance;

    public static FastTravelCP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // store files to cache
        FileCache.initializeFiles();

        new FTPCommand(this);

        PlayerCache.refreshRegionData(false);
        PlayerCache.initializePlayerData();

        Bukkit.getPluginManager().registerEvents(new MovementListener(), instance);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
