package net.zerotoil.cybertravel.commands;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.RegionObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.*;

import static java.lang.System.currentTimeMillis;

public class CTPCommand implements CommandExecutor {

    public CTPCommand(CyberTravel main) {
        main.getCommand("ctp").setExecutor(this);
        this.main = main;
    }

    private CyberTravel main;
    private HashMap<String, HashMap<String, Long>> regionCooldown = new HashMap<>();
    private HashMap<String, Long> globalCooldown = new HashMap<>();
    private Map<Player, BukkitTask> cmdCountdown = new HashMap<>();

    public HashMap<String, HashMap<String, Long>> getRegionCooldown() {
        return regionCooldown;
    }
    public HashMap<String, Long> getGlobalCooldown() {
        return globalCooldown;
    }
    public Map<Player, BukkitTask> getCmdCountdown() {
        return cmdCountdown;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player;
        String uuid;

        if (!(sender instanceof Player) && (!(args.length == 1) || (!(args[0].equalsIgnoreCase("reload")) &&
                !(args[0].equalsIgnoreCase("about")) && !(args[0].equalsIgnoreCase("version"))))) {
            System.out.println("Console can only use the reload/about command!");
            return true;
        } else if (!(sender instanceof Player)){
            player = null;
            uuid = null;
        } else {
            player = (Player) sender;
            uuid = player.getUniqueId().toString();
        }

        // basic info
        String fl = "regions.";
        String pl = "players.";
        Configuration dataConfig = main.getFileCache().getStoredFiles().get("data").getConfig();
        Configuration regionsConfig = main.getFileCache().getStoredFiles().get("regions").getConfig();

        // 0 arguments
        if (args.length ==  0) {
            sendHelpMessage(sender);
            return true;
        }

        // 1 argument
        if (args.length == 1) {

            // done
            if (args[0].equalsIgnoreCase("about") || args[0].equalsIgnoreCase("version")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("player-about"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                sender.sendMessage(main.getLangUtils().getColor("&c&lCyber&f&lTravel &fv" + main.getDescription().getVersion() + " &7(&7&nhttps://bit.ly/3x7Rsuu&7).", false));
                sender.sendMessage(main.getLangUtils().getColor("&fDeveloped by &c" + main.getDescription().getAuthors().toString()
                        .replace("[", "").replace("]", "") + "&f.", false));
                sender.sendMessage(main.getLangUtils().getColor("&7Discover regions scattered across your world to be able", false));
                sender.sendMessage(main.getLangUtils().getColor("&7to quickly travel back to that spot with a simple command!", false));
                return true;
            }

            // done
            // /ftp help
            if (args[0].equalsIgnoreCase("help")) {
                sendHelpMessage(sender);
                return true;
            }

            // done
            // /ftp reload
            if (args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-reload"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                sender.sendMessage(main.getLangUtils().getMessage("reloading", true));
                main.getFileCache().initializeFiles();
                main.getConfigCache().initializeConfig();
                main.getLangCache().initializeConfig();
                main.getRegionCache().loadRegionData();
                main.getPlayerCache().initializePlayerData();
                sender.sendMessage(main.getLangUtils().getMessage("reloaded", true));
                return true;
            }

            // done
            if (args[0].equalsIgnoreCase("regions")) {

                if (!sender.hasPermission(main.getFileUtils().getPermission("player-discovered-list", "CyberTravel.player.list"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (!main.getRegionCache().getRegions().isEmpty()) {

                    if (!main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {
                        sender.sendMessage(main.getLangCache().getMessages().get("no-checkpoints").getMessage(true));
                        return true;
                    }

                    sender.sendMessage(main.getLangUtils().getMessage("unlocked-regions-header", false));

                    for (String i : main.getRegionCache().getRegions().keySet()) {

                        if (main.getRegionCache().getRegions().get(i).isEnabled()) {

                            if (main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {

                                if (main.getPlayerCache().getPlayerRegions().get(uuid).contains(i)) {

                                    sender.sendMessage(main.getLangCache().getMessages().get("unlocked-region").getMessage(false, "displayName",
                                            getDisplayName(i), "region", i, "price", String.format("%.2f", main.getRegionCache().getRegions().get(i).getPrice())));

                                } else {

                                    sender.sendMessage(main.getLangCache().getMessages().get("locked-region").getMessage(false, "displayName",
                                            getDisplayName(i), "region", i, "price", String.format("%.2f", main.getRegionCache().getRegions().get(i).getPrice())));

                                }
                            } else {

                                sender.sendMessage(main.getLangCache().getMessages().get("locked-region").getMessage(false, "displayName",
                                        getDisplayName(i), "region", i, "price", String.format("%.2f", main.getRegionCache().getRegions().get(i).getPrice())));

                            }

                        }

                    }

                    sender.sendMessage(main.getLangUtils().getMessage("unlocked-regions-footer", false));

                } else {

                    sender.sendMessage(main.getLangUtils().getMessage("no-regions", true));

                }

                return true;
            }

            // send help message
            sendHelpMessage(sender);
            return true;
        }

        // 2 arguments
        if (args.length == 2) {

            // /ftp create <region>
            if (args[0].equalsIgnoreCase("create")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-create-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // if region exists
                if (main.getRegionCache().getRegions().containsKey(args[1])) {
                    sender.sendMessage(main.getLangCache().getMessages().get("region-exists").getMessage(true, "region", args[1], "displayName", getDisplayName(args[1])));
                    return true;
                }

                try {
                    // creates region and stores name
                    regionsConfig.set(fl + args[1] + ".location.world", player.getWorld().getName());
                    regionsConfig.set(fl + args[1] + ".enabled", false);
                    main.getFileCache().getStoredFiles().get("regions").saveConfig();
                    main.getRegionCache().getRegions().put(args[1], new RegionObject(args[1], player.getWorld().getName()));

                    sender.sendMessage(main.getLangCache().getMessages().get("region-created").getMessage(true, "region", args[1], "displayName", getDisplayName(args[1])));

                } catch (IOException e) {
                }

                return true;

            }

            // /ftp <pos1|pos2|settp> <region>
            if (args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2") || args[0].equalsIgnoreCase("settp")) {

                // permission nodes
                if ((!sender.hasPermission(main.getLangUtils().getPermission("admin-set-position1")) && args[0].equalsIgnoreCase("pos1")) ||
                        (!sender.hasPermission(main.getLangUtils().getPermission("admin-set-position2")) && args[0].equalsIgnoreCase("pos2")) ||
                        (!sender.hasPermission(main.getLangUtils().getPermission("admin-set-tp-location")) && args[0].equalsIgnoreCase("settp"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // invalid location
                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    player.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1], "displayName", args[1]));
                    return true;
                }

                // is there no world registered?
                if (!(player.getWorld().getName().equalsIgnoreCase(main.getRegionCache().getRegions().get(args[1]).getWorld()))) {
                    sender.sendMessage(main.getLangCache().getMessages().get("incorrect-world").getMessage(true, "world",
                            main.getRegionCache().getRegions().get(args[1]).getWorld(), "region", args[1], "displayName", getDisplayName(args[1])));
                    return true;
                }

                // gets player location and saves it
                String location = (0.5 + player.getLocation().getBlockX()) + ", " + player.getLocation().getBlockY() + ", " + (0.5 + player.getLocation().getBlockZ());
                regionsConfig.set(fl + args[1] + ".location." + args[0], location);
                double[] locationArray = new double[]{0.5 + player.getLocation().getBlockX(), player.getLocation().getBlockY(), 0.5 + player.getLocation().getBlockZ()};

                if (args[0].equalsIgnoreCase("pos1")) {
                    main.getRegionCache().getRegions().get(args[1]).setPos1(locationArray);
                } else if (args[0].equalsIgnoreCase("pos2")) {
                    main.getRegionCache().getRegions().get(args[1]).setPos2(locationArray);
                } else if (args[0].equalsIgnoreCase("settp")) {
                    main.getRegionCache().getRegions().get(args[1]).setSetTP(locationArray);
                }

                // save changes
                try {
                    main.getFileCache().getStoredFiles().get("regions").saveConfig();
                    sender.sendMessage(main.getLangUtils().getColor("&aLocation set to (" + location + "&a) in world \"" + player.getWorld().getName() + ".\"", true));
                } catch (IOException e) {
                }

                if ((args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2")) &&
                        (main.getRegionCache().getRegions().get(args[1]).getPos1() != null) && (main.getRegionCache().getRegions().get(args[1]).getPos2() != null))
                    main.getBlockUtils().regionOutline(player, args[1], false);

                if (args[0].equalsIgnoreCase("settp") && (main.getRegionCache().getRegions().get(args[1]).getSetTP() != null))
                    main.getBlockUtils().regionOutline(player, args[1], true);

                // auto enable
                if (main.getConfigCache().isAutoEnableRegions()) {
                    if ((main.getRegionCache().getRegions().get(args[1]).getPos1() != null) && (main.getRegionCache().getRegions().get(args[1]).getPos2() != null) &&
                            (main.getRegionCache().getRegions().get(args[1]).getSetTP() != null) && (!main.getRegionCache().getRegions().get(args[1]).isEnabled())) {

                        regionsConfig.set(fl + args[1] + ".enabled", true);
                        main.getRegionCache().getRegions().get(args[1]).setEnabled(true);

                        try {
                            main.getFileCache().getStoredFiles().get("regions").saveConfig();
                        } catch (Exception e) {}
                        sender.sendMessage(main.getLangCache().getMessages().get("auto-ready").getMessage(true, "region", args[1], "displayName", getDisplayName(args[1])));

                    }

                }

                return true;
            }

            // /ftp <teleport|tp> <region>
            if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("player-teleport"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (!enabledRegion(player, args[1])) return true;

                double[] teleportLocation = main.getRegionCache().getRegions().get(args[1]).getSetTP();

                // bypass permission
                if (sender.hasPermission(main.getFileUtils().getPermission("admin-teleport-bypass", "CyberTravel.admin.bypass"))) {
                    teleportPlayer(player, args[1], teleportLocation);
                    return true;
                }

                // no checkpoints saved
                if (!main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {
                    sender.sendMessage(main.getLangUtils().getMessage("no-checkpoints", true));
                    return true;
                }

                // not discovered
                if (!main.getPlayerCache().getPlayerRegions().get(uuid).contains(args[1])) {
                    sender.sendMessage(main.getLangCache().getMessages().get("not-discovered").getMessage(true, "region", args[1], "displayName", getDisplayName(args[1])));
                    return true;
                }

                if (main.getVault().isEnabled()) {

                    if (!((main.getVault().getEconomy().getBalance(player) >= main.getRegionCache().getRegions().get(args[1]).getPrice()))) {

                        sender.sendMessage(main.getLangCache().getMessages().get("not-enough-money").getMessage(true, "price",
                                String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                "displayName", getDisplayName(args[1])));
                        return true;
                    }

                }

                boolean regionCoolDownEnabled = main.getConfigCache().isRegionCoolDownEnabled();
                boolean globalCooldownEnabled = main.getConfigCache().isGlobalCoolDownEnabled();

                // global cooldown
                if (globalCooldownEnabled) {
                    if (globalCooldown.containsKey(uuid)) {

                        long gcdTime = main.getConfigCache().getGlobalCoolDownSeconds();
                        long globalTimeRemaining = ((globalCooldown.get(uuid) / 1000) + gcdTime) - (currentTimeMillis() / 1000);

                        if (globalTimeRemaining > 0) {

                            // player still on a cooldown
                            sender.sendMessage(main.getLangCache().getMessages().get("global-cooldown").getMessage(true, "region",
                                    args[1], "displayName", getDisplayName(args[1]), "time", main.getLangUtils().formatTime(globalTimeRemaining)));
                            return true;
                        }

                    }

                }

                // region cooldown module
                if (regionCoolDownEnabled) {

                    for (String i : main.getPlayerCache().getPlayerRegions().get(uuid)) {
                        if (i.equalsIgnoreCase(args[1])) {

                            long rcdTime = main.getConfigCache().getRegionCoolDownSeconds();

                            if (regionCooldown.containsKey(uuid)) {

                                if (regionCooldown.get(uuid).containsKey(args[1])) {
                                    long regionTimeRemaining = ((regionCooldown.get(uuid).get(args[1]) / 1000) + rcdTime) - (currentTimeMillis() / 1000);
                                    if (regionTimeRemaining > 0) {

                                        // player still on a cooldown
                                        sender.sendMessage(main.getLangCache().getMessages().get("region-cooldown").getMessage(true, "time",
                                                main.getLangUtils().formatTime(regionTimeRemaining), "region", args[1], "displayName", getDisplayName(args[1])));
                                        return true;
                                    }

                                }

                                // deletes prior time for this region
                                regionCooldown.get(player.getUniqueId().toString()).remove(args[1]);

                            }

                            // creates player hashmap if not present
                            if (!regionCooldown.containsKey(uuid)) regionCooldown.put(uuid, new HashMap<>());


                        }

                    }

                }

                //count-down module
                if (main.getConfigCache().isCountDownEnabled()) {

                    long cndTime = main.getConfigCache().getCountDownSeconds();

                    if (!this.cmdCountdown.containsKey(player)) {

                        sender.sendMessage(main.getLangCache().getMessages().get("countdown").getMessage(true, "time", main.getLangUtils().formatTime(cndTime),
                                "region", args[1], "displayName", getDisplayName(args[1])));

                        this.cmdCountdown.put(player, (new BukkitRunnable() {

                            @Override
                            public void run() {
                                teleportPlayer(player, args[1], teleportLocation);
                                cmdCountdown.remove(player);
                                if (regionCoolDownEnabled) regionCooldown.get(player.getUniqueId().toString()).put(args[1], System.currentTimeMillis());
                                if (globalCooldownEnabled) globalCooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());

                                if (main.getVault().isEnabled()) {
                                    main.getVault().getEconomy().withdrawPlayer(player, main.getRegionCache().getRegions().get(args[1]).getPrice());
                                    sender.sendMessage(main.getLangCache().getMessages().get("money-charged").getMessage(true, "price",
                                            String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                            "displayName", getDisplayName(args[1])));
                                }
                            }

                        }).runTaskLater(main, 20L * cndTime));

                    } else {

                        sender.sendMessage(main.getLangCache().getMessages().get("already-teleporting").getMessage(true, "region",
                                args[1], "displayName", getDisplayName(args[1])));
                    }
                } else {

                    teleportPlayer(player, args[1], teleportLocation);
                    if (regionCoolDownEnabled) regionCooldown.get(uuid).put(args[1], System.currentTimeMillis());
                    if (globalCooldownEnabled) globalCooldown.put(uuid, System.currentTimeMillis());

                    if (main.getVault().isEnabled()) {
                        main.getVault().getEconomy().withdrawPlayer(player, main.getRegionCache().getRegions().get(args[1]).getPrice());
                        sender.sendMessage(main.getLangCache().getMessages().get("money-charged").getMessage(true, "price",
                                String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                "displayName", getDisplayName(args[1])));
                    }
                    // teleport to this location
                }

                return true;

            }

            // /ftp delete <region>
            if (args[0].equalsIgnoreCase("delete")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-delete-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // region does not exist
                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    sender.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                    return true;

                }

                // remove region data
                regionsConfig.set(fl + args[1], null);
                main.getRegionCache().getRegions().remove(args[1]);

                // remove from player's data
                for (String i : main.getPlayerCache().getPlayerRegions().keySet()) {

                    List<String> playerRegions = main.getPlayerCache().getPlayerRegions().get(i);

                    if (playerRegions.contains(args[1])) {
                        playerRegions.remove(args[1]);
                        dataConfig.set(pl + i, playerRegions);
                        main.getPlayerCache().getPlayerRegions().get(i).remove(args[1]);
                    }
                }

                // attempts to save changes
                try {

                    main.getFileCache().getStoredFiles().get("data").saveConfig();
                    main.getFileCache().getStoredFiles().get("regions").saveConfig();
                    sender.sendMessage(main.getLangCache().getMessages().get("delete-region").getMessage(true, "region", args[1]));

                } catch (Exception e) {

                    sender.sendMessage(main.getLangCache().getMessages().get("delete-failed").getMessage(true, "region", args[1]));

                }
                return true;

            }

            // /ftp border <region>
            if (args[0].equalsIgnoreCase("border") || args[0].equalsIgnoreCase("outline")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-view-border"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // region does not exist
                if (!enabledRegion(player, args[1])) return true;

                main.getBlockUtils().regionOutline(player, args[1], true);
                main.getBlockUtils().regionOutline(player, args[1], false);

                return true;

            }

            // /ftp setenabled <region>
            if (args[0].equalsIgnoreCase("setenabled")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-set-enabled"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    player.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                    return true;
                } else {
                    if (main.getRegionCache().getRegions().get(args[1]).isEnabled()) {
                        sender.sendMessage(main.getLangCache().getMessages().get("already-enabled").getMessage(true, "region",
                                args[1], "displayName", getDisplayName(args[1])));
                        return true;
                    }
                }

                if ((main.getRegionCache().getRegions().get(args[1]).getPos1() != null) && (main.getRegionCache().getRegions().get(args[1]).getPos2() != null) &&
                        (main.getRegionCache().getRegions().get(args[1]).getSetTP() != null)) {

                    main.getRegionCache().getRegions().get(args[1]).setEnabled(true);
                    regionsConfig.set(fl + args[1] + ".enabled", true);

                    try {
                        main.getFileCache().getStoredFiles().get("regions").saveConfig();
                    } catch (Exception e) {}

                    sender.sendMessage(main.getLangCache().getMessages().get("region-set-enabled").getMessage(true, "region",
                            args[1], "displayName", getDisplayName(args[1])));

                } else {
                    sender.sendMessage(main.getLangCache().getMessages().get("location-not-set-up").getMessage(true, "region",
                            args[1], "displayName", getDisplayName(args[1])));
                }

                return true;

            }

            // /ftp setdisabled <region>
            if (args[0].equalsIgnoreCase("setdisabled")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-set-disabled"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    player.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                    return true;
                } else {
                    if (!main.getRegionCache().getRegions().get(args[1]).isEnabled()) {
                        sender.sendMessage(main.getLangCache().getMessages().get("already-disabled").getMessage(true, "region",
                                args[1], "displayName", getDisplayName(args[1])));
                        return true;
                    }
                }

                main.getRegionCache().getRegions().get(args[1]).setEnabled(false);
                regionsConfig.set(fl + args[1] + ".enabled", false);

                try {
                    main.getFileCache().getStoredFiles().get("regions").saveConfig();
                } catch (Exception e) {}

                sender.sendMessage(main.getLangCache().getMessages().get("region-set-disabled").getMessage(true, "region",
                        args[1], "displayName", getDisplayName(args[1])));

                return true;

            }

            sendHelpMessage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("setdisplayname")) {

            if (!player.hasPermission(main.getLangUtils().getPermission("admin-set-display-name"))) {
                main.getLangUtils().noPermission(player);
                return true;
            }

            // region does not exist
            if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                sender.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                return true;

            }

            String displayNameString = args[2];

            for (int i = 3; i < args.length; i++) {
                displayNameString = displayNameString + " " + args[i];
            }

            if (displayNameString.length() >= 120) {
                sender.sendMessage(main.getLangCache().getPrefix() + "&cThat display name is too long! Please use 120 characters or less!");
                return true;
            }

            main.getRegionCache().getRegions().get(args[1]).setDisplayName(displayNameString);
            regionsConfig.set(fl + args[1] + ".settings.display-name", displayNameString);
            try {
                main.getFileCache().getStoredFiles().get("regions").saveConfig();
            } catch (Exception e) {
            }

            sender.sendMessage(main.getLangCache().getMessages().get("region-set-display-name").getMessage(true, "region", args[1], "displayName", getDisplayName(args[1])));
            return true;

        }

        // 3 arguments
        if (args.length == 3) {

            // /ftp addregion <region> <player>
            if (args[0].equalsIgnoreCase("addplayerregion")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-add-player-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // invalid location
                if (!enabledRegion(player, args[1])) return true;

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                    uuid = onlinePlayer.getUniqueId().toString();

                    if (onlinePlayer.getName().equalsIgnoreCase(args[2])) {

                        // has the player discovered this region yet?
                        if (main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {
                            if (main.getPlayerCache().getPlayerRegions().get(uuid).contains(args[1])) {

                                sender.sendMessage(main.getLangCache().getMessages().get("already-discovered").getMessage(true, "region",
                                        args[1], "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));

                            } else {

                                main.getPlayerCache().getPlayerRegions().get(uuid).add(args[1]);

                                List playerRegions = dataConfig.getList(pl + uuid);

                                if (!playerRegions.contains(args[1])) {

                                    playerRegions.add(args[1]);
                                    dataConfig.set(pl + uuid, playerRegions);

                                    try {

                                        main.getFileCache().getStoredFiles().get("data").saveConfig();

                                    } catch (IOException e) {}
                                }

                                sender.sendMessage(main.getLangCache().getMessages().get("added-player-region").getMessage(true, "region",
                                        args[1], "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));

                            }

                            return true;

                        }

                    }

                }

                sender.sendMessage(main.getLangCache().getMessages().get("not-online").getMessage(true, "region",
                        args[1], "displayName", getDisplayName(args[1]), "player", args[2]));
                return true;


            }

            // /ftp delregion <region> <player>
            if (args[0].equalsIgnoreCase("delplayerregion")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-del-player-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // invalid location
                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    player.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                    return true;
                }

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().equalsIgnoreCase(args[2])) {

                        uuid = onlinePlayer.getUniqueId().toString();

                        // no checkpoints saved
                        if (!main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {
                            sender.sendMessage(main.getLangCache().getMessages().get("has-no-checkpoints").getMessage(true, "player", onlinePlayer.getDisplayName()));
                            return true;
                        }

                        // has the player discovered this region yet?
                        if (main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {
                            if (!main.getPlayerCache().getPlayerRegions().get(uuid).contains(args[1])) {

                                sender.sendMessage(main.getLangCache().getMessages().get("has-not-discovered").getMessage(true, "region",
                                        args[1], "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));

                            } else {

                                main.getPlayerCache().getPlayerRegions().get(uuid).remove(args[1]);
                                List playerRegions = dataConfig.getList(pl + uuid);
                                if (playerRegions.contains(args[1])) {
                                    playerRegions.remove(args[1]);
                                    dataConfig.set(pl + uuid, playerRegions);
                                    try {
                                        main.getFileCache().getStoredFiles().get("data").saveConfig();
                                    } catch (IOException e) {}
                                }

                                sender.sendMessage(main.getLangCache().getMessages().get("deleted-player-region").getMessage(true, "region",
                                        args[1], "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));


                            }

                            return true;

                        }

                    }

                }

                sender.sendMessage(main.getLangCache().getMessages().get("not-online").getMessage(true, "region",
                        args[1], "displayName", getDisplayName(args[1]), "player", args[2]));
                return true;

            }

            // /ftp rename <region> <new name>
            if (args[0].equalsIgnoreCase("rename")) {

                if (!player.hasPermission(main.getLangUtils().getPermission("admin-rename-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // region does not exist
                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    sender.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                    return true;

                }

                // new region exists
                if (main.getRegionCache().getRegions().containsKey(args[2])) {
                    sender.sendMessage(main.getLangCache().getMessages().get("region-exists").getMessage(true, "region", args[2], "displayName", getDisplayName(args[2])));
                    return true;

                }

                // file
                ConfigurationSection oldConfigSection = regionsConfig.getConfigurationSection(fl + args[1]);
                regionsConfig.set(fl + args[1], null);
                regionsConfig.set(fl + args[2], oldConfigSection);
                try {
                    main.getFileCache().getStoredFiles().get("regions").saveConfig();
                } catch (Exception e) {}

                // cache
                RegionObject oldRegionObject = main.getRegionCache().getRegions().get(args[1]);
                main.getRegionCache().getRegions().put(args[2], oldRegionObject);
                main.getRegionCache().getRegions().remove(args[1]);

                // player file
                for (String i : dataConfig.getConfigurationSection("players").getKeys(false)) {

                    List<String> oldPlayerConfig = dataConfig.getStringList(pl + i);
                    if (oldPlayerConfig.contains(args[1])) {

                        oldPlayerConfig.remove(args[1]);
                        oldPlayerConfig.add(args[2]);
                        dataConfig.set(pl + i, oldPlayerConfig);

                    }

                }
                try {
                    main.getFileCache().getStoredFiles().get("data").saveConfig();
                } catch (Exception e) {}

                // player cache
                for (String i : main.getPlayerCache().getPlayerRegions().keySet()) {

                    if (main.getPlayerCache().getPlayerRegions().get(i).contains(args[1])) {

                        main.getPlayerCache().getPlayerRegions().get(i).remove(args[1]);
                        main.getPlayerCache().getPlayerRegions().get(i).add(args[2]);

                    }


                }

                sender.sendMessage(main.getLangCache().getMessages().get("region-renamed").getMessage(true, "region", args[1], "newName", args[2], "displayName", getDisplayName(args[2])));
                return true;

            }

            // /ftp tp <region> <player>
            if ((args[0].equalsIgnoreCase("tp")) || (args[0].equalsIgnoreCase("teleport"))) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-tp-player-to-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (!enabledRegion(player, args[1])) return true;

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                    if (args[2].equalsIgnoreCase(onlinePlayer.getName())) {

                        uuid = onlinePlayer.getUniqueId().toString();

                        double[] teleportLocation = main.getRegionCache().getRegions().get(args[1]).getSetTP();

                        // bypass permission
                        if (onlinePlayer.hasPermission(main.getFileUtils().getPermission("admin-teleport-bypass", "CyberTravel.admin.bypass"))) {
                            teleportPlayer(onlinePlayer, args[1], teleportLocation);
                            return true;
                        }

                        // no checkpoints saved
                        if (!main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {
                            sender.sendMessage(main.getLangCache().getMessages().get("has-no-checkpoints").getMessage(true, "player", onlinePlayer.getDisplayName()));
                            return true;
                        }

                        // not discovered
                        if (!main.getPlayerCache().getPlayerRegions().get(uuid).contains(args[1])) {
                            sender.sendMessage(main.getLangCache().getMessages().get("has-not-discovered").getMessage(true, "region", args[1],
                                    "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));
                            return true;
                        }

                        if (main.getVault().isEnabled()) {

                            if (!((main.getVault().getEconomy().getBalance(onlinePlayer) >= main.getRegionCache().getRegions().get(args[1]).getPrice()))) {

                                sender.sendMessage(main.getLangCache().getMessages().get("not-enough-money").getMessage(true, "price",
                                        String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                        "displayName", getDisplayName(args[1])));
                                return true;
                            }

                        }

                        boolean regionCoolDownEnabled = main.getConfigCache().isRegionCoolDownEnabled();
                        boolean globalCooldownEnabled = main.getConfigCache().isGlobalCoolDownEnabled();

                        // global cooldown
                        if (globalCooldownEnabled) {
                            if (globalCooldown.containsKey(uuid)) {

                                long gcdTime = main.getConfigCache().getGlobalCoolDownSeconds();
                                long globalTimeRemaining = ((globalCooldown.get(uuid) / 1000) + gcdTime) - (currentTimeMillis() / 1000);

                                if (globalTimeRemaining > 0) {

                                    // player still on a cooldown
                                    sender.sendMessage(main.getLangCache().getMessages().get("global-cooldown").getMessage(true, "region",
                                            args[1], "displayName", getDisplayName(args[1]), "time", main.getLangUtils().formatTime(globalTimeRemaining)));
                                    return true;
                                }

                            }

                        }

                        // region cooldown module
                        if (regionCoolDownEnabled) {

                            for (String i : main.getPlayerCache().getPlayerRegions().get(uuid)) {
                                if (i.equalsIgnoreCase(args[1])) {

                                    long rcdTime = main.getConfigCache().getRegionCoolDownSeconds();

                                    if (regionCooldown.containsKey(uuid)) {

                                        if (regionCooldown.get(uuid).containsKey(args[1])) {
                                            long regionTimeRemaining = ((regionCooldown.get(uuid).get(args[1]) / 1000) + rcdTime) - (currentTimeMillis() / 1000);
                                            if (regionTimeRemaining > 0) {

                                                // player still on a cooldown
                                                sender.sendMessage(main.getLangCache().getMessages().get("region-cooldown").getMessage(true, "time",
                                                        args[1], "displayName", getDisplayName(args[1]), "region", main.getRegionCache().getRegions().get(args[1]).getDisplayName()));
                                                return true;
                                            }

                                        }

                                        // deletes prior time for this region
                                        regionCooldown.get(uuid).remove(args[1]);

                                    }

                                    // creates player hashmap if not present
                                    if (!regionCooldown.containsKey(uuid)) regionCooldown.put(uuid, new HashMap<>());


                                }

                            }

                        }

                        //count-down module
                        if (main.getConfigCache().isCountDownEnabled()) {

                            long cndTime = main.getConfigCache().getCountDownSeconds();

                            if (!this.cmdCountdown.containsKey(onlinePlayer)) {

                                onlinePlayer.sendMessage(main.getLangCache().getMessages().get("countdown").getMessage(true, "time", main.getLangUtils().formatTime(cndTime),
                                        "region", args[1], "displayName", getDisplayName(args[1])));
                                sender.sendMessage(main.getLangCache().getMessages().get("countdown").getMessage(true, "time", main.getLangUtils().formatTime(cndTime),
                                        "region", args[1], "displayName", getDisplayName(args[1])));

                                this.cmdCountdown.put(onlinePlayer, (new BukkitRunnable() {

                                    @Override
                                    public void run() {

                                        teleportPlayer(onlinePlayer, args[1], teleportLocation);
                                        cmdCountdown.remove(onlinePlayer);
                                        if (regionCoolDownEnabled) regionCooldown.get(onlinePlayer.getUniqueId().toString()).put(args[1], System.currentTimeMillis());
                                        if (globalCooldownEnabled) globalCooldown.put(onlinePlayer.getUniqueId().toString(), System.currentTimeMillis());
                                        sender.sendMessage(main.getLangCache().getMessages().get("teleported-player").getMessage(true, "region",
                                                args[1], "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));
                                        if (main.getVault().isEnabled()) {
                                            main.getVault().getEconomy().withdrawPlayer(onlinePlayer, main.getRegionCache().getRegions().get(args[1]).getPrice());
                                            sender.sendMessage(main.getLangCache().getMessages().get("money-charged").getMessage(true, "price",
                                                    String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                                    "displayName", getDisplayName(args[1])));
                                            onlinePlayer.sendMessage(main.getLangCache().getMessages().get("money-charged").getMessage(true, "price",
                                                    String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                                    "displayName", getDisplayName(args[1])));
                                        }

                                    }

                                }).runTaskLater(main, 20L * cndTime));

                            } else {

                                sender.sendMessage(main.getLangCache().getMessages().get("already-teleporting").getMessage(true, "region",
                                        args[1], "displayName", getDisplayName(args[1])));
                            }
                        } else {

                            teleportPlayer(onlinePlayer, args[1], teleportLocation);
                            if (regionCoolDownEnabled) regionCooldown.get(uuid).put(args[1], System.currentTimeMillis());
                            if (globalCooldownEnabled) globalCooldown.put(uuid, System.currentTimeMillis());
                            sender.sendMessage(main.getLangCache().getMessages().get("teleported-player").getMessage(true, "region",
                                    args[1], "displayName", getDisplayName(args[1]), "player", onlinePlayer.getDisplayName()));
                            if (main.getVault().isEnabled()) {
                                main.getVault().getEconomy().withdrawPlayer(onlinePlayer, main.getRegionCache().getRegions().get(args[1]).getPrice());
                                sender.sendMessage(main.getLangCache().getMessages().get("money-charged").getMessage(true, "price",
                                        String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                        "displayName", getDisplayName(args[1])));
                                onlinePlayer.sendMessage(main.getLangCache().getMessages().get("money-charged").getMessage(true, "price",
                                        String.format("%.2f", main.getRegionCache().getRegions().get(args[1]).getPrice()), "region", args[1],
                                        "displayName", getDisplayName(args[1])));
                            }

                        }

                        return true;

                    }

                }

                sender.sendMessage(main.getLangCache().getMessages().get("not-online").getMessage(true, "region",
                        args[1], "displayName", getDisplayName(args[1]), "player", args[2]));

            }

            // /ftp reset playerProgress <region>
            if (args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("playerdata")) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-reset-player-region-progress"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (main.getPlayerCache().getPlayerRegions().isEmpty()) {
                    sender.sendMessage(main.getLangCache().getMessages().get("no-regions").getMessage(true));
                    return true;
                }

                if (args[2].equalsIgnoreCase("all")) {
                    if (!main.getRegionCache().getRegions().containsKey("all")) {

                        dataConfig.getConfigurationSection("players").getKeys(false).forEach(key -> dataConfig.set(pl + key, null));
                        main.getPlayerCache().getPlayerRegions().clear();
                        try {
                            main.getFileCache().getStoredFiles().get("data").saveConfig();
                        } catch (Exception e) {}
                        sender.sendMessage(main.getLangCache().getMessages().get("deleted-player-progress").getMessage(true, "region", "all regions",
                                "displayName", "all regions"));
                        return true;

                    }

                }

                if (!enabledRegion(player, args[2])) return true;

                for (String i : main.getPlayerCache().getPlayerRegions().keySet()) {

                    List playerRegions = main.getPlayerCache().getPlayerRegions().get(i);

                    if (playerRegions.contains(args[2])) {
                        playerRegions.remove(args[2]);
                        dataConfig.set(pl + i, playerRegions);
                        main.getPlayerCache().getPlayerRegions().get(i).remove(args[2]);
                    }
                }

                try {

                    main.getFileCache().getStoredFiles().get("data").saveConfig();
                    sender.sendMessage(main.getLangCache().getMessages().get("deleted-player-progress").getMessage(true, "region", args[2],
                            "displayName", getDisplayName(args[2])));

                } catch (Exception e) {

                    sender.sendMessage(main.getLangCache().getMessages().get("delete-failed").getMessage(true, "region", args[1]));

                }

                return true;

            }

            // /ftp setPrice <region> <price>
            if (args[0].equalsIgnoreCase("setprice")) {

                if (!player.hasPermission(main.getLangUtils().getPermission("admin-set-price"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                // region does not exist
                if (!main.getRegionCache().getRegions().containsKey(args[1])) {
                    sender.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", args[1]));
                    return true;

                }

                if (!main.getVault().isEnabled()) {
                    sender.sendMessage(main.getLangUtils().getMessage("vault-not-enabled", true));
                    return true;
                }

                if (Double.parseDouble(args[2]) <= 0) args[2] = "0";

                regionsConfig.set(fl + args[1] + ".settings.price", Double.parseDouble(args[2]));
                main.getRegionCache().getRegions().get(args[1]).setPrice(Double.parseDouble(args[2]));

                try {
                    main.getFileCache().getStoredFiles().get("regions").saveConfig();
                } catch (Exception e) {}

                sender.sendMessage(main.getLangCache().getMessages().get("region-set-price").getMessage(true, "region", args[1],
                        "price", String.format("%.2f", Double.parseDouble(args[2]))));
                return true;

            }

            sendHelpMessage(sender);
            return true;

        }

        if (args.length == 4) {

            // /ftp tp <region> <player> bypass
            if ((args[0].equalsIgnoreCase("tp")) || (args[0].equalsIgnoreCase("teleport"))) {

                if (!sender.hasPermission(main.getLangUtils().getPermission("admin-tp-player-to-region"))) {
                    main.getLangUtils().noPermission(player);
                    return true;
                }

                if (!enabledRegion(player, args[1])) return true;

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (args[2].equalsIgnoreCase(onlinePlayer.getName())) {

                        if (args[3].equalsIgnoreCase("bypass"))

                            teleportPlayer(onlinePlayer, args[1], main.getRegionCache().getRegions().get(args[1]).getSetTP());

                        sender.sendMessage(main.getLangCache().getMessages().get("teleported-player").getMessage(true, "region",
                                args[1], "player", onlinePlayer.getDisplayName()));

                        return true;

                    }
                }

                sender.sendMessage(main.getLangCache().getMessages().get("not-online").getMessage(true, "region",
                        args[1], "player", args[2]));

            }

            sendHelpMessage(sender);
            return true;

        }

        sendHelpMessage(sender);
        return true;

    }

    private void teleportPlayer(Player player, String region, double[] arrayLocation) {

        player.teleport(new Location(Bukkit.getWorld(main.getRegionCache().getRegions().get(region).getWorld()), arrayLocation[0],
                arrayLocation[1], arrayLocation[2], 0, 0));

        player.sendMessage(main.getLangCache().getMessages().get("teleport").getMessage(true, "region", region, "displayName", getDisplayName(region)));

    }

    private void sendHelpMessage(CommandSender sender) {

        if (sender.hasPermission(main.getLangUtils().getPermission("admin-help-list"))) {
            for (String i : main.getLangCache().getAdminHelp()) sender.sendMessage(i);
            return;
        }

        if (sender.hasPermission(main.getLangUtils().getPermission("player-help-list"))) {
            for (String i : main.getLangCache().getPlayerHelp()) sender.sendMessage(i);
            return;
        }

    }

    private boolean enabledRegion(Player player, String region) {

        if (!main.getRegionCache().getRegions().containsKey(region)) {
            player.sendMessage(main.getLangCache().getMessages().get("unknown-region").getMessage(true, "region", region));
            return false;
        } else {
            if (!main.getRegionCache().getRegions().get(region).isEnabled()) {
                player.sendMessage(main.getLangCache().getMessages().get("location-not-set-up").getMessage(true, "region", region, "displayName", getDisplayName(region)));
                return false;
            }
        }

        return true;

    }

    public String getDisplayName(String region) {
        return main.getRegionCache().getRegions().get(region).getDisplayName();
    }

}
