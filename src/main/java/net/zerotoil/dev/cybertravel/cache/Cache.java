package net.zerotoil.dev.cybertravel.cache;

import net.zerotoil.dev.cybertravel.CyberTravel;

public class Cache {

    final CyberTravel main;

    private Config config;
    private Players players;
    private Regions regions;

    public Cache(CyberTravel main) {
        this.main = main;
    }

    /**
     * Reload all cache.
     */
    public void reload() {
        load(true);
    }

    public void load(boolean loadCore) {

        if (loadCore) main.reloadCore();

        config = new Config(main);
        regions = new Regions(this);
        regions.reloadRegions();
        players = new Players(this);
        players.loadPlayers();

        if (loadCore) main.core().loadFinish();

    }

    /**
     * Contains all values of the config.yml
     * saved to cache.
     *
     * @return Config containing values
     */
    public Config config() {
        return config;
    }

    /**
     * Contains add cache involving regions.
     *
     * @return Region cache
     */
    public Regions regions() {
        return regions;
    }

    /**
     * Contains add cache involving players.
     *
     * @return Player cache
     */
    public Players players() {
        return players;
    }

}
