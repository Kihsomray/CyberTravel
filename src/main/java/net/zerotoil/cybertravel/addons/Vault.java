package net.zerotoil.cybertravel.addons;

import net.milkbowl.vault.economy.Economy;
import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class Vault {

    private CyberTravel main;
    private Economy economy;
    private boolean enabled;

    public Vault(CyberTravel main) {
        this.main = main;
        enabled = setupEconomy();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }
    public boolean isEnabled() {
        return enabled;
    }

}
