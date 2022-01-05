package me.tomexas.skyblock.Commands;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ResetIslandCommand implements CommandExecutor {

    private final Map<Player, Boolean> confirmation = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (label.equals("salosrestart")) {
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
            Island island = superiorPlayer.getIsland();
            if (island != null) {
                if (args.length < 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lIsland | &7Ar tikrai norite restartuoti savo salą? &cPrarasite visą jos progresą!"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lIsland | &7Naudokite &a/salosrestart confirm&7, jog patvirtintumėt salos restartą!"));
                    confirmation.put(player, true);
                } else {
                    if (args[0].equals("confirm")) {
                        if (confirmation.get(player) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lIsland | &cNeturite ką patvirtinti!"));
                            return true;
                        }
                        if (confirmation.get(player)) {
                            SuperiorSkyblockAPI.deleteIsland(island);
                            SuperiorSkyblockAPI.createIsland(superiorPlayer, "normal", BigDecimal.ONE, Biome.PLAINS, player.getName());
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "qc reset " + player.getName() + " -all");
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "qc stop " + player.getName() + " -all");
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set questnpc.completed false");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lIsland | &7Jūsų sala buvo restartuota sėkmingai!"));
                            confirmation.remove(player);
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lIsland | &cNeturite ką patvirtinti!"));
                        }
                    }
                }
            }
        }
        return false;
    }
}
