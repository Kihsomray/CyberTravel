package net.zerotoil.cybertravel.commands;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
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
        String uuid = player.getUniqueId().toString();

        List<String> args0 = new ArrayList<String>();
        List<String> args0Comp = new ArrayList<String>();

        if (player.hasPermission(main.getLangUtils().getPermission("player-about"))) args0.add("about");
        if (player.hasPermission(main.getLangUtils().getPermission("player-help-list")) ||
                player.hasPermission(main.getLangUtils().getPermission("admin-help-list"))) args0.add("help");
        if (player.hasPermission(main.getLangUtils().getPermission("player-discovered-list"))) args0.add("regions");
        if (player.hasPermission(main.getLangUtils().getPermission("player-teleport"))) {
            args0.add("teleport");
            args0.add("tp");
        }
        if (player.hasPermission(main.getLangUtils().getPermission("admin-reload"))) args0.add("reload");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-view-border"))) {
            args0.add("border");
            args0.add("outline");
        }
        if (player.hasPermission(main.getLangUtils().getPermission("admin-create-region"))) args0.add("create");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-delete-region"))) args0.add("delete");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-position1"))) args0.add("pos1");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-position2"))) args0.add("pos2");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-tp-location"))) args0.add("setTP");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-add-player-region"))) args0.add("addPlayerRegion");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-del-player-region"))) args0.add("delPlayerRegion");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-enabled"))) args0.add("setEnabled");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-disabled"))) args0.add("setDisabled");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-rename-region"))) args0.add("rename");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-price"))) args0.add("setPrice");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-reset-player-region-progress"))) args0.add("reset");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-set-display-name"))) args0.add("setDisplayName");
        if (player.hasPermission(main.getLangUtils().getPermission("admin-info-list"))) {
            args0.add("info");
            args0.add("list");
        }
        if (player.hasPermission(main.getLangUtils().getPermission("admin-del-command"))) {
            args0.add("delCMD");
            args0.add("delCommand");
        }
        if (player.hasPermission(main.getLangUtils().getPermission("admin-add-command"))) {
            args0.add("addCMD");
            args0.add("addCommand");
        }

        if (args.length == 1) {

            StringUtil.copyPartialMatches(args[0], args0, args0Comp);
            Collections.sort(args0Comp);
            return args0Comp;

        }

        if (args.length == 2) {

            List<String> args1 = new ArrayList<>();
            List<String> args1Comp = new ArrayList<>();

            if (cmdReq0(args[0], args0, "create")) {

                args1.add("<region>");
                StringUtil.copyPartialMatches(args[1], args1, args1Comp);
                Collections.sort(args1Comp);
                return args1Comp;

            }

            // teleport module
            if (cmdReq0(args[0], args0, "teleport") || cmdReq0(args[0], args0, "tp")) {

                // bypass permission
                if (sender.hasPermission(main.getLangUtils().getPermission("admin-teleport-bypass"))) {

                    if (main.getRegionCache().getRegions().size() != 0) {

                        args1 = new ArrayList<>(main.getRegionCache().getRegions().keySet());

                    } else {

                        args1.add("<region>");

                    }

                } else {

                    if (main.getPlayerCache().getPlayerRegions().containsKey(uuid)) {

                        if (main.getPlayerCache().getPlayerRegions().get(uuid).size() != 0) {

                            args1 = main.getPlayerCache().getPlayerRegions().get(uuid);

                        } else {

                            args1.add("<region>");

                        }
                    } else {

                        args1.add("<region>");

                    }

                }

                StringUtil.copyPartialMatches(args[1], args1, args1Comp);
                Collections.sort(args1Comp);
                return args1Comp;

            }

            // region identification module
            if (cmdReq0(args[0], args0, "pos1") || cmdReq0(args[0], args0, "pos2") || cmdReq0(args[0], args0, "setTP") ||
                    cmdReq0(args[0], args0, "delete") || cmdReq0(args[0], args0, "border") || cmdReq0(args[0], args0, "outline") ||
                    cmdReq0(args[0], args0, "delCMD") || cmdReq0(args[0], args0, "delCommand") || (cmdReq0(args[0], args0, "addPlayerRegion")) ||
                    (cmdReq0(args[0], args0, "delPlayerRegion")) || (cmdReq0(args[0], args0, "setEnabled")) || (cmdReq0(args[0], args0, "setDisabled")) ||
                    (cmdReq0(args[0], args0, "setDisplayName")) || (cmdReq0(args[0], args0, "rename")) || (cmdReq0(args[0], args0, "setPrice")) ||
                    cmdReq0(args[0], args0, "addCMD") || cmdReq0(args[0], args0, "addCommand")) {

                if (main.getRegionCache().getRegions().size() != 0) {

                    args1 = new ArrayList<>(main.getRegionCache().getRegions().keySet());

                } else {

                    args1.add("<region>");

                }

                StringUtil.copyPartialMatches(args[1], args1, args1Comp);
                Collections.sort(args1Comp);
                return args1Comp;

            }

            // reset playerData
            if (cmdReq0(args[0], args0, "reset")) {

                args1.add("playerData");
                StringUtil.copyPartialMatches(args[1], args1, args1Comp);
                Collections.sort(args1Comp);
                return args1Comp;

            }

        }

        if ((args.length >= 3) && (cmdReq0(args[0], args0, "setDisplayName"))) {
            List<String> argsName = new ArrayList<>();
            List<String> argsNameComp = new ArrayList<>();

            argsName.add("<display name>");

            StringUtil.copyPartialMatches(args[args.length - 1], argsName, argsNameComp);
            Collections.sort(argsNameComp);
            return argsNameComp;

        }

        if (args.length == 3) {

            List<String> args2 = new ArrayList<>();
            List<String> args2Comp = new ArrayList<>();

            if (cmdReq0(args[0], args0, "rename")) {

                args2.add("<new name>");

                StringUtil.copyPartialMatches(args[2], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;

            }

            if (cmdReq0(args[0], args0, "setPrice")) {

                args2.add("<price>");

                StringUtil.copyPartialMatches(args[2], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;

            }

            if (cmdReq0(args[0], args0, "delCMD") || cmdReq0(args[0], args0, "delCommand")) {

                args2.add("<id>");

                StringUtil.copyPartialMatches(args[2], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;

            }

            if (cmdReq0(args[0], args0, "addCMD") || cmdReq0(args[0], args0, "addCommand")) {

                args2.add("<command>");

                StringUtil.copyPartialMatches(args[2], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;

            }

            if ((cmdReq0(args[0], args0, "reset")) && args[1].equalsIgnoreCase("playerData")) {

                if (!main.getRegionCache().getRegions().isEmpty()) {

                    args2 = new ArrayList<>(main.getRegionCache().getRegions().keySet());

                } else {

                    args2.add("<region>");

                }

                args2.add("all");
                StringUtil.copyPartialMatches(args[2], args2, args2Comp);
                Collections.sort(args2Comp);
                return args2Comp;

            }

        }

        if (args.length == 4) {

            List<String> args3 = new ArrayList<>();
            List<String> args3Comp = new ArrayList<>();

            if (cmdReq0(args[0], args0, "tp") || cmdReq0(args[0], args0, "teleport")) {

                if (player.hasPermission(main.getLangUtils().getPermission("admin-tp-player-to-region"))) {

                    args3.add("bypass");

                    StringUtil.copyPartialMatches(args[3], args3, args3Comp);
                    Collections.sort(args3Comp);
                    return args3Comp;

                }

            }


        }

        return null;

    }

    private boolean cmdReq0(String arg0, List<String> args0, String command) {
        return (arg0.equalsIgnoreCase(command) && args0.contains(command));
    }

}
