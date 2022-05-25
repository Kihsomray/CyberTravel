package net.zerotoil.dev.cybertravel.cache;

import lombok.Getter;
import net.zerotoil.dev.cybertravel.CyberTravel;

public class Cache {

    private final CyberTravel main;

    @Getter Config config;

    public Cache(CyberTravel main) {
        this.main = main;
        load(false);
    }

    public void reload() {
        load(true);
    }

    private void load(boolean loadCore) {

        if (loadCore) main.reloadCore();

        config = new Config(main);

        if (loadCore) main.core().loadFinish();

    }

}
