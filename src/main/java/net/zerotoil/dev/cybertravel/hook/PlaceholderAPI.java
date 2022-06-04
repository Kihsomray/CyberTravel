package net.zerotoil.dev.cybertravel.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.zerotoil.dev.cybertravel.CyberTravel;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final CyberTravel main;

    public PlaceholderAPI(CyberTravel main) {
        this.main = main;
        register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return main.getDescription().getPrefix();
    }

    @Override
    public @NotNull String getAuthor() {
        return main.getAuthors();
    }

    @Override
    public @NotNull String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        return null;
    }

}
