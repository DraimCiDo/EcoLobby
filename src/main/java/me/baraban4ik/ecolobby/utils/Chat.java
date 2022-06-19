package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    private static Configurations config;

    public Chat(Configurations configurations) {
        config = configurations;
    }


    public static void sendMessage(CommandSender sender, String s) {
        sender.sendMessage(replace(s, sender));
    }
    public static void sendTitle(Player player, String s, String subs, int duration) {
        player.sendTitle(replace(s, player), replace(subs, player), duration, 20, 70);
    }
    public static void sendActionBar(Player player, String s) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(replace(s, player)));
    }

    public static String replace(String s, CommandSender sender) {
        s = s.replace("{prfx}", Objects.requireNonNull(config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("prefix", "&8|&a&lEco&f&lLobby&8| &f")));
        if (sender instanceof Player) {
            Player p = (Player) sender;
            s = s.replace("{player}", p.getName());

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                s = PlaceholderAPI.setPlaceholders(p, s);
            }
        }
        if (EcoLobby.getVersion() >= 1.16f) {
            Pattern pattern = Pattern.compile(Objects.requireNonNull(config.get("config.yml").getString("settings.hex-pattern", "#[a-fA-F0-9]{6}")));
            Matcher match = pattern.matcher(s);

            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                s = s.replace(color, ChatColor.of(color) + "");
                match = pattern.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
