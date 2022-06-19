package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HideStreamListener implements Listener {
    private final Configurations config;

    public HideStreamListener(Configurations configurations) {
        config = configurations;
    }


    @EventHandler
    public void messJoin(PlayerJoinEvent e) {
        if (config.get("config.yml").getBoolean("settings.enable-hidestream")) {
            e.setJoinMessage(null);
        }
    }
    @EventHandler
    public void messQuit(PlayerQuitEvent e) {
        if (config.get("config.yml").getBoolean("settings.enable-hidestream")) {
            e.setQuitMessage(null);
        }
    }
    @EventHandler
    public void messDeath(PlayerDeathEvent e) {
        if (config.get("config.yml").getBoolean("settings.enable-hidestream")) {
            e.setDeathMessage(null);
        }
    }
    @EventHandler
    public void messKick(PlayerKickEvent e) {
        if (config.get("config.yml").getBoolean("settings.enable-hidestream")) {
            e.setLeaveMessage("");
        }
    }
}
