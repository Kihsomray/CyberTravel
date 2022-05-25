package net.zerotoil.dev.cybertravel.cache;

import net.zerotoil.dev.cybertravel.CyberTravel;

public class Cache {

    private final CyberTravel main;

    // cache classes here

    public Cache(CyberTravel main) {
        this.main = main;
        load(false);
    }

    public void reload() {
        load(true);
    }

    private void load(boolean loadCore) {

        if (loadCore) main.reloadCore();

        // cache stuff here

        if (loadCore) main.core().loadFinish();

    }

}
