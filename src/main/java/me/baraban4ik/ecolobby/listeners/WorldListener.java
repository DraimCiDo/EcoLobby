package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.baraban4ik.ecolobby.EcoLobby.config;

public class WorldListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJumpVoid(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (config.getBoolean("world_settings.jump_to_void.enabled")) {
            if (player.getLocation().getY() <= config.getDouble("world_settings.jump_to_void.height")) {

                Location spawn = Spawn.get();
                if (spawn != null) {
                    player.teleport(spawn);
                    Chat.sendMessage("jump_to_void", player);
                }
            }
        }
    }
}
