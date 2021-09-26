import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.util.*;

public class RunScript {
    public static void main(String[] args) {
        // * Replace with ScriptAlias path or your path for testing
        // * /web-server/public_html/cgi-bin/perl_env
        String pathToScript = "/home/ajwc/SFSU/CSC667/Assignments/web-server/public_html/cgi-bin/perl_env";
        String cmd = "perl ";

        ProcessBuilder pb = new ProcessBuilder(pathToScript);
        
        try {
            // * Place the environment variables in a map
            Map<String, String> env = pb.environment();
            // * Print the env variables to stdout
            env.forEach((key, value) -> System.out.println(key + value));
            
            // * Create two new file objects: env variables, errors
            File outputTxt = new File("standard-output.txt");
            File errorLog = new File("error.log");
            
            // pb.redirectErrorStream(true);
            // env.put("GREETING", "Hola Mundo");

            pb.command(pathToScript);
            pb.redirectOutput(Redirect.appendTo(outputTxt));
            pb.redirectError(Redirect.appendTo(errorLog));
            pb.start();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
