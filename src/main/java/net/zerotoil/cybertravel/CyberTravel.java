package net.zerotoil.cybertravel;

import net.zerotoil.cybertravel.addons.Metrics;
import net.zerotoil.cybertravel.addons.Vault;
import net.zerotoil.cybertravel.cache.FileCache;
import net.zerotoil.cybertravel.cache.PlayerCache;
import net.zerotoil.cybertravel.commands.CTPCommand;
import net.zerotoil.cybertravel.commands.CTPTabComplete;
import net.zerotoil.cybertravel.listeners.MovementListener;
import net.zerotoil.cybertravel.objects.ConfigObject;
import net.zerotoil.cybertravel.utilities.BlockUtils;
import net.zerotoil.cybertravel.utilities.FileUtils;
import net.zerotoil.cybertravel.utilities.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberTravel extends JavaPlugin {

    private FileCache fileCache;
    private ConfigObject configCache;
    private PlayerCache playerCache;
    private CTPCommand ctpCommand;
    private CTPTabComplete ctpTabComplete;
    private FileUtils fileUtils;
    private MessageUtils messageUtils;
    private BlockUtils blockUtils;
    private Vault vault;

    public FileCache getFileCache() {
        return this.fileCache;
    }
    public ConfigObject getConfigCache() {
        return this.configCache;
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
    public BlockUtils getBlockUtils() {
        return this.blockUtils;
    }
    public Vault getVault() {
        return this.vault;
    }

    private CyberTravel instance;

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

        configCache = new ConfigObject(this);

        // player data to cache
        playerCache = new PlayerCache(this);
        this.playerCache.refreshRegionData(false);
        this.playerCache.initializePlayerData();

        blockUtils = new BlockUtils(this);

        // listeners
        Bukkit.getPluginManager().registerEvents(new MovementListener(this), instance);

        Metrics metrics = new Metrics(this, 12217);
        vault = new Vault(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
