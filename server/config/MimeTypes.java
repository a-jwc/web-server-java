package server.config;

import java.util.*;

public class MimeTypes {
    private static Hashtable<String, String[]> mimeTypesTable;

    public MimeTypes(Hashtable<String, String[]> mimeTypes) {
        mimeTypesTable = mimeTypes;
    }

    public Hashtable<String, String[]> getTable() {
        return mimeTypesTable;
    }

}
