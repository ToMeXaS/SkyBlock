package me.tomexas.skyblock.MobSpawners.Managers;

import me.tomexas.skyblock.Skyblock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class spawnerFileManager {

    private FileConfiguration config = null;
    private File configFile = null;
    private Skyblock plugin;

    public spawnerFileManager(Skyblock plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "spawnerLoc.yml");

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("spawnerLoc.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if(this.config == null)
            reloadConfig();

        return this.config;
    }

    public void saveConfig() {
        if (this.config == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "spawnerLoc.yml");

        if (!this.configFile.exists()) {
            this.plugin.saveResource("spawnerLoc.yml", false);
        }
    }
}
