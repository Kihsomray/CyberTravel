package net.zerotoil.cybertravel.addons;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import static java.lang.System.currentTimeMillis;

public class PlaceholderAPI extends PlaceholderExpansion {

    private CyberTravel main;
    private String zeroSec;

    public PlaceholderAPI(CyberTravel main) {

        this.main = main;
        zeroSec = ChatColor.stripColor(main.getLangUtils().formatTime(0));

    }

    @Override
    public String getAuthor() {
        return main.getDescription().getAuthors().toString().replace("[", "").replace("]", "'");
    }

    @Override
    public String getIdentifier() {
        return "CyberTravel";
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override // required
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        String uuid = player.getUniqueId().toString();

        // global cooldown
        if (identifier.equalsIgnoreCase("cooldown_global")) {
            if (!main.getConfigCache().isGlobalCoolDownEnabled()) return zeroSec;
            if (!main.getCtpCommand().getGlobalCooldown().containsKey(uuid)) return zeroSec;

            long gcdTime = main.getConfigCache().getGlobalCoolDownSeconds();
            long globalTimeRemaining = ((main.getCtpCommand().getGlobalCooldown().get(uuid) / 1000) + gcdTime) - (currentTimeMillis() / 1000);

            if (globalTimeRemaining > 0) return ChatColor.stripColor(main.getLangUtils().formatTime(globalTimeRemaining));
            return zeroSec;

        }

        // region cooldown
        if (identifier.contains("cooldown_region_")) {

            String region = identifier.substring(16);

            if (!main.getRegionCache().getRegions().containsKey(region)) return "invalid region";
            if (!main.getConfigCache().isRegionCoolDownEnabled()) return zeroSec;
            if (!main.getCtpCommand().getRegionCooldown().containsKey(uuid)) return zeroSec;
            if (!main.getCtpCommand().getRegionCooldown().get(uuid).containsKey(region)) return zeroSec;

            long rcdTime = main.getConfigCache().getRegionCoolDownSeconds();
            long regionTimeRemaining = ((main.getCtpCommand().getRegionCooldown().get(uuid).get(region) / 1000) + rcdTime) - (currentTimeMillis() / 1000);
            if (regionTimeRemaining > 0) return ChatColor.stripColor(main.getLangUtils().formatTime(regionTimeRemaining));

            return zeroSec;

        }





        if (identifier.equalsIgnoreCase("placeholder2")) {
            return main.getConfig().getString("placeholders.placeholder2", "default2");
        }

        return null; // Placeholder is unknown by the Expansion
    }

}
