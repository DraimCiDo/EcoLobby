package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static me.baraban4ik.ecolobby.utils.Chat.sendMessageSection;

public class PlayerListener implements Listener {
    private final Configurations config;
    private final EcoLobby plugin;

    public PlayerListener(Configurations configurations, EcoLobby plugin) {
        config = configurations;
        this.plugin = plugin;

    }

    // Chat and Commands:
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.chat") && !config.get("config.yml").getBoolean("Player.enable-chat")) {

            sendMessageSection(player, "disable-chat");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommands(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if (!config.get("config.yml").getBoolean("Player.enable-commands") && !player.hasPermission("ecolobby.bypass.commands")) {
            String[] message = e.getMessage().replace("/", "").split(" ");

            if (!config.get("config.yml").getStringList("Player.use-commands").contains(message[0])) {

                sendMessageSection(player, "disable-commands");
                e.setCancelled(true);
            }
        }
    }

    // Fly:
    @EventHandler
    public void fly(PlayerJoinEvent e) {
        if (config.get("config.yml").getBoolean("Player.enable-fly")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setAllowFlight(true);
            }
        }
        else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setAllowFlight(false);
            }
        }
    }

    // Damage:
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !config.get("config.yml").getBoolean("Player.enable-damage")) {
            e.setCancelled(true);
        }
    }

    // Break, Interact and Place blocks:
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.break")) {
            if (!config.get("config.yml").getBoolean("Player.enable-break-blocks")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPermission("ecolobby.bypass.place")) {
            if (!config.get("config.yml").getBoolean("Player.enable-place-blocks")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInteractBlock(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!e.getPlayer().hasPermission("ecolobby.bypass.interact")) {

                if (!config.get("config.yml").getBoolean("Player.enable-interact-blocks")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    // Hunger:
    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (!config.get("config.yml").getBoolean("Player.enable-hunger")) {
            e.setCancelled(true);
        }
    }

    // Movements:
    @EventHandler
    public void onMovements(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (!config.get("config.yml").getBoolean("Player.enable-movements")) {
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


    // Statistics:
    @EventHandler
    public void statistics(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setGameMode(GameMode.valueOf((config.get("config.yml").getString("Player.gamemode")).toUpperCase()));

        player.setLevel(config.get("config.yml").getInt("Player.level-exp"));
        player.setHealth(config.get("config.yml").getDouble("Player.health"));

        int newSpeed = Math.min(Math.max(config.get("config.yml").getInt("Player.speed", 2), -10), 10);
        player.setWalkSpeed(newSpeed * 0.1f);

        List<String> effects = config.get("config.yml").getStringList("Player.effects");
        effects.forEach((x) -> {

            PotionEffectType types = PotionEffectType.getByName(x.split(":")[0].toUpperCase());
            int level = Integer.parseInt(x.split(":")[1]) - 1;
            assert types != null;

            player.addPotionEffect(new PotionEffect(types, 9999999, level));
        });
    }

    // Hidestream:
    @EventHandler
    public void hidestreamJoin(PlayerJoinEvent e) {
        if (config.get("config.yml").getBoolean("Player.hidestream")) {
            e.setJoinMessage(null);
        }
    }

    @EventHandler
    public void hidestreamQuit(PlayerQuitEvent e) {
        if (config.get("config.yml").getBoolean("Player.hidestream")) {
            e.setQuitMessage(null);
        }
    }

    @EventHandler
    public void hidestreamDeath(PlayerDeathEvent e) {
        if (config.get("config.yml").getBoolean("Player.hidestream")) {
            e.setDeathMessage(null);
        }
    }

    @EventHandler
    public void hidestreamKick(PlayerKickEvent e) {
        if (config.get("config.yml").getBoolean("Player.hidestream")) {
            e.setLeaveMessage("");
        }
    }

    // Hide players:
    @EventHandler
    public void hidePlayers(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (config.get("config.yml").getBoolean("Player.hide-players")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(plugin, p);
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.showPlayer(plugin, p);
            }
        }

    }
}
