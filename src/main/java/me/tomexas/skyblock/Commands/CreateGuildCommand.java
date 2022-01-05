package me.tomexas.skyblock.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.api.events.island.IslandCreatedEvent;

public class CreateGuildCommand implements Listener {

    @EventHandler
    public void onCommand(IslandCreatedEvent event) {
        Player player = Bukkit.getPlayer(event.getOwner());
        event.getIsland().setName(player.getName() + " guild");
    }
}
