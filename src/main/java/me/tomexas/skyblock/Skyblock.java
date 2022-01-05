package me.tomexas.skyblock;

import me.tomexas.skyblock.Commands.CreateGuildCommand;
import me.tomexas.skyblock.Commands.ResetIslandCommand;
import me.tomexas.skyblock.Listeners.PlayerListeners;
import me.tomexas.skyblock.MobSpawners.GUI.SpawnerEditingGUI;
import me.tomexas.skyblock.MobSpawners.Managers.spawnerFileManager;
import me.tomexas.skyblock.MobSpawners.Cmds.SpawnerCommands;
import me.tomexas.skyblock.Utils.MysqlSetterGetter;
import me.tomexas.skyblock.Utils.Placeholders;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public final class Skyblock extends JavaPlugin {

    private Connection connection;
    public String host, database, username, password, spawnerTable;
    public int port;

    public MysqlSetterGetter mysql;

    public spawnerFileManager spawnerFile;
    public SpawnerEditingGUI spawnerEditingGUI;

    public HashMap<String, String> messages = new HashMap<>();

    @Override
    public void onLoad() {
        setupMessages();
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(), this);
        pm.registerEvents(new CreateGuildCommand(), this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new Placeholders(this).register();

        this.spawnerFile = new spawnerFileManager(this);

        //MobSpawners
        //getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        //getServer().getPluginManager().registerEvents(new SpawnerEditingListener(this), this);
        //startSpawningTask();

        this.getCommand("addspawner").setExecutor(new SpawnerCommands(this, spawnerEditingGUI));
        this.getCommand("editspawner").setExecutor(new SpawnerCommands(this, spawnerEditingGUI));
        this.getCommand("salosrestart").setExecutor(new ResetIslandCommand());

        //mysqlSetup();
        this.mysql = new MysqlSetterGetter(this);
    }

    @Override
    public void onDisable() {
    }

    public void mysqlSetup() {
        host = "45.81.254.6";
        port = 3306;
        database = "s2_skyblock";
        username = "u2_1mErfKSBAu";
        password = "v^ou@i6RcALSjiNOP.Wn=uOU";
        spawnerTable = "spawners";

        try {
            synchronized (this) {
                if(getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Skyblock] Connected to MYSQL database successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupMessages() {
        messages.put("usage", "§cUsage: /addspawner <mobType> <amount> <radiusCheck> <maxLvl> <minLvl>");
        messages.put("noperm", "§cNo permission!");
        messages.put("created", "§aSpawner created successfully!");
        messages.put("spawnerExists", "§cA spawner in this area already exists!");
        messages.put("noSpawnerInArea", "§cThere's no spawner in this area!");

        messages.put("spawnedQuestNPC", "§aJūs atspawninote užduočių NPC!");
    }

    private void startSpawningTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld().getName().equals("dungeon")) {
                    int size = this.spawnerFile.getConfig().getConfigurationSection("locations").getKeys(false).size();

                    for (int i = 1; i <= size; i++) {
                        Location loc = this.spawnerFile.getConfig().getLocation("locations." + i + ".location");
                        int radius = this.spawnerFile.getConfig().getInt("locations." + i + ".radiusCheck");
                        int amount = this.spawnerFile.getConfig().getInt("locations." + i + ".amount");
                        String entityType = this.spawnerFile.getConfig().getString("locations." + i + ".mobType");

                        if (loc.distanceSquared(player.getLocation()) <= Math.pow(radius, 2)) {
                            Collection<Entity> nearbyEntities = loc.getWorld().getNearbyEntities(loc, radius, radius, radius);
                            int entityNumber = 0;
                            for (Entity entity : nearbyEntities)
                                if (entity instanceof LivingEntity && entity.getType() != EntityType.PLAYER)
                                    entityNumber++;

                            if (entityNumber < 1) {
                                for (int j = 0; j < amount; j++) {
                                    Location location = new Location(loc.getWorld(), loc.getX() + getRandom(radius, radius), loc.getY(), loc.getZ() + getRandom(radius, radius));
                                    spawnEntity(location, EntityType.valueOf(entityType), i);
                                }
                            }
                        }
                    }
                }
            }
        }, 20, 20);
    }

    private void spawnEntity(Location loc, EntityType entityType, int i) {
        LivingEntity entity =  (LivingEntity) loc.getWorld().spawnEntity(loc, entityType);
        int maxLvl = this.spawnerFile.getConfig().getInt("locations." + i + ".maxLvl");
        int minLvl = this.spawnerFile.getConfig().getInt("locations." + i + ".minLvl");
        int hpScaling = this.spawnerFile.getConfig().getInt("locations." + i + ".hpScaling");
        ItemStack helmSlot = this.spawnerFile.getConfig().getItemStack("locations." + i + ".equipment.helmSlot");
        ItemStack chestSlot = this.spawnerFile.getConfig().getItemStack("locations." + i + ".equipment.chestSlot");
        ItemStack legsSlot = this.spawnerFile.getConfig().getItemStack("locations." + i + ".equipment.legsSlot");
        ItemStack bootSlot = this.spawnerFile.getConfig().getItemStack("locations." + i + ".equipment.bootSlot");
        ItemStack mainHandSlot = this.spawnerFile.getConfig().getItemStack("locations." + i + ".equipment.mainHandSlot");
        ItemStack offHandSlot = this.spawnerFile.getConfig().getItemStack("locations." + i + ".equipment.offHandSlot");

        Random r = new Random();
        int lvl = r.nextInt(maxLvl - minLvl) + minLvl;
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(lvl * hpScaling);
        entity.setHealth(lvl * hpScaling);
        entity.setCustomNameVisible(true);

        if (helmSlot != null)
            entity.getEquipment().setHelmet(helmSlot);
        if (chestSlot != null)
            entity.getEquipment().setChestplate(chestSlot);
        if (legsSlot != null)
            entity.getEquipment().setLeggings(legsSlot);
        if (bootSlot != null)
            entity.getEquipment().setBoots(bootSlot);
        if (mainHandSlot != null)
            entity.getEquipment().setItemInMainHand(mainHandSlot);
        if (offHandSlot != null)
            entity.getEquipment().setItemInOffHand(offHandSlot);

        int maxHP = (int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        int currentHP = (int) entity.getHealth();
        entity.setCustomName("[§eLv" + lvl + "§f] " + convertName(entityType.toString()) + "§e " + currentHP + "/" + maxHP + "§c❤");
    }

    private int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper + lower) + 1) - lower;
    }

    private String convertName(String name) {
        switch (name) {
            case "ZOMBIE": return "ZOMBIS";
            case "CREEPER": return "KRYPERIS";
            case "SKELETON": return "SKELETAS";
            case "ENDERMAN": return "ENDERIS";
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /*private void spawnPortalParticles() {
        for (String key : this.portalFile.getConfig().getConfigurationSection("portals").getKeys(false)) {
            ConfigurationSection block = this.portalFile.getConfig().getConfigurationSection("portals." + key);
            Location loc = block.getLocation("location");
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                loc.getWorld().spawnParticle(Particle.PORTAL, loc, 50);
            }, 20, 20);
        }
    }*/
}
