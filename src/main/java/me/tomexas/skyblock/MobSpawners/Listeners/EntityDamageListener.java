package me.tomexas.skyblock.MobSpawners.Listeners;

import me.tomexas.skyblock.Skyblock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDamageListener implements Listener {

    private Skyblock plugin;

    public EntityDamageListener(Skyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            String name = entity.getCustomName();
            if (name != null && !entity.getType().equals(EntityType.ARMOR_STAND)) {
                int damage = (int) event.getDamage();
                String[] nameArray = entity.getCustomName().split(" ");
                String[] strHP = nameArray[2].split("/");

                LivingEntity livingEntity = (LivingEntity) entity;

                int HP = (int) livingEntity.getHealth();
                HP -= damage;

                if (HP < 0) {
                    entity.setCustomName(name.replace(strHP[0] + "/", "0/"));
                } else {
                    entity.setCustomName(name.replace(strHP[0] + "/", HP + "/"));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getName().contains("[§eLv")) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }
}
