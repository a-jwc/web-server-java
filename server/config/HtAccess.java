package server.config;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class HtAccess {
    private static ConcurrentHashMap<String, String> htAccessMap;

    public HtAccess(String htAccessPath) {
        // * Parse and put htAccess items in the hash map
        try {
            FileInputStream fis = new FileInputStream(htAccessPath);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            StringBuilder sb = new StringBuilder();
            String line;
            String htArr[];
            while((line = br.readLine()) != null) {
                if(!line.startsWith("#") && !line.isBlank()) {
                    htArr = line.split("\\s+", 2);                    
                    if(htArr.length == 2) {                        
                        // System.out.println(mimeTypesMap.size() + " : " + mimeType[0] + " " + mimeType[1]);
                        htAccessMap.put(htArr[0], htArr[1]);
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
            System.out.println("⌛ Successfully read in " + htAccessPath + "\n");
        }

    }

    // * Get absolute path of .htpassword
    public String getAuthUserFile() {
        return htAccessMap.get("AuthUserFile");
    }

    // * Only need to support "Basic"
    public String getAuthType() {
        return htAccessMap.get("AuthType");
    }

    // * Get the name provided in the authentication window provided by clients
    public String getAuthName() {
        return htAccessMap.get("AuthName");
    }

    // * Get the user or group that can access a resource ("valid-user")
    public String getReq() {
        return htAccessMap.get("Require");
    }
}
