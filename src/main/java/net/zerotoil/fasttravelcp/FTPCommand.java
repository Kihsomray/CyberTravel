package net.zerotoil.fasttravelcp;

import net.zerotoil.fasttravelcp.cache.FileCache;
import net.zerotoil.fasttravelcp.cache.PlayerCache;
import net.zerotoil.fasttravelcp.utilities.FileUtils;
import net.zerotoil.fasttravelcp.utilities.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.*;

import static java.lang.System.currentTimeMillis;

public class FTPCommand implements CommandExecutor {

    public FTPCommand(FastTravelCP main) {
        main.getCommand("fastteleport").setExecutor(this);
    }

    public HashMap<String, HashMap<String, Long>> regionCooldown = new HashMap<>();
    public HashMap<String, Long> globalCooldown = new HashMap<>();
    public static Map<Player, BukkitTask> cmdCountdown = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return true;

        // basic info
        String fl = "regions.";
        String pl = "players.";
        Player player = (Player) sender;
        Configuration dataConfig = FileCache.storedFiles.get("data").getConfig();

        // 0 arguments
        if (args.length ==  0) {
            sendHelpMessage(sender);
            return true;
        }

        // 1 argument
        if (args.length == 1) {

            // /ftp help
            if (args[0].equalsIgnoreCase("help")) {
                sendHelpMessage(sender);
                return true;
            }

            // /ftp reload
            if (args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission("FastTravel.admin")) {
                    MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
                    return true;
                }

                MessageUtils.sendMessage("lang", "messages.reloading", "&7Reloading...", sender);
                FileCache.initializeFiles();
                PlayerCache.refreshRegionData(false);
                PlayerCache.initializePlayerData();
                MessageUtils.sendMessage("lang", "messages.reloaded", "&aReloaded!", sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("regions")) {

                if (!sender.hasPermission("FastTravel.player")) {
                    MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
                    return true;
                }

                if (!PlayerCache.regions.isEmpty()) {
                    MessageUtils.sendMessage("lang", "messages.unlocked-regions-header",
                            "&8&m―――――&8<&b&l Unlocked &f&lRegions: &8>&8&m―――――", player, "none", "none", false);
                    for (String i : PlayerCache.regions.keySet()) {

                        if (PlayerCache.playerRegions.containsKey(player.getUniqueId().toString())) {
                            if (PlayerCache.playerRegions.get(player.getUniqueId().toString()).contains(i)) {
                                MessageUtils.sendMessage("lang", "messages.unlocked-region", "&8➼ &7 Region &f\"" + i + "\" &a&o(unlocked)", player, "region", i, false);
                            } else {
                                MessageUtils.sendMessage("lang", "messages.locked-region", "&8➼ &7 Region &f\"" + i + "\" &c&o(locked)", player, "region", i, false);
                            }
                        } else {
                            MessageUtils.sendMessage("lang", "messages.locked-region", "&8➼ &7 Region &f\"" + i + "\" &c&o(locked)", player, "region", i, false);
                        }

                    }
                    MessageUtils.sendMessage("lang", "messages.unlocked-regions-footer",
                            "&8&m――――――――――――――――――――――――――――――――――", player, "none", "none", false);

                    return true;
                }
            }

            // send help message
            sendHelpMessage(sender);
            return true;
        }

