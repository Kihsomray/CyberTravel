package net.zerotoil.dev.cybertravel;

import net.zerotoil.dev.cybercore.CoreSettings;
import net.zerotoil.dev.cybercore.CyberCore;
import net.zerotoil.dev.cybercore.utilities.GeneralUtils;
import net.zerotoil.dev.cybertravel.addons.Addons;
import net.zerotoil.dev.cybertravel.cache.Cache;
import net.zerotoil.dev.cybertravel.commands.CTRCommand;
import net.zerotoil.dev.cybertravel.events.Events;
import net.zerotoil.dev.cybertravel.objects.PlayerObject;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public final class CyberTravel extends JavaPlugin {

    private CyberCore core;

    private Cache cache;
    private Addons addons;
    private Events events;

    @Override
    public void onEnable() {

        if (!CyberCore.restrictVersions(7, 18, "CTR", getDescription().getVersion())) return;

        reloadCore();
        loadPlugin();
        core.loadFinish();

    }

    public void reloadCore() {
        core = new CyberCore(this);
        CoreSettings settings = core.getSettings();
        settings.setBootColor('c');
        String author = "&7Author: &f" + getAuthors();
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
                "&c/    \\  \\/   |    |    |       _/",
                "&c\\     \\____  |    |    |    |   \\",
                "&c \\______  /  |____|    |____|_  /",
                "&c        \\/                    \\/ ",
                author,
                version);
        core.loadStart("regions", "plugin-data");
        File playerData = new File(getDataFolder(),"player_data");
        if (!playerData.exists()) playerData.mkdirs();
    }

    private void loadPlugin() {
        reloadPlugin();
        events = new Events(this);
        new CTRCommand(this);
    }

    public void reloadPlugin() {
        cache = new Cache(this);
        cache.load(false);
        addons = new Addons(this);
    }

    @Override
    public void onDisable() {
        cache.unloadPlayers();
    }

    public CyberCore core() {
        return core;
    }

    /**
     * Stores the elementary data of regions
     * and players.
     *
     * @return Data stored in memory
     */
    public Cache cache() {
        return cache;
    }

    /**
     * Addon manager of the plugin.
     *
     * @return Addon manager
     */
    public Addons addons() {
        return addons;
    }

    /**
     * Events manager of the plugin.
     *
     * @return Events manager
     */
    public Events events() {
        return events;
    }

    /**
     * Send a message to a player or console
     * if the player is null. The player's
     * placeholders and chat colors will be
     * applied in the process.
     *
     * @param player Player to send to
     * @param messageKey Message in lang.yml
     * @return true always
     */
    public boolean sendMessage(@Nullable Player player, @NotNull String messageKey) {
        return sendMessage(player, messageKey, null);
    }

    /**
     * Send a message to a player or console
     * if the player is null. The player's
     * placeholders and chat colors will be
     * applied in the process.
     *
     * Additional placeholders may be parsed
     * if included in the placeholders field.
     *
     * @param player Player to send to
     * @param messageKey Message in lang.yml
     * @param placeholders Array of placeholders
     * @param replacements Array of replacements for placeholders
     * @return true always
     */
    public boolean sendMessage(Player player, String messageKey, String[] placeholders, String... replacements) {
        PlayerObject playerObject = cache().getPlayer(player);
        core.sendMessage(
                player,
                messageKey,
                playerObject != null ? GeneralUtils.combineArrays(placeholders, playerObject.getPlaceholders()) : placeholders,
                playerObject != null ? GeneralUtils.combineArrays(replacements, playerObject.getPlaceholders()) : replacements
        );
        return true;
    }

    /**
     * Gets the authors of this plugin.
     *
     * @return Authors of this plugin
     */
    public String getAuthors() {
        return this.getDescription().getAuthors().toString().replace("[", "").replace("]", "");
    }

    /**
     * Sends a message to console with
     * colors parsed.
     *
     * @param message Message to send
     */
    public void logger(String... message) {
        core.logger(message);
    }

}