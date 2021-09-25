package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Configuration {
    final String httpdConfFile;
    final String mimeTypesFile;
    static Hashtable<String, String> configMap;
    static Hashtable<String, String[]> mimeTypesMap;
 
    public Configuration(String httpdConfFile, String mimeTypesFile) {
        System.out.println("⏳ Reading httpd.conf and mime.types...");
        this.httpdConfFile = httpdConfFile;
        this.mimeTypesFile = mimeTypesFile;
        try {
            configMap = new Hashtable<>();
            FileInputStream fis = new FileInputStream(httpdConfFile);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            StringBuilder sb = new StringBuilder();
            String line;
            String directive[];
            
            while((line = br.readLine()) != null) {
                // if(!line.startsWith("#") && !line.isBlank() && line.contains("lias")) {
                //     directive = line.split(" ", 3);
                //     String alias[] = {directive[1], directive[2]};
                //     configMap.put(directive[0], alias);
                // }
                if(!line.startsWith("#") && !line.isBlank()) {
                    directive = line.split(" ", 2);
                    configMap.put(directive[0], directive[1]);
                    System.out.println(configMap.get(directive[0]));
                    sb.append(line + "\r\n");
                    // TODO: handle aliases (/~traciely/ "/home/ajwc/SFSU/CSC667/Assignments/web-server/public_html/")
                } 
            }
            // print(configMap)
            br.close();
            dis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("⏳ Moving to mime.types...");
        }
        try {
            String[] fileExtension;
            mimeTypesMap = new Hashtable<>();
            FileInputStream fis = new FileInputStream(mimeTypesFile);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            StringBuilder sb = new StringBuilder();
            String line;
            String mimeType[];
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.isBlank()) {
                    mimeType = line.split("\\s+", 2);                    
                    if(mimeType.length == 2) {                        
                        fileExtension = mimeType[1].trim().split("\\s+");
                        // System.out.println(mimeTypesMap.size() + " : " + mimeType[0] + " " + mimeType[1]);
                        mimeTypesMap.put(mimeType[0], fileExtension);
                        // System.out.println(Arrays.toString(mimeTypesMap.get(mimeType[0])));
                        sb.append(line + "\r\n");
                    } 
                    // else {
                    //     fileExtension = new String[0];
                    //     mimeTypesMap.put(mimeType[0], fileExtension);
                    // }
                }
            }
            br.close();
            dis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("⌛ ✨Successfully read in " + httpdConfFile + " and " + mimeTypesFile + "✨\n");
        }
    }
    public static void main(String[] args) throws IOException{

    }

    public Hashtable<String, String> getConfigMap() {
        return configMap;
    }

    public Hashtable<String, String[]> getMimeTypesMap() {
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