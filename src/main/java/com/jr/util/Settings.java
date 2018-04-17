package com.jr.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class Settings {
    private static final String MAX_ID_NAME = "maxId";

    private static synchronized Map<String, String> getSettingsFromFile() {
        List<Map<String, String>> allSettingsList = FileOps.getAll(FileOps.getSettingsName());
        Map<String, String> result = new HashMap<>();

        for (Map<String, String> settingsLine : allSettingsList) {
            for (Map.Entry entry : settingsLine.entrySet()) {
                result.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return result;
    }

    private static synchronized void save(String key, String value) {
        Map<String, String> settings = getSettingsFromFile();
        settings.put(key, value);

        List<Map<String, String>> settingsListToSave = new ArrayList<>();
        for (Map.Entry entry : settings.entrySet()) {
            Map<String, String> settingsLine = new HashMap<>();
            settingsLine.put((String) entry.getKey(), (String) entry.getValue());

            settingsListToSave.add(settingsLine);
        }
        FileOps.put(FileOps.getSettingsName(), settingsListToSave, false);
    }

    private static synchronized String get(String key) {
        return getSettingsFromFile().get(key);
    }


    public static synchronized long getNextId() {
        Long maxId = Long.parseLong(get(MAX_ID_NAME));
        maxId++;
        save(MAX_ID_NAME, maxId.toString());
        return maxId;
    }

}