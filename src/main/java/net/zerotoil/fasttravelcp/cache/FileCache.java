package net.zerotoil.fasttravelcp.cache;

import net.zerotoil.fasttravelcp.FastTravelCP;
import net.zerotoil.fasttravelcp.objects.FileObject;

import java.util.HashMap;

public class FileCache {

    public static HashMap<String, FileObject> storedFiles = new HashMap<>();

    public static void initializeFiles() {

        // clear stored files
        if (!storedFiles.isEmpty()) storedFiles.clear();

        // front-end
        storedFiles.put("config", new FileObject(FastTravelCP.getInstance(), "config.yml"));
        storedFiles.put("lang", new FileObject(FastTravelCP.getInstance(), "lang.yml"));

        // back-end
        storedFiles.put("data", new FileObject(FastTravelCP.getInstance(), "data.yml"));

    }


}
