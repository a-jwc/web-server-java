package server;

import java.io.*;
import java.net.*;
import java.time.*;
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

    private void proccessRequest() throws IOException {
        while (true) {
            // Handle a new incoming message
            // try (Socket client = serverSocket.accept()) {
            // client <-- messages queued up in it!!
            // System.out.println("Debug: got new message " + client.toString());
            // Read the request - listen to the messages; Bytes -> Chars
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
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

            checkRequest(socket, request);
            // client.close();
            // }
        }
    }

    public static void checkRequest(Socket client, StringBuilder req) throws IOException {
        String reqArr[] = req.toString().split("\\r?\\n", 2);
        // Get the first line of the request; Get "resource" and "method" from first
        // line
        String firstLine = reqArr[0];
        String method = firstLine.split(" ")[0];
        String resource = firstLine.split(" ")[1];
        String secondLine = reqArr[1];
        String thirdLine = reqArr[2];
        String fourthLine = reqArr[3];
        String fifthLine = reqArr[4];
        
        switch (method) {
            case "GET":
                getRequest(client, resource);
                break;
            case "HEAD":
                headRequest(client, resource);
                break;
            case "POST":
                String sixthLine = reqArr[5];
                postRequest(client, resource, fifthLine, sixthLine);
                break;
            case "PUT":
                String sixthLines = reqArr[5];
                putRequest(client, resource, fifthLine, sixthLines);
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
            // Send back an image
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
            // print
            pw.print(("What are you looking for?"));
            pw.print("\r\n");
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
            response_200(pw);
            pw.flush();
        } else {
            // Status code
            response_200(pw);
            pw.print(("What are you looking for?"));
            pw.print("\r\n");
            pw.flush();
        }
    }

    private static void postRequest(Socket client, String resource, String fifthLine, String sixthLine)
            throws IOException {
        System.out.println("POST request resource from: " + resource);
        LocalDateTime dateTime = LocalDateTime.now();

        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());

        // Status Code
        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
        // Date
        clientOutput.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
        // Server
        clientOutput.write(("Server: " + server + "\r\n").getBytes());
        // Content-Type
        clientOutput.write((sixthLine + "\r\n").getBytes());
        // Content-Length
        clientOutput.write((fifthLine + "\r\n").getBytes());
        clientOutput.flush();

    }

    private static void putRequest(Socket client, String resource, String fifthLine, String sixthLines)
            throws IOException {
        System.out.println("PUT request resource from: " + resource);
        LocalDateTime dateTime = LocalDateTime.now();

        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 201 Created\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());

        // Status Code
        clientOutput.write(("HTTP/1.1 201 Created" + "\r\n").getBytes());
        // Date
        clientOutput.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
        // Server
        clientOutput.write(("Server: " + server + "\r\n").getBytes());
        // Content-Type
        clientOutput.write((sixthLines + "\r\n").getBytes());
        // Content-Length
        clientOutput.write((fifthLine + "\r\n").getBytes());
        clientOutput.flush();

    }

    private static void deleteRequest(Socket client, String resource) throws IOException {
        System.out.println("DELETE request resource from: " + resource);

        LocalDateTime dateTime = LocalDateTime.now();

        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());

        // Status Code
        clientOutput.write(("HTTP/1.1 204 Delete File" + "\r\n").getBytes());
        // Date
        clientOutput.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
        // Server
        clientOutput.write(("Server: " + server + "\r\n").getBytes());
        // Content-Type
        clientOutput.write(("Content-Type: text/html; charset=utf-8" + "\r\n").getBytes());
        // Content-Length
        clientOutput.write(("Content-Length: 0" + "\r\n").getBytes());
        clientOutput.flush();

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

}
