package net.zerotoil.dev.cybertravel.listener;

import net.zerotoil.dev.cybertravel.CyberTravel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnMovement implements Listener {

    private final CyberTravel main;

    public OnMovement(CyberTravel main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {

        main.cache().checkRegionDiscovery(event.getPlayer());


    }


}
