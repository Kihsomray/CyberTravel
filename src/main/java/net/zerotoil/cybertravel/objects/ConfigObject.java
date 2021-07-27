package net.zerotoil.cybertravel.objects;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.Bukkit;

import java.util.List;

public class ConfigObject {

    private CyberTravel main;

    private boolean countDownEnabled;
    private int countDownSeconds;

    private boolean regionCoolDownEnabled;
    private int regionCoolDownSeconds;

    private boolean globalCoolDownEnabled;
    private int globalCoolDownSeconds;

    private boolean displayBorderEnabled;
    private List<Integer> displayBorderRGB;
    private int displayBoarderSeconds;

    public ConfigObject(CyberTravel main) {

        // work in progress

        /* if (isSet("countdown.enabled")) {
            countDownEnabled = getBoolean("countdown.enabled");

            if (isSet("countdown.seconds")) {

            }

        } else {

            Bukkit.getLogger().info("POOP");
            countDownEnabled = false;
        }

        if (isSet("region-cooldown.enabled")) {
            regionCoolDownEnabled = getBoolean("region-cooldown.enabled");
        } else {
            regionCoolDownEnabled = false;
        }


        if (isSet("global-cooldown.enabled")) {
            regionCoolDownEnabled = getBoolean("global-cooldown.enabled");
        } else {
            regionCoolDownEnabled = false;
        } */

    }

    private boolean isSet(String path) {
        if (main.getFileCache().getStoredFiles().get("config").getConfig().isSet("config." + path)) return true;
        return false;
    }

    private boolean getBoolean(String path) {
        return main.getFileCache().getStoredFiles().get("config").getConfig().getBoolean("config." + path);
    }
    private int getInt(String path) {
        return main.getFileCache().getStoredFiles().get("config").getConfig().getInt("config." + path);
    }
    private List<Integer> getIntegerList(String path) {
        return main.getFileCache().getStoredFiles().get("config").getConfig().getIntegerList("config." + path);
    }

}
