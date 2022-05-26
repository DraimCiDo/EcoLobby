package me.baraban4ik.ecolobby;

import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Events;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Arrays;
import java.util.List;

public class EcoListener implements Listener {
    private final EcoLobby plugin;
    private final FileConfiguration c;
    private final FileConfiguration m;
    private final FileConfiguration s;

    public EcoListener(FileConfiguration cfg,FileConfiguration mess, FileConfiguration spawn, EcoLobby plugin) {
        this.plugin = plugin;
        c = cfg;
        m = mess;
        s = spawn;
    }

    private static final List<String> MOTD = Arrays.asList
            (
                    "",
                    "          &8[&a&lLobby&8]",
                    "",
                    "   &fwelcome to the server &a&lserver",
                    "   &fThere are &amany &fadventures waiting for you here.",
                    "",
                    "   &fHope you &alike &fit here!",
                    ""
            );

    @EventHandler
    public void join(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        if (s.getString("spawn.x") != null && s.getString("spawn.y") != null) {
            Events.tpSpawn(player);
        }
        if (c.getBoolean("settings.enable-motd")) {
            List<String> motd = m.getStringList("motd");
            if (motd.isEmpty()) {
                MOTD.forEach((x) -> Chat.sendMessage(player, x));
                return;
            }
            motd.forEach((x) -> Chat.sendMessage(player, x));
        }
        if (c.getBoolean("settings.enable-hidestream")) {
            e.setJoinMessage(null);
        }
        if (c.getBoolean("settings.title-motd.enabled")) {
            String text = ChatColor.translateAlternateColorCodes('&', c.getString("settings.title-motd.title-text"));
            String subText = ChatColor.translateAlternateColorCodes('&', c.getString("settings.title-motd.subtitle-text"));
            int duration = c.getInt("settings.title-motd.duration");
            Chat.sendTitle(player, text, subText, duration);
        }
        if (c.getBoolean("settings.action-bar-motd.enabled")) {
            String text = ChatColor.translateAlternateColorCodes('&', c.getString("settings.action-bar-motd.text"));
            Chat.sendActionBar(player, text);
        }
        if (c.getBoolean("settings.player-hide")) {
            player.hidePlayer(plugin, player);
        }
        else {
            player.showPlayer(plugin, player);
        }
        player.setGameMode(GameMode.valueOf(c.getString("settings.gamemode").toUpperCase()));
        player.setLevel(c.getInt("settings.level-exp"));
        player.setMaxHealth(c.getDouble("settings.player.health"));

        Events.addEffects(player, c.getStringList("settings.effects"));
        if (c.getBoolean("settings.music.enabled")) {
            Events.music(player, c.getString("settings.music.disk"));
        }
        if (c.getBoolean("settings.join-clear.chat")) {
            for(int i = 0; i < 120; ++i) {
                player.sendMessage("");
            }
        }
        if (c.getBoolean("settings.join-clear.inventory")) {
            player.getInventory().clear();
        }
        if (c.getBoolean("settings.player-fly")) {
            player.setFlying(true);
        }
    }

    @EventHandler
    public void jumpVoid(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (c.getBoolean("settings.player.jump-to-void")) {
            if (player.getLocation().getY() < 0.0D && s.getString("spawn.x") != null && s.getString("spawn.y") != null) {
                Events.tpSpawn(player);
                Chat.sendMessage(player, m.getString("jump-to-void", "You fell into the void, I returned you to spawn."));
            }
        }
    }


    @EventHandler
    public void messQuit(PlayerQuitEvent e) {
        if (c.getBoolean("settings.enable-hidestream")) {
            e.setQuitMessage(null);
        }
    }
    @EventHandler
    public void messDeath(PlayerDeathEvent e) {
        if (c.getBoolean("settings.enable-hidestream")) {
            e.setDeathMessage(null);
        }
    }
    @EventHandler
    public void messKick(PlayerKickEvent e) {
        if (c.getBoolean("settings.enable-hidestream")) {
            e.setLeaveMessage("");
        }
    }


    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (c.getBoolean("settings.player.movements")) {
            if (!player.hasPermission("ecolobby.bypass.move")) {
                Location from = e.getFrom().clone();
                Location to = e.getTo();
                from.setYaw(to.getYaw());
                from.setPitch(to.getPitch());
                if (!from.equals(to)) e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("ecolobby.bypass.break")) {
            if (c.getBoolean("settings.player.breaking-blocks")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("ecolobby.bypass.place")) {
            if (c.getBoolean("settings.player.place-blocks")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void damagePlayer(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && c.getBoolean("settings.player.damage")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void hungerPlayer(FoodLevelChangeEvent e) {
        if (c.getBoolean("settings.player.hunger")) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        if (!player.hasPermission("ecolobby.bypass.chat") && !c.getBoolean("settings.enable-chat")) {
            Chat.sendMessage(player, m.getString("disable-chat", "I'm sorry but you can't chat!"));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void command(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        if (!c.getBoolean("settings.enable-commands") && !player.hasPermission("ecolobby.bypass.commands")) {
            String[] message = e.getMessage().replace("/", "").split(" ");
            if (!c.getStringList("settings.use-commands").contains(message[0])) {
                Chat.sendMessage(player, m.getString("disable-commands", "You can't use this command!"));
                e.setCancelled(true);
            }
        }
    }
}


