package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import server.config.Configuration;

public class Server {
  private static int port;
  ServerSocket serverSocket;
  ExecutorService threadPool;
  String httpd_alex = "conf/httpd_alex.conf";

  public Server() {
    // * Instantiate new Configuration object and sets config private members with
    // * conf file paths
    Configuration config = new Configuration("conf/httpd_alex.conf", "conf/mime.types");
    // * Parse httpd.conf file into hash Map
    config.readHttpdConfig();
    // * Get the "Listen" directive from the hash Map member as the listening port
    port = Integer.parseInt(config.getConfigMap().get("Listen"));
    serverSocket = null;
    var boundedQueue = new ArrayBlockingQueue<Runnable>(1000);
    threadPool = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, boundedQueue, new AbortPolicy());
    // threadPool = Executors.newCachedThreadPool();
  }

  public void start() throws IOException {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("✨ Server started. \n" + "📭 Listening for messages on port " + port + "\n");
      while (!Thread.currentThread().isInterrupted()) {
        // * Wait for a connection request
        // * There is some thread that is responsible for handling the request
        // * Pass the socket to a new Worker thread and start
        Socket socketAccept = serverSocket.accept();
        Future<?> future = threadPool.submit(new Worker(socketAccept));
        // if(future == null) {
        // Thread.yield();
        // }
      }

    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      threadPool.shutdown();
    }
  }

  public void stop() throws IOException {
    serverSocket.close();
  }

}
