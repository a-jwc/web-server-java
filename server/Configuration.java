package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Configuration {
    final String fileName;
    static HashMap<String, String> configMap;

    public Configuration(String fileName) {
        this.fileName = fileName;
        configMap = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            DataInputStream fr = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr));
            StringBuilder sb = new StringBuilder();
            String line;
            String directive[];
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.isBlank()) {
                    directive = line.split(" ", 2);
                    configMap.put(directive[0], directive[1]);
                    System.out.println(configMap.get(directive[0]));
                    sb.append(line + "\r\n");
                } 
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException{

    }

    public HashMap<String, String> getConfigMap() {
        return configMap;
    }

    private void checkDirectives() {
        for(String i : configMap.keySet()) {
            getDirective(i);
        }
        // switch(configMap) {
        //     case "Listen": {
        //         getDirective(directive[0]);
        //         break;
        //     }
        //     case "DocumentRoot": {

        //         break;
        //     }
        //     case "LogFile": {

        //         break;
        //     }
        //     case "ScriptAlias": {

        //         break;
        //     }
        //     case "Alias": {

        //         break;
        //     }
        //     default: {
        //         System.out.println("y u here");
        //     }
        // }
    }

    private String getServerRoot() {
        return configMap.get("Listen");
    }
    public String getDirective(String directive) {
        return configMap.get(directive);
    }
}
