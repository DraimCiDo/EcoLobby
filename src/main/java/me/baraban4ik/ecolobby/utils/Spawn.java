package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;

public class Spawn {
    private static Configurations config;

    public Spawn(Configurations configurations) {
        config = configurations;
    }

    public static void getLocation(Player player) throws IOException {
        FileConfiguration s = config.get("spawn.yml");
        Location loc = player.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float pitch = loc.getPitch();
        float yaw = loc.getYaw();
        s.set("spawn.x", x);
        s.set("spawn.y", y);
        s.set("spawn.z", z);
        s.set("spawn.pitch", pitch);
        s.set("spawn.yaw", yaw);
        s.set("spawn.world", player.getWorld().getName());
        config.save(s, "spawn.yml");
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
