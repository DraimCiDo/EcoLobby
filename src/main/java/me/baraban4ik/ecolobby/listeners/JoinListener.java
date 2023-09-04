package me.baraban4ik.ecolobby.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.managers.ItemManager;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.baraban4ik.ecolobby.EcoLobby.config;
import static me.baraban4ik.ecolobby.EcoLobby.messages;

public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        // WhiteList
        if (config.getBoolean("join_settings.whitelist.enabled")) {
            if (!config.getStringList("join_settings.whitelist.players").contains(event.getName())) {

                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                event.setKickMessage(Chat.format(messages.getString("whitelist_kick_message"), Bukkit.getPlayer(event.getName())));
            }
        }
        // BlackList
        if (config.getBoolean("join_settings.blacklist.enabled")) {
            if (config.getStringList("join_settings.blacklist.players").contains(event.getName())) {

                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Chat.format(messages.getString("blacklist_kick_message"), Bukkit.getPlayer(event.getName())));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Hide player in TabList
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(EcoLobby.instance, PacketType.Play.Server.PLAYER_INFO) {
            public void onPacketSending(PacketEvent event) {
                event.setCancelled(config.getBoolean("tab_settings.hide_player"));
            }
        });

        // Clear chat
        if (config.getBoolean("join_settings.clear_chat")) {
            for (int i = 0; i < 120; ++i) player.sendMessage("");
        }
        // Clear inventory
        if (config.getBoolean("join_settings.clear_inventory")) {
            player.getInventory().clear();
        }

        // Give join items
        ConfigurationSection items = config.getConfigurationSection("join_settings.custom_join_items.items");

		if (items != null) {
			for (String section : items.getKeys(false)) {
				ItemStack item = ItemManager.get(player, section);

				if (item != null) {
					player.getInventory().setItem(items.getInt(section + ".slot"), item);
				}
			}
		}

        // Join spawn
        if (config.getBoolean("join_settings.spawn_join")) {
            Location spawn = Spawn.get();
            if (spawn != null) player.teleport(spawn);
        }

        // MOTD Message
        if (config.getBoolean("join_settings.motd_message")) {

            List<String> motd = messages.getStringList("motd");

            if (motd.isEmpty()) return;

            Chat.sendMessage(motd, player);
        }
        // Title MOTD message
        if (config.getBoolean("join_settings.title_motd.enabled")) {

            String text = config.getString("join_settings.title_motd.title_text");
            String subText = config.getString("join_settings.title_motd.subtitle_text");

            int duration = config.getInt("join_settings.title_motd.duration");

            Chat.sendTitle(player, text, subText, duration);
        }
        // Action bar MOTD message
        if (config.getBoolean("join_settings.action_bar_motd.enabled")) {
            Chat.sendActionBar(player, config.getString("join_settings.action_bar_motd.text"));
        }
    }
}
