package net.zerotoil.dev.cybertravel.listener;

import net.zerotoil.dev.cybertravel.CyberTravel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnJoin implements Listener {

    private final CyberTravel main;

    public OnJoin(CyberTravel main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        loadPlayer(event.getPlayer());
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent event) {
        unloadPlayer(event.getPlayer());
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        unloadPlayer(event.getPlayer());
    }

    // loads player into cache
    private void loadPlayer(Player player) {
        boolean bool = main.cache().players().loadPlayer(player);
        if (main.cache().config().isDebugPlayerJoinLeave())
            main.logger("&aPlayer " + player.getName() + " was loaded into cache (&7" + bool + "&a).");
    }

    // unloads player from cache
    private void unloadPlayer(Player player) {
        boolean bool = main.cache().players().unloadPlayer(player);
        if (main.cache().config().isDebugPlayerJoinLeave())
            main.logger("&cPlayer " + player.getName() + " was unloaded from cache (&7" + bool + "&c).");
    }


}
