package net.zerotoil.dev.cybertravel.object.region.settings;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;
import net.zerotoil.dev.cybertravel.object.region.Region;
import net.zerotoil.dev.cybertravel.utility.LangUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class RegionCommands {

    private final CyberTravel main;
    @Getter private final Region region;

    @Getter private List<String> commands;

    public RegionCommands(CyberTravel main, Region region) {
        this.main = main;
        this.region = region;

        ConfigurationSection section = main.core().files().getConfig("regions").getConfigurationSection("regions." + region.getId() + ".settings");
        if (section == null) return;

        commands = LangUtils.convertList(section, "commands");

    }


}
