import java.io.*;
import java.net.*;

public class WebServer {
  public static void main(String[] args) throws Exception {
    // Start recieving messages - ready to recieve messages!
    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      System.out.println("Server started. \nListening for messages.");
      while (true) {
        // Handle a new incoming message
        try (Socket client = serverSocket.accept()) {
          // client <-- messages queued up in it!!
          System.out.println("Debug: got new message " + client.toString());
          // Read the request - listen to the messages
          // Bytes -> Chars
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

          // System.out.println("--REQUEST--");
          // System.out.println("Request: " + request);

          // Decide how we'd like to respond
          checkRequest(client, request);
          client.close();
        }
      }
    }
  }

  /*
   * • HTTP_METHOD IDENTIFIER HTTP_VERSION
   * 
   * HTTP_HEADERS
   * 
   * BODY
   * 
   * • HTTP_METHOD specifies an HTTP method (verb): GET, HEAD, POST, PUT, DELETE,
   * TRACE, OPTIONS, CONNECT, PATCH • IDENTIFIER is the URI of the resource or the
   * body • HTTP_VERSION is the HTTP version being used by the client • BODY is an
   * optional payload (the Content-Length header must be present if BODY is
   * present)
   */

  public static void checkRequest(Socket client, StringBuilder req) throws IOException {
    String reqArr[] = req.toString().split("\\r?\\n", 2);
    // Get the first line of the request
    String firstLine = reqArr[0];
    // System.out.println("first line " + firstLine + "\nsecond line " + reqArr[1]);
    // Get "resource" and "method" from first line
    String method = firstLine.split(" ")[0];
    String resource = firstLine.split(" ")[1];
    switch (method) {
      case "GET":
        getRequest(client, resource);
        break;
      case "POST":
        // postRequest(body);
        break;
      case "HEAD":
        break;
      case "PUT":
        break;
      case "DELETE":
        break;
    }
  }

  private static void getRequest(Socket client, String resource) throws IOException {
    // Compare the "resource" to our list of things
    System.out.println("get request resource from: " + resource);
    OutputStream clientOutput = client.getOutputStream();

    if (resource.equals("/mj")) {
      System.out.println(resource.equals("/mj"));
      // Send back an image

      // Load the image from the filesystem
      FileInputStream image = new FileInputStream("fav.jpg");
      System.out.println(image.toString());
      // Turn the image into bytes?
      // Set the ContentType?
      status200(clientOutput);
      clientOutput.write(image.readAllBytes());
      clientOutput.flush();
      image.close();
    } else if (resource.equals("/hello")) {
      status200(clientOutput);
      clientOutput.write(("Hello World").getBytes());
      clientOutput.flush();
    } else {
      clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
      clientOutput.write(("\r\n").getBytes());
      clientOutput.write(("What are you looking for?").getBytes());
      clientOutput.flush();
    }
  }

  private static void postRequest(String body) {

  }

  private static void status200(OutputStream co) throws IOException {
    co.write(("HTTP/1.1 200 OK\r\n").getBytes());
    co.write(("\r\n").getBytes());
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
}
