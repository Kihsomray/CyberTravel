package net.zerotoil.cybertravel.listeners;

import net.zerotoil.cybertravel.CTPCommand;
import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.utilities.FileUtils;
import net.zerotoil.cybertravel.cache.FileCache;
import net.zerotoil.cybertravel.cache.PlayerCache;
import net.zerotoil.cybertravel.objects.RegionObject;
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
                main.getMessageUtils().sendMessage("lang", "messages.teleport-cancelled", "&cYou moved! Teleportation canceled!", player);
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
                main.getMessageUtils().sendMessage("lang", "messages.teleport-cancelled", "&cYou moved! Teleportation canceled!", player);
            }

        }

        if (player.hasPermission("FastTravel.player")) {

            for (String i : main.getPlayerCache().getRegions().keySet()) {

                RegionObject region = main.getPlayerCache().getRegions().get(i);

                if (player.getWorld().getName().equalsIgnoreCase(region.getWorld())) {

                    String uuid = player.getUniqueId().toString();

                    if (main.getPlayerCache().getPlayerRegions().containsKey(uuid) &&
                            (main.getPlayerCache().getPlayerRegions().get(uuid).contains(region.getRegion()))) {
                    } else {

                        double pX = event.getTo().getX();
                        if ((region.getPosMin(0) <= pX) && (pX <= region.getPosMax(0))) {

                            double pZ = event.getTo().getZ();
                            if ((region.getPosMin(2) <= pZ) && (pZ <= region.getPosMax(2))) {

                                double pY = event.getTo().getY();
                                if ((region.getPosMin(1) <= pY) && (pY <= region.getPosMax(1))) {



                /* old, over-complicated code
                } else {
                    Double pMinX = event.getFrom().getX();
                    Double pMaxX = event.getTo().getX();
                    Double sMinX = Math.min(region.getPos1()[0], region.getPos2()[0]);
                    Double sMaxX = Math.max(region.getPos1()[0], region.getPos2()[0]);


                    if (((sMinX <= pMinX) && (pMinX <= sMaxX)) || ((sMinX <= pMaxX) && (pMaxX <= sMaxX)) || ((pMinX <= sMinX) && (sMaxX <= pMaxX))) {
                        Double pMinY = event.getFrom().getY();
                        Double pMaxY = event.getTo().getY();
                        Double sMinY = Math.min(region.getPos1()[1], region.getPos2()[1]);
                        Double sMaxY = Math.max(region.getPos1()[1], region.getPos2()[1]);


                        if (((sMinY <= pMinY) && (pMinY <= sMaxY)) || ((sMinY <= pMaxY) && (pMaxY <= sMaxY)) || ((pMinY <= sMinY) && (sMaxY <= pMaxY))) {
                            Double pMinZ = event.getFrom().getZ();
                            Double pMaxZ = event.getTo().getZ();
                            Double sMinZ = Math.min(region.getPos1()[2], region.getPos2()[2]);
                            Double sMaxZ = Math.max(region.getPos1()[2], region.getPos2()[2]);


                            if (((sMinZ <= pMinZ) && (pMinZ <= sMaxZ)) || ((sMinZ <= pMaxZ) && (pMaxZ <= sMaxZ)) || ((pMinZ <= sMinZ) && (sMaxZ <= pMaxZ))) {*/


                                    List<String> playerInfo;

                                    if (main.getFileUtils().dataFile().isList("players." + uuid)) {
                                        playerInfo = main.getFileUtils().dataFile().getStringList("players." + uuid);

                                    } else {
                                        playerInfo = new ArrayList<>();

                                    }

                                    playerInfo.add(region.getRegion());
                                    main.getFileCache().getStoredFiles().get("data").getConfig().set("players." + uuid, playerInfo);
                                    main.getFileCache().getStoredFiles().get("data").saveConfig();

                                    if (!main.getPlayerCache().getPlayerRegions().containsKey(uuid))
                                        main.getPlayerCache().getPlayerRegions().put(uuid, new ArrayList<>());

                                    main.getPlayerCache().getPlayerRegions().get(uuid).add(i);

                                    main.getMessageUtils().sendMessage("lang", "messages.enter-region", "&aYou have entered the region " + region.getRegion() +
                                            "! Use &6/ftp tp " + region.getRegion() + "&a to teleport back to this region!", player, "region", region.getRegion());

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}
