package server.config;

import java.io.*;
import java.net.*;
import java.util.*;

public class Configuration {
    final String httpdConfFile;
    final String mimeTypesFile;
    static Hashtable<String, String> configTable;
    static Hashtable<String, String[]> mimeTypesTable;
 
    public Configuration(String httpdConfFile, String mimeTypesFile) {
        System.out.println("⏳ Reading httpd.conf and mime.types...");
        this.httpdConfFile = httpdConfFile;
        this.mimeTypesFile = mimeTypesFile;
        try {
            configTable = new Hashtable<>();
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
                //     configTable.put(directive[0], alias);
                // }
                if(!line.startsWith("#") && !line.isBlank()) {
                    directive = line.split(" ", 3);
                    if(directive[0].contains("Alias")) {
                        String aliasDir = directive[0].concat(" ").concat(directive[1]);
                        configTable.put(aliasDir.toString().trim(), directive[2].trim());  
                        System.out.println(configTable.get(aliasDir));
  
                    } else {
                        configTable.put(directive[0], directive[1]);
                        System.out.println(configTable.get(directive[0]));
                    }

                    sb.append(line + "\r\n");
                } 
            }
            // print(configTable)
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
            mimeTypesTable = new Hashtable<>();
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
                        // System.out.println(mimeTypesTable.size() + " : " + mimeType[0] + " " + mimeType[1]);
                        mimeTypesTable.put(mimeType[0], fileExtension);
                        // System.out.println(Arrays.toString(mimeTypesTable.get(mimeType[0])));
                        sb.append(line + "\r\n");
                    } 
                    // else {
                    //     fileExtension = new String[0];
                    //     mimeTypesTable.put(mimeType[0], fileExtension);
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

    public Hashtable<String, String> getConfigTable() {
        return configTable;
    }

    public Hashtable<String, String[]> getMimeTypesTable() {
        return mimeTypesTable;
    }

    private void checkDirectives() {
        for(String i : configTable.keySet()) {
            getDirective(i);
        }
        // switch(configTable) {
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
        return configTable.get("Listen");
    }
    public String getDirective(String directive) {
        return configTable.get(directive);
    }
}