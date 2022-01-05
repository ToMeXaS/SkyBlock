package me.tomexas.skyblock.MobSpawners.Cmds;

import me.tomexas.skyblock.MobSpawners.GUI.SpawnerEditingGUI;
import me.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.apache.commons.lang.enums.EnumUtils;

public class SpawnerCommands implements CommandExecutor {

    private final Skyblock plugin;
    private final SpawnerEditingGUI spawnerEditingGUI;

    public SpawnerCommands(Skyblock plugin, SpawnerEditingGUI spawnerEditingGUI) {
        this.plugin = plugin;
        this.spawnerEditingGUI = spawnerEditingGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.equals("addspawner")) {
            if(!player.hasPermission("skyblock.cmd.addspawner")) player.sendMessage(this.plugin.messages.get("noperm"));
            else {
                if (args.length < 4) player.sendMessage(this.plugin.messages.get("usage"));
                else {
                    Location blockLocation = player.getWorld().getBlockAt(player.getLocation()).getLocation();
                    String mobType = args[0].toUpperCase();
                    String amount = args[1];
                    String radiusCheck = args[2];
                    String maxLvl = args[3];
                    String minLvl = args[4];

                    if (!isNumeric(radiusCheck) || !isNumeric(maxLvl) || !isNumeric(minLvl) || !isNumeric(amount) || !isValidMobType(mobType)) {
                        player.sendMessage(this.plugin.messages.get("usage"));
                        return false;
                    }

                    ConfigurationSection section = this.plugin.spawnerFile.getConfig().getConfigurationSection("locations");
                    if (section != null) {
                        int size = section.getKeys(false).size();
                        if (isSpawnerInRadius(size, player)) { player.sendMessage(this.plugin.messages.get("spawnerExists")); return false; }

                        size += 1;
                        setDefaults(size, blockLocation, mobType, maxLvl, minLvl, amount, radiusCheck);
                    } else setDefaults(1, blockLocation, mobType, maxLvl, minLvl, amount, radiusCheck);

                    this.plugin.spawnerFile.saveConfig();
                    this.plugin.mysql.addSpawner(blockLocation, mobType, amount, radiusCheck, maxLvl, minLvl);
                    player.sendMessage(this.plugin.messages.get("created"));
                }
            }
        }

        if (label.equals("editspawner")) {
            if(!player.hasPermission("skyblock.cmd.editspawner")) player.sendMessage(this.plugin.messages.get("noperm"));
            else {
                ConfigurationSection section = this.plugin.spawnerFile.getConfig().getConfigurationSection("locations");
                if (section != null) {
                    int size = section.getKeys(false).size();
                    if (!isSpawnerInRadius(size, player)) { player.sendMessage(this.plugin.messages.get("noSpawnerInArea")); return false; }

                    SpawnerEditingGUI spawnerGUI = new SpawnerEditingGUI(this.plugin, player);
                    player.openInventory(spawnerGUI.getInventory());
                }
            }
        }
        return false;
    }

    private void setDefaults(int i, Location blockLocation, String mobType, String maxLvl, String minLvl, String amount, String radiusCheck) {
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".location", blockLocation);
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".mobType", mobType);
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".npcName", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".npcSkinName", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".npcDamage", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".maxLvl", Integer.parseInt(maxLvl));
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".minLvl", Integer.parseInt(minLvl));
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".hpScaling", 5);
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".amount", Integer.parseInt(amount));
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".radiusCheck", Integer.parseInt(radiusCheck));
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".equipment.helmSlot", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".equipment.chestSlot", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".equipment.legsSlot", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".equipment.bootSlot", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".equipment.mainHandSlot", "");
        this.plugin.spawnerFile.getConfig().set("locations." + i + ".equipment.offHandSlot", "");
    }

    private static boolean isValidMobType(String str) {
        try{
            EntityType.valueOf(str);
            return true;
        }catch(IllegalArgumentException e){
            return false;
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean isSpawnerInRadius(int size, Player player) {
        for (int i = 1; i <= size; i++) {
            Location loc = this.plugin.spawnerFile.getConfig().getLocation("locations." + i + ".location");
            int radius = this.plugin.spawnerFile.getConfig().getInt("locations." + i + ".radiusCheck");
            if (player.getWorld() != Bukkit.getWorld("dungeon") || loc.distanceSquared(player.getLocation()) <= Math.pow(radius, 2))
                return true;
        }
        return false;
    }
}
