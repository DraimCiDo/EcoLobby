package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.baraban4ik.ecolobby.EcoLobby.config;

public class HiderListener implements Listener {
    @EventHandler
    public void hideJoin(PlayerJoinEvent event) {
        if (config.getBoolean("Hiders.hide_stream"))
            event.setJoinMessage(null);
        if (config.getBoolean("Hiders.hide_players")) {
            for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
                otherPlayer.hidePlayer(EcoLobby.instance, event.getPlayer());
                event.getPlayer().hidePlayer(EcoLobby.instance, otherPlayer);
            }
        }
        else {
            for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
                otherPlayer.showPlayer(EcoLobby.instance, event.getPlayer());
                event.getPlayer().showPlayer(EcoLobby.instance, otherPlayer);
            }
        }
    }
    @EventHandler
    public void hideLeave(PlayerQuitEvent event) {
        if (config.getBoolean("Hiders.hide_stream"))
            event.setQuitMessage(null);
    }
    @EventHandler
    public void hideDeath(PlayerDeathEvent event) {
        if (config.getBoolean("Hiders.hide_stream"))
            event.setDeathMessage(null);
    }
    @EventHandler
    public void hideKick(PlayerKickEvent event) {
        if (config.getBoolean("Hiders.hide_stream"))
            event.setLeaveMessage("");
    }
}
