import java.io.*;
import java.util.*;

public class RunScript {
    public static void main(String[] args) {
        // * Replace with ScriptAlias path or your path for testing
        // * /web-server/public_html/cgi-bin/perl_env
        String pathToScript = "/home/ajwc/SFSU/CSC667/Assignments/web-server/public_html/cgi-bin/perl_env";

        ProcessBuilder pb = new ProcessBuilder(pathToScript);
        
        try {
            Map<String, String> env = pb.environment();
            // for(Header header: request.getHeaders()) {

            // }
            pb.redirectOutput(new File("temp.txt"));
            pb.start();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
