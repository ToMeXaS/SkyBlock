package me.tomexas.skyblock.Utils;

import me.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlSetterGetter {

    private Skyblock plugin;

    public MysqlSetterGetter (Skyblock plugin) {
        this.plugin = plugin;
    }

    // MobSpawners
    public void addSpawner(Location loc, String mobType, String amount, String radiusCheck, String maxLvl, String minLvl) {
        try {
                PreparedStatement insert = this.plugin.getConnection()
                        .prepareStatement("INSERT INTO " + this.plugin.spawnerTable + " (location,entityType,maxLvl,minLvl,hpScaling,amount,radiusCheck," +
                                "helmSlot,chestSlot,legsSlot,bootsSlot,mainHandSlot,offHandSlot) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
                insert.setString(1, loc.toString());
                insert.setString(2, mobType);
                insert.setInt(3, Integer.parseInt(maxLvl));
                insert.setInt(4, Integer.parseInt(minLvl));
                insert.setInt(5, 5);
                insert.setInt(6, Integer.parseInt(amount));
                insert.setInt(7, Integer.parseInt(radiusCheck));
                insert.setString(8, "");
                insert.setString(9, "");
                insert.setString(10, "");
                insert.setString(11, "");
                insert.setString(12, "");
                insert.setString(13, "");
                insert.executeUpdate();

                this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Spawner Inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // -- //
}
