package permissionData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class PscoutParser {

    private static String jsonFileName = System.getProperty("user.dir") +
            "/pscout_results/permissionMappings.json";

    private static Map<String, List<Permission>> permissionMappings = getOrCreatePermissionMappings();

    private static Map<String, List<Permission>> getOrCreatePermissionMappings() {
        BufferedReader in;
        try {
            // read the .json file and load the hashmap
            in = new BufferedReader(new FileReader(jsonFileName));
        } catch (FileNotFoundException e) {
            // meaning the the pscout database has not been parsed yet
            // parse the pscout data and store them into the hashmap
            // close the file stream and create a mapping
            return createPermissionMappings();
        }
        // read the json string and reconstruct a hashmap from json using gson
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, List<Permission>>>(){}.getType();
        try {
            String jsonString = in.readLine();
            return gson.fromJson(jsonString, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * parse the pscout data and create a permission mapping hashmap;
     * store this hashmap into permissionMappings.json file for future use
     *
     * @return
     */
    private static Map<String,List<Permission>> createPermissionMappings() {

        Map<String, List<Permission>> mappings = new HashMap<>();

        String pscoutDataFileName = System.getProperty("user.dir") +
                "/pscout_results/results/API_22/allmappings";

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(pscoutDataFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line;
        Permission permission = null;
        String signature;
        List<Permission> permissions;

        try {
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Permission:")) {
                    // upon reaching a new permission
                    String permissionName = line.substring(line.indexOf(':') + 1);
                    permission = new Permission(permissionName);
                    // discard the next line
                    in.readLine();
                } else {
                    // the line must be an api signature
                    signature = line.substring(0, line.indexOf('>') + 1);
                    if (!mappings.containsKey(signature)) {
                        mappings.put(signature, new ArrayList<>());
                    }
                    mappings.get(signature).add(permission);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // store this hashmap to json file
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Permission>>>(){}.getType();
        String mappingString = gson.toJson(mappings, type);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFileName));
            writer.write(mappingString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappings;
    }

    /**
     * Given an API call signature, parses the PScout's database to find all permissions required
     * by this function.
     *
     * @return a list of strings representing the permissions needed for the given api call.
     */
    public static List<Permission> getPermissionMapping(String signature) {
        return permissionMappings.get(signature);
    }
}
