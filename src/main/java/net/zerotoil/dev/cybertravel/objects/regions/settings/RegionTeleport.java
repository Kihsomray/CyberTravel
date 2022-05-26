package net.zerotoil.dev.cybertravel.objects.regions.settings;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.cache.Config;
import net.zerotoil.dev.cybertravel.objects.regions.Region;
import net.zerotoil.dev.cybertravel.utilities.WorldUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RegionTeleport {

    private final CyberTravel main;
    @Getter private final Region region;

    @Getter private boolean enabled = false;
    @Getter private String world;
    @Getter private double[] location;

    @Getter private Map<String, Long> cooldowns = new HashMap<>();

    public RegionTeleport(CyberTravel main, Region region) {
        this.main = main;
        this.region = region;

        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions." + region.getId() + ".settings.teleport");
        if (section == null) return;

        enabled = section.getBoolean("enabled", enabled);
        world = section.getString("world", WorldUtils.defaultWorld());

        if (!WorldUtils.isWorld(world)) {
            main.logger("&cInvalid teleport world for region " + region.getId() + "! Disabling teleport...");
            enabled = false;
            return;
        }

        location = WorldUtils.coordinateStringToDouble(section.getString("location"));

    }

    /**
     * Add player to region cooldown for teleportation
     *
     * @param player Player to add
     * @return False if player already added.
     */
    public boolean addPlayerCooldown(Player player) {
        Config config = main.cache().config();
        String uuid = player.getUniqueId().toString();

        if (!config.isRegionCooldownEnabled()) return true;
        if (cooldowns.containsKey(uuid) && config.getRegionCooldownSeconds() * 1000
                < System.currentTimeMillis() - cooldowns.get(uuid)) return false;

        cooldowns.put(uuid, System.currentTimeMillis());
        return true;

    }

    private void addPlayerCooldown(String uuid, long epochTime) {
        if (System.currentTimeMillis() > epochTime) return;
        cooldowns.put(uuid, epochTime);
    }

    public void unloadCooldowns() {
        Configuration config = main.core().files().getConfig("plugin-data");
        config.set("region-cooldowns." + region.getId(), null);

        for (String uuid : cooldowns.keySet()) {
            long value = cooldowns.get(uuid);
            if (System.currentTimeMillis() > value) continue;
            config.set("region-cooldowns." + region.getId() + "." + uuid, value);
        }

        try {
            main.core().files().get("plugin-data").saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCooldowns() {

    }


}
