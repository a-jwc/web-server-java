package server;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

import server.config.Configuration;

public class Server {
    private static final String server = "Chau & Satumba";
    private static int port;
    private Hashtable<String, String> configMap;

    public Server() {
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
        config.readHttpdConfig();
        this.configMap = config.getConfigTable();
        port = Integer.parseInt(configMap.get("Listen"));
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
