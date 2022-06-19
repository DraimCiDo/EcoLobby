package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RestrictionsListener implements Listener {
    private final Configurations config;

    public RestrictionsListener(Configurations configurations) {
        config = configurations;
    }


    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("ecolobby.bypass.break")) {
            if (config.get("config.yml").getBoolean("settings.player.breaking-blocks")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("ecolobby.bypass.place")) {
            if (config.get("config.yml").getBoolean("settings.player.place-blocks")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void damagePlayer(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && config.get("config.yml").getBoolean("settings.player.damage")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void hungerPlayer(FoodLevelChangeEvent e) {
        if (config.get("config.yml").getBoolean("settings.player.hunger")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("ecolobby.bypass.chat") && !config.get("config.yml").getBoolean("settings.enable-chat")) {
            Chat.sendMessage(player, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("disable-chat", "sorry but you can't chat!"));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (!config.get("config.yml").getBoolean("settings.enable-commands") && !player.hasPermission("ecolobby.bypass.commands")) {
            String[] message = e.getMessage().replace("/", "").split(" ");
            if (!config.get("config.yml").getStringList("settings.use-commands").contains(message[0])) {
                Chat.sendMessage(player, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("disable-commands", "You can't use this command!"));
                e.setCancelled(true);
            }
        }
    }
}
