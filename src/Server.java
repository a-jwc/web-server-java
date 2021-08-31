import java.net.*;
import java.io.*;

public class Server {
    public static final int DEFAULT_PORT = 3100;

    private void run(int port) throws IOException {
        ServerSocket socket = new ServerSocket(DEFAULT_PORT);
        Socket client = null;
        StringBuilder sb = new StringBuilder();

        try {
            client = socket.accept();
        } catch (IOException e) {
            e.getStackTrace();
        }
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
        while ((line = br.readLine()) != null) {
            pw.println(line);
            if("* ".equals(line)) {
                pw.println("Ending connection...");
                break;
            }
        }
        outputRequest(client);
        sendResponse(client);
        client.close();
        socket.close();
    }

    private static void sendResponse(Socket client) throws IOException {

    }

    private static void outputRequest(Socket client) throws IOException {

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run(DEFAULT_PORT);
    }
}
