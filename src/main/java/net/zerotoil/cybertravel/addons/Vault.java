package net.zerotoil.cybertravel.addons;

import net.milkbowl.vault.economy.Economy;
import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class Vault {

    private CyberTravel main;
    private Economy econ;

    public Vault(CyberTravel main) {
        this.main = main;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
