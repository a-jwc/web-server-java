package server;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

public class Server {
    private static final String server = "Chau & Satumba";
    private static int port;
    private static HashMap<String, String> configMap;
    private static HashMap<String, String[]> mimeTypesMap;
    private static String documentRoot;
    private static String logFile;
    private static String scriptAlias;
    private static String alias;

    public Server() {
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
        configMap = config.getConfigMap();
        mimeTypesMap = config.getMimeTypesMap();
        port = Integer.parseInt(configMap.get("Listen".toString()));
    }

    public static void main(String[] args) throws Exception {
        // Start recieving messages - ready to recieve messages!
    }

    public void start() throws IOException {
        new Thread(new Worker(port)).start();
    }

}
