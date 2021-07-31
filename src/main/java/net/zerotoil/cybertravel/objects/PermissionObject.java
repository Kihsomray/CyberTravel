package net.zerotoil.cybertravel.objects;

import net.zerotoil.cybertravel.CyberTravel;

import java.io.IOException;

public class PermissionObject {

    private CyberTravel main;

    private String permission;
    private String path;
    private boolean updateConfig;

    public PermissionObject(CyberTravel main, boolean updateConfig, String path, String defaultPermission) {
        this.main = main;
        this.updateConfig = updateConfig;
        this.path = "permissions." + path;

        if (isSet(path)) {

            permission = getMessage(path);

        } else {

            if (updateConfig) {
                setMessage(path, defaultPermission);
            }

            permission = defaultPermission;

        }

    }

    private boolean isSet(String path) {
        return main.getFileCache().getStoredFiles().get("lang").getConfig().isSet("permissions." + path);
    }
    private String getMessage(String path) {
        return main.getFileCache().getStoredFiles().get("lang").getConfig().getString("permissions." + path);
    }

    private void setMessage(String path, String message) {
        main.getFileCache().getStoredFiles().get("lang").getConfig().set("permissions." + path, message);
        try {
            main.getFileCache().getStoredFiles().get("lang").saveConfig();
        } catch (IOException e) {}
    }

    public String getPermission() {
        return this.permission;
    }
    public String getPath() {
        return this.path;
    }

}
