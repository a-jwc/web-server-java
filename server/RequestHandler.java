package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements Runnable {
    final static String CRLF = "\r\n";
    private static String server = "Chau & Satumba";
    private Socket socket;
  
    public RequestHandler(Socket socket) {
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

    private void proccessRequest() {
        while (true) {
            // Handle a new incoming message
            try (Socket client = socket.accept()) {
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
      
            //   checkRequest(client, request);
              client.close();
            }
          }
    }
    
}
