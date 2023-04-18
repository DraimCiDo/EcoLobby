package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    private static Configurations config;

    public Chat(Configurations configurations) {
        config = configurations;
    }


    public static void sendMessageSection(CommandSender sender, String section) {
		if (getLang().getString(section).isEmpty()) {
			return;
		}
        sender.sendMessage(format(getLang().getString(section), sender));
    }
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(format(message, sender));
    }
    public static void sendTitle(Player player, String title, String subTitle, int duration) {
        player.sendTitle(format(title, player), format(subTitle, player), duration, 20, 70);
    }
    public static void sendActionBar(Player player, String actionMessage) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(actionMessage, player)));
    }

    public static FileConfiguration getLang() {
        return config.get("lang/" + config.get("config.yml").get("Main.lang") + ".yml");
    }

    public static String format(String message, CommandSender sender) {
        message = message.replace("{prfx}", getLang().getString("prefix"));

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (EcoLobby.getPlugin("PlaceholderAPI")) {
                message = PlaceholderAPI.setPlaceholders(p, message);
            }
            message = message.replace("{player}", p.getName());
            message = message.replace("{NL}", "\n");
        }
        else {
            message = message.replace("{NL}", "");
        }
        if (EcoLobby.getVersion() >= 1.16f) {
            Pattern pattern = Pattern.compile(config.get("config.yml").getString("hex-pattern", "&#[a-fA-F0-9]{6}"));
            Matcher match = pattern.matcher(message);

            while (match.find()) {
                String color = message.substring(match.start(), match.end());
                message = message.replace(color, ChatColor.of(color) + "");

                match = pattern.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
