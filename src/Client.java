import java.io.*;
import java.net.*;
import java.time.*;

public class Client {

    // initialize address and ports
    String ipAddress = "127.0.0.1";
    int portNumber = 3100;
    OutputStream os;
    OutputStreamWriter osw;
    InputStream is;
    InputStreamReader isr;

    PrintWriter pw;
    BufferedReader br;

    public Client() {

    }

    public void run() {
        // listen on portNumber
        try {
            Socket socket = new Socket(ipAddress, portNumber);

            for (int i = 0; i < 10; i++) {
                sendMessage(socket, i);
                readResponse(socket);
            }

            socket.close();
        } catch (UnknownHostException u) {
            u.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }

    public void sendMessage(Socket socket, int counter) throws IOException {
        System.out.println("📧 Sending message...");
        pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println( "Message " + counter );
        pw.printf( "now: %s%n", LocalDateTime.now() );
    }

    public void readResponse(Socket socket) throws IOException {
        System.out.println("⏳ Reading response...");
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("* " + line);
        }
    }
}