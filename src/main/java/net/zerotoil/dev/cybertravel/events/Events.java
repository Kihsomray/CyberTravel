package net.zerotoil.dev.cybertravel.events;

import net.zerotoil.dev.cybertravel.CyberTravel;

public class Events {

    private final CyberTravel main;
    private int counter = 0;

    // event classes here

    public Events(CyberTravel main) {
        this.main = main;
        load();
    }

    private void load() {

        main.logger("&cLoading events...");
        long startTime = System.currentTimeMillis();

        // event stuff here

        main.logger("&7Loaded " + counter + " events in &a" + (System.currentTimeMillis() - startTime) + "ms&7.", "");

    }

}
