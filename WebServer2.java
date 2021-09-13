import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

class Main {
  public static void main(String[] args) throws Exception {
    // Start recieving messages - ready to recieve messages!
    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      System.out.println("Server started. \nListening for messages.");

<<<<<<< Updated upstream
      while (true) {
        // Handle a new incoming message

        try (Socket client = serverSocket.accept()) {
          // client <-- messages queued up in it!!
          System.out.println("Debug: got new message " + client.toString());

          // Read the request - listen to the messages
          InputStreamReader isr = new InputStreamReader(client.getInputStream());

          BufferedReader br = new BufferedReader(isr);

          // Read the first request from the client
          StringBuilder request = new StringBuilder();
          String line; // Temp variable called line that holds one line at a time of our message

=======
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
>>>>>>> Stashed changes
          line = br.readLine();
          while (!line.isBlank()) {
            request.append(line + "\r\n");
            line = br.readLine();
          }

          System.out.println("--REQUEST--");
          System.out.println(request);

          //Decide how we'd like to respond

            // Get the first line of the request
            String firstLine = request.toString().split("\n")[0];
            // System.out.println(firstLine);
            // Get the second thing "resource" from the first line (seperated by spaces)
            String resource = firstLine.split(" ")[1];
            // Compare the "resource" to our list of things
            // System.out.println(resource);

            OutputStream clientOutput = client.getOutputStream();
            if (resource.equals("/")) {
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Hello World").getBytes());
            clientOutput.write(("\r\n").getBytes());

            //Status code
            clientOutput.write(("HTTP/1.1 200 OK").getBytes());
            clientOutput.write(("\r\n").getBytes());

            //Date
            clientOutput.write(("Date: ").getBytes());
            clientOutput.write(("\r\n").getBytes());

            //Server
            clientOutput.write(("Server:Chau & Satumba").getBytes());
            clientOutput.write(("\r\n").getBytes());

            //Content-Type
            clientOutput.write(("Content-Type: text/html").getBytes());
            clientOutput.write(("\r\n").getBytes());

            //Content-Length
            clientOutput.write(("Content-Length: ").getBytes());
            clientOutput.write(("\r\n").getBytes());

            clientOutput.flush();
          } else {
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("What are you looking for?").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.flush();
          }
















<<<<<<< Updated upstream
          client.close();
=======
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
          // Status code
          clientOutput.print(("HTTP/1.1 200 OK\r\n"));
          // Date
          clientOutput.print(("Date:  \r\n"));
          // Server
          clientOutput.print(("Server: Chau & Satumba2\r\n"));
          // Content-Type
          clientOutput.print(("Content-Type: text/html\r\n"));
          // Content-Length
          clientOutput.print(("Content-Length: \r\n"));
          clientOutput.flush();
        } else {
          clientOutput.print(("HTTP/1.1 200 OK\r\n"));
          clientOutput.print(("What are you looking for?\r\n"));
          clientOutput.print(("HTTP/1.1 200 OK\r\n"));
          clientOutput.flush();
>>>>>>> Stashed changes
        }
      }
    }
  }
}
