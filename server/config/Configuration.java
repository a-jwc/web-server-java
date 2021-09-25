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
        System.out.println("⏳ Reading httpd.conf...");
        this.httpdConfFile = httpdConfFile;
        this.mimeTypesFile = mimeTypesFile;
    }

    public void readHttpdConfig() {
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
                        String path = directive[2].replaceAll("^\"+|\"+$", "");
                        configTable.put(aliasDir.toString().trim(), path.trim());  
                        // System.out.println(configTable.get(aliasDir));
  
                    } else {
                        String path = directive[1].replaceAll("^\"+|\"+$", "");
                        configTable.put(directive[0], path);
                        // System.out.println(configTable.get(directive[0]));
                    }

                    sb.append(line + "\r\n");
                } 
            }
            br.close();
            dis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("⌛ Successfully read in " + httpdConfFile + "\n");
        }
    }

    public void readMimeTypes() {
        try {
            System.out.println("⏳ Reading mime.types...");
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
                }
            }
            br.close();
            dis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("⌛ Successfully read in " + mimeTypesFile + "\n");
        }
    }

    public Hashtable<String, String> getConfigTable() {
        return configTable;
    }

    public Hashtable<String, String[]> getMimeTypesTable() {
        return mimeTypesTable;
    }

    public String getDirective(String directive) {
        return configTable.get(directive);
    }
}