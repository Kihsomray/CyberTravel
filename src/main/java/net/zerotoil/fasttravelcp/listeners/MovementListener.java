package net.zerotoil.fasttravelcp.listeners;

import net.zerotoil.fasttravelcp.FileUtils;
import net.zerotoil.fasttravelcp.MessageUtils;
import net.zerotoil.fasttravelcp.cache.FileCache;
import net.zerotoil.fasttravelcp.cache.PlayerCache;
import net.zerotoil.fasttravelcp.objects.RegionObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovementListener implements Listener {



    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) throws IOException {

        for (String i : PlayerCache.regions.keySet()) {

            RegionObject region = PlayerCache.regions.get(i);

            Player player = event.getPlayer();

            if (player.getWorld().getName().equalsIgnoreCase(region.getWorld())) {

                String uuid = player.getUniqueId().toString();

                if (PlayerCache.playerRegions.containsKey(uuid) &&
                        (PlayerCache.playerRegions.get(uuid).contains(region.getRegion()))) {
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

                                if (FileUtils.dataFile.isSet("players." + uuid)) {
                                    playerInfo = FileUtils.dataFile.getStringList("players." + uuid);

                                } else {
                                    playerInfo = new ArrayList<>();

                                }

                                playerInfo.add(i);
                                FileUtils.dataFile.set("players." + uuid, playerInfo);
                                FileCache.storedFiles.get("data").saveConfig();

                                if (!PlayerCache.playerRegions.containsKey(uuid)) PlayerCache.playerRegions.put(uuid, new ArrayList<>());

                                PlayerCache.playerRegions.get(uuid).add(i);

                                MessageUtils.sendMessage("lang", "messages.enter-region", "&aYou have entered the region " + region.getRegion() +
                                        "! Use &6/ftp tp " + region.getRegion() + "&a to teleport back to this region!", player, "region", region.getRegion());

                            }

                        }

                    }

                }

            }

        }

    }






}
