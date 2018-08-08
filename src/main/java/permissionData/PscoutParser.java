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

    private static HashMap<String, List<String>> permissionMappings = getOrCreatePermissionMappings();

    private static HashMap<String, List<String>> getOrCreatePermissionMappings() {
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
        Type type = new TypeToken<HashMap<String, List<String>>>(){}.getType();
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
    private static HashMap<String,List<String>> createPermissionMappings() {

        HashMap<String, List<String>> mappings = new HashMap<>();

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
        String permission = null;
        String signature;
        List<String> permissions;

        try {
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Permission:")) {
                    // upon reaching a new permission
                    permission = line.substring(line.indexOf(':') + 1);
                    // discard the next line
                    in.readLine();
                } else {
                    // the line must be an api signature
                    signature = line.substring(0, line.indexOf('>') + 1);
                    if (mappings.containsKey(signature)) {
                        mappings.get(signature).add(permission);
                    } else {
                        permissions = new ArrayList<>();
                        permissions.add(permission);
                        mappings.put(signature, permissions);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // store this hashmap to json file
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
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
    public static List<String> getPermissionMapping(String signature) {
        return permissionMappings.get(signature);
    }

    public static void main (String[] args) {
        permissionMappings = getOrCreatePermissionMappings();
        for (String key : permissionMappings.keySet()) {
            System.out.println("api " + key + " requires permissions: ");
            System.out.println(permissionMappings.get(key));
        }
    }
}
