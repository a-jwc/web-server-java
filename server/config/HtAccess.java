package server.config;

import java.util.concurrent.ConcurrentHashMap;

public class HtAccess {
    private static ConcurrentHashMap<String, String> htAccessMap;

    public HtAccess(ConcurrentHashMap<String, String> htAccess) {
        htAccessMap = htAccess;
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
