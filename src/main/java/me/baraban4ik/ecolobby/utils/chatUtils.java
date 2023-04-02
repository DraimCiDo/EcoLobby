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

public class chatUtils {
    private static Configurations config;

    public chatUtils(Configurations configurations) {
        config = configurations;
    }


    public static void sendMessage(CommandSender sender, String s) {
        sender.sendMessage(format(s, sender));
    }

    public static void sendTitle(Player player, String s, String subs, int duration) {
        player.sendTitle(format(s, player), format(subs, player), duration, 20, 70);
    }

    public static void sendActionBar(Player player, String s) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(s, player)));
    }

    public static FileConfiguration getLang() {
        return config.get("lang/" + config.get("config.yml").get("lang") + ".yml");
    }

    public static String format(String s, CommandSender sender) {
        s = s.replace("{prfx}", getLang().getString("prefix"));
        s = s.replace("{NL}", "\n");

        if (sender instanceof Player) {
            Player p = (Player) sender;

            s = s.replace("{player}", p.getName());

            if (EcoLobby.getPlugin("PlaceholderAPI")) {
                s = PlaceholderAPI.setPlaceholders(p, s);
            }
        }
        if (EcoLobby.getVersion() >= 1.16f) {
            Pattern pattern = Pattern.compile(config.get("config.yml").getString("hex-pattern", "&#[a-fA-F0-9]{6}"));
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
