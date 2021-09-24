// package public_html.cgi-bin;

import java.io.IOException;
import java.util.Map;

public class RunScript {
    public static void main(String[] args) throws IOException {
        String pathToScript = "/home/ajwc/SFSU/CSC667/Assignments/web-server/public_html/cgi-bin/perl_env";

        ProcessBuilder pb = new ProcessBuilder(pathToScript);

        Map<String, String> env = pb.environment();
    }
}
