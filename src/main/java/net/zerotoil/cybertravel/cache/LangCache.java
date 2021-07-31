package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.MessageObject;
import net.zerotoil.cybertravel.objects.PermissionObject;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LangCache {

    private CyberTravel main;
    private Configuration config;
    private boolean updateConfig;

    private String prefix;

    private List<String> adminHelp = new ArrayList<>();
    private List<String> playerHelp = new ArrayList<>();

    private HashMap<String, MessageObject> messages = new HashMap<>();
    private HashMap<String, PermissionObject> permissions = new HashMap<>();

    private String timeDaysFormat;
    private String timeHoursFormat;
    private String timeMinutesFormat;
    private String timeSecondsFormat;
    private String timeSplitterFormat;

    public LangCache(CyberTravel main) {

        this.main = main;
        autoUpdateConfig();
        initializeConfig();

    }

    public void autoUpdateConfig() {
        // should config auto update?
        if (main.getFileCache().getStoredFiles().get("config").getConfig().isSet("config.auto-update-configs.lang")) {
            updateConfig = main.getFileCache().getStoredFiles().get("config").getConfig().getBoolean("config.auto-update-configs.lang");
        } else {
            updateConfig = false;
        }
    }

    public void initializeConfig() {

        config = main.getFileCache().getStoredFiles().get("lang").getConfig();
        autoUpdateConfig();

        // prefix
        if (config.isSet("messages.prefix")) {
            if (!config.getString("messages.prefix").equalsIgnoreCase("")) {
                prefix = getColor(config.getString("messages.prefix") + " ");
            } else {
                prefix = "";
            }
        } else {
            if (updateConfig) setMessage("messages.prefix", "&c&lCyber&f&lTravel &8»");
            prefix = getColor("&c&lCyber&f&lTravel &8»");
        }

        // help messages
        List<String> defaultPlayerHelp = new ArrayList<>();
        List<String> defaultAdminHelp = new ArrayList<>();

        defaultPlayerHelp.add("&8&m―――――&8<&c&l Cyber&f&lTravel &8>&8&m―――――");
        defaultPlayerHelp.add("&8➼ &c/ctp about &fAbout the plugin.");
        defaultPlayerHelp.add("&8➼ &c/ctp help &fSee the help menu.");
        defaultPlayerHelp.add("&8➼ &c/ctp (tp | teleport) <region> &fTeleport to location.");
        defaultPlayerHelp.add("&8➼ &c/ctp (regions) <region> &fSee all locked/unlocked regions.");
        defaultPlayerHelp.add("&8&m――――――――――――――――――――――――――――――");

        defaultAdminHelp.add("&8&m―――――&8<&c&l Cyber&f&lTravel &8>&8&m―――――");
        defaultAdminHelp.add("&8➼ &c/ctp about &fAbout the plugin.");
        defaultAdminHelp.add("&8➼ &c/ctp help &fSee the help menu.");
        defaultAdminHelp.add("&8➼ &c/ctp reload &fReload the plugin.");
        defaultAdminHelp.add("&8➼ &c/ctp create <region> &fCreate a region in your world.");
        defaultAdminHelp.add("&8➼ &c/ctp rename <region> &fChange the ID/name of a region.");
        defaultAdminHelp.add("&8➼ &c/ctp delete <region> &fDelete an existing region.");
        defaultAdminHelp.add("&8➼ &c/ctp (pos1 | pos2) <region> &fSet cuboid boundaries of a region.");
        defaultAdminHelp.add("&8➼ &c/ctp setTP <region> &fSet teleport location.");
        defaultAdminHelp.add("&8➼ &c/ctp setDisplayName <region> <display name> &fChange the display name.");
        defaultAdminHelp.add("&8➼ &c/ctp setPrice <region> <price> &fChange the price of teleportation.");
        defaultAdminHelp.add("&8➼ &c/ctp setEnabled <region> &fEnable a region.");
        defaultAdminHelp.add("&8➼ &c/ctp setDisabled <region> &fDisable a region.");
        defaultAdminHelp.add("&8➼ &c/ctp reset playerData <region | all> &fDelete player data for a region.");
        defaultAdminHelp.add("&8➼ &c/ctp (tp | teleport) <region> <player> [bypass] &fTeleport player to region.");
        defaultAdminHelp.add("&8➼ &c/ctp regions &fSee all locked/unlocked regions.");
        defaultAdminHelp.add("&8➼ &c/ctp addPlayerRegion <region> <player> &fAdd a region to a player's list.");
        defaultAdminHelp.add("&8➼ &c/ctp delPlayerRegion <region> <player> &fRemove a region from a player's list.");
        defaultAdminHelp.add("&8&m――――――――――――――――――――――――――――――");

        if (!playerHelp.isEmpty()) playerHelp.clear();
        if (isSet("messages.help-player")) {
            for (String i : config.getStringList("messages.help-player")) playerHelp.add(getColor(i));
        } else {
            if (updateConfig) {
                config.set("messages.help-player", defaultPlayerHelp);
                try {
                    main.getFileCache().getStoredFiles().get("lang").saveConfig();
                } catch (IOException e) {}
            }
            for (String i : defaultPlayerHelp) playerHelp.add(getColor(i));
        }

        if (!adminHelp.isEmpty()) adminHelp.clear();
        if (isSet("messages.help-admin")) {
            for (String i : config.getStringList("messages.help-admin")) adminHelp.add(getColor(i));
        } else {
            if (updateConfig) {
                config.set("messages.help-admin", defaultAdminHelp);
                try {
                    main.getFileCache().getStoredFiles().get("lang").saveConfig();
                } catch (IOException e) {}
            }
            for (String i : defaultAdminHelp) adminHelp.add(getColor(i));
        }

        // messages
        if (!messages.isEmpty()) messages.clear();

        messages.put("no-permission", new MessageObject(main, updateConfig, prefix, "no-permission", "&cYou don't have permission to do that!"));
        messages.put("reloading", new MessageObject(main, updateConfig, prefix, "reloading", "&7Reloading..."));
        messages.put("reloaded", new MessageObject(main, updateConfig, prefix, "reloaded", "&aReloaded!"));
        messages.put("region-exists", new MessageObject(main, updateConfig, prefix, "region-exists", "&cThe region \"{region}&c\" already exists. Please use another name!"));
        messages.put("region-created", new MessageObject(main, updateConfig, prefix, "region-created", "&aRegion \"{region}&a\" has successfully been created!"));
        messages.put("region-renamed", new MessageObject(main, updateConfig, prefix, "region-renamed", "&aRegion \"{region}&a\" has been successfully renamed to \"{newName}\"!"));
        messages.put("region-set-display-name", new MessageObject(main, updateConfig, prefix, "region-set-display-name", "&aRegion \"{region}&a\" display name set to \"{displayName}&a\"!"));
        messages.put("unknown-region", new MessageObject(main, updateConfig, prefix, "unknown-region", "&cThe region \"{region}&c\" does not exist!"));
        messages.put("incorrect-world", new MessageObject(main, updateConfig, prefix, "incorrect-world", "&cThe region \"{region}&c\" is registered in the world \"{world}\"!"));
        messages.put("auto-ready", new MessageObject(main, updateConfig, prefix, "auto-ready", "&aLooks like you have finished setting up region \"{region}&a\"! This region is now active!"));
        messages.put("no-checkpoints", new MessageObject(main, updateConfig, prefix, "no-checkpoints", "&cYou do not have any checkpoints saved!"));
        messages.put("not-discovered", new MessageObject(main, updateConfig, prefix, "not-discovered", "&cYou still haven't been to the region \"{displayName}&c\"!"));
        messages.put("region-cooldown", new MessageObject(main, updateConfig, prefix, "region-cooldown", "&cCannot teleport to the region \"{displayName}&c\" for another {time}&c!"));
        messages.put("global-cooldown", new MessageObject(main, updateConfig, prefix, "global-cooldown", "&cCannot teleport to any region for another {time}&c!"));
        messages.put("countdown", new MessageObject(main, updateConfig, prefix, "countdown", "&7Teleporting to \"{displayName}&7\" in {time}&c..."));
        messages.put("teleport", new MessageObject(main, updateConfig, prefix, "teleport", "&aSuccessfully teleported to \"{displayName}&a\"!"));
        messages.put("enter-region", new MessageObject(main, updateConfig, prefix, "enter-region", "&aYou have entered the region \"{displayName}&a\"! Use &6/ctp tp {region} &ato teleport back to this region!"));
        messages.put("delete-region", new MessageObject(main, updateConfig, prefix, "delete-region", "&aYou have successfully deleted \"{region}&a!\""));
        messages.put("delete-failed", new MessageObject(main, updateConfig, prefix, "delete-failed", "&cThe region \"{region}&c\" cannot be deleted!"));
        messages.put("already-teleporting", new MessageObject(main, updateConfig, prefix, "already-teleporting", "&cAlready being sent to \"{displayName}&c\"! Please wait before you can teleport elsewhere."));
        messages.put("teleport-cancelled", new MessageObject(main, updateConfig, prefix, "teleport-cancelled", "&cYou moved! Teleportation canceled!"));
        messages.put("unlocked-regions-header", new MessageObject(main, updateConfig, prefix, "unlocked-regions-header", "&8&m―――――&8<&b&l Unlocked &f&lRegions &8>&8&m―――――"));
        messages.put("unlocked-regions-footer", new MessageObject(main, updateConfig, prefix, "unlocked-regions-footer", "&8&m―――――――――――――――――――――――――――――――――"));
        messages.put("unlocked-region", new MessageObject(main, updateConfig, prefix, "unlocked-region", "&8➼ &7{region} &f\"{displayName}&f\" &a&o(unlocked) &7- &a&n${price}"));
        messages.put("locked-region", new MessageObject(main, updateConfig, prefix, "locked-region", "&8➼ &7{region} &f\"{displayName}&f\" &c&o(locked)"));
        messages.put("no-regions", new MessageObject(main, updateConfig, prefix, "no-regions", "&cNo regions exist!"));
        messages.put("not-online", new MessageObject(main, updateConfig, prefix, "not-online", "&cThe player \"{player}&c\" is not currently online!"));
        messages.put("has-no-checkpoints", new MessageObject(main, updateConfig, prefix, "has-no-checkpoints", "&cThe player \"{player}&c\" has no checkpoints saved!"));
        messages.put("has-not-discovered", new MessageObject(main, updateConfig, prefix, "has-not-discovered", "&cThe player \"{player}&c\" has not yet discovered the region \"{region]\"!"));
        messages.put("deleted-player-region", new MessageObject(main, updateConfig, prefix, "deleted-player-region", "&aSuccessfully deleted \"{region}&a\" from \"{player}'s&a\" discovered regions."));
        messages.put("already-discovered", new MessageObject(main, updateConfig, prefix, "already-discovered", "&cThe player \"{player}&c\" has already found the region \"{region}&c\"!"));
        messages.put("added-player-region", new MessageObject(main, updateConfig, prefix, "added-player-region", "&aSuccessfully added \"{region}&a\" to \"{player}'s&a\" discovered regions."));
        messages.put("location-not-set-up", new MessageObject(main, updateConfig, prefix, "location-not-set-up", "&cThe region \"{region}&c\" is not set up yet!"));
        messages.put("displaying-region-border", new MessageObject(main, updateConfig, prefix, "displaying-region-border", "&aShowing the border/outline for \"{region}&c\"."));
        messages.put("teleported-player", new MessageObject(main, updateConfig, prefix, "teleported-player", "&aSuccessfully teleported \"{player}&a\" to the region \"{region}&a\"!"));
        messages.put("deleted-player-progress", new MessageObject(main, updateConfig, prefix, "deleted-player-progress", "&aSuccessfully deleted all player progress for \"{region}&a\"!"));
        messages.put("vault-not-enabled", new MessageObject(main, updateConfig, prefix, "vault-not-enabled", "&cVault is not enabled!"));
        messages.put("region-set-price", new MessageObject(main, updateConfig, prefix, "region-set-price", "&aSet the price of \"{region}&a\" to ${price} successfully!"));
        messages.put("not-enough-money", new MessageObject(main, updateConfig, prefix, "not-enough-money", "&cNot enough money (price: &a${price}&c) to teleport to \"{displayName}&c\"!"));
        messages.put("money-charged", new MessageObject(main, updateConfig, prefix, "money-charged", "&aCharged &a&n${price}&a for teleporting to \"{displayName}&a\"!"));
        messages.put("region-set-enabled", new MessageObject(main, updateConfig, prefix, "region-set-enabled", "&aThe region \"{region}&a\" has been enabled!"));
        messages.put("region-set-disabled", new MessageObject(main, updateConfig, prefix, "region-set-disabled", "&aThe region \"{region}&a\" has been disabled!"));
        messages.put("already-enabled", new MessageObject(main, updateConfig, prefix, "already-enabled", "&cThe region \"{region}&c\" is already enabled!"));
        messages.put("already-disabled", new MessageObject(main, updateConfig, prefix, "already-disabled", "&cThe region \"{region}&c\" is already disabled!"));

        // time formatting
        if (config.isConfigurationSection("time")) {

            if (isSet("time.days")) {
                timeDaysFormat = getMessage("time.days");
            } else {
                if (updateConfig) setMessage("time.days", "&c{time} Day(s)");
                timeDaysFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Day(s)");
            }
            if (isSet("time.hours")) {
                timeHoursFormat = getMessage("time.hours");
            } else {
                if (updateConfig) setMessage("time.hours", "&c{time} Hour(s)");
                timeHoursFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Hour(s)");
            }
            if (isSet("time.minutes")) {
                timeMinutesFormat = getMessage("time.minutes");
            } else {
                if (updateConfig) setMessage("time.minutes", "&c{time} Minute(s)");
                timeMinutesFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Minute(s)");
            }
            if (isSet("time.seconds")) {
                timeSecondsFormat = getMessage("time.seconds");
            } else {
                if (updateConfig) setMessage("time.seconds", "&c{time} Second(s)");
                timeSecondsFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Second(s)");
            }
            if (isSet("time.splitter")) {
                timeSplitterFormat = getMessage("time.splitter");
            } else {
                if (updateConfig) setMessage("time.splitter", "&c, ");
                timeSplitterFormat = ChatColor.translateAlternateColorCodes('&', "&c, ");
            }

        } else {

            if (updateConfig) {
                setMessage("time.days", "&c{time} Day(s)");
                setMessage("time.hours", "&c{time} Hour(s)");
                setMessage("time.minutes", "&c{time} Minute(s)");
                setMessage("time.seconds", "&c{time} Second(s)");
                setMessage("time.splitter", "&c, ");
            }

            timeDaysFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Day(s)");
            timeHoursFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Hour(s)");
            timeMinutesFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Minute(s)");
            timeSecondsFormat = ChatColor.translateAlternateColorCodes('&', "&c{time} Second(s)");
            timeSplitterFormat = ChatColor.translateAlternateColorCodes('&', "&c, ");

        }

        // permissions
        if (!permissions.isEmpty()) permissions.clear();

        permissions.put("player-about", new PermissionObject(main, updateConfig, "player-about", "CyberTravel.player.about"));
        permissions.put("player-help-list", new PermissionObject(main, updateConfig, "player-help-list", "CyberTravel.player.help"));
        permissions.put("player-discover-region", new PermissionObject(main, updateConfig, "player-discover-region", "CyberTravel.player.discover"));
        permissions.put("player-discovered-list", new PermissionObject(main, updateConfig, "player-discovered-list", "CyberTravel.player.list"));
        permissions.put("player-teleport", new PermissionObject(main, updateConfig, "player-teleport", "CyberTravel.player.teleport"));
        permissions.put("admin-help-list", new PermissionObject(main, updateConfig, "admin-help-list", "CyberTravel.admin.help"));
        permissions.put("admin-reload", new PermissionObject(main, updateConfig, "admin-reload", "CyberTravel.admin.reload"));
        permissions.put("admin-teleport-bypass", new PermissionObject(main, updateConfig, "admin-teleport-bypass", "CyberTravel.admin.bypass"));
        permissions.put("admin-view-border", new PermissionObject(main, updateConfig, "admin-view-border", "CyberTravel.admin.border"));
        permissions.put("admin-create-region", new PermissionObject(main, updateConfig, "admin-create-region", "CyberTravel.admin.edit.create"));
        permissions.put("admin-rename-region", new PermissionObject(main, updateConfig, "admin-rename-region", "CyberTravel.admin.edit.rename"));
        permissions.put("admin-set-display-name", new PermissionObject(main, updateConfig, "admin-set-display-name", "CyberTravel.admin.edit.displayname"));
        permissions.put("admin-set-price", new PermissionObject(main, updateConfig, "admin-set-price", "admin-set-price"));
        permissions.put("admin-add-command", new PermissionObject(main, updateConfig, "admin-add-command", "CyberTravel.admin.edit.addcommand"));
        permissions.put("admin-del-command", new PermissionObject(main, updateConfig, "admin-del-command", "CyberTravel.admin.edit.deletecommand"));
        permissions.put("admin-set-enabled", new PermissionObject(main, updateConfig, "admin-set-enabled", "CyberTravel.admin.edit.enable"));
        permissions.put("admin-set-disabled", new PermissionObject(main, updateConfig, "admin-set-disabled", "CyberTravel.admin.edit.disable"));
        permissions.put("admin-delete-region", new PermissionObject(main, updateConfig, "admin-delete-region", "CyberTravel.admin.edit.delete"));
        permissions.put("admin-set-position1", new PermissionObject(main, updateConfig, "admin-set-position1", "CyberTravel.admin.edit.pos1"));
        permissions.put("admin-set-position2", new PermissionObject(main, updateConfig, "admin-set-position2", "CyberTravel.admin.edit.pos2"));
        permissions.put("admin-set-tp-location", new PermissionObject(main, updateConfig, "admin-set-tp-location", "CyberTravel.admin.edit.settp"));
        permissions.put("admin-add-player-region", new PermissionObject(main, updateConfig, "admin-add-player-region", "CyberTravel.admin.manage.add"));
        permissions.put("admin-del-player-region", new PermissionObject(main, updateConfig, "admin-del-player-region", "CyberTravel.admin.manage.delete"));
        permissions.put("admin-tp-player-to-region", new PermissionObject(main, updateConfig, "admin-tp-player-to-region", "CyberTravel.admin.manage.teleport"));
        permissions.put("admin-reset-player-region-progress", new PermissionObject(main, updateConfig, "admin-reset-player-region-progress", "CyberTravel.admin.manage.reset"));

    }

    private String getColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private boolean isSet(String path) {
        return main.getFileCache().getStoredFiles().get("lang").getConfig().isSet(path);
    }
    private String getMessage(String path) {
        return main.getFileCache().getStoredFiles().get("lang").getConfig().getString(path);
    }

    private void setMessage(String path, String message) {
        main.getFileCache().getStoredFiles().get("lang").getConfig().set(path, message);
        try {
            main.getFileCache().getStoredFiles().get("lang").saveConfig();
        } catch (IOException e) {}
    }

    public Configuration getConfig() {
        return this.config;
    }
    public boolean isUpdateConfig() {
        return this.updateConfig;
    }

    public String getPrefix() {
        return this.prefix;
    }
    public List<String> getAdminHelp() {
        return adminHelp;
    }
    public List<String> getPlayerHelp() {
        return playerHelp;
    }
    public HashMap<String, MessageObject> getMessages() {
        return messages;
    }
    public HashMap<String, PermissionObject> getPermissions() {
        return permissions;
    }
    public String getTimeDaysFormat() {
        return timeDaysFormat;
    }
    public String getTimeHoursFormat() {
        return timeHoursFormat;
    }
    public String getTimeMinutesFormat() {
        return timeMinutesFormat;
    }
    public String getTimeSecondsFormat() {
        return timeSecondsFormat;
    }
    public String getTimeSplitterFormat() {
        return timeSplitterFormat;
    }
}
