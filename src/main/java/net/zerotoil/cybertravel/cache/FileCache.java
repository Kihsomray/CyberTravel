package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.FileObject;

import java.util.HashMap;

public class FileCache {

    private CyberTravel main;
    private HashMap<String, FileObject> storedFiles = new HashMap<>();

    public FileCache(CyberTravel main) {

        this.main = main;

        initializeFiles();

    }

    public void initializeFiles() {

        // clear stored files
        if (!storedFiles.isEmpty()) storedFiles.clear();

        // front-end
        storedFiles.put("config", new FileObject(main, "config.yml"));
        storedFiles.put("lang", new FileObject(main, "lang.yml"));
        storedFiles.put("regions", new FileObject(main, "regions.yml"));

        // back-end
        storedFiles.put("data", new FileObject(main, "data.yml"));

    }

    public HashMap<String, FileObject> getStoredFiles() {
        return this.storedFiles;
    }

}
