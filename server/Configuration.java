package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Configuration {
    final String httpdConfFile;
    final String mimeTypesFile;
    static HashMap<String, String> configMap;
    static HashMap<String, String> mimeTypesMap;
 
    public Configuration(String httpdConfFile, String mimeTypesFile) {
        this.httpdConfFile = httpdConfFile;
        this.mimeTypesFile = mimeTypesFile;
        try {
            configMap = new HashMap<>();
            FileInputStream fis = new FileInputStream(httpdConfFile);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
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
            br.close();
            dis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Successfully read in " + httpdConfFile + " and " + mimeTypesFile + "\n");
        }
        try {
            mimeTypesMap = new HashMap<>();
            FileInputStream fis = new FileInputStream(mimeTypesFile);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            StringBuilder sb = new StringBuilder();
            String line;
            String mimeType[];
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.isBlank()) {
                    mimeType = line.split("\\s+", 2);
                    if(mimeType.length < 1) {
                        System.out.println(mimeTypesMap.size() + " : " + mimeType[0] + " " + mimeType[1]);
                        mimeTypesMap.put(mimeType[0], mimeType[1]);
                        System.out.println(mimeTypesMap.get(mimeType[0]));
                        sb.append(line + "\r\n");
                    }
                }
            }
            br.close();
            dis.close();
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

    public HashMap<String, String> getMimeTypesMap() {
        return mimeTypesMap;
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
