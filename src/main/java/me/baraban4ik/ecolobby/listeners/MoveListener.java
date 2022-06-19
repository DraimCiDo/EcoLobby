package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    private final Configurations config;

    public MoveListener(Configurations configurations) {
        config = configurations;
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (config.get("config.yml").getBoolean("settings.player.movements")) {
            if (!player.hasPermission("ecolobby.bypass.move")) {
                Location from = e.getFrom().clone();
                Location to = e.getTo();
                assert to != null;
                from.setYaw(to.getYaw());
                from.setPitch(to.getPitch());
                if (!from.equals(to)) e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void jumpVoid(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (config.get("config.yml").getBoolean("settings.player.jump-to-void.enabled")) {
            if (player.getLocation().getY() <= config.get("config.yml").getDouble("settings.player.jump-to-void.height") && config.get("spawn.yml").getString("spawn.x") != null && config.get("spawn.yml").getString("spawn.y") != null) {
                Spawn.tpSpawn(player);
                Chat.sendMessage(player, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("jump-to-void", "You fell into the void, I returned you to spawn."));
            }
        }
    }
}
