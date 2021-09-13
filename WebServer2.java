import java.io.*;
import java.net.*;
import java.net.http.HttpResponse;

class WebServer2 {
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
        // Thread t1 = new Thread(new Runnable () { 

        // Read the request - listen to the messages
        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        // Read the first request from the client
        StringBuilder request = new StringBuilder();
        String line; // Temp variable called line that holds one line at a time of our message
        String http_version = "";
        line = br.readLine();
        while (!line.isBlank()) {
          request.append(line + "\r\n");    
          if(line.contains("HTTP/1.1")) {
            http_version = line;
          }
          line = br.readLine();
        }

        System.out.println("--REQUEST--");
        System.out.println(request);

        // Decide how we'd like to respond

        String reqArr[] = request.toString().split("\\r?\\n", 2);
        // Get the first line of the request
        String firstLine = reqArr[0];
        // System.out.println("first line " + firstLine + "\nsecond line " + reqArr[1]);
        // Get "resource" and "method" from first line
        String method = firstLine.split(" ")[0];
        String resource = firstLine.split(" ")[1];
        // Compare the "resource" to our list of things
        // System.out.println(resource);

        // OutputStream clientOutput = client.getOutputStream();
        PrintWriter clientOutput = new PrintWriter(client.getOutputStream());
        // HttpResponse hr = client.send()
        if (resource.equals("/")) {
          clientOutput.print(("HTTP/1.1 200 OK\r\n"));
          clientOutput.print(("\r\n"));
          clientOutput.print(("Hello World"));
          clientOutput.print(("\r\n"));

          // Status code
          clientOutput.print(("HTTP/1.1 200 OK"));
          clientOutput.print(("\r\n"));

          // Date
          clientOutput.print(("Date: "));
          clientOutput.print(("\r\n"));

          // Server
          clientOutput.print(("Server:Chau & Satumba"));
          clientOutput.print(("\r\n"));

          // Content-Type
          clientOutput.print(("Content-Type: text/html"));
          clientOutput.print(("\r\n"));

          // Content-Length
          clientOutput.print(("Content-Length: "));
          clientOutput.print(("\r\n"));

          clientOutput.flush();
        } else {
          clientOutput.print(("HTTP/1.1 200 OK\r\n"));
          clientOutput.print(("\r\n"));
          clientOutput.print(("What are you looking for?"));
          clientOutput.print(("\r\n"));
          clientOutput.print(("HTTP/1.1 200 OK\r\n"));
          clientOutput.print(("\r\n"));
          clientOutput.flush();
        }
      // });
        client.close();
      }
    }
  }
}
