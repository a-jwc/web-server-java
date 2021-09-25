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

    public void printAll() {
        System.out.println(httpdConfigTable.entrySet());
    }
}
