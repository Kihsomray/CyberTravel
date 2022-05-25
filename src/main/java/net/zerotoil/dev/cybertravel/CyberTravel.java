package net.zerotoil.dev.cybertravel;

import net.zerotoil.dev.cybercore.CoreSettings;
import net.zerotoil.dev.cybercore.CyberCore;
import net.zerotoil.dev.cybertravel.addons.Addons;
import net.zerotoil.dev.cybertravel.cache.Cache;
import net.zerotoil.dev.cybertravel.events.Events;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CyberTravel extends JavaPlugin {

    private CyberCore core;

    private Cache cache;
    private Addons addons;
    private Events events;

    @Override
    public void onEnable() {

        if (!CyberCore.restrictVersions(7, 18, "CTP", getDescription().getVersion())) return;

        reloadCore();
        loadPlugin();
        core.loadFinish();

    }

    public void reloadCore() {
        core = new CyberCore(this);
        CoreSettings settings = core.getSettings();
        settings.setBootColor('c');
        String author = "&7Authors: &f" + getAuthors();
        String version = "&7Version: &f" + this.getDescription().getVersion();
        settings.setBootLogo(
                "&c╭━━━╮&7╱╱╱&c╭╮&7╱╱╱╱╱&c╭━━━━╮&7╱╱╱╱╱╱╱╱╱&c╭╮",
                "&c┃╭━╮┃&7╱╱╱&c┃┃&7╱╱╱╱╱&c┃╭╮╭╮┃&7╱╱╱╱╱╱╱╱╱&c┃┃",
                "&c┃┃&7╱&c╰╋╮&7╱&c╭┫╰━┳━━┳┻┫┃┃┣┻┳━━┳╮╭┳━━┫┃",
                "&c┃┃&7╱&c╭┫┃&7╱&c┃┃╭╮┃┃━┫╭╯┃┃┃╭┫╭╮┃╰╯┃┃━┫┃",
                "&c┃╰━╯┃╰━╯┃╰╯┃┃━┫┃&7╱&c┃┃┃┃┃╭╮┣╮╭┫┃━┫╰╮",
                "&c╰━━━┻━╮╭┻━━┻━━┻╯&7╱&c╰╯╰╯╰╯╰╯╰╯╰━━┻━╯",
                "&7╱╱╱╱&c╭━╯┃ " + author,
                "&7╱╱╱╱&c╰━━╯ " + version);
        settings.setLegacyBootLogo(
                "&c_________  _____________________ ",
                "&c\\_   ___ \\ \\__    ___/\\______   \\",
                "&c/    \\  \\/   |    |    |     ___/",
                "&c\\     \\____  |    |    |    |    ",
                "&c \\______  /  |____|    |____|    ",
                "&c        \\/                       ",
                author,
                version);
        core.loadStart("regions", "plugin-data");
        File playerData = new File(getDataFolder(),"player_data");
        if (!playerData.exists()) playerData.mkdirs();
    }

    public void loadPlugin() {
        reloadPlugin();
        events = new Events(this);
    }

    public void reloadPlugin() {
        cache = new Cache(this);
        addons = new Addons(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public CyberCore core() {
        return core;
    }

    public Cache cache() {
        return cache;
    }
    public Addons addons() {
        return addons;
    }
    public Events events() {
        return events;
    }


    public String getAuthors() {
        return this.getDescription().getAuthors().toString().replace("[", "").replace("]", "");
    }
    public void logger(String... msg) {
        core.logger(msg);
    }

}