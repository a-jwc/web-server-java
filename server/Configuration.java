package server;

import java.io.*;
import java.net.*;

public class Configuration {
    String fileName;
    public Configuration(String fileName) {
        this.fileName = fileName;
    }
    public static void main(String[] args) throws IOException{

    }

    public void readConfig() {
        try {
            FileInputStream conf = new FileInputStream("conf/httpd.conf");
            DataInputStream fr = new DataInputStream(conf);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr));
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            conf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
