package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.baraban4ik.ecolobby.EcoLobby.config;

public class ItemsListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveItems(InventoryClickEvent event) {
        if (!event.getWhoClicked().hasPermission("ecolobby.bypass.items.move") && !config.getBoolean("items_settings.move"))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDropItems(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("ecolobby.bypass.items.drop") && !config.getBoolean("items_settings.drop"))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPickupItems(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {

            if (!event.getEntity().hasPermission("ecolobby.bypass.items.pickup") && !config.getBoolean("items_settings.pickup"))
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ConfigurationSection items = config.getConfigurationSection("join_settings.custom_join_items.items");
            ItemStack item = event.getItem();

            for (String section : items.getKeys(false)) {
                if (ItemManager.checkNBT(item, section)) {
                    ItemManager.run(event.getPlayer(), items.getStringList(section + ".actions"));
                }
            }
        }
    }
}
