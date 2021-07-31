package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigCache {

    private CyberTravel main;
    private Configuration config;
    private boolean updateConfig;

    private boolean countDownEnabled;
    private long countDownSeconds;

    private boolean regionCoolDownEnabled;
    private long regionCoolDownSeconds;

    private boolean globalCoolDownEnabled;
    private long globalCoolDownSeconds;

    private boolean displayBorderEnabled;
    private List<Integer> displayBorderRGB;
    private long displayBorderSeconds;

    private boolean autoEnableRegions;

    // create class
    public ConfigCache(CyberTravel main) {

        this.main = main;
        initializeConfig();

    }

    // init config data
    public void initializeConfig() {

        config = main.getFileCache().getStoredFiles().get("config").getConfig();

        // should config auto update?
        if (isSet("auto-update-configs.config")) {
            updateConfig = getBoolean("auto-update-configs.config");
        } else {
            updateConfig = false;
        }

        // countdown module
        if (isSet("countdown.enabled")) {

            countDownEnabled = getBoolean("countdown.enabled");

            if (isSet("countdown.seconds")) {

                countDownSeconds = getLong("countdown.seconds");

            } else {

                if (updateConfig) {

                    set("countdown.seconds", 5);
                    countDownSeconds = 5;
                    Bukkit.getLogger().info("[CyberTravel] [config > countdown > seconds] was previously not set. It has been added to the config and set to 5 seconds.");

                } else {

                    countDownEnabled = false;
                    Bukkit.getLogger().info("[CyberTravel] [config > countdown > seconds] is not set. Countdown is now internally disabled.");

                }


            }

        } else {

            if (updateConfig) {

                set("countdown.enabled", false);
                if (!isSet("countdown.seconds")) set("countdown.seconds", 5);
                Bukkit.getLogger().info("[CyberTravel] [config > countdown > enabled] was previously not set. It has been added to the config and disabled.");

            }

            countDownEnabled = false;

        }

        // region cooldown module
        if (isSet("region-cooldown.enabled")) {

            regionCoolDownEnabled = getBoolean("region-cooldown.enabled");

            if (isSet("region-cooldown.seconds")) {

                regionCoolDownSeconds = getLong("region-cooldown.seconds");

            } else {

                if (updateConfig) {

                    set("region-cooldown.seconds", 300);
                    regionCoolDownSeconds = 300;
                    Bukkit.getLogger().info("[CyberTravel] [config > region-cooldown > seconds] was previously not set. It has been added to the config and set to 300 seconds (5 Minutes).");

                } else {

                    regionCoolDownEnabled = false;
                    Bukkit.getLogger().info("[CyberTravel] [config > region-cooldown > seconds] is not set. Region cooldown is now internally disabled.");

                }


            }

        } else {

            if (updateConfig) {

                set("region-cooldown.enabled", false);
                if (!isSet("region-cooldown.seconds")) set("region-cooldown.seconds", 300);
                Bukkit.getLogger().info("[CyberTravel] [config > region-cooldown > enabled] was previously not set. It has been added to the config and disabled.");

            }

            regionCoolDownEnabled = false;

        }

        // global cooldown module
        if (isSet("global-cooldown.enabled")) {

            globalCoolDownEnabled = getBoolean("global-cooldown.enabled");

            if (isSet("global-cooldown.seconds")) {

                globalCoolDownSeconds = getLong("global-cooldown.seconds");

            } else {

                if (updateConfig) {

                    set("global-cooldown.seconds", 300);
                    globalCoolDownSeconds = 300;
                    Bukkit.getLogger().info("[CyberTravel] [config > global-cooldown > seconds] was previously not set. It has been added to the config and set to 300 seconds (5 Minutes).");

                } else {

                    globalCoolDownEnabled = false;
                    Bukkit.getLogger().info("[CyberTravel] [config > global-cooldown > seconds] is not set. Global cooldown is now internally disabled.");

                }


            }

        } else {

            if (updateConfig) {

                set("global-cooldown.enabled", false);
                if (!isSet("global-cooldown.seconds")) set("global-cooldown.seconds", 300);
                Bukkit.getLogger().info("[CyberTravel] [config > global-cooldown > enabled] was previously not set. It has been added to the config and disabled.");

            }

            globalCoolDownEnabled = false;

        }

        // default RGB for border
        Integer[] defaultRGBArray = new Integer[]{0, 127, 255};
        List<Integer> defaultRGBList = Arrays.asList(defaultRGBArray);

        // border module
        if (isSet("display-border.enabled")) {

            displayBorderEnabled = getBoolean("display-border.enabled");

            if (isSet("display-border.seconds")) {

                displayBorderSeconds = getLong("display-border.seconds");

            } else {

                if (updateConfig) {

                    set("display-border.seconds", 10);
                    displayBorderSeconds = 10;
                    Bukkit.getLogger().info("[CyberTravel] [config > display-border > seconds] was previously not set. It has been added to the config and set to 10 seconds.");

                } else {

                    displayBorderEnabled = false;
                    displayBorderSeconds = 10;
                    Bukkit.getLogger().info("[CyberTravel] [config > display-border > seconds] is not set. Border is now internally disabled.");

                }


            }

            if (isSet("display-border.rgb")) {

                displayBorderRGB = config.getIntegerList("config.display-border.rgb");

            } else {

                if (updateConfig) {

                    set("display-border.rgb", defaultRGBArray);
                    displayBorderRGB = defaultRGBList;
                    Bukkit.getLogger().info("[CyberTravel] [config > display-border > rgb] was previously not set. It has been added to the config and set to a color of blue.");

                } else {

                    displayBorderEnabled = false;
                    displayBorderRGB = defaultRGBList;
                    Bukkit.getLogger().info("[CyberTravel] [config > display-border > rgb] is not set. Border is now internally disabled.");

                }


            }

        } else {

            if (updateConfig) {

                set("display-border.enabled", true);
                displayBorderEnabled = true;
                if (!isSet("display-border.seconds")) {

                    set("display-border.seconds", 10);
                    displayBorderSeconds = 10;

                } else {

                    displayBorderSeconds = getLong("display-border.seconds");

                }
                if (!isSet("display-border.rgb")) {

                    set("display-border.rgb", defaultRGBArray);
                    displayBorderRGB = defaultRGBList;

                } else {

                    displayBorderRGB = config.getIntegerList("config.display-border.rgb");

                }
                Bukkit.getLogger().info("[CyberTravel] [config > display-border > enabled] was previously not set. It has been added to the config and enabled.");

            } else {

                displayBorderEnabled = false;
                if (!isSet("display-border.seconds")) {

                    displayBorderSeconds = 10;

                } else {

                    displayBorderSeconds = getLong("display-border.seconds");

                }
                if (!isSet("display-border.rgb")) {

                    displayBorderRGB = defaultRGBList;

                } else {

                    displayBorderRGB = config.getIntegerList("config.display-border.rgb");

                }

            }

        }

        // auto enable region module
        if (isSet("auto-enable-regions")) {

            autoEnableRegions = config.getBoolean("config.auto-enable-regions");

        } else {

            if (updateConfig) {

                set("auto-enable-regions", true);
                autoEnableRegions = true;

            } else {

                autoEnableRegions = false;

            }

        }

    }

    // private accessors
    private boolean isSet(String path) {
        return config.isSet("config." + path);
    }
    private boolean getBoolean(String path) {
        return config.getBoolean("config." + path);
    }
    private long getLong(String path) {
        return config.getLong("config." + path);
    }
    private List<Short> getShortList(String path) {
        return config.getShortList("config." + path);
    }
    private void set(String path, Object object) {
        config.set("config." + path, object);
        try {
            main.getFileCache().getStoredFiles().get("config").saveConfig();
        } catch (IOException e) {}
    }

    // public getters
    public boolean isUpdateConfig() {
        return updateConfig;
    }
    public boolean isCountDownEnabled() {
        return countDownEnabled;
    }
    public long getCountDownSeconds() {
        return countDownSeconds;
    }
    public boolean isRegionCoolDownEnabled() {
        return regionCoolDownEnabled;
    }
    public long getRegionCoolDownSeconds() {
        return regionCoolDownSeconds;
    }
    public boolean isGlobalCoolDownEnabled() {
        return globalCoolDownEnabled;
    }
    public long getGlobalCoolDownSeconds() {
        return globalCoolDownSeconds;
    }
    public boolean isDisplayBorderEnabled() {
        return displayBorderEnabled;
    }
    public List<Integer> getDisplayBorderRGB() {
        return displayBorderRGB;
    }
    public long getDisplayBorderSeconds() {
        return displayBorderSeconds;
    }
    public boolean isAutoEnableRegions() {
        return autoEnableRegions;
    }

}
