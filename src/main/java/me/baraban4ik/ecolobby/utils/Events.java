package me.baraban4ik.ecolobby.utils;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Events {
    private static FileConfiguration c;
    private static FileConfiguration s;

    public Events(FileConfiguration cfg, FileConfiguration spawn) {
        Events.c = cfg;
        Events.s = spawn;
    }

    public static void getLocation(Player player) throws IOException {

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
        s.save("spawn.yml");
    }

    public static void tpSpawn(Player player) {
        double x = s.getDouble("spawn.x");
        double y = s.getDouble("spawn.y");
        double z = s.getDouble("spawn.z");
        float pitch = (float) s.getDouble("spawn.pitch");
        float yaw = (float) s.getDouble("spawn.yaw");
        World world = Bukkit.getWorld(Objects.requireNonNull(s.getString("spawn.world")));
        Location loc = new Location(world, x, y, z, yaw, pitch);
        player.teleport(loc);
    }

    public static void addEffects(Player player, List<String> effects) {
        if (effects != null) {
            effects.forEach((x) -> {
                PotionEffectType types = PotionEffectType.getByName(x.split(":")[0].toUpperCase());
                int level = Integer.parseInt(x.split(":")[1]) - 1;
                player.addPotionEffect(new PotionEffect(types, 9999999, level));
            });
        }
    }

    public static void setRules() {
        for (World w : Bukkit.getWorlds()) {

            w.setTime(c.getLong("settings.time-set"));

            if (!c.getBoolean("settings.rules.day-light-cycle")) {
                w.setGameRuleValue("doDaylightCycle", "false");
            }else {
                w.setGameRuleValue("doDaylightCycle", "true");
            }
            if (!c.getBoolean("settings.rules.weather-cycle")) {
                w.setGameRuleValue("doWeatherCycle", "false");
            }else {
                w.setGameRuleValue("doWeatherCycle", "true");
            }
            if (!c.getBoolean("settings.rules.mob-spawning")) {
                w.setGameRuleValue("doMobSpawning", "false");
            }else {
                w.setGameRuleValue("doMobSpawning", "true");
            }
            if (!c.getBoolean("settings.rules.fire-spread")) {
                w.setGameRuleValue("doFireTick", "false");
            }else {
                w.setGameRuleValue("doFireTick", "true");
            }
        }
    }
    public static void music(Player p, String s) {
        Sound sound = Sound.valueOf("MUSIC_DISC_" + s.toUpperCase());
        p.playSound(p.getLocation(), sound, 1, 0);
    }
}

