package me.tomexas.skyblock.MobSpawners.Managers;

import me.tomexas.skyblock.Skyblock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class npcFileManager {

    private FileConfiguration config = null;
    private File configFile = null;
    private Skyblock plugin;

    public npcFileManager(Skyblock plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File("/plugins/Citizens", "saves.yml");

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
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
            this.configFile = new File("/plugins/Citizens", "saves.yml");

        if (!this.configFile.exists()) {
            this.plugin.saveResource("saves.yml", false);
        }
    }
}
