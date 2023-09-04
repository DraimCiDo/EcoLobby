package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.EcoLobby;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.baraban4ik.ecolobby.EcoLobby.config;
import static me.baraban4ik.ecolobby.EcoLobby.messages;

public class Chat {
    public static void sendMessage(String path, CommandSender sender) {
        sender.sendMessage(format(messages.getString(path), sender));
    }
    public static void sendMessage(List<String> message, CommandSender sender) {
        message.forEach(x -> sender.sendMessage(format(x, sender)));
    }

    public static void sendPluginMessage(String message, CommandSender sender) {
        sender.sendMessage(format(message, sender));
    }
    public static void sendPluginMessage(List<String> message, CommandSender sender) {
        message.forEach(x -> sender.sendMessage(format(x, sender)));
    }

    public static void sendTitle(@NotNull Player player, String title, String subTitle, int duration) {
        player.sendTitle(format(title, player), format(subTitle, player), duration, 20, 70);
    }
    public static void sendActionBar(Player player, String actionMessage) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(actionMessage, player)));
    }

    public static String format(String text, CommandSender sender) {
        text = text.replace("%prfx%", messages.getString("prefix"))
                .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()));

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                text = PlaceholderAPI.setPlaceholders(player, text);
            }
            text = text.replace("%player%", player.getDisplayName())
                    .replace("%NL%", "\n");
        }
        text = text.replace("%NL%", "");

        if (EcoLobby.instance.getVersion() >= 1.16f) {
            Pattern pattern = Pattern.compile(config.getString("hex_pattern", "#[a-fA-F0-9]{6}"));
            Matcher match = pattern.matcher(text);

            while (match.find()) {
                String color = text.substring(match.start(), match.end());
                text = text.replace(color, String.valueOf(ChatColor.of(color)));

                match = pattern.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
