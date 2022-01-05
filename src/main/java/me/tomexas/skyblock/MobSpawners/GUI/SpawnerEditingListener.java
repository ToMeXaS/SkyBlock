package me.tomexas.skyblock.MobSpawners.GUI;

import me.tomexas.skyblock.Skyblock;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SpawnerEditingListener implements Listener {

    private final Skyblock plugin;
    private final ItemStack dslot;

    public SpawnerEditingListener (Skyblock plugin) {
        this.plugin = plugin;

        dslot = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta slotMeta = dslot.getItemMeta();
        slotMeta.setDisplayName("§eEquipment slot");
        List<String> slotLore = Arrays.asList(
                "§fClick here with any type of item,",
                "§fto make the entity wear/use it."
        );
        slotMeta.setLore(slotLore);
        dslot.setItemMeta(slotMeta);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() instanceof SpawnerEditingGUI) {
            event.setCancelled(true);

            ItemStack cursorItem = event.getCursor();
            ItemStack slotItem = event.getCurrentItem();
            ItemStack idItem = event.getClickedInventory().getItem(4);
            ItemMeta itemMeta = idItem.getItemMeta();
            String stringId = itemMeta.getLore().get(0);
            int id = Integer.parseInt(stringId.replace(" §e• §fID: §e", ""));
            if (slotItem == null || cursorItem == null) return;

            int slot = event.getSlot();
            switch (slot) {
                case 15: {
                    if (!cursorItem.getType().equals(Material.AIR)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.helmSlot", cursorItem);
                        this.plugin.spawnerFile.saveConfig();
                        event.getClickedInventory().setItem(slot, cursorItem);
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    } else if (!slotItem.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.helmSlot", "");
                        this.plugin.spawnerFile.saveConfig();

                        event.getClickedInventory().setItem(slot, dslot);
                        event.getWhoClicked().setItemOnCursor(slotItem);
                    }
                    break;
                }
                case 24: {
                    if (!cursorItem.getType().equals(Material.AIR)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.chestSlot", cursorItem);
                        this.plugin.spawnerFile.saveConfig();
                        event.getClickedInventory().setItem(slot, cursorItem);
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    } else if (!slotItem.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.chestSlot", "");
                        this.plugin.spawnerFile.saveConfig();

                        event.getClickedInventory().setItem(slot, dslot);
                        event.getWhoClicked().setItemOnCursor(slotItem);
                    }
                    break;
                }
                case 33: {
                    if (!cursorItem.getType().equals(Material.AIR)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.legsSlot", cursorItem);
                        this.plugin.spawnerFile.saveConfig();
                        event.getClickedInventory().setItem(slot, cursorItem);
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    } else if (!slotItem.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.legsSlot", "");
                        this.plugin.spawnerFile.saveConfig();

                        event.getClickedInventory().setItem(slot, dslot);
                        event.getWhoClicked().setItemOnCursor(slotItem);
                    }
                    break;
                }
                case 42: {
                    if (!cursorItem.getType().equals(Material.AIR)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.bootSlot", cursorItem);
                        this.plugin.spawnerFile.saveConfig();
                        event.getClickedInventory().setItem(slot, cursorItem);
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    } else if (!slotItem.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.bootSlot", "");
                        this.plugin.spawnerFile.saveConfig();

                        event.getClickedInventory().setItem(slot, dslot);
                        event.getWhoClicked().setItemOnCursor(slotItem);
                    }
                    break;
                }
                case 23: {
                    if (!cursorItem.getType().equals(Material.AIR)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.mainHandSlot", cursorItem);
                        this.plugin.spawnerFile.saveConfig();
                        event.getClickedInventory().setItem(slot, cursorItem);
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    } else if (!slotItem.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.mainHandSlot", "");
                        this.plugin.spawnerFile.saveConfig();

                        event.getClickedInventory().setItem(slot, dslot);
                        event.getWhoClicked().setItemOnCursor(slotItem);
                    }
                    break;
                }
                case 25: {
                    if (!cursorItem.getType().equals(Material.AIR)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.offHandSlot", cursorItem);
                        this.plugin.spawnerFile.saveConfig();
                        event.getClickedInventory().setItem(slot, cursorItem);
                        event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                    } else if (!slotItem.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                        this.plugin.spawnerFile.getConfig().set("locations." + id + ".equipment.offHandSlot", "");
                        this.plugin.spawnerFile.saveConfig();

                        event.getClickedInventory().setItem(slot, dslot);
                        event.getWhoClicked().setItemOnCursor(slotItem);
                    }
                    break;
                }
            }
        }
    }
}
