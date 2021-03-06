package net.zerotoil.dev.cybertravel.listener;

import net.zerotoil.dev.cybertravel.CyberTravel;

public class Events {

    private final CyberTravel main;
    private final int counter = 4;

    // event classes here

    public Events(CyberTravel main) {
        this.main = main;
        load();
    }

    private void load() {

        main.logger("&cLoading events...");
        long startTime = System.currentTimeMillis();

        new OnJoin(main);
        new OnMovement(main);

        main.logger("&7Loaded &e" + counter + " &7events in &a" + (System.currentTimeMillis() - startTime) + "ms&7.", "");

    }

}
