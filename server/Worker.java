package server;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;
// * Current "run server" command: javac server/Configuration.java;javac server/Server.java;javac server/Worker.java;javac WebServer.java;java WebServer

import server.config.Configuration;
import server.config.HttpdConfig;
import server.config.MimeTypes;

public class Worker implements Runnable {
    final static String CRLF = "\r\n";
    private static String server = "Chau & Satumba";
    protected Socket socket;    
    private static String documentRoot;
    private static String logFile;
    private static String scriptAlias;
    private static String abAlias;
    private static String tracielyAlias;
    private static String directoryIndex;
    private static HttpdConfig httpdConfig;
    private static MimeTypes mimeTypes;

    public Worker(Socket socket) {
        this.socket = socket;
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
        this.httpdConfig = new HttpdConfig(config.getConfigTable());
        this.mimeTypes = new MimeTypes(config.getMimeTypesTable());
    }

    @Override
    public void run() {
        try {
            proccessRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void proccessRequest() {
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
                int count = 0;
                while (!line.isEmpty()) {
                    request.append(line + "\r\n");
                    line = br.readLine();
                    // System.out.println(request);
                    count++;
                }

                System.out.println("--REQUEST--");
                System.out.println(request);

                checkRequest(socket, request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static synchronized void checkRequest(Socket client, StringBuilder req) throws IOException {
        String reqArr[] = req.toString().split("\\r?\\n", 10);
        // Get the first line of the request; Get "resource" and "method" from first line
        String firstLine = reqArr[0];
        // System.out.println(firstLine);
        String requestLine[] = firstLine.split(" ", 0);
        String method = requestLine[0];
        String resource = requestLine[1];
        System.out.println("");
        // httpdConfig.printAll();
        // String fifthLine = reqArr[4];
        String fifthLine = "null";
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

    private static synchronized void getRequest(Socket client, String resource) throws IOException {
        // Compare the "resource" to our list of things
        System.out.println("GET request resource from: " + resource);
        // System.out.println(client);
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        BufferedOutputStream bw = new BufferedOutputStream(client.getOutputStream());
        LocalDateTime dateTime = LocalDateTime.now();

        for(Map.Entry<String, String> e : httpdConfig.getTable().entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }

        if (resource.equals("/")) {
            System.out.println("1: " + client);
            // Load the image from the filesystem
            documentRoot = httpdConfig.getDocumentRoot("DocumentRoot");
            directoryIndex = httpdConfig.getDirectoryIndex();
            FileInputStream indexHTML = new FileInputStream(documentRoot + "/" + directoryIndex);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
            System.out.println("2: " + client);

        } else if (resource.equals("/image")) {
            // Load the image from the filesystem
            FileInputStream image = new FileInputStream("./public_html/images/sushi.jpg");
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
        } else if (resource.equals("/ab/")) {
            abAlias = httpdConfig.getabAlias("Alias " + resource);
            // System.out.println("abAlias: " + resource + " " + abAlias);
            System.out.println("1: " + client);
            // Load the image from the filesystem
            FileInputStream indexHTML = new FileInputStream(abAlias);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
            System.out.println("2: " + client);

        } else if (resource.equals("/~traciely/")) {
            tracielyAlias = httpdConfig.getabAlias("Alias " + resource);
            // System.out.println("abAlias: " + resource + " " + abAlias);
            System.out.println("1: " + client);
            // Load the image from the filesystem
            FileInputStream indexHTML = new FileInputStream(tracielyAlias);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
            System.out.println("2: " + client);

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

    private static void headRequest(Socket client, String resource) throws IOException {
        System.out.println("HEAD request resource from: " + resource);
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        BufferedOutputStream bw = new BufferedOutputStream(client.getOutputStream());
        LocalDateTime dateTime = LocalDateTime.now();

        if (resource.equals("/")) {
            // Load the image from the filesystem
            FileInputStream indexHTML = new FileInputStream("./public_html/ab1/ab2/index.html");
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
            // Server
            clientOutput.write(("Server: " + server + "\r\n").getBytes());
            // Content-Length
            clientOutput.write(("Content-Length: 0" + "\r\n").getBytes());
            // Content-Type
            clientOutput.write(("Content-Type: text/html; charset=utf-8" + "\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
        } else if (resource.equals("/400")) {
            // Status code
            pw.print(("HTTP/1.1 400 Bad Request\r\n"));
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
        // Content-Length
        clientOutput.write((fifthLine + "\r\n").getBytes());
        // Content-Type
        clientOutput.write((sixthLine + "\r\n").getBytes());
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
        // Content-Length
        clientOutput.write((fifthLine + "\r\n").getBytes());
        // Content-Type
        clientOutput.write((sixthLines + "\r\n").getBytes());
        clientOutput.flush();
    }

    private static void deleteRequest(Socket client, String resource) throws IOException {
        System.out.println("DELETE request resource from: " + resource);
        LocalDateTime dateTime = LocalDateTime.now();

        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());
        // Status Code
        clientOutput.write(("HTTP/1.1 204 No Content" + "\r\n").getBytes());
        // Date
        clientOutput.write(("Date: " + dateTime.toString() + "\r\n").getBytes());
        // Server
        clientOutput.write(("Server: " + server + "\r\n").getBytes());
        // Content-Length
        clientOutput.write(("Content-Length: 0" + "\r\n").getBytes());
        // Content-Type
        clientOutput.write(("Content-Type: text/html; charset=utf-8" + "\r\n").getBytes());
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
        // Content-Length
        pw.print(("Content-Length: 0"));
        pw.print("\r\n");
        // Content-Type
        pw.print(("Content-Type: text/html; charset=utf-8"));
        pw.print("\r\n");
    }

    private static void printRequest(StringBuilder request) {
        System.out.println("--REQUEST--");
        System.out.println("Request: " + request);
    }

    public static void send401Response(Socket socket) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        writer.write("HTTP/1.1 401 Unauthorized \r\n");
        writer.write("Server: itsmejrob\r\n");
        writer.write("WW-Authenticate: Basic\r\n");
        writer.flush();
    }
}
