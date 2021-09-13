import java.io.*;
import java.net.*;

public class WebServer {

  static String get_req = "GET /index.html HTTP/1.1";
//   User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
//   Host: www.tutorialspoint.com
//   Accept-Language: en-us
//   Accept-Encoding: gzip, deflate
//   Connection: Keep-Alive";

  public static void main(String[] args) {
    checkRequest(get_req);
  }

  /*
        GET /hello.htm HTTP/1.1
        User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)
        Host: www.tutorialspoint.com
        Accept-Language: en-us
        Accept-Encoding: gzip, deflate
        Connection: Keep-Alive
    */

  /* • HTTP_METHOD IDENTIFIER HTTP_VERSION

        HTTP_HEADERS

        BODY
        
        • HTTP_METHOD specifies an HTTP method (verb): GET, HEAD, 
        POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH
        • IDENTIFIER is the URI of the resource or the body
        • HTTP_VERSION is the HTTP version being used by the client
        • BODY is an optional payload (the Content-Length header must be present if BODY is present)
    */

  public static void getRequest(String URI) {}
  
  public static void checkRequest(String get_req) {
    // Split by new lines
    String reqArr[] = get_req.split("\\r?\\n", 1);

    // Split first line by whitespace
    String headerArr[] = reqArr[0].split(" ");

    for (int i = 0; i < reqArr.length; i++) {
      System.out.println(reqArr[i] + "\n");
    }

    // HTTP method string
    String http_method = headerArr[0];
    // identifier string
    String uri = headerArr[1];
    // HTTP version string
    String http_version = headerArr[2];

    switch (http_method) {
      case "GET":
        System.out.println(
          "HTTP_Method: " +
          http_method +
          "\nURI: " +
          uri +
          "\nHTTP_Version: " +
          http_version +
          "\n"
        );
        getRequest(uri);
        break;
      case "POST":
        break;
      case "HEAD":
        break;
      case "PUT":
        break;
      case "DELETE":
        break;
    }
  }
<<<<<<< Updated upstream
=======

  private static void getRequest(Socket client, String resource) throws IOException {
    // Compare the "resource" to our list of things
    System.out.println("get request resource from: " + resource);
    PrintWriter pw = new PrintWriter(client.getOutputStream());
    LocalDateTime dateTime = LocalDateTime.now();

    if (resource.equals("/")) {
      // Status code
      pw.print(("HTTP/1.1 200 OK\r\n"));
      pw.print(("Date: " + dateTime.toString() + "\r\n"));
      pw.print(("Server: " + server + "\r\n"));
      // Content-Type
      pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
      // Content-Length
      pw.print(("Content-Length: \r\n"));
      pw.flush();
    } else if (resource.equals("/mj")) {
      System.out.println(resource.equals("/mj"));
      // Send back an image
      // Load the image from the filesystem
      FileInputStream image = new FileInputStream("fav.jpg");
      System.out.println(image.toString());
      // Turn the image into bytes?
      // Set the ContentType?
      status200(pw);
      pw.print(image);
      pw.flush();
      image.close();
    } else if (resource.equals("/hello")) {
      status200(pw);
      pw.print(("Hello World!"));
      pw.flush();
    } else {
      status200(pw);
      pw.print(("What are you looking for?"));
      pw.flush();
    }
  }

  private static void postRequest(Socket client, String body) {
  }

  private static void headRequest(Socket client, String resource) {
  }

  private static void putRequest(Socket client, String resource) {
  }

  private static void deleteRequest(Socket client, String resource) {
  }

  private static void status200(PrintWriter pw) throws IOException {
    pw.print(("HTTP/1.1 200 OK\r\n"));
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
>>>>>>> Stashed changes
}
