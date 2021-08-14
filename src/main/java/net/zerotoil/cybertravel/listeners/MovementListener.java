package net.zerotoil.cybertravel.listeners;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.RegionObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovementListener implements Listener {

    public MovementListener(CyberTravel main) {
        this.main = main;
    }

    private CyberTravel main;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            BukkitTask task = main.getCtpCommand().getCmdCountdown().get(player);

            if (task != null) {
                task.cancel();
                main.getCtpCommand().getCmdCountdown().remove(player);
                player.sendMessage(main.getLangCache().getMessages().get("teleport-cancelled").getMessage(true));
            }
        }
    }


    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) throws IOException {

        Player player = event.getPlayer();

        if((event.getTo().getX() != event.getFrom().getX()) || (event.getTo().getY() != event.getFrom().getY()) || (event.getTo().getZ() != event.getFrom().getZ())){

            BukkitTask task = main.getCtpCommand().getCmdCountdown().get(player);

            if(task != null){
                task.cancel();
                main.getCtpCommand().getCmdCountdown().remove(player);
                player.sendMessage(main.getLangCache().getMessages().get("teleport-cancelled").getMessage(true));
            }

        }

        // checks if player has the permission to discover regions
        if (!player.hasPermission(main.getLangUtils().getPermission("player-discover-region"))) return;

        RegionObject region;
        String uuid = player.getUniqueId().toString();

        // loops through region names
        for (String i : main.getRegionCache().getRegions().keySet()) {


            // throws the region into memory
            region = main.getRegionCache().getRegions().get(i);

            // checks if region enabled
            if (!region.isEnabled()) continue;

            // checks region discovery
            if (main.getPlayerCache().getPlayerRegions().containsKey(uuid) && main.getPlayerCache().getPlayerRegions().get(uuid).contains(i)) continue;

            // check world
            if (!player.getWorld().getName().equalsIgnoreCase(region.getWorld())) continue;

            // checks X coordinate
            double pX = event.getTo().getX();
            if (!((region.getPosMin(0) <= pX) && (pX <= region.getPosMax(0)))) continue;

            // checks Z coordinate
            double pZ = event.getTo().getZ();
            if (!((region.getPosMin(2) <= pZ) && (pZ <= region.getPosMax(2)))) continue;

            // checks Y coordinate
            double pY = event.getTo().getY();
            if (!((region.getPosMin(1) <= pY) && (pY <= region.getPosMax(1)))) continue;


            // saves the player progress to the file
            List<String> playerInfo;
            if (main.getFileUtils().dataFile().isList("players." + uuid)) {

                playerInfo = main.getFileUtils().dataFile().getStringList("players." + uuid);

            } else {

                playerInfo = new ArrayList<>();

            }
            playerInfo.add(i);
            main.getFileCache().getStoredFiles().get("data").getConfig().set("players." + uuid, playerInfo);
            main.getFileCache().getStoredFiles().get("data").saveConfig();

            // saves progress to cache
            if (!main.getPlayerCache().getPlayerRegions().containsKey(uuid))
                main.getPlayerCache().getPlayerRegions().put(uuid, new ArrayList<>());
            main.getPlayerCache().getPlayerRegions().get(uuid).add(i);

            // sends discover region message
            player.sendMessage(main.getLangCache().getMessages().get("enter-region").getMessage(true, "region", i,
                    "displayName", main.getCtpCommand().getDisplayName(i)));

            // run commands
            if (region.getCommands().size() > 0) {
                for (String a : region.getCommands()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), a.replace("/", "").replace("{player}", player.getPlayer().getName())
                            .replace("{region}", region.getName()).replace("{displayName}", region.getDisplayName()).replace("{price}", region.getPrice() + ""));
                }
            }

        }


    }

}
