package net.zerotoil.cybertravel.cache;

import net.zerotoil.cybertravel.CyberTravel;
import net.zerotoil.cybertravel.objects.FileObject;

import java.util.HashMap;

public class FileCache {

    public FileCache(CyberTravel main) {
        this.main = main;
    }

    private HashMap<String, FileObject> storedFiles = new HashMap<>();
    private CyberTravel main;

    public HashMap<String, FileObject> getStoredFiles() {
        return this.storedFiles;
    }

    public void initializeFiles() {

        // clear stored files
        if (!storedFiles.isEmpty()) storedFiles.clear();

        // front-end
        storedFiles.put("config", new FileObject(main, "config.yml"));
        storedFiles.put("lang", new FileObject(main, "lang.yml"));

        // back-end
        storedFiles.put("data", new FileObject(main, "data.yml"));

    }


}
