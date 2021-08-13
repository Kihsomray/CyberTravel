package net.zerotoil.cybertravel.objects;

import net.zerotoil.cybertravel.CyberTravel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionObject {

    private boolean enabled;
    private double[] pos1;
    private double[] pos2;
    private double[] setTP;
    private List<String> commands = new ArrayList<>();
    private String name;
    private String displayName;
    private String world;
    private double price;
    private long cooldown;

    public RegionObject() {

    }

    public RegionObject(String name, String world) {

        this.name = name;
        this.world = world;
        this.enabled = false;
        price = 0;
        cooldown = -1;

    }

    public boolean isEnabled() {
        return enabled;
    }
    public String getDisplayName() {
        if (displayName != null) return ChatColor.translateAlternateColorCodes('&', displayName + "&r");
        return name;
    }
    public double[] getPos1() {
        return pos1;
    }
    public double[] getPos2() {
        return pos2;
    }
    public double[] getSetTP() {
        return setTP;
    }
    public List<String> getCommands() {
        return this.commands;
    }
    public String getName() {
        return name;
    }
    public String getWorld() {
        return world;
    }
    public double getPrice() {
        return price;
    }
    public long getCooldown() {
        return cooldown;
    }

    public double getPosMin(int arrayNumber) {
        return Math.min(this.pos1[arrayNumber], this.pos2[arrayNumber]);
    }
    public double getPosMax(int arrayNumber) {
        return Math.max(this.pos1[arrayNumber], this.pos2[arrayNumber]);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setPos1(double[] pos1) {
        this.pos1 = pos1;
    }
    public void setPos2(double[] pos2) {
        this.pos2 = pos2;
    }
    public void setSetTP(double[] setTP) {
        this.setTP = setTP;
    }
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    public void addCommand(String command) {
        this.commands.add(command);
    }
    public boolean removeCommand(int index) {
        if (this.commands.size() > index) {
            this.commands.remove(index);
            return true;
        }
        return false;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setWorld(String world) {
        this.world = world;
    }
    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            this.price = 0;
        }
    }
    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public void sendCommands(CyberTravel main, Player player) {
        if (commands.isEmpty()) return;
        player.sendMessage(main.getLangUtils().getMessage("region-info-cmd-header", false));
        for (String i : commands) {
            player.sendMessage(main.getLangCache().getMessages().get("region-info-cmd").getMessage(false, "command", i));
        }
    }
    public void sendInfo(CyberTravel main, Player player) {
        player.sendMessage(main.getLangCache().getMessages().get("region-info-name").getMessage(false,
                "region", name, "displayName", getDisplayName(), "status", enabled + ""));
        player.sendMessage(main.getLangCache().getMessages().get("region-info-location").getMessage(false,
                "world", world, "pos1", removeBrackets(pos1), "pos2", removeBrackets(pos2), "setTP", removeBrackets(setTP)));
        player.sendMessage(main.getLangCache().getMessages().get("region-info-price").getMessage(false, "price", String.format("%.2f", price)));
        sendCommands(main, player);
    }

    private String removeBrackets(double[] input) {
        String string = Arrays.toString(input).replace("[", "").replace("]", "");
        if (string.equalsIgnoreCase("null")) return "not set";
        return string;
    }

}
