package net.zerotoil.dev.cybertravel.addons;

import net.zerotoil.dev.cybertravel.CyberTravel;
import org.bukkit.Bukkit;

public class Addons {

    private final CyberTravel main;

    private PlaceholderAPI placeholderAPI;

    public Addons(CyberTravel main) {
        this.main = main;
        reload();
    }

    public void reload() {

        main.logger("&Loading addons...");
        long startTime = System.currentTimeMillis();

        if (isEnabled("PlaceholderAPI")) placeholderAPI = new PlaceholderAPI(main);

        main.logger("&7Loaded events in &a" + (System.currentTimeMillis() - startTime) + "ms&7.", "");

    }

    private boolean isEnabled(String plugin) {
        return (Bukkit.getPluginManager().getPlugin(plugin) != null);
    }

    public PlaceholderAPI placeholderAPI() {
        return placeholderAPI;
    }

}
