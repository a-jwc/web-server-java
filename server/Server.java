package server;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

public class Server {
    private static final String server = "Chau & Satumba";
    private static int port;
    private Hashtable<String, String> configMap;
    private Hashtable<String, String[]> mimeTypesMap;
    private static String documentRoot;
    private static String logFile;
    private static String scriptAlias;
    private static String alias;

    public Server() {
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
        this.configMap = config.getConfigMap();
        this.mimeTypesMap = config.getMimeTypesMap();
        port = Integer.parseInt(configMap.get("Listen".toString()));
    }

    public static void main(String[] args) throws Exception {
        // Start recieving messages - ready to recieve messages!
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. \nListening for messages on port " + port);
            while (true) {
                // Handle a new incoming message
                try {
                    // there is some thread that is responsible for handling the request
                    // Pass the socket to a new thread called worker
                    new Thread(new Worker(serverSocket.accept())).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            // serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

}
