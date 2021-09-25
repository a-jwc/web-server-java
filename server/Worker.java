package server;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

public class Worker implements Runnable {
    final static String CRLF = "\r\n";
    private static String server = "Chau & Satumba";
    protected Socket socket;

    public Worker(Socket socket) {
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
            try {
                // System.out.println("Debug: got new message " + client.toString());
                // Read the request - listen to the messages; Bytes -> Chars
                InputStreamReader isr = new InputStreamReader(this.socket.getInputStream());
                // Reads text from char-input stream,
                BufferedReader br = new BufferedReader(isr);
                // Read the first request from the client
                StringBuilder request = new StringBuilder();            
                String line;
                
                line = br.readLine();
                while (!line.isBlank()) {
                    request.append(line + "\r\n");
                    line = br.readLine();
                }
                
                System.out.println("--REQUEST--");
                System.out.println(request);
                
                checkRequest(socket, request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // client.close();
            // }
        }
    }

    public static void checkRequest(Socket client, StringBuilder req) throws IOException {
        String reqArr[] = req.toString().split("\\r?\\n", 10);
        // Get the first line of the request; Get "resource" and "method" from first
        // line
        String firstLine = reqArr[0];
        String resource = firstLine.split(" ")[1];
        String method = firstLine.split(" ")[0];

        String fifthLine = reqArr[4];

        switch (method) {
            case "GET":
                getRequest(client, resource);
                break;
        }
    }

    private static void getRequest(Socket client, String resource) throws IOException {
        // Compare the "resource" to our list of things
        System.out.println("GET request resource from: " + resource);
        // System.out.println(client);
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        BufferedOutputStream bw = new BufferedOutputStream(client.getOutputStream());
        LocalDateTime dateTime = LocalDateTime.now();

        if (resource.equals("/")) {
            System.out.println("1: " + client);
            // Load the image from the filesystem
            FileInputStream indexHTML = new FileInputStream("public_html/ab1/ab2/index.html");
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
            System.out.println("2: " + client);

        } else if (resource.equals("/image")) {
            // Load the image from the filesystem
            FileInputStream image = new FileInputStream("public_html/images/sushi.jpg");
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(image.readAllBytes());
            clientOutput.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
            // Server
            clientOutput.write(("Server: " + server + "\r\n").getBytes());
            // Content-Length
            clientOutput.write(("Content-Length: 0" + "\r\n").getBytes());
            // Content-Type
            clientOutput.write(("Content-Type: image/jpg; charset=utf-8" + "\r\n").getBytes());
            image.close();
            clientOutput.flush();
        } else if (resource.equals("/400")) {
            // Status code
            pw.print(("HTTP/1.1 400 Not Found\r\n"));
            pw.print("\r\n");
            // print
            pw.print(("HTTP/1.1 400 Not Found\r\n"));
            // Date
            pw.print(("Date: " + dateTime.toString()));
            pw.print("\r\n");
            // Server
            pw.print(("Server: " + server));
            pw.print("\r\n");
            // Content-Length
            pw.print(("Content-Length: 0"));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: text/html; charset=utf-8"));
            pw.print("\r\n");
            pw.flush();
        } else if (resource.equals("/500")) {
            // Status code
            pw.print(("HTTP/1.1 500 Internal Server Error\r\n"));
            pw.print("\r\n");
            // print
            pw.print(("HTTP/1.1 500 Internal Server Error\r\n"));
            // Date
            pw.print(("Date: " + dateTime.toString()));
            pw.print("\r\n");
            // Server
            pw.print(("Server: " + server));
            pw.print("\r\n");
            // Content-Length
            pw.print(("Content-Length: 0"));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: text/html; charset=utf-8"));
            pw.print("\r\n");
            pw.flush();
        } else if (resource.equals("/304")) {
            // Status code
            pw.print(("HTTP/1.1 200 OK\r\n"));
            pw.print("\r\n");
            // print
            pw.print(("HTTP/1.1 304 Not Modified\r\n"));
            // Date
            pw.print(("Date: " + dateTime.toString()));
            pw.print("\r\n");
            // Server
            pw.print(("Server: " + server));
            pw.print("\r\n");
            // Content-Length
            pw.print(("Content-Length: 0"));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: text/html; charset=utf-8"));
            pw.print("\r\n");
            pw.flush();
        } else {
            // Status code
            pw.print(("HTTP/1.1 404 Not Found\r\n"));
            pw.print("\r\n");
            // print
            pw.print(("HTTP/1.1 404 Not Found\r\n"));
            // Date
            pw.print(("Date: " + dateTime.toString()));
            pw.print("\r\n");
            // Server
            pw.print(("Server: " + server));
            pw.print("\r\n");
            // Content-Length
            pw.print(("Content-Length: 0"));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: text/html; charset=utf-8"));
            pw.print("\r\n");
            pw.flush();
        }
    }
}
