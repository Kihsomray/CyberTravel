package net.zerotoil.dev.cybertravel;

import net.zerotoil.dev.cybercore.CoreSettings;
import net.zerotoil.dev.cybercore.CyberCore;
import net.zerotoil.dev.cybercore.utilities.GeneralUtils;
import net.zerotoil.dev.cybertravel.hook.Hooks;
import net.zerotoil.dev.cybertravel.cache.Cache;
import net.zerotoil.dev.cybertravel.command.CTRCommand;
import net.zerotoil.dev.cybertravel.listener.Events;
import net.zerotoil.dev.cybertravel.object.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

public final class CyberTravel extends JavaPlugin {

    private CyberCore core;

    private Cache cache;
    private Hooks hooks;
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
        hooks = new Hooks(this);
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
    public Hooks addons() {
        return hooks;
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
        if (placeholders != null) placeholders = Arrays.copyOf(placeholders, placeholders.length);
        if (replacements != null) replacements = Arrays.copyOf(replacements, replacements.length);
        PlayerData playerData = cache().getPlayer(player);
        core.sendMessage(
                player,
                messageKey,
                player != null ? GeneralUtils.combineArrays(placeholders, playerData.getPlaceholders()) : placeholders,
                player != null ? GeneralUtils.combineArrays(replacements, playerData.getPlaceholders()) : replacements
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