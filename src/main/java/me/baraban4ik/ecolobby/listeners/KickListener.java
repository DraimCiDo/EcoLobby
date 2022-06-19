package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.util.Objects;

public class KickListener implements Listener {
    private final Configurations config;

    public KickListener(Configurations configurations) {
        config = configurations;
    }

    @EventHandler
    public void whitelist(PlayerPreLoginEvent e) {
        if (config.get("config.yml").getBoolean("settings.join.whitelist.enabled")) {
            if (!config.get("config.yml").getStringList("settings.join.whitelist.players").contains(e.getName())) {
                e.setResult(PlayerPreLoginEvent.Result.KICK_WHITELIST);
                e.setKickMessage(Objects.requireNonNull(config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("whitelist-kick")));
            }
        }
    }
    @EventHandler
    public void blacklist(PlayerPreLoginEvent e) {
        if (config.get("config.yml").getBoolean("settings.join.blacklist.enabled")) {
            if (config.get("config.yml").getStringList("settings.join.blacklist.players").contains(e.getName())) {
                e.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(Objects.requireNonNull(config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("blacklist-kick")));
            }
        }
    }
}
