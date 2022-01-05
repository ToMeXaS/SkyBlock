package me.tomexas.skyblock.MobSpawners.GUI;

import me.tomexas.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SpawnerEditingGUI implements InventoryHolder {

    private final Skyblock plugin;
    private final Inventory inv;
    private final Player player;

    public SpawnerEditingGUI (Skyblock plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        inv = Bukkit.createInventory(this, 54, "Editing spawner in this area...");
        init();
    }

    public void init() {
        ItemStack filler = createItem(Material.BLACK_STAINED_GLASS_PANE, "", Collections.singletonList(""));
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }

        setItems(inv, getID());

    }

    public int getID() {
        int size = this.plugin.spawnerFile.getConfig().getConfigurationSection("locations").getKeys(false).size();

        for (int i = 1; i <= size; i++) {
            Location loc = this.plugin.spawnerFile.getConfig().getLocation("locations." + i + ".location");
            int radius = this.plugin.spawnerFile.getConfig().getInt("locations." + i + ".radiusCheck");
            if (loc.distanceSquared(this.player.getLocation()) <= Math.pow(radius, 2)) {
                return i;
            }
        }
        return 1;
    }

    private void setItems(Inventory inv, int id) {
        Location loc = this.plugin.spawnerFile.getConfig().getLocation("locations." + id + ".location");
        String mobType = this.plugin.spawnerFile.getConfig().getString("locations." + id + ".mobType");
        String npcName = this.plugin.spawnerFile.getConfig().getString("locations." + id + ".npcName");
        String npcSkinName = this.plugin.spawnerFile.getConfig().getString("locations." + id + ".npcSkinName");
        int maxLvl = this.plugin.spawnerFile.getConfig().getInt("locations." + id + ".maxLvl");
        int minLvl = this.plugin.spawnerFile.getConfig().getInt("locations." + id + ".minLvl");
        int npcDamage = this.plugin.spawnerFile.getConfig().getInt("locations." + id + ".npcDamage");
        int hpScaling = this.plugin.spawnerFile.getConfig().getInt("locations." + id + ".hpScaling");


        String infoItemDisplayName = "§eSpawner information";
        List <String> infoItemLore = new LinkedList<>(Arrays.asList(
                " §e• §fID: §e" + id,
                " §e• §fLocation: §ex:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ() + " (world: " + loc.getWorld().getName() + ")",
                " §e• §fType: §e" + mobType,
                " §e• §fLevel: §e" + minLvl + "-" + maxLvl,
                " §e• §fHP scaling: §e+" + hpScaling + "§c❤"
        ));
        if (mobType.equals("NPC")) {
            infoItemLore.add(" §e• §fNPC name: §e" + npcName);
            infoItemLore.add(" §e• §fNPC skin name: §e" + npcSkinName);
            infoItemLore.add(" §e• §fNPC damage: §e" + npcDamage);
        }
        ItemStack infoItem = createItem(Material.NAME_TAG, infoItemDisplayName, infoItemLore);
        inv.setItem(4, infoItem);


        String defSlotDisplayName = "§eEquipment slot";
        List<String> defSlotLore = Arrays.asList(
                "§fClick here with any type of item,",
                "§fto make the entity wear/use it."
            );
        ItemStack defItem = createItem(Material.WHITE_STAINED_GLASS_PANE, defSlotDisplayName, defSlotLore);

        String deletionItemDN = "§cRemove this spawner";
        List<String> deletionItemL = Arrays.asList(
                "§fRemoves spawner in",
                "§fyour area!"
        );
        ItemStack deletionItem = createItem(Material.BARRIER, deletionItemDN, deletionItemL);
        inv.setItem(53, deletionItem);


        if (this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.helmSlot") != null) {
            ItemStack helmItem = this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.helmSlot");
            inv.setItem(15, helmItem);
        } else inv.setItem(15, defItem);

        if (this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.chestSlot") != null) {
            ItemStack chestItem = this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.chestSlot");
            inv.setItem(24, chestItem);
        } else inv.setItem(24, defItem);

        if (this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.legsSlot") != null) {
            ItemStack legsItem = this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.legsSlot");
            inv.setItem(33, legsItem);
        } else inv.setItem(33, defItem);

        if (this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.bootSlot") != null) {
            ItemStack bootItem = this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.bootSlot");
            inv.setItem(42, bootItem);
        } else inv.setItem(42, defItem);

        if (this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.mainHandSlot") != null) {
            ItemStack mainHandItem = this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.mainHandSlot");
            inv.setItem(23, mainHandItem);
        } else inv.setItem(23, defItem);

        if (this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.offHandSlot") != null) {
            ItemStack offHandItem = this.plugin.spawnerFile.getConfig().getItemStack("locations." + id + ".equipment.offHandSlot");
            inv.setItem(25, offHandItem);
        } else inv.setItem(25, defItem);
    }

    private ItemStack createItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
