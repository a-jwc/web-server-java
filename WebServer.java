import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

import server.*;

public class WebServer {

  private static final String server = "Chau & Satumba";
  private static int port = 8080;
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
    port = Integer.parseInt(configMap.get("Listen"));
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

  private static void listenForReq(ServerSocket serverSocket) throws IOException {
    while (true) {
      // Handle a new incoming message
      try (Socket client = serverSocket.accept()) {
        // client <-- messages queued up in it!!
        System.out.println("Debug: got new message " + client.toString());
        // Read the request - listen to the messages; Bytes -> Chars        
        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        // Reads text from char-input stream,
        BufferedReader br = new BufferedReader(isr);
        // Read the first request from the client
        StringBuilder request = new StringBuilder();
        String line; // Temp variable called line that holds one line at a time of our message

        line = br.readLine();
        while (!line.isBlank()) {
          request.append(line + "\r\n");
          line = br.readLine();
        }

        System.out.println("--REQUEST--");
        System.out.println(request);

        checkRequest(client, request);
        client.close();
      }
    }
  }

  public static void checkRequest(Socket client, StringBuilder req) throws IOException {
    String reqArr[] = req.toString().split("\\r?\\n", 2);
    // Get the first line of the request; Get "resource" and "method" from first line
    String firstLine = reqArr[0];    
    String method = firstLine.split(" ")[0];
    String resource = firstLine.split(" ")[1];

    switch (method) {
      case "GET":
        getRequest(client, resource);
        break;
      case "POST":
        postRequest(client, resource);
        break;
      case "HEAD":
        headRequest(client, resource);
        break;
      case "PUT":
        putRequest(client, resource);
        break;
      case "DELETE":
        deleteRequest(client, resource);
        break;
    }
  }

  private static void getRequest(Socket client, String resource) throws IOException {
    // Compare the "resource" to our list of things
    System.out.println("GET request resource from: " + resource);
    PrintWriter pw = new PrintWriter(client.getOutputStream());
    BufferedOutputStream bw = new BufferedOutputStream(client.getOutputStream());
    LocalDateTime dateTime = LocalDateTime.now();

    if (resource.equals("/")) {
      // Status code
      pw.print(("HTTP/1.1 200 OK\r\n"));
      pw.print("\r\n");
      response_200(pw);
      pw.flush();
    } else if (resource.equals("/image")) {
      //Send back an image
      // Load the image from the filesystem
      FileInputStream image = new FileInputStream("public_html/images/sushi.jpg");
      System.out.println(image.toString());
      // Turn the image into bytes?
      // Set the ContentType?
      OutputStream clientOutput = client.getOutputStream();
      clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
      clientOutput.write(("\r\n").getBytes());
      clientOutput.write(image.readAllBytes());
      image.close();
      clientOutput.flush();
    } else {
      // Status code
      pw.print(("HTTP/1.1 200 OK\r\n"));
      pw.print("\r\n");
      response_200(pw);
      //print
      pw.print(("What are you looking for?"));
      pw.print("\r\n");
      pw.flush();
    }
  }

  private static void postRequest(Socket client, String resource) throws IOException {
    System.out.println("POST request resource from: " + resource);

  }

  private static void headRequest(Socket client, String resource) throws IOException {
      System.out.println("HEAD request resource from: " + resource);
      PrintWriter pw = new PrintWriter(client.getOutputStream());
      BufferedOutputStream bw = new BufferedOutputStream(client.getOutputStream());
      LocalDateTime dateTime = LocalDateTime.now();

      if (resource.equals("/")) {
        // Status code
        pw.print(("HTTP/1.1 200 OK\r\n"));
        // Date
        pw.print(("Date: " + dateTime.toString() + "\r\n"));
        // Server
        pw.print(("Server: " + server + "\r\n"));
        // Content-Type
        pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
        // Content-Length
        pw.print(("Content-Length:  \r\n"));
        pw.flush();
      } else if (resource.equals("/sushi")) {
        System.out.println("This is sushi");
        // Load the image from the filesystem
        FileInputStream image = new FileInputStream("public_html/images/sushi.jpg");
        System.out.println(image.toString());
        // pw.write(response_200());
        pw.print(("HTTP/1.1 200 OK\r\n"));
        // Date
        pw.print(("Date: " + dateTime.toString() + "\r\n"));
        // Server
        pw.print(("Server: " + server + "\r\n"));
        // Content-Type
        pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
        // Content-Length
        pw.print(("Content-Length:  \r\n"));
        bw.write(image.readAllBytes());;
        image.close();
        bw.flush();
      } else {
        // status200(pw);
                // Status code
        pw.print(("HTTP/1.1 200 OK\r\n"));
        // Date
        pw.print(("Date: " + dateTime.toString() + "\r\n"));
        // Server
        pw.print(("Server: " + server + "\r\n"));
        // Content-Type
        pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
        // Content-Length
        pw.print(("Content-Length:  \r\n"));
        pw.print(("What are you looking for?"));
        pw.flush();
      }
    }

  private static void putRequest(Socket client, String resource) throws IOException {
      System.out.println("PUT request resource from: " + resource);

    }


  private static void deleteRequest(Socket client, String resource) throws IOException {
      System.out.println("DELETE request resource from: " + resource);

    }


  // * Print functions
  private static void response_200(PrintWriter pw) {
    LocalDateTime dateTime = LocalDateTime.now();
    pw.print(("HTTP/1.1 200 OK"));
    pw.print("\r\n");
    // Date
    pw.print(("Date: " + dateTime.toString()));
    pw.print("\r\n");
    // Server
    pw.print(("Server: " + server));
    pw.print("\r\n");
    // Content-Type
    pw.print(("Content-Type: text/html; charset=utf-8"));
    pw.print("\r\n");
    // Content-Length
    pw.print(("Content-Length: 2"));
    pw.print("\r\n");
  }

  private static void printRequest(StringBuilder request) {
    System.out.println("--REQUEST--");
    System.out.println("Request: " + request);
  }

  private static void printRequestLine(String http_method, String uri, String http_version) {
    System.out.println("HTTP_Method: " + http_method + "\nURI: " + uri + "\nHTTP_Version: " + http_version + "\n");
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

  // private static String getBody(BufferedReader req) {
  // StringBuilder sb = new StringBuilder();
  // BufferedReader br = new req.getReader();
  // String line;
  // while((line = br.readLine()) != null) {
  // sb.append(line);
  // sb.append(System.lineSeparator());
  // }
  // return sb.toString();
  // }
}

