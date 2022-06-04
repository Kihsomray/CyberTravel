package net.zerotoil.dev.cybertravel.object.region.settings;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.cache.Config;
import net.zerotoil.dev.cybertravel.object.region.Region;
import net.zerotoil.dev.cybertravel.utility.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionTeleport {

    private final CyberTravel main;
    @Getter private final Region region;

    @Getter private boolean enabled = false;
    @Getter private String world;
    @Getter private double[] location;

    @Getter private Map<UUID, Long> cooldowns = new HashMap<>();

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

        loadCooldowns();

    }

    /**
     * Teleports player to region location.
     *
     * @param player Player in question
     * @return False if not teleported
     */
    public boolean teleportPlayer(@NotNull Player player) {

        if (!player.hasPermission("ctr.player.teleport")) return !main.sendMessage(player, "no-permission");

        main.sendMessage(player, "region-teleporting", region.getPlaceholders(), region.getReplacements());
        boolean teleport = addPlayerCooldown(player);
        if (!teleport) return !main.sendMessage(player, "region-cooldown", new String[]{"time"},
                (cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000 + "");

        player.teleport(getLocation());
        return main.sendMessage(player, "region-teleported", region.getPlaceholders(), region.getReplacements());
    }

    /**
     * Add player region cooldown based on the
     * value set in config.yml.
     *
     * @param player Player to add
     * @return False if player already added
     */
    public boolean addPlayerCooldown(Player player) {

        return addPlayerCooldown(player, main.cache().config().getRegionCooldownSeconds() * 20);

    }

    /**
     * Add player to region cooldown for teleportation.
     *
     * @param player Player to add
     * @param ticks Cooldown in ticks
     * @return False if player already added
     */
    public boolean addPlayerCooldown(Player player, long ticks) {
        return setPlayerCooldown(player.getUniqueId(), ticks * 50);
    }

    private boolean setPlayerCooldown(UUID uuid, long epochTime) {
        Config config = main.cache().config();
        if (!config.isRegionCooldownEnabled()) return true;

        if (cooldowns.containsKey(uuid) &&
                System.currentTimeMillis() < cooldowns.get(uuid)) return false;

        cooldowns.put(uuid, epochTime);
        return true;

    }

    /**
     * Unload cooldowns from plugin-data.yml
     */
    public void unloadCooldowns() {

        if (!main.cache().config().isRegionCooldownEnabled()) return;

        Configuration config = main.core().files().getConfig("plugin-data");
        config.set("region-cooldowns." + region.getId(), new String[0]);

        for (UUID uuid : cooldowns.keySet()) {
            long value = cooldowns.get(uuid);
            if (System.currentTimeMillis() > value) continue;
            config.set("region-cooldowns." + region.getId() + "." + uuid.toString(), value);
        }

        try {
            main.core().files().get("plugin-data").saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cooldowns.clear();

    }

    /**
     * Load cooldowns from plugin-data.yml
     */
    private void loadCooldowns() {
        cooldowns.clear();

        if (!main.cache().config().isRegionCooldownEnabled()) return;

        ConfigurationSection section = main.core().files().getConfig("plugin-data").getConfigurationSection("region-cooldowns");
        if (section == null || section.getConfigurationSection(region.getId()) == null) unloadCooldowns();

        assert section != null; // pointless
        section = section.getConfigurationSection(region.getId());
        assert section != null; // pointless

        for (String s : section.getKeys(false)) {

            if (section.getLong(s) < System.currentTimeMillis()) continue;
            cooldowns.put(UUID.fromString(s), section.getLong(s));

        }

    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), location[0], location[1], location[2]);
    }

}
