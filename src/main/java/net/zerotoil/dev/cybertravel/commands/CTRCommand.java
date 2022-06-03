package net.zerotoil.dev.cybertravel.commands;

import net.zerotoil.dev.cybertravel.CyberTravel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CTRCommand implements CommandExecutor {

    private final CyberTravel main;
    private final List<String> consoleCommands = Arrays.asList(
            "about",
            "reload");

    public CTRCommand(CyberTravel main) {
        this.main = main;
        main.getCommand("ctr").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final int len = args.length;

        // no args
        if (len == 0) return sendHelp(sender);

        // null if not player
        Player player = toPlayer(sender);

        // if not player and is not in console commands.
        if (player == null && !consoleCommands.contains(args[0].toLowerCase(Locale.ROOT)))
            return main.sendMessage(null, "console-restricted");

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "about":
            case "version":
                main.core().getTextUtilities().sendMessageList(sender, Arrays.asList(
                        "&c&lCyber&f&lLevels &fv" + main.getDescription().getVersion() + "&7.",
                        "&fDeveloped by &c" + main.getAuthors() + "&f.",
                        "&7Discover regions scattered across your world to",
                        "&7be able to quickly teleport back to that spot."
                ));
                return true;

            case "reload":
                main.sendMessage(player, "reloading");
                main.onDisable();
                main.reloadPlugin();
                return main.sendMessage(player, "reloaded");

            case "pos1":
                main.cache().getRegionFactory().setPos1(player, player.getLocation());
                return true;

            case "pos2":
                main.cache().getRegionFactory().setPos2(player, player.getLocation());
                return true;

            case "create":
                if (len == 2) {
                    main.cache().getRegionFactory().createRegion(player, args[1]);
                    return true;
                }

            default:
                return sendHelp(sender);

        }

    }

    /**
     * Sends help menu to a command-sender
     * based on inherited permissions.
     *
     * @param sender Sender in question
     * @return true always
     */
    boolean sendHelp(CommandSender sender) {
        Player player = toPlayer(sender);
        if (sender.hasPermission("ctp.admin.help")) main.sendMessage(player, "admin-help");
        else if (sender.hasPermission("ctp.player.help")) main.sendMessage(player, "player-help");
        else main.sendMessage(player, "no-permission");
        return true;
    }

    Player toPlayer(CommandSender sender) {
        return (sender instanceof Player) ? (Player) sender : null;
    }

}
