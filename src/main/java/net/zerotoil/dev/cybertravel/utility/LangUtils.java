package net.zerotoil.dev.cybertravel.utility;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class LangUtils {


    // converts message to list
    public static List<String> convertList(ConfigurationSection config, String path) {

        if (!config.isSet(path)) return new ArrayList<>();

        // if already list
        if (config.isList(path)) return config.getStringList(path);

        // if single string
        List<String> list = new ArrayList<>();
        if (!config.getString(path).contains("[Ljava.lang.String")) list.add(config.getString(path));
        return list;

    }


}
