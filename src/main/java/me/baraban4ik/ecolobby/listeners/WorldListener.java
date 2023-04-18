package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldInitEvent;

import static me.baraban4ik.ecolobby.EcoLobby.getVersion;
import static me.baraban4ik.ecolobby.utils.Chat.sendMessageSection;
import static me.baraban4ik.ecolobby.utils.Utils.teleportSpawn;

public class WorldListener implements Listener {
    private final Configurations config;

    public WorldListener(Configurations configurations) {
        config = configurations;
    }

    // World rules
    @EventHandler
    public void worldRules(WorldInitEvent e) {
        for (World w : Bukkit.getWorlds()) {

            w.setTime(config.get("config.yml").getLong("World.time-set"));

            if (getVersion() < 1.13) {
                w.setGameRuleValue("doDaylightCycle", String.valueOf(config.get("config.yml").getBoolean("World.rules.day-light-cycle")));
                w.setGameRuleValue("doWeatherCycle", String.valueOf(config.get("config.yml").getBoolean("World.rules.weather-cycle")));
                w.setGameRuleValue("doMobSpawning", String.valueOf(config.get("config.yml").getBoolean("World.rules.mob-spawning")));
                w.setGameRuleValue("doFireTick", String.valueOf(config.get("config.yml").getBoolean("World.rules.fire-spread")));
            } else {
                w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, config.get("config.yml").getBoolean("World.rules.day-light-cycle"));
                w.setGameRule(GameRule.DO_WEATHER_CYCLE, config.get("config.yml").getBoolean("World.rules.weather-cycle"));
                w.setGameRule(GameRule.DO_MOB_SPAWNING, config.get("config.yml").getBoolean("World.rules.mob-spawning"));
                w.setGameRule(GameRule.DO_FIRE_TICK, config.get("config.yml").getBoolean("World.rules.fire-spread"));
            }
        }
    }

    // Jump to void
    @EventHandler
    public void jumpVoid(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (config.get("config.yml").getBoolean("Player.jump-to-void.enabled")) {
            if (player.getLocation().getY() <= config.get("config.yml").getDouble("Player.jump-to-void.height")) {

                if (config.get("spawn.yml").getString("spawn.x") == null) {
                    return;
                }

                teleportSpawn(player, "spawn");
                sendMessageSection(player, "jump-to-void");

            }
        }
    }

    // Teleport to spawn
    @EventHandler
    public void onSpawn(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (player.hasPlayedBefore()) {
            if (config.get("spawn.yml").getString("spawn.x") == null) {
                return;
            }
            teleportSpawn(player, "spawn");
        }
        else {
            if (config.get("spawn.yml").getString("firstSpawn.x") == null) {
                if (config.get("spawn.yml").getString("spawn.x") != null) {

                    teleportSpawn(player, "spawn");
                    return;
                }

            }
            teleportSpawn(player, "firstSpawn");
        }
    }
}
