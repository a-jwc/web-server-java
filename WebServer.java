import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

import server.*;

public class WebServer {

  private static final String server = "Chau & Satumba";
  private static int port;
  private static HashMap<String,String> configMap;
  private static HashMap<String,String[]> mimeTypesMap;
  private static String documentRoot;
  private static String logFile;
  private static String scriptAlias;
  private static String alias;

  public WebServer() {
    Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
    configMap = config.getConfigMap();
    mimeTypesMap = config.getMimeTypesMap();
    port = Integer.parseInt(configMap.get("Listen".toString()));
  }

  public static void main(String[] args) throws Exception {
    // Start recieving messages - ready to recieve messages!
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      WebServer webServer = new WebServer();
      System.out.println("Server started. \nListening for messages.");
      start(serverSocket);
      serverSocket.close();
    }
  }

  private static void start(ServerSocket serverSocket) throws IOException {
    while (true) {
      // Handle a new incoming message
      try {
        new Thread(
          new RequestHandler(serverSocket.accept())
        ).start();
      } catch (SocketException e) {
        e.printStackTrace();
      }
    }
  }

  private static void printRequest(StringBuilder request) {
    System.out.println("--REQUEST--");
    System.out.println("Request: " + request);
  }

  public static void send401Response(Socket socket) throws IOException {
    BufferedWriter writer = new BufferedWriter(
    new OutputStreamWriter(socket.getOutputStream())
    );

    writer.write("HTTP/1.1 401 Unauthorized \r\n");
    writer.write("Server: itsmejrob\r\n");
    writer.write("WW-Authenticate: Basic\r\n");
    writer.flush();
  }
}
