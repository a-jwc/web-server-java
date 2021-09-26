package server.config;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {
    final String httpdConfFile;
    final String mimeTypesFile;
    static ConcurrentHashMap<String, String> configMap;
    static ConcurrentHashMap<String[], String> mimeTypesMap;
 
    public Configuration(String httpdConfFile, String mimeTypesFile) {
        System.out.println("⏳ Reading httpd.conf...");
        this.httpdConfFile = httpdConfFile;
        this.mimeTypesFile = mimeTypesFile;
    }

    public void readHttpdConfig() {
        try {
            configMap = new ConcurrentHashMap<>();
            FileInputStream fis = new FileInputStream(httpdConfFile);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            StringBuilder sb = new StringBuilder();
            String line;
            String directive[];
            
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.isBlank()) {
                    directive = line.split(" ", 3);
                    if(directive[0].contains("Alias")) {
                        String aliasDir = directive[0].concat(" ").concat(directive[1]);
                        String path = directive[2].replaceAll("^\"+|\"+$", "");
                        configMap.put(aliasDir.toString().trim(), path.trim());  
                        // System.out.println(configMap.get(aliasDir));
  
                    } else {
                        String path = directive[1].replaceAll("^\"+|\"+$", "");
                        configMap.put(directive[0], path);
                        // System.out.println(configMap.get(directive[0]));
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
            System.out.println("✅ Successfully read in " + httpdConfFile + "\n");
        }
    }

    public void readMimeTypes() {
        try {
            System.out.println("⏳ Reading mime.types...");
            String[] fileExtension;
            mimeTypesMap = new ConcurrentHashMap<>();
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
                        mimeTypesMap.put(fileExtension, mimeType[0]);
                        // System.out.println(Arrays.toString(mimeTypesMap.get(mimeType[0])));
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
            System.out.println("✅ Successfully read in " + mimeTypesFile + "\n");
        }
    }

    public ConcurrentHashMap<String, String> getConfigMap() {
        return configMap;
    }

    public ConcurrentHashMap<String[], String> getMimeTypesMap() {
        return mimeTypesMap;
    }

    public String getDirective(String directive) {
        return configMap.get(directive);
    }
}