package public_html;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.util.*;

public class RunScript {
    private static Map<String, String> env;
    public static void main(String[] args) {

    }

    public static void start() {
        System.out.println("✨ In RunScript...");
        // * Replace with ScriptAlias path or your path for testing
        // * /web-server/public_html/cgi-bin/perl_env
        String pathToScript = "/home/ajwc/SFSU/CSC667/Assignments/web-server/public_html/cgi-bin/perl_env";
        ProcessBuilder pb = new ProcessBuilder(pathToScript);
        try {
            // * Place the environment variables in a map
            env = pb.environment();
            // * Print the env variables to stdout
            env.forEach((key, value) -> System.out.println(key + value));
            
            // * Create two new file objects: env variables, errors
            File outputTxt = new File("cgi-bin/standard-output.txt");
            File errorLog = new File("cgi-bin/error.log");

            // * Run script
            pb.command(pathToScript);

            // * Append output to text and log files respectively
            pb.redirectOutput(Redirect.appendTo(outputTxt));
            pb.redirectError(Redirect.appendTo(errorLog));
            pb.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
