package net.zerotoil.cybertravel;

import net.zerotoil.cybertravel.addons.Metrics;
import net.zerotoil.cybertravel.addons.PlaceholderAPI;
import net.zerotoil.cybertravel.addons.Vault;
import net.zerotoil.cybertravel.cache.*;
import net.zerotoil.cybertravel.commands.CTPCommand;
import net.zerotoil.cybertravel.commands.CTPTabComplete;
import net.zerotoil.cybertravel.listeners.MovementListener;
import net.zerotoil.cybertravel.utilities.BlockUtils;
import net.zerotoil.cybertravel.utilities.FileUtils;
import net.zerotoil.cybertravel.utilities.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CyberTravel extends JavaPlugin {

    private FileCache fileCache;
    private ConfigCache configCache;
    private LangCache langCache;
    private PlayerCache playerCache;
    private RegionCache regionCache;

    private CTPCommand ctpCommand;
    private CTPTabComplete ctpTabComplete;

    private FileUtils fileUtils;
    private LangUtils langUtils;
    private BlockUtils blockUtils;

    private Vault vault;

    public FileCache getFileCache() {
        return this.fileCache;
    }
    public ConfigCache getConfigCache() {
        return this.configCache;
    }
    public PlayerCache getPlayerCache() {
        return this.playerCache;
    }
    public LangCache getLangCache() {
        return this.langCache;
    }
    public RegionCache getRegionCache() {
        return this.regionCache;
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
    public LangUtils getLangUtils() {
        return this.langUtils;
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
        configCache = new ConfigCache(this);
        langCache = new LangCache(this);

        // init commands
        ctpCommand = new CTPCommand(this);
        ctpTabComplete = new CTPTabComplete(this);
        this.getCommand("ctp").setTabCompleter(ctpTabComplete);

        fileUtils = new FileUtils(this);
        langUtils = new LangUtils(this);

        // player/region data to cache
        regionCache = new RegionCache(this);
        playerCache = new PlayerCache(this);

        blockUtils = new BlockUtils(this);

        // listeners
        Bukkit.getPluginManager().registerEvents(new MovementListener(this), this);

        Metrics metrics = new Metrics(this, 12217);
        vault = new Vault(this);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceholderAPI(this).register();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
