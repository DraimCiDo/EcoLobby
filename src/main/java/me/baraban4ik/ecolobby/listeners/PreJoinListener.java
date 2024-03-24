package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.utils.Format;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static me.baraban4ik.ecolobby.EcoLobby.config;
import static me.baraban4ik.ecolobby.EcoLobby.messages;

public class PreJoinListener implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (config.getBoolean("Whitelist.enabled")) {
            if (config.getStringList("Whitelist.players").contains(event.getName())) return;

            String kickPathMessage = messages.getString("whitelist_kick_message");
            String kickMessage = Format.format(kickPathMessage, Bukkit.getPlayer(event.getName()));

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
            event.setKickMessage(kickMessage);
        }
        if (config.getBoolean("Blacklist.enabled")) {
            if (!config.getStringList("Blacklist.players").contains(event.getName())) return;

            String kickPathMessage = messages.getString("blacklist_kick_message");
            String kickMessage = Format.format(kickPathMessage, Bukkit.getPlayer(event.getName()));

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(kickMessage);
        }
    }
}
