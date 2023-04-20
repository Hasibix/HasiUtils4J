package io.hasibix.hasiutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvVars {
    private static final Logger logger = Logger.of(EnvVars.class);
    private static Map<String, String> variables = new LinkedHashMap<String, String>();

    public static void Load(String envFile, Map<String, String> systemEnv) {
        systemEnv.forEach((key, value) -> {
            variables.putIfAbsent(key, value);
        });
        
        if(!ObjUtils.String.IsEmpty(envFile)) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(envFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        variables.putIfAbsent(key, value);
                    }
                }
                br.close();
            } catch (IOException e) {
                logger.error("An exception occurred while trying to load environment variables!");
                logger.trace(e);
            }
        }
    }

    public static String Get(String key) {
        return variables.get(key);
    }
}
