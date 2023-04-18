package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.item.ItemManager;
import me.baraban4ik.ecolobby.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class ItemsListener implements Listener {
    private final Configurations config;
    private final EcoLobby plugin;

    public ItemsListener(Configurations configurations, EcoLobby plugin) {
        config = configurations;
        this.plugin = plugin;
    }

    // Items:
    @EventHandler
    public void onMoveItems(InventoryClickEvent e) {
        if (!config.get("config.yml").getBoolean("Items.move-items")) {

            if (!e.getWhoClicked().hasPermission("ecolobby.bypass.moveitems") && !e.isCancelled()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDropItems(PlayerDropItemEvent e) {
        Player player = e.getPlayer();

        if (!config.get("config.yml").getBoolean("Items.drop-items") && player.hasPermission("ecolobby.bypass.dropitems")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK ||
                e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getItem() != null) {
                if (Objects.equals(e.getItem().getItemMeta(), ItemManager.joinItem.getItemMeta())) {

                    Player player = e.getPlayer();

                    for (String str : config.get("config.yml").getStringList("Items.join-item.actions")) {
                        if (str.startsWith("{MSG}")) {
                            if (str.split("\\{MSG\\}")[1] != null) {

                                Chat.sendMessage(player, str.split("\\{MSG\\}")[1]);

                            }
                        }
                        else if (str.startsWith("{CONSOLE}")) {
                            if (str.split("\\{CONSOLE\\}")[1] != null) {

                                ConsoleCommandSender console = plugin.getServer().getConsoleSender();
                                Bukkit.dispatchCommand(console, Chat.format((str.split("\\{CONSOLE\\}")[1]), player));

                            }
                        }
                        else if (str.startsWith("{CMD}")) {
                            if (str.split("\\{CMD\\}")[1] != null) {

                                player.performCommand(Chat.format(str.split("\\{CMD\\}")[1], player));

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void joinItem(PlayerJoinEvent e) {
        ItemManager.init(config, e.getPlayer());
        if (ItemManager.joinItem != null) {
            e.getPlayer().getInventory().addItem(ItemManager.joinItem);
        }
    }
}
