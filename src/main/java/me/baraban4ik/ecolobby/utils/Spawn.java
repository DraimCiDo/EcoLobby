package me.baraban4ik.ecolobby.utils;


import me.baraban4ik.ecolobby.EcoLobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.baraban4ik.ecolobby.EcoLobby.spawn;

public class Spawn {
    public static void set(@NotNull Player player) {
        Location location = player.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        float pitch = location.getPitch();
        float yaw = location.getYaw();

        spawn.set("x", x);
        spawn.set("y", y);
        spawn.set("z", z);

        spawn.set("pitch", pitch);
        spawn.set("yaw", yaw);
        spawn.set("world", player.getWorld().getName());

        EcoLobby.instance.getConfigurations().save(spawn, "spawn.yml");
    }

    public static Location get() {
        if (spawn.get("x") == null && spawn.get("y") == null) return null;

        double x = spawn.getDouble("x");
        double y = spawn.getDouble("y");
        double z = spawn.getDouble("z");

        double pitch = spawn.getDouble("pitch");
        double yaw = spawn.getDouble("yaw");

        World world = Bukkit.getWorld(spawn.getString("world"));

        return new Location(world, x, y, z, (float)yaw, (float)pitch);
    }
}
