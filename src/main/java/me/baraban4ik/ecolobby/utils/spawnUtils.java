package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;

public class spawnUtils {
    private static Configurations config;

    public spawnUtils(Configurations configurations) {
        config = configurations;
    }

    public static void getLocation(Player player) throws IOException {
        FileConfiguration spawn = config.get("spawn.yml");

        Location loc = player.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        float pitch = loc.getPitch();
        float yaw = loc.getYaw();

        spawn.set("spawn.x", x);
        spawn.set("spawn.y", y);
        spawn.set("spawn.z", z);
        spawn.set("spawn.pitch", pitch);
        spawn.set("spawn.yaw", yaw);
        spawn.set("spawn.world", player.getWorld().getName());

        config.save(spawn, "spawn.yml");
    }

    public static void tpSpawn(Player player) {
        double x = config.get("spawn.yml").getDouble("spawn.x");
        double y = config.get("spawn.yml").getDouble("spawn.y");
        double z = config.get("spawn.yml").getDouble("spawn.z");

        float pitch = (float) config.get("spawn.yml").getDouble("spawn.pitch");
        float yaw = (float) config.get("spawn.yml").getDouble("spawn.yaw");

        World world = Bukkit.getWorld(Objects.requireNonNull(config.get("spawn.yml").getString("spawn.world")));

        Location loc = new Location(world, x, y, z, yaw, pitch);
        player.teleport(loc);
    }
}
