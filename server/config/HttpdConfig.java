package server.config;

import java.util.*;

public class HttpdConfig {
    private static Hashtable<String,String> httpdConfigTable;

    public HttpdConfig(Hashtable<String,String> config) {
        httpdConfigTable = config;
    }

    public int getListen(String listenPort) {
        return Integer.parseInt(httpdConfigTable.get(listenPort));
        // return 0;
    }

    public Hashtable<String,String> getTable() {
        return httpdConfigTable;
    }

    public void printAll() {
        System.out.println(httpdConfigTable.entrySet());
    }

    public String getabAlias(String abAlias) {
        return httpdConfigTable.get(abAlias);
    }

    public String getDocumentRoot(String documentRoot) {
        return httpdConfigTable.get(documentRoot);
    }

    public String getDirectoryIndex() {
        return "index.html";
    }

    public String getScriptAlias(String scriptAlias) {
        return httpdConfigTable.get(scriptAlias);
    }
}
