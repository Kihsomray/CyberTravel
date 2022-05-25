package net.zerotoil.dev.cybertravel.events;

import net.zerotoil.dev.cybertravel.CyberTravel;

public class Events {

    private final CyberTravel main;

    // event classes here

    public Events(CyberTravel main) {
        this.main = main;
        load();
    }

    private void load() {

        main.logger("&Loading events...");
        long startTime = System.currentTimeMillis();

        // event stuff here

        main.logger("&7Loaded events in &a" + (System.currentTimeMillis() - startTime) + "ms&7.", "");

    }

}
