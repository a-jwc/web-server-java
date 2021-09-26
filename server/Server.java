package server;

import java.io.*;
import java.net.*;
import java.util.*;

import server.config.Configuration;

public class Server {
    private static int port;

    public Server() {
        // * Instantiate new Configuration object and sets config private members with conf file paths
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
        // * Parse httpd.conf file into hash Map
        config.readHttpdConfig();
        // * Get the "Listen" directive from the hash Map member as the listening port
        port = Integer.parseInt(config.getConfigMap().get("Listen"));
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("✨ Server started. \n" + "📭 Listening for messages on port " + port + "\n");
            while (!serverSocket.isClosed()) {
                try {
                    // * Wait for a connection request
                    // * There is some thread that is responsible for handling the request
                    // * Pass the socket to a new Worker thread and start
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
