package net.zerotoil.dev.cybertravel.object.region.settings;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.PlayerData;
import net.zerotoil.dev.cybertravel.object.region.Region;
import net.zerotoil.dev.cybertravel.utility.LangUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionMessage {

    private final CyberTravel main;
    @Getter private final Region region;

    @Getter private boolean header = true;
    @Getter private boolean footer = true;
    @Getter private List<String> content;


    public RegionMessage(CyberTravel main, Region region) {
        this.main = main;
        this.region = region;

        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions." + region.getId() + ".settings.message");
        if (section == null) return;

        header = section.getBoolean("header", header);
        footer = section.getBoolean("footer", footer);

        content = LangUtils.convertList(section, "content");

    }

    public void sendMessage(Player player) {
        if (header) main.sendMessage(player, "discovery-header", region.getPlaceholders(), region.getReplacements());

        PlayerData playerData = main.cache().getPlayer(player);
        main.core().getTextUtilities().sendMessageList(player, content, playerData.getPlaceholders(), playerData.getReplacements());

        if (footer) main.sendMessage(player, "discovery-footer", region.getPlaceholders(), region.getReplacements());

    }

}
