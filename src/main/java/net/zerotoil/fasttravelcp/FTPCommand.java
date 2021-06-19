package net.zerotoil.fasttravelcp;

import net.zerotoil.fasttravelcp.cache.FileCache;
import net.zerotoil.fasttravelcp.cache.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public class FTPCommand implements CommandExecutor {

    public FTPCommand(FastTravelCP main) {
        main.getCommand("fastteleport").setExecutor(this);
    }

    public HashMap<String, HashMap<String, Long>> cmdCooldown = new HashMap<>();

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
                MessageUtils.sendMessage("lang", "messages.reloading", "&7Reloading...", sender);
                FileCache.initializeFiles();
                PlayerCache.initializeRegionData();
                PlayerCache.initializePlayerData();
                MessageUtils.sendMessage("lang", "messages.reloaded", "&aReloaded!", sender);
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
                String location = player.getLocation().getBlockX() + ".5, " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ".5";
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
                    PlayerCache.initializeRegionData();
                    MessageUtils.sendMessage("lang", "messages.auto-ready", "&aLooks like you have finished setting up region " + args[1] +
                            ". This region is now active!", sender, "region", args[1]);
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {

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

                for (String i : dataConfig.getStringList(pl + player.getUniqueId().toString())) {
                    if (i.equalsIgnoreCase(args[1])) {

                        // cool-down module
                        if (FileCache.storedFiles.get("config").getConfig().getBoolean("config.cooldown.enabled")) {

                            int cdTime = FileCache.storedFiles.get("config").getConfig().getInt("config.cooldown.seconds");

                            if (cmdCooldown.containsKey(player.getUniqueId().toString())) {
                                if (cmdCooldown.get(player.getUniqueId().toString()).containsKey(args[1])) {
                                    long timeRemaining = ((cmdCooldown.get(player.getUniqueId().toString()).get(args[1]) / 1000) + cdTime) - (currentTimeMillis() / 1000);
                                    if (timeRemaining > 0) {

                                        // player still on a cooldown
                                        MessageUtils.sendMessage("lang", "messages.cooldown", "&cYou cant use that commands for another " +
                                                timeRemaining + " seconds!", sender, "time", timeRemaining + "");
                                        return true;
                                    }
                                }
                            }
                            // creates player hashmap if not present
                            if (!cmdCooldown.containsKey(player.getUniqueId().toString())) cmdCooldown.put(player.getUniqueId().toString(), new HashMap<>());

                            // deletes prior time for this region
                            if (cmdCooldown.get(player.getUniqueId().toString()).containsKey(args[1])) cmdCooldown.get(player.getUniqueId().toString()).remove(args[1]);

                            // adds new time for this region
                            cmdCooldown.get(player.getUniqueId().toString()).put(args[1], System.currentTimeMillis());
                        }

                        //count-down module
                        if (FileCache.storedFiles.get("config").getConfig().getBoolean("config.countdown.enabled")) {
                            int cndTime = FileCache.storedFiles.get("config").getConfig().getInt("config.countdown.seconds");
                            try {
                                MessageUtils.sendMessage("lang", "messages.countdown", "&7Teleporting in " +
                                        cndTime + " seconds... (doesn't work)", sender, "time", cndTime + "");
                                TimeUnit.SECONDS.sleep(cndTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // teleport to this location
                        String stringLocation = dataConfig.getString(fl + args[1] + ".settp");
                        String[] arrLocation = stringLocation.split(", ");
                        player.teleport(new Location(Bukkit.getWorld(dataConfig.getString(fl + args[1] + ".world")), Double.parseDouble(arrLocation[0]),
                                Double.parseDouble(arrLocation[1]), Double.parseDouble(arrLocation[2]), 0, 0));
                        MessageUtils.sendMessage("lang", "messages.teleport", "&aSuccessfully teleported to " +
                                args[1] + "!", sender, "region", args[1]);
                        return true;
                    }
                }
            }

            sendHelpMessage(sender);
            return true;
        }


        // 3 arguments
        if (args.length == 3) {

        }



        sendHelpMessage(sender);
        return true;

    }

    public void sendHelpMessage(CommandSender sender) {
        if(!FileCache.storedFiles.get("lang").getConfig().isSet("messages.help")){
            sender.sendMessage(MessageUtils.getColor("&8&m―――――&8<&b&l Fast&f&lTeleport &8>&8&m―――――", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp help &fSee the help menu.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp reload &fReload the plugin.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp create <region> &fCreate a region in your world.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp (pos1|pos2) <region> &fSet cuboid boundaries of region.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp settp <region> &fSet teleport location.", false));
            sender.sendMessage(MessageUtils.getColor("&8➼ &7/ftp (tp|teleport) <region> &fTeleport to location.", false));
            sender.sendMessage(MessageUtils.getColor("&8&m――――――――――――――――――――――――――――――", false));
            return;
        }
        if(FileCache.storedFiles.get("lang").getConfig().getString("messages.help").equalsIgnoreCase("")){
            return;
        }
        for (String x : FileCache.storedFiles.get("lang").getConfig().getStringList("messages.help")) {
            sender.sendMessage(MessageUtils.getColor(x, false));
        }
        return;
    }



}