        // 2 arguments
        if (args.length == 2) {

            // /ftp create <region>
            if (args[0].equalsIgnoreCase("create")) {

                if (!sender.hasPermission("FastTravel.admin")) {
                    MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
                    return true;
                }

                // if region exists
                if (dataConfig.isConfigurationSection(fl + args[1])) {
                    MessageUtils.sendMessage("lang", "messages.region-exists", "&cThe name " + args[1] +
                            " already exists. Please use another name!", sender, "region", args[1]);
                } else {
                    try {

                        // creates region and stores name
                        dataConfig.set(fl + args[1] + ".world", player.getWorld().getName());
                        FileCache.storedFiles.get("data").saveConfig();
                        MessageUtils.sendMessage("lang", "messages.region-created", "&aRegion " + args[1] +
                                " has successfully been created!", sender, "region", args[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

            // /ftp [pos1|pos2|settp] <region>
            if (args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2") || args[0].equalsIgnoreCase("settp")) {

                if (!sender.hasPermission("FastTravel.admin")) {
                    MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
                    return true;
                }

                // region does not exist
                if (!dataConfig.isConfigurationSection(fl + args[1])) {
                    MessageUtils.sendMessage("lang", "messages.unknown-region", "&c" + args[1] +
                            " is not a region!", sender, "region", args[1]);
                    return true;
                }

                // is there no world registered?
                if (!dataConfig.isSet(fl + args[1] + ".world")) {
                    dataConfig.set(fl + args[1] + ".world", player.getWorld().toString());

                // is the player in a different world?
                } else if (!(player.getWorld().getName().equalsIgnoreCase(dataConfig.getString(fl + args[1] + ".world")))) {
                    MessageUtils.sendMessage("lang", "messages.incorrect-world", "&cThis region is registered in the world \"" +
                            dataConfig.getString(fl + args[1] + ".world") + "!", sender, "world", dataConfig.getString(fl + args[1] + ".world"));
                    return true;
                }

                // gets player location and saves it
                String location = (0.5 + player.getLocation().getBlockX()) + ", " + player.getLocation().getBlockY() + ", " + (0.5 + player.getLocation().getBlockZ());
                dataConfig.set(fl + args[1] + "." + args[0], location);

                // save changes
                try {
                    FileCache.storedFiles.get("data").saveConfig();
                    sender.sendMessage(MessageUtils.getColor("&aLocation set to (" + location + "&a) in world \"" + dataConfig.get(fl + args[1] + ".world") + ".\"", true));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // adds it to memory
                if (dataConfig.isSet(fl + args[1] + ".pos1") && dataConfig.isSet(fl + args[1] + ".pos2") && dataConfig.isSet(fl + args[1] + ".settp")) {
                    PlayerCache.refreshRegionData(true);
                    MessageUtils.sendMessage("lang", "messages.auto-ready", "&aLooks like you have finished setting up region " + args[1] +
                            ". This region is now active!", sender, "region", args[1]);
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {

                if (!sender.hasPermission("FastTravel.player")) {
                    MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
                    return true;
                }

                // invalid location
                if (!dataConfig.isConfigurationSection(fl + args[1])) {
                    MessageUtils.sendMessage("lang", "messages.invalid-location", "&cThat is not a valid location!", sender);
                    return true;
                }

                // no checkpoints saved
                if (!dataConfig.isSet(pl + player.getUniqueId().toString())) {
                    MessageUtils.sendMessage("lang", "messages.no-checkpoints", "&cYou do not have any checkpoints saved!", sender);
                    return true;
                }

                // has the player discovered this region yet?
                if (PlayerCache.playerRegions.containsKey(player.getUniqueId().toString())) {
                    if (!PlayerCache.playerRegions.get(player.getUniqueId().toString()).contains(args[1])) {
                        MessageUtils.sendMessage("lang", "messages.not-discovered", "&cYou still haven't been to this region!", sender);
                        return true;
                    }
                }

                boolean cooldownEnabled = FileCache.storedFiles.get("config").getConfig().getBoolean("config.region-cooldown.enabled");
                boolean globalCooldownEnabled = FileCache.storedFiles.get("config").getConfig().getBoolean("config.global-cooldown.enabled");

                if (globalCooldownEnabled) {
                    if (globalCooldown.containsKey(player.getUniqueId().toString())) {

                        int gcdTime = FileCache.storedFiles.get("config").getConfig().getInt("config.global-cooldown.seconds");

                        if (globalCooldown.containsKey(player.getUniqueId().toString())) {
                            long globalTimeRemaining = ((globalCooldown.get(player.getUniqueId().toString()) / 1000) + gcdTime) - (currentTimeMillis() / 1000);
                            if (globalTimeRemaining > 0) {

                                // player still on a cooldown
                                MessageUtils.sendMessage("lang", "messages.global-cooldown", "&cYou cant teleport to that region for another " +
                                        globalTimeRemaining + " seconds!", sender, "time", globalTimeRemaining + "");
                                return true;
                            }
                        }
                    }
                }


                for (String i : dataConfig.getStringList(pl + player.getUniqueId().toString())) {
                    if (i.equalsIgnoreCase(args[1])) {



                        // cool-down module
                        if (cooldownEnabled) {

                            int cdTime = FileCache.storedFiles.get("config").getConfig().getInt("config.region-cooldown.seconds");

                            if (regionCooldown.containsKey(player.getUniqueId().toString())) {
                                if (regionCooldown.get(player.getUniqueId().toString()).containsKey(args[1])) {
                                    long timeRemaining = ((regionCooldown.get(player.getUniqueId().toString()).get(args[1]) / 1000) + cdTime) - (currentTimeMillis() / 1000);
                                    if (timeRemaining > 0) {

                                        // player still on a cooldown
                                        MessageUtils.sendMessage("lang", "messages.region-cooldown", "&cYou cannot teleport to any region for another " +
                                                timeRemaining + " seconds!", sender, "time", timeRemaining + "");
                                        return true;
                                    }
                                }
                            }
                            // creates player hashmap if not present
                            if (!regionCooldown.containsKey(player.getUniqueId().toString())) regionCooldown.put(player.getUniqueId().toString(), new HashMap<>());

                            // deletes prior time for this region
                            if (regionCooldown.get(player.getUniqueId().toString()).containsKey(args[1])) regionCooldown.get(player.getUniqueId().toString()).remove(args[1]);

                        }

                        //count-down module
                        String[] teleportLocation = dataConfig.getString(fl + args[1] + ".settp").split(", ");;

                        if (FileCache.storedFiles.get("config").getConfig().getBoolean("config.countdown.enabled")) {
                            int cndTime = FileCache.storedFiles.get("config").getConfig().getInt("config.countdown.seconds");
                            if (!this.cmdCountdown.containsKey(player)) {
                                MessageUtils.sendMessage("lang", "messages.countdown", "&7Teleporting in " +
                                        cndTime + " seconds...", sender, "time", cndTime + "");
                                this.cmdCountdown.put(player, (new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        teleportPlayer(player, args[1], teleportLocation);
                                        cmdCountdown.remove(player);
                                        if (cooldownEnabled) regionCooldown.get(player.getUniqueId().toString()).put(args[1], System.currentTimeMillis());
                                        if (globalCooldownEnabled) globalCooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());
                                    }

                                }).runTaskLater(FastTravelCP.getInstance(), 20L * cndTime));
                            } else {
                                MessageUtils.sendMessage("lang", "messages.already-teleporting", "&cYou are already being sent to " + args[1] +
                                        "! Please wait before you can teleport elsewhere.", sender, "region", args[1]);
                            }
                        } else {

                            teleportPlayer(player, args[1], teleportLocation);
                            if (cooldownEnabled) regionCooldown.get(player.getUniqueId().toString()).put(args[1], System.currentTimeMillis());
                            if (globalCooldownEnabled) globalCooldown.put(player.getUniqueId().toString(), System.currentTimeMillis());
                            // teleport to this location
                        }
                        return true;
                    }
                }
            }

            if (args[0].equalsIgnoreCase("delete")) {

                if (!sender.hasPermission("FastTravel.admin")) {
                    MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
                    return true;
                }

                // region does not exist
                if (!dataConfig.isConfigurationSection(fl + args[1])) {
                    MessageUtils.sendMessage("lang", "messages.unknown-region", "&c" + args[1] +
                            " is not a region!", sender, "region", args[1]);
                    return true;
                }

                //
                try {
                    dataConfig.set(fl + args[1], null);
                    PlayerCache.refreshRegionData(false);
                    for (String i : dataConfig.getConfigurationSection("players").getKeys(false)) {
                        List playerRegions = dataConfig.getList(pl + i);
                        if (playerRegions.contains(args[1])) {
                            playerRegions.remove(args[1]);
                            dataConfig.set(pl + i, playerRegions);
                            PlayerCache.initializePlayerData();
                            FileCache.storedFiles.get("data").saveConfig();
                        }
                    }
                    MessageUtils.sendMessage("lang", "messages.delete-region", "&aYou have successfully deleted " + args[1] +
                            "!", sender, "region", args[1]);
                } catch (Exception e) {
                    MessageUtils.sendMessage("lang", "messages.delete-failed", "&cThe region " + args[1] +
                            " cannot be deleted!", sender, "region", args[1]);
                }
                return true;

            }


            sendHelpMessage(sender);
            return true;
        }

        sendHelpMessage(sender);
        return true;

    }

    public void teleportPlayer(Player player, String region, String[] arrayLocation) {
        player.teleport(new Location(Bukkit.getWorld(FileUtils.dataFile().getString("regions." + region + ".world")), Double.parseDouble(arrayLocation[0]),
                Double.parseDouble(arrayLocation[1]), Double.parseDouble(arrayLocation[2]), 0, 0));
        MessageUtils.sendMessage("lang", "messages.teleport", "&aSuccessfully teleported to " +
                region + "!", player, "region", region);

        if (FileUtils.configFile().getBoolean("config.global-cooldown")) {

        }
    }

    public void sendHelpMessage(CommandSender sender) {
        if(sender.hasPermission("FastTravel.admin") && !FileCache.storedFiles.get("lang").getConfig().isSet("messages.help-admin")){
            if(FileCache.storedFiles.get("lang").getConfig().getString("messages.help-admin").equalsIgnoreCase("")){
                return;
            }

            sender.sendMessage(MessageUtils.getColor("&8&m―――――&8<&b&l Fast&f&lTeleport &8>&8&m―――――", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp help &fSee the help menu.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp reload &fReload the plugin.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp create <region> &fCreate a region in your world.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp delete <region> &fDelete an existing region.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp (pos1|pos2) <region> &fSet cuboid boundaries of region.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp settp <region> &fSet teleport location.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp (tp|teleport) <region> &fTeleport to location.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp (regions) <region> &fSee all locked/unlocked regions.", false));
            sender.sendMessage(MessageUtils.getColor("&8&m――――――――――――――――――――――――――――――", false));
            return;

        } else if (sender.hasPermission("FastTravel.admin")) {
            for (String x : FileCache.storedFiles.get("lang").getConfig().getStringList("messages.help-admin")) {
                sender.sendMessage(MessageUtils.getColor(x, false));

            }

        } else if(sender.hasPermission("FastTravel.player") && !FileCache.storedFiles.get("lang").getConfig().isSet("messages.help-player")){

            if(FileCache.storedFiles.get("lang").getConfig().getString("messages.help-admin").equalsIgnoreCase("")){
                return;
            }

            sender.sendMessage(MessageUtils.getColor("&8&m―――――&8<&b&l Fast&f&lTeleport &8>&8&m―――――", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp help &fSee the help menu.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp (tp|teleport) <region> &fTeleport to location.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp regions &fSee all locked/unlocked regions.", false));
            sender.sendMessage(MessageUtils.getColor("&8&m――――――――――――――――――――――――――――――", false));
            return;
        } else if (sender.hasPermission("FastTravel.player")) {
            for (String x : FileCache.storedFiles.get("lang").getConfig().getStringList("messages.help-player")) {
                sender.sendMessage(MessageUtils.getColor(x, false));

            }

        } else {
            MessageUtils.sendMessage("lang", "messages.no-permission", "&cYou don't have permission to do this!", sender);
        }

        return;
    }



}
