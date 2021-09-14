import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

public class Multithreaded {

  public Multithreaded() {

  }

  public static void main(String[] args) throws Exception {
    // Start recieving messages - ready to recieve messages!
    final int port = 8080;
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server started. \nListening for messages.");
      start(serverSocket);
      serverSocket.close();
    }
  }

  private static void start(ServerSocket serverSocket) throws IOException {
    while (true) {
      // Handle a new incoming message
      try {
        // client <-- messages queued up in it!!
        // client = serverSocket.accept();
        // System.out.println("Debug: got new message " + client.toString());
        // HttpRequestHandler httpRequest = new HttpRequestHandler(client);
        // Thread thread = new Thread(httpRequest);
        // thread.start();
        // client.close();
        new Thread(
          new HttpRequestHandler(serverSocket.accept())
        ).start();
      } catch (SocketException e) {
        e.printStackTrace();
      }
    }
  }
}

final class HttpRequestHandler implements Runnable {
  final static String CRLF = "\r\n";
  private static String server = "Chau & Satumba";
  private ServerSocket socket;

  public HttpRequestHandler(ServerSocket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      proccessRequest();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void proccessRequest() throws IOException {
    // Read the request - listen to the messages
    // Bytes -> Chars
    InputStreamReader isr = new InputStreamReader(socket.getInputStream());
    // Reads text from char-input stream
    BufferedReader br = new BufferedReader(isr);
    // Read the first request from the client
    StringBuilder request = new StringBuilder();
    String http_version = "";
    // String reqLine = br.readLine();
    // System.out.println("reqLine: " + reqLine + "\n");
    String headerLine = br.readLine();
    while (!headerLine.isBlank()) {
      request.append(headerLine + "\r\n");
      if (headerLine.contains("HTTP/1.1")) {
        http_version = headerLine;
      }
      headerLine = br.readLine();
      System.out.println("headerLine: " + headerLine + "\n");
    }

    // printRequest(request);
    checkRequest(socket, request);
  }

  public static void checkRequest(Socket client, StringBuilder req) throws IOException {
    String reqArr[] = req.toString().split("\\r?\\n", 2);
    // Get the first line of the request
    String firstLine = reqArr[0];
    // System.out.println("first line " + firstLine + "\nsecond line " + reqArr[1]);
    // Get "resource" and "method" from first line
    String method = firstLine.split(" ")[0];
    String resource = firstLine.split(" ")[1];
    System.out.println("method: " + method + "resource: " + resource);
    // Decide how we'd like to respond
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
    System.out.println("get request resource from: " + resource);
    // OutputStream clientOutput = client.getOutputStream();
    BufferedWriter clientOutput = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    resource = resource.toString();
    if (resource.equals("/")) {
      clientOutput.write(("HTTP/1.1 200 OK\r\n"));
      clientOutput.write(("\r\n"));
      clientOutput.write(("What are you looking for?"));
      clientOutput.flush();
    } else if (resource.equals("/hello")) {
      clientOutput.write(response_200());
      clientOutput.write(("Hello World!"));
      clientOutput.flush();
    } else if (resource.equals("/sushi")) {
      System.out.println("This is sushi");
      // Load the image from the filesystem
      FileInputStream image = new FileInputStream("public_html/images/sushi.jpg");
      System.out.println(image.toString());
      clientOutput.write(response_200());
      // clientOutput.write(image);
      image.close();
      clientOutput.flush();
    } else {
      clientOutput.write(response_200());
      clientOutput.write(("What are you looking for?"));
      clientOutput.flush();
    }
    // clientOutput.flush();
  }

  private static void postRequest(Socket client, String body) {
  }

  private static void headRequest(Socket client, String resource) {
  }

  private static void putRequest(Socket client, String resource) {
  }

  private static void deleteRequest(Socket client, String resource) {
  }

  private static void printRequest(StringBuilder request) {
    System.out.println("--REQUEST--");
    System.out.println("Request: " + request);
  }

  private static void printRequestLine(String http_method, String uri, String http_version) {
    System.out.println("HTTP_Method: " + http_method + "\nURI: " + uri + "\nHTTP_Version: " + http_version + "\n");
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

  private static String response_200() {
    LocalDateTime dateTime = LocalDateTime.now();
    String response = "HTTP/1.1 200 OK\r\n" + "Date: " + dateTime.toString() + "\r\n" + "Server: " + server + "\r\n"
        + "Content-Type: text/html; charset=utf-8\r\n" + "Content-Length: \r\n".getBytes();
    return response;
  }

}