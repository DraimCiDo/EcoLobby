package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static me.baraban4ik.ecolobby.EcoLobby.config;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.chat") && !config.getBoolean("player_settings.abilities.chat")) {
            Chat.sendMessage("deny_chat", player);
            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onCommands(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.commands") && !config.getBoolean("player_settings.abilities.commands")) {
            String[] command = event.getMessage().replace("/", "").split(" ");

            if (!config.getStringList("player_settings.abilities.use_commands").contains(command[0])) {
                Chat.sendMessage("deny_commands", player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && !config.getBoolean("player_settings.abilities.damage"))
            event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onHunger(FoodLevelChangeEvent event) {
        if (!config.getBoolean("player_settings.abilities.hunger") && event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMovements(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.movements") && !config.getBoolean("player_settings.abilities.movements")) {

            Location from = event.getFrom().clone();
            Location to = event.getTo();

            from.setYaw(to.getYaw());
            from.setPitch(to.getPitch());

            if (!from.equals(to)) event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void fly(PlayerJoinEvent event) {
        event.getPlayer().setAllowFlight(config.getBoolean("player_settings.abilities.fly"));
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.blocks.break") && !config.getBoolean("player_settings.abilities.blocks.break")) {

            if (config.getBoolean("player_settings.abilities.blocks.use_particle_effect"))
                player.playEffect(event.getBlock().getLocation().add(0.5, 1, 0.5), Effect.SMOKE, BlockFace.UP);
            if (config.getBoolean("player_settings.abilities.blocks.use_deny_messages"))
                Chat.sendMessage("deny_break_blocks", player);

            event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.blocks.place") && !config.getBoolean("player_settings.abilities.blocks.place")) {

            if (config.getBoolean("player_settings.abilities.blocks.use_particle_effect"))
                player.playEffect(event.getBlockPlaced().getLocation().add(0.5, 1, 0.5), Effect.SMOKE, BlockFace.UP);
            if (config.getBoolean("player_settings.abilities.blocks.use_deny_messages"))
                Chat.sendMessage("deny_place_blocks", player);

            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void statistics(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.valueOf((config.getString("player_settings.stats.gamemode")).toUpperCase()));

        player.setLevel(config.getInt("player_settings.stats.level_exp"));
        player.setHealth(config.getDouble("player_settings.stats.health"));

        List<String> effects = config.getStringList("player_settings.stats.effects");
        effects.forEach((x) -> {

            PotionEffectType types = PotionEffectType.getByName(x.split(":")[0].toUpperCase());
            int level = Integer.parseInt(x.split(":")[1]) - 1;
            assert types != null;

            player.addPotionEffect(new PotionEffect(types, 9999999, level));
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void hideJoinMessage(PlayerJoinEvent event) {
        if (config.getBoolean("player_settings.hide_stream")) {
            event.setJoinMessage(null);
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void hideQuitMessage(PlayerQuitEvent event) {
        if (config.getBoolean("player_settings.hide_stream")) {
            event.setQuitMessage(null);
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void hideDeathMessage(PlayerDeathEvent event) {
        if (config.getBoolean("player_settings.hide_stream")) {
            event.setDeathMessage(null);
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void hideKickMessage(PlayerKickEvent event) {
        if (config.getBoolean("player_settings.hide_stream")) {
            event.setLeaveMessage("");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void hidePlayers(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.getBoolean("player_settings.hide_players")) {
            for (Player other : Bukkit.getOnlinePlayers())
                other.hidePlayer(EcoLobby.instance, player);
        } else {
            for (Player other : Bukkit.getOnlinePlayers())
                other.showPlayer(EcoLobby.instance, player);
        }
    }
}
