package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.MESSAGES;
import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.baraban4ik.ecolobby.utils.Chat.*;

public class Utils {
    private static Configurations config;

    public Utils(Configurations config) {
        Utils.config = config;
    }


    public static Boolean hasPermission(String permission, CommandSender sender) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sendMessageSection(sender, "no-permission");
        return false;
    }

    public static Boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }

        if (Objects.equals(config.get("config.yml").getString("Main.lang"), "ru")) {
            sendMessage(sender, MESSAGES.NO_PLAYER_RU);
            return false;
        }
        sendMessage(sender, MESSAGES.NO_PLAYER_EN);
        return false;
    }

    public static void setSpawn(Player player, String section) {
        FileConfiguration spawn = config.get("spawn.yml");

        Location location = player.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        float pitch = location.getPitch();
        float yaw = location.getYaw();

        spawn.set(section + ".x", x);
        spawn.set(section + ".y", y);
        spawn.set(section + ".z", z);

        spawn.set(section + ".pitch", pitch);
        spawn.set(section + ".yaw", yaw);
        spawn.set(section + ".world", player.getWorld().getName());

        config.save(spawn, "spawn.yml");
    }

    public static void teleportSpawn(Player player, String section) {
        FileConfiguration spawn = config.get("spawn.yml");

        double x = spawn.getDouble(section + ".x");
        double y = spawn.getDouble(section + ".y");
        double z = spawn.getDouble(section + ".z");

        double pitch = spawn.getDouble(section + ".pitch");
        double yaw = spawn.getDouble(section + ".yaw");

        World world = Bukkit.getWorld(Objects.requireNonNull(spawn.getString(section + ".world")));

        player.teleport(new Location(world, x, y, z, (float)yaw, (float)pitch));
    }
}
