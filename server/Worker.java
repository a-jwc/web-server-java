package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;
// * Current "run server" command: javac public_html/cgi-bin/RunScript.java;javac server/config/Configuration.java;javac server/Server.java;javac server/Worker.java;javac WebServer.java;java WebServer

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
    private static String directoryIndex;
    private static HttpdConfig httpdConfig;
    private static MimeTypes mimeTypes;
    private static String contentType;
    private static int contentLength;
    private static String extension;
    private static String lastModified;
    private static String htAccess;
    private static String dateTime;
    private static String dirAlias = null;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public Worker(Socket socket) {
        this.socket = socket;
        // * Instantiate new Configuration object
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");
        config.readHttpdConfig(); config.readMimeTypes();
        // * Local access to the directives via httpdConfig object
        httpdConfig = new HttpdConfig(config.getConfigMap());
        // * Local access to the mime types via mimeTypes object
        mimeTypes = new MimeTypes(config.getMimeTypesMap());
        contentLength = 0;
        
    }

    @Override
    public void run() {
        try {
            System.out.println("🎈 " + Thread.currentThread().getName() + " running.");
            proccessRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private synchronized void proccessRequest() {
        running.set(true);        
        while (running.get()) {
            try {
                // notify();
                // System.out.println("Debug: got new message " + client.toString());
                // * Read the request - listen to the messages; Bytes -> Chars
                InputStreamReader isr = new InputStreamReader(this.socket.getInputStream());
                // * Reads text from char-input stream,
                BufferedReader br = new BufferedReader(isr);
                // * Read the first request from the client
                StringBuilder request = new StringBuilder();
                String line;
                line = br.readLine();
                int count = 0;

                while (line != null && !line.isEmpty()) {
                    request.append(line + "\r\n");
                    line = br.readLine();
                    count++;
                }

                printRequest(request);
                parseResource(socket, request);    

                if(request != null && request.toString().length() != 0) {

                }
                System.out.println("Thread count: " + Thread.activeCount());
                System.out.println("Thread " + Thread.currentThread() + " is currently ");
                
                Thread.yield();
                // stopThread();
                // wait();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static synchronized void parseResource(Socket client, StringBuilder req) throws IOException {
        String reqArr[] = req.toString().split("\\r?\\n", 10);
        // * Get the first line of the request; Get "resource" and "method" from first line
        String firstLine = reqArr[0];
        // System.out.println(firstLine);
        // * Split by whitespace for as many elements are in the first line
        String requestLine[] = firstLine.split(" ", 0);
        String method = requestLine[0];
        String resource = requestLine[1];
        // httpdConfig.printAll();
        // String fifthLine = reqArr[4];

        // * Create and format Date field
        String dateTimePattern = "EEE, d MMM yyyy HH:mm:ss z";
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern);
        dateTime = sdf.format(new Date());

        // * Get document roots and index from hash Map
        documentRoot = httpdConfig.getDocumentRoot("DocumentRoot");
        directoryIndex = httpdConfig.getDirectoryIndex();

        // * Get alias
        String tempAlias = "Alias " + resource;
        if(httpdConfig.getMap().containsKey(tempAlias)) {
            dirAlias = httpdConfig.getMap().get(tempAlias);
        }

        // * Check if requesting file
        // * Get the content type by looping through the set of keys which are string arrays that contain the extensions
        if(resource.contains(".")) {
            extension = resource.substring(resource.lastIndexOf(".") + 1);
            System.out.println("Extension: " + extension);
            for(String[] eleArray : mimeTypes.getMap().keySet()) {
                for(int j = 0; j < eleArray.length; j++) {
                    if(eleArray[j].equals(extension)) {
                        contentType = mimeTypes.getMap().get(eleArray);
                    }
                }
            }
        } else {
            // * If the resource is not a file, append index.html to the end
            dirAlias = dirAlias + directoryIndex;
        }

        // * If htaccess exists, get headers for auth
        // * Else, return 401 response
        htAccess = httpdConfig.getAccessFileName();
        if(htAccessExist()) {
            // TODO
            getAccessHeaders();
            
        } else {
            PrintWriter pw = new PrintWriter(client.getOutputStream());
            // Status code
            pw.print(("HTTP/1.1 401 Unauthorized\r\n"));
            pw.print("\r\n");
            // Date
            pw.print(("Date: " + dateTime));
            pw.print("\r\n");
            // Server
            pw.print(("Server: " + server));
            pw.print("\r\n");
            pw.print(("WWW-Authenticate: Basic realm=\"Access to staging site\""));
            pw.print("\r\n");
            // Content-Length
            pw.print(("Content-Length: " + contentLength));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: text/html; charset=utf-8"));
            pw.print("\r\n");
            pw.flush();
        }

        // * If the file exists, continue to check the request method (GET, POST, HEAD, etc.)
        // * Else, respond with a 400 response 
        // TODO: Put 404 response into its own method
        if(fileExists(dirAlias)) {
            checkRequestVerb(client, method, resource);
        } else {
            PrintWriter pw = new PrintWriter(client.getOutputStream());

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
            pw.print(("Content-Length: " + contentLength));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: " + contentType + "\r\n"));
            pw.print("\r\n");
            pw.flush();
        }
    }

    private static boolean fileExists(String dirAlias) {
        File file = new File(dirAlias);
        if(file.exists()) {
            return true;
        }
        return false;
    }

    private static synchronized void checkRequestVerb(Socket client, String method, String resource) throws IOException {

        // * Switch on method
        switch (method) {
            case "GET":
                getRequest(client, resource);
                break;
            case "HEAD":
                headRequest(client, resource);
                break;
            case "POST":
                // String sixthLine = reqArr[5];
                // postRequest(client, resource, fifthLine, sixthLine);
                break;
            case "PUT":
                // String sixthLines = reqArr[5];
                // putRequest(client, resource, fifthLine, sixthLines);
                break;
            case "DELETE":
                deleteRequest(client, resource);
                break;
        }
    }

    private static synchronized void getRequest(Socket client, String resource) throws IOException {
        System.out.println("GET request resource from: " + resource);

        // System.out.println("Content type: " + contentType);

        // Worker.scriptAlias = httpdConfig.getScriptAlias("scriptAlias");

        // System.out.println("mimetypes: " + mimeTypes.getMap().entrySet());
        System.out.println("Socket object: " + client);

        // * Compare the "resource" to our list of resources
        if (resource.equals("/")) {            
            String defaultIndex = documentRoot + "/" + directoryIndex;
            System.out.println("default index: " + defaultIndex);
            FileInputStream indexHTML = new FileInputStream(defaultIndex);
            // FileInputStream indexHTML = new FileInputStream("./public_html/ab1/ab2/index.html");
            OutputStream clientOutput = client.getOutputStream();
            co_response_200(clientOutput, indexHTML);
            // System.out.println("2: " + client);
        } else if (resource.contains("/image")) {
            // Load the image from the filesystem
            // FileInputStream image = new FileInputStream("./public_html/images/sushi.jpg");
            FileInputStream image = new FileInputStream(documentRoot + resource);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(image.readAllBytes());
            clientOutput.write(("Date: " + dateTime + "\r\n").getBytes());
            // Server
            clientOutput.write(("Server: " + server + "\r\n").getBytes());
            // Content-Length
            clientOutput.write(("Content-Length: " + contentLength + "\r\n").getBytes());
            // Content-Type
            clientOutput.write(("Content-Type: " + contentType + "\r\n").getBytes());
            image.close();
            clientOutput.flush();
        } else if (resource.equals("/ab/")) {
            System.out.println("1: " + client);
            FileInputStream indexHTML = new FileInputStream(dirAlias);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
        } else if (resource.equals("/~traciely/")) {
            FileInputStream indexHTML = new FileInputStream(dirAlias);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
        } else if (resource.equals("/cgi-bin/")) {
            System.out.println("1: " + client);
            FileInputStream indexHTML = new FileInputStream(dirAlias);
            OutputStream clientOutput = client.getOutputStream();
            clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
        } else if (resource.equals("/500")) {
            PrintWriter pw = new PrintWriter(client.getOutputStream());

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
            pw.print(("Content-Length: " + contentLength));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: " + contentType + "\r\n"));
            pw.print("\r\n");
            pw.flush();
        } else if (resource.equals("/304")) {
            PrintWriter pw = new PrintWriter(client.getOutputStream());

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
            pw.print(("Content-Length: " + contentLength));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: " + contentType + "\r\n"));
            pw.print("\r\n");
            // Last-modified
            pw.print(("Last-Modified: " + lastModified + "\r\n"));
            pw.print("\r\n");
            pw.flush();
        } else {
            PrintWriter pw = new PrintWriter(client.getOutputStream());

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
            pw.print(("Content-Length: " + contentLength));
            pw.print("\r\n");
            // Content-Type
            pw.print(("Content-Type: " + contentType + "\r\n"));
            pw.print("\r\n");
            pw.flush();
        }
    }

    private static void headRequest(Socket client, String resource) throws IOException {
        System.out.println("HEAD request resource from: " + resource);
        PrintWriter pw = new PrintWriter(client.getOutputStream());
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
            clientOutput.write(("Content-Length: " + contentLength + "\r\n").getBytes());
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
            pw.print(("Content-Length: " + contentLength));
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
            pw.print(("Content-Length: " + contentLength));
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
            pw.print(("Content-Length: " + contentLength));
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
        clientOutput.write(("Content-Length: " + contentLength + "\r\n").getBytes());
        // Content-Type
        clientOutput.write(("Content-Type: text/html; charset=utf-8" + "\r\n").getBytes());
        clientOutput.flush();
    }

    private static boolean htAccessExist() {
        if(htAccess.length() != 0) {
            return true;
        }
        return false;
    }

    private static void getAccessHeaders() {

    }

    private void stopThread() {
        running.set(false);
    }

    // * Print functions
    private static void pw_response_200(PrintWriter pw) {
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
        pw.print(("Content-Length: " + contentLength));
        pw.print("\r\n");
        // Content-Type
        pw.print(("Content-Type: text/html; charset=utf-8"));
        pw.print("\r\n");
    }

    private static void co_response_200(OutputStream clientOutput, FileInputStream indexHTML) {
        try {
            clientOutput.write(("HTTP/1.1 200 OK").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Server: " + server).getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Connection: Keep-Alive").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Content-Type: " + contentType).getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Content-Language: en").getBytes());
            clientOutput.write(("\r\n").getBytes());
            // clientOutput.write(("Transfer-Encoding: chunked").getBytes());
            // clientOutput.write(("\r\n").getBytes());
            // clientOutput.write(("Content-Encoding: gzip").getBytes());
            // clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Keep-Alive: timeout=5, max=999").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("Vary: Accept-Encoding").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(("\r\n").getBytes());
            clientOutput.write(indexHTML.readAllBytes());
            indexHTML.close();
            clientOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
