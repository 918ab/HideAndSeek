package main.hideandseek.Static;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataManager {
    private final File configFile;
    private final FileConfiguration config;

    public DataManager(Plugin plugin, String fileName) {
        this.configFile = new File(plugin.getDataFolder(), fileName);
        this.config = YamlConfiguration.loadConfiguration(configFile);

    }
    public int getIndex(String key) {
        int index = 0;
        while (get(key + "." + index) != null) {
            index++;
        }
        return index;
    }
    public void set(String key, Object value) {

        config.set(key, value);
        saveConfig();
    }

    public Object get(String key) {
        return config.get(key);
    }

    public void remove(String key) {
        config.set(key, null);
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getNames(String key) {
        ConfigurationSection duelSection = config.getConfigurationSection(key);
        if (duelSection != null) {
            Set<String> keys = duelSection.getKeys(false);
            return new ArrayList<>(keys);
        }
        return new ArrayList<>();
    }
    public void createFileIfNotExists() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
