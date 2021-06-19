package net.zerotoil.fasttravelcp.listeners;

import net.zerotoil.fasttravelcp.MessageUtils;
import net.zerotoil.fasttravelcp.cache.FileCache;
import net.zerotoil.fasttravelcp.cache.PlayerCache;
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

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        for (int i = 0; i < PlayerCache.regions.size(); i++) {

            if (player.getWorld().getName().equalsIgnoreCase(PlayerCache.worlds.get(i))) {

                if (PlayerCache.playerRegions.containsKey(player.getUniqueId().toString()) &&
                        (PlayerCache.playerRegions.get(player.getUniqueId().toString()).contains(PlayerCache.regions.get(i)))) {
/*                }else {

                    Double pX = event.getTo().getX();
                    Double sMinX = Math.min(PlayerCache.pos1.get(i)[0], PlayerCache.pos2.get(i)[0]);
                    Double sMaxX = Math.max(PlayerCache.pos1.get(i)[0], PlayerCache.pos2.get(i)[0]);
                    if ((sMinX <= pX) && (pX <= sMaxX)) {

                        Double sMinY = Math.min(PlayerCache.pos1.get(i)[1], PlayerCache.pos2.get(i)[1]);
                        Double sMaxY = Math.max(PlayerCache.pos1.get(i)[1], PlayerCache.pos2.get(i)[1]);
                    }*/

                } else {
                    Double pMinX = event.getFrom().getX();
                    Double pMaxX = event.getTo().getX();
                    Double sMinX = Math.min(PlayerCache.pos1.get(i)[0], PlayerCache.pos2.get(i)[0]);
                    Double sMaxX = Math.max(PlayerCache.pos1.get(i)[0], PlayerCache.pos2.get(i)[0]);


                    if (((sMinX <= pMinX) && (pMinX <= sMaxX)) || ((sMinX <= pMaxX) && (pMaxX <= sMaxX)) || ((pMinX <= sMinX) && (sMaxX <= pMaxX))) {
                        Double pMinY = event.getFrom().getY();
                        Double pMaxY = event.getTo().getY();
                        Double sMinY = Math.min(PlayerCache.pos1.get(i)[1], PlayerCache.pos2.get(i)[1]);
                        Double sMaxY = Math.max(PlayerCache.pos1.get(i)[1], PlayerCache.pos2.get(i)[1]);


                        if (((sMinY <= pMinY) && (pMinY <= sMaxY)) || ((sMinY <= pMaxY) && (pMaxY <= sMaxY)) || ((pMinY <= sMinY) && (sMaxY <= pMaxY))) {
                            Double pMinZ = event.getFrom().getZ();
                            Double pMaxZ = event.getTo().getZ();
                            Double sMinZ = Math.min(PlayerCache.pos1.get(i)[2], PlayerCache.pos2.get(i)[2]);
                            Double sMaxZ = Math.max(PlayerCache.pos1.get(i)[2], PlayerCache.pos2.get(i)[2]);


                            if (((sMinZ <= pMinZ) && (pMinZ <= sMaxZ)) || ((sMinZ <= pMaxZ) && (pMaxZ <= sMaxZ)) || ((pMinZ <= sMinZ) && (sMaxZ <= pMaxZ))) {
                                if (FileCache.storedFiles.get("data").getConfig().isSet("players." + uuid)) {
                                    List<String> playerInfo = FileCache.storedFiles.get("data").getConfig().getStringList("players." + uuid);
                                    playerInfo.add(PlayerCache.regions.get(i));
                                    FileCache.storedFiles.get("data").getConfig().set("players." + uuid, playerInfo);


                                } else {
                                    List<String> playerInfo = new ArrayList<>();
                                    playerInfo.add(PlayerCache.regions.get(i));
                                    FileCache.storedFiles.get("data").getConfig().set("players." + uuid, playerInfo);


                                }
                                FileCache.storedFiles.get("data").saveConfig();
                                if (PlayerCache.playerRegions.containsKey(uuid)) {
                                } else {
                                    PlayerCache.playerRegions.put(uuid, new ArrayList<>());
                                }
                                PlayerCache.playerRegions.get(uuid).add(PlayerCache.regions.get(i));
                                player.sendMessage(MessageUtils.getColor("&aYou have entered the region " + PlayerCache.regions.get(i) +
                                        "! Use &6/ftp " + PlayerCache.regions.get(i) + "&a to teleport back to this region!", true));

                            }

                        }

                    }

                }

            }

        }


    }






}
