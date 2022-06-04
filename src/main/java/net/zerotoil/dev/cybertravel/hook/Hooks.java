package net.zerotoil.dev.cybertravel.hook;

import net.zerotoil.dev.cybertravel.CyberTravel;
import org.bukkit.Bukkit;

public class Hooks {

    private final CyberTravel main;
    private int counter = 0;

    private PlaceholderAPI placeholderAPI;

    public Hooks(CyberTravel main) {
        this.main = main;
        reload();
    }

    public void reload() {

        main.logger("&cLoading addons...");
        long startTime = System.currentTimeMillis();

        if (addHook("PlaceholderAPI")) placeholderAPI = new PlaceholderAPI(main);

        main.logger("&7Loaded &e" + counter + " &7addons in &a" + (System.currentTimeMillis() - startTime) + "ms&7.", "");

    }

    private boolean isEnabled(String plugin) {
        return (Bukkit.getPluginManager().getPlugin(plugin) != null);
    }

    private boolean addHook(String plugin) {
        boolean bool = isEnabled(plugin);
        if (bool) counter++;
        return bool;
    }

    public PlaceholderAPI placeholderAPI() {
        return placeholderAPI;
    }

}
