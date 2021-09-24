package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Configuration {
    final String httpdConfFile;
    final String mimeTypesFile;
    static HashMap<String, ArrayList<String>> configMap;
    static HashMap<String, String[]> mimeTypesMap;
 
    public Configuration(String httpdConfFile, String mimeTypesFile) {
        System.out.println("⏳ Reading httpd.conf and mime.types...");
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
            ArrayList<String> alias = new ArrayList<>();
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.isBlank()) {
                    directive = line.split("\\s+", 3);      
                    if(directive.length == 3) {
                        // alias = directive[1].trim().split("\\s+");
                        alias.add(directive[1]);
                        alias.add(directive[2]);
                        configMap.put(directive[0], alias);
                    } else {
                        alias.add(directive[1]);
                        configMap.put(directive[0], alias);
                    }    
                    System.out.println(directive[0] + " : " + alias);
                    System.out.println(configMap);
                    alias.clear();    
                    sb.append(line + "\r\n");   
                    // TODO: handle aliases (/~traciely/ "/home/ajwc/SFSU/CSC667/Assignments/web-server/public_html/")
                } 
            }
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

    public HashMap<String, ArrayList<String>> getConfigMap() {
        return configMap;
    }

    public HashMap<String, String[]> getMimeTypesMap() {
        return mimeTypesMap;
    }

    private void checkDirectives() {
        for(String i : configMap.keySet()) {
            // getDirective(i);
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

    // public String[] getDirective(String directive) {
    //     return configMap.get(directive);
    // }
}
