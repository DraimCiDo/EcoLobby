package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.EcoLobby;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Chat {

    private static FileConfiguration m;
    private static FileConfiguration c;

    public Chat(FileConfiguration mess, FileConfiguration cfg) {
        m = mess;
        c = cfg;
    }

    public static String color(String s) {
        if (EcoLobby.getVersion() >= 1.16f) {
            Pattern pattern = Pattern.compile(Objects.requireNonNull(c.getString("settings.hex-pattern", "#[a-fA-F0-9]{6}")));
            Matcher match = pattern.matcher(s);

            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                s = s.replace(color, ChatColor.of(color) + "");
                match = pattern.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(CommandSender sender, String s) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            s = s.replace("{player}", player.getName());
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                s = PlaceholderAPI.setPlaceholders(player, s);
            }
        }
        s = s.replace("{prfx}", Objects.requireNonNull(m.getString("prefix", "&8|&a&lEco&f&lLobby&8| &f")));

        sender.sendMessage(color(s));
    }
    public static void sendTitle(Player player, String s, String subs, int duration) {
        s = s.replace("{player}", player.getName());
        s = s.replace("{prfx}", Objects.requireNonNull(m.getString("prefix", "&8|&a&lEco&f&lLobby&8| &f")));
        subs = subs.replace("{player}", player.getName());
        subs = subs.replace("{prfx}", Objects.requireNonNull(m.getString("prefix", "&8|&a&lEco&f&lLobby&8| &f")));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            s = PlaceholderAPI.setPlaceholders(player, s);
            subs = PlaceholderAPI.setPlaceholders(player, subs);
        }
        player.sendTitle(s, subs, duration, 20, 70);
    }
    public static void sendActionBar(Player player, String s) {
        s = s.replace("{player}", player.getName());
        s = s.replace("{prfx}", Objects.requireNonNull(m.getString("prefix", "&8|&a&lEco&f&lLobby&8| &f")));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            s = PlaceholderAPI.setPlaceholders(player, s);
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(s));
    }
}
