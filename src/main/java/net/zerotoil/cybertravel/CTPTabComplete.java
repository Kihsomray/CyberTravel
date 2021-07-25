package net.zerotoil.cybertravel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CTPTabComplete implements TabCompleter {

    private CyberTravel main;

    public CTPTabComplete(CyberTravel main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        Player player = (Player) sender;

        List<String> args1 = new ArrayList<String>();
        List<String> args1Comp = new ArrayList<String>();

        if (hasPermission(player,"player-about", "CyberTravel.player.about")) args1.add("about");
        if (hasPermission(player,"player-help-list", "CyberTravel.player.help") || hasPermission(player,"admin-help-list", "CyberTravel.admin.help")) args1.add("help");
        if (hasPermission(player,"player-discovered-list", "CyberTravel.player.list")) args1.add("regions");
        if (hasPermission(player,"player-teleport", "CyberTravel.player.teleport")) args1.add("teleport");
        if (hasPermission(player,"player-teleport", "CyberTravel.player.teleport")) args1.add("tp");
        if (hasPermission(player,"admin-reload", "CyberTravel.admin.reload")) args1.add("reload");
        if (hasPermission(player,"admin-create-region", "CyberTravel.admin.edit.create")) args1.add("create");
        if (hasPermission(player,"admin-delete-region", "CyberTravel.admin.edit.delete")) args1.add("delete");
        if (hasPermission(player,"admin-set-position1", "CyberTravel.admin.edit.pos1")) args1.add("pos1");
        if (hasPermission(player,"admin-set-position2", "CyberTravel.admin.edit.pos2")) args1.add("pos2");
        if (hasPermission(player,"admin-set-tp-location", "CyberTravel.admin.edit.settp")) args1.add("settp");
        if (hasPermission(player,"admin-add-player-region", "CyberTravel.admin.manage.add")) args1.add("addregion");
        if (hasPermission(player,"admin-del-player-region", "CyberTravel.admin.manage.delete")) args1.add("delregion");

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], args1, args1Comp);
            Collections.sort(args1Comp);
            return args1Comp;

        }

        if (args.length == 2) {

            List<String> args2 = new ArrayList<>();
            List<String> args2Comp = new ArrayList<>();

            if (args[0].equalsIgnoreCase("create") && args1.contains("create")) {
                args2.add("<region>");
                StringUtil.copyPartialMatches(args[1], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;
            }

            if (args[0].equalsIgnoreCase("teleport") && args1.contains("teleport") || args[0].equalsIgnoreCase("tp") && args1.contains("tp")) {

                // bypass permission
                if (sender.hasPermission(main.getFileUtils().getPermission("admin-teleport-bypass", "CyberTravel.admin.bypass"))) {
                    if (main.getFileUtils().dataFile().getConfigurationSection("regions") != null) {
                        if (main.getFileUtils().dataFile().getConfigurationSection("regions").getKeys(false).size() != 0) {
                            args2 = new ArrayList<>(main.getFileUtils().dataFile().getConfigurationSection("regions").getKeys(false));
                        } else {
                            args2.add("<region>");
                        }
                    } else {
                        args2.add("<region>");
                    }

                } else {

                    if (main.getPlayerCache().getPlayerRegions().containsKey(player.getUniqueId().toString())) {
                        if (main.getPlayerCache().getPlayerRegions().get(player.getUniqueId().toString()).size() != 0) {
                            args2 = main.getPlayerCache().getPlayerRegions().get(player.getUniqueId().toString());
                        } else {
                            args2.add("<region>");
                        }
                    } else {
                        args2.add("<region>");
                    }

                }

                StringUtil.copyPartialMatches(args[1], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;

            }

            if ((args[0].equalsIgnoreCase("pos1") && args1.contains("pos1")) || (args[0].equalsIgnoreCase("pos2") && args1.contains("pos2"))
                    || (args[0].equalsIgnoreCase("settp") && args1.contains("settp")) || (args[0].equalsIgnoreCase("delete") && args1.contains("delete"))) {
                if (main.getFileUtils().dataFile().getConfigurationSection("regions") != null) {
                    if (main.getFileUtils().dataFile().getConfigurationSection("regions").getKeys(false).size() != 0) {
                        args2 = new ArrayList<>(main.getFileUtils().dataFile().getConfigurationSection("regions").getKeys(false));
                    } else {
                        args2.add("<region>");
                    }
                } else {
                    args2.add("<region>");
                }
                StringUtil.copyPartialMatches(args[1], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;
            }

        }

        if (args.length == 3) {
            List<String> args3 = new ArrayList<>();
            List<String> args3Comp = new ArrayList<>();

            if ((args[0].equalsIgnoreCase("addregion") && args1.contains("addregion")) || (args[0].equalsIgnoreCase("delregion") && args1.contains("delregion"))) {

                if (!main.getPlayerCache().getRegions().isEmpty()) {
                    args3 = new ArrayList<>(main.getPlayerCache().getRegions().keySet());
                } else {
                    args3.add("<region>");
                }
                StringUtil.copyPartialMatches(args[2], args3, args3Comp);
                Collections.sort(args3Comp);
                return args3Comp;

            }

        }

        return null;

    }

    private boolean hasPermission(Player player, String permissionName, String defaultPermission) {
        return player.hasPermission(main.getFileUtils().getPermission(permissionName, defaultPermission));
    }



}
