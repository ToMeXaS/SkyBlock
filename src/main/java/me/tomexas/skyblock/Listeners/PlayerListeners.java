package me.tomexas.skyblock.Listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PlayerListeners implements Listener {

    private final Map<SuperiorPlayer, Boolean> firstLogin = new HashMap<>();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        SuperiorPlayer sPlayer = SuperiorSkyblockAPI.getPlayer(player);

        sPlayer.teleport(sPlayer.getIsland());
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        if (!player.hasPlayedBefore() || !superiorPlayer.hasIsland()) {
            SuperiorSkyblockAPI.createIsland(superiorPlayer, "normal", BigDecimal.ONE, Biome.PLAINS, player.getName());
            firstLogin.put(superiorPlayer, true);
        }
    }

    @EventHandler
    public void onIslandCreate(IslandCreateEvent event) {
        Bukkit.getScheduler().runTaskLater(Skyblock.getPlugin(Skyblock.class), () -> {
            SuperiorPlayer sPlayer = event.getPlayer();
            if (firstLogin.get(sPlayer) == null) return;
            if (sPlayer.getIsland() != null && firstLogin.get(sPlayer)) {
                sPlayer.teleport(sPlayer.getIsland());
                firstLogin.remove(sPlayer);
            }
        }, 20);
    }
}
