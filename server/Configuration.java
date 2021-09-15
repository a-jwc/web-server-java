package server;

import java.io.*;
import java.net.*;

public class Configuration {
    String fileName;
    String serverRoot;
    String documentRoot;
    String logFile;
    String scriptAlias;
    String alias[];
    public Configuration(String fileName) {
        this.fileName = fileName;
    }
    public static void main(String[] args) throws IOException{

    }

    public void readConfig() {
        try {
            FileInputStream fis = new FileInputStream("conf/httpd.conf");
            DataInputStream fr = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr));
            StringBuilder sb = new StringBuilder();
            String line;
            String directive[];
            while((line = br.readLine()) != null) {
                directive = line.split(" ", 2);
                System.out.println(directive[0]);
                switch(directive[0]) {
                    case "Listen": {
                        
                        break;
                    }
                    case "ServerRoot": {
                        break;
                    }
                    case "DocumentRoot": {

                        break;
                    }
                    case "LogFile": {

                        break;
                    }
                    case "ScriptAlias": {

                        break;
                    }
                    case "Alias": {

                        break;
                    }
                    default: {
                        System.out.println("y u here");
                    }
                }
                sb.append(line + "\r\n");
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
