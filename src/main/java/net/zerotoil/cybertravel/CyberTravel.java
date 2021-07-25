package net.zerotoil.cybertravel;

import net.zerotoil.cybertravel.cache.FileCache;
import net.zerotoil.cybertravel.cache.PlayerCache;
import net.zerotoil.cybertravel.listeners.MovementListener;
import net.zerotoil.cybertravel.utilities.FileUtils;
import net.zerotoil.cybertravel.utilities.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberTravel extends JavaPlugin {

    private FileCache fileCache;
    private PlayerCache playerCache;
    private CTPCommand ctpCommand;
    private CTPTabComplete ctpTabComplete;
    private FileUtils fileUtils;
    private MessageUtils messageUtils;

    public FileCache getFileCache() {
        return this.fileCache;
    }
    public PlayerCache getPlayerCache() {
        return this.playerCache;
    }
    public CTPCommand getCtpCommand() {
        return this.ctpCommand;
    }
    public CTPTabComplete getCtpTabComplete() {
        return this.ctpTabComplete;
    }
    public FileUtils getFileUtils() {
        return this.fileUtils;
    }
    public MessageUtils getMessageUtils() {
        return this.messageUtils;
    }

    private static CyberTravel instance;

    @Override
    public void onEnable() {
        instance = this;

        // store files to cache
        fileCache = new FileCache(this);
        this.fileCache.initializeFiles();

        // init commands
        ctpCommand = new CTPCommand(this);
        ctpTabComplete = new CTPTabComplete(this);
        this.getCommand("ctp").setTabCompleter(ctpTabComplete);

        fileUtils = new FileUtils(this);
        messageUtils = new MessageUtils(this);

        // player data to cache
        playerCache = new PlayerCache(this);
        this.playerCache.refreshRegionData(false);
        this.playerCache.initializePlayerData();

        // listeners
        Bukkit.getPluginManager().registerEvents(new MovementListener(this), instance);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
