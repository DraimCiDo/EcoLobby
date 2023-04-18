package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static me.baraban4ik.ecolobby.utils.Chat.*;
import static me.baraban4ik.ecolobby.utils.Chat.sendActionBar;

public class JoinListener implements Listener {
    private final Configurations config;
    private final EcoLobby plugin;

    public JoinListener(Configurations configurations, EcoLobby plugin) {
        config = configurations;
        this.plugin = plugin;
    }

    // Whitelist and Blacklist
    @EventHandler
    public void whitelist(AsyncPlayerPreLoginEvent e) {
        if (config.get("config.yml").getBoolean("Join.whitelist.enabled")) {
            if (!config.get("config.yml").getStringList("Join.whitelist.players").contains(e.getName())) {

                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                e.setKickMessage(getLang().getString("whitelist-kick"));

            }
        }
    }
    @EventHandler
    public void blacklist(AsyncPlayerPreLoginEvent e) {
        if (config.get("config.yml").getBoolean("Join.blacklist.enabled")) {
            if (config.get("config.yml").getStringList("Join.blacklist.players").contains(e.getName())) {

                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(getLang().getString("blacklist-kick"));

            }
        }
    }

    // Glow:
    @EventHandler
    public void setGlow(PlayerJoinEvent e) {
        if (config.get("config.yml").getBoolean("Join.glow")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, true, false));
            }
        }
        else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.removePotionEffect(PotionEffectType.GLOWING);
            }
        }
    }

    // Clear:
    @EventHandler
    public void clearChat(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (config.get("config.yml").getBoolean("Join.clear.chat")) {
            for (int i = 0; i < 120; ++i) {
                player.sendMessage("");
            }
        }
        
    }

    @EventHandler
    public void clearInventory(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (config.get("config.yml").getBoolean("Join.clear.inventory")) {

            player.getInventory().clear();

        }
    }

    // Motd:
    @EventHandler
    public void motd(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (config.get("config.yml").getBoolean("Join.motd-message")) {

            List<String> motd = getLang().getStringList("motd");

            if (motd.isEmpty()) {
                return;
            }

            motd.forEach((x) -> sendMessage(player, x));
        }
        if (config.get("config.yml").getBoolean("Join.title-motd.enabled")) {

            String text = config.get("config.yml").getString("Join.title-motd.title-text");
            String subText = config.get("config.yml").getString("Join.title-motd.subtitle-text");

            int duration = config.get("config.yml").getInt("Join.title-motd.duration");

            sendTitle(player, text, subText, duration);
        }
        if (config.get("config.yml").getBoolean("Join.action-bar-motd.enabled")) {
            sendActionBar(player, config.get("config.yml").getString("Join.action-bar-motd.text"));
        }
    }
}
