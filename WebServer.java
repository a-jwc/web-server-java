import java.io.*;
import java.net.*;
import java.time.*;

public class WebServer {
  private static String server = "Chau & Satumba";

  public static void main(String[] args) throws Exception {
    // Start recieving messages - ready to recieve messages!
    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      System.out.println("Server started. \nListening for messages.");
      listenForReq(serverSocket);
    }
  }

  private static void listenForReq(ServerSocket serverSocket) throws IOException {
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

        String http_version = "";
        line = br.readLine();
        while (!line.isBlank()) {
          request.append(line + "\r\n");
          if (line.contains("HTTP/1.1")) {
            http_version = line;
          }
          line = br.readLine();
        }

        System.out.println("--REQUEST--");
        System.out.println(request);

        // printRequest(request);
        // Decide how we'd like to respond
        checkRequest(client, request);
        client.close();
      }
    }
  }

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
      // Date
      pw.print(("Date: " + dateTime.toString() + "\r\n"));
      pw.print("\r\n");
      // Server
      pw.print(("Server: " + server + "\r\n"));
      pw.print("\r\n");
      // Content-Type
      pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
      pw.print("\r\n");
      // Content-Length
      pw.print(("Content-Length: 2 \r\n"));
      pw.print("\r\n");
      pw.flush();
    } else if (resource.equals("/sushi")) {
      System.out.println("This is sushi");
      // Load the image from the filesystem
      FileInputStream image = new FileInputStream("public_html/images/sushi.jpg");
      OutputStream os = client.getOutputStream();
      System.out.println(image.toString());
      // pw.write(response_200());
      os.write(("HTTP/1.1 200 OK\r\n").getBytes());
      os.write(("\r\n").getBytes());
      // Date
      os.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
      os.write(("\r\n").getBytes());
      // Server
      os.write(("Server: " + server + "\r\n").getBytes());
      os.write(("\r\n").getBytes());
      // Content-Type
      os.write(("Content-Type: text/html; charset=utf-8\r\n").getBytes());
      os.write(("\r\n").getBytes());
      // Content-Length
      os.write(("Content-Length:  \r\n").getBytes());
      os.write(("\r\n").getBytes());
      os.write(image.readAllBytes());
      image.close();
      os.flush();
    } else {
      // Status code
      pw.print(("HTTP/1.1 200 OK\r\n"));
      pw.print("\r\n");
      // Date
      pw.print(("Date: " + dateTime.toString() + "\r\n"));
      pw.print("\r\n");
      // Server
      pw.print(("Server: " + server + "\r\n"));
      pw.print("\r\n");
      // Content-Type
      pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
      pw.print("\r\n");
      // Content-Length
      pw.print(("Content-Length:  \r\n"));
      pw.print("\r\n");
      pw.print(("What are you looking for?"));
      pw.flush();
    }
  }

  private static void postRequest(Socket client, String resource) throws IOException {
    System.out.println("POST request resource from: " + resource);
    PrintWriter pw = new PrintWriter(client.getOutputStream());
    LocalDateTime dateTime = LocalDateTime.now();

    if (resource.equals("/")) {
      // Status code
      pw.print(("HTTP/1.1 201 Created\r\n"));
      // Date
      pw.print(("Date: " + dateTime.toString() + "\r\n"));
      // Server
      pw.print(("Server: " + server + "\r\n"));
      // Content-Type
      pw.print(("Content-Type: text/html; charset=utf-8\r\n"));
      // Content-Length
      pw.print(("Content-Length:  \r\n"));
      pw.flush();
    } else {
      status200(pw);
      pw.print(("What are you looking for?"));
      pw.flush();
    }
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
      PrintWriter pw = new PrintWriter(client.getOutputStream());
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
      } else {
        status200(pw);
        pw.print(("What are you looking for?"));
        pw.flush();
      }
    }


  private static void deleteRequest(Socket client, String resource) throws IOException {
      System.out.println("DELETE request resource from: " + resource);
      PrintWriter pw = new PrintWriter(client.getOutputStream());
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
      } else {
        status200(pw);
        pw.print(("What are you looking for?"));
        pw.flush();
      }
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
