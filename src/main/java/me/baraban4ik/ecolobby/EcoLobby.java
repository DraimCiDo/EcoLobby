package me.baraban4ik.ecolobby;

import me.baraban4ik.ecolobby.command.LobbyCommand;
import me.baraban4ik.ecolobby.command.LobbyTabCompleter;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.listeners.*;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Metrics;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class EcoLobby extends JavaPlugin {

    private Configurations config = new Configurations(this, "config.yml", "lang/en.yml", "lang/ru.yml", "spawn.yml");
    private final Chat chat = new Chat(config);
    private final Spawn Spawn = new Spawn(config);

    public final List<String> ENABLE_MESSAGE = Arrays.asList
            (
                    "",
                    "        §a§lEco§f§lLobby §8— §7Multifunctional lobby",
                    "",
                    "              §7Author §8— §aBaraban4ik",
                    "                 §7Version §8— §a" + this.getDescription().getVersion(),
                    "",
                    "             §a§lEco§f§lLobby §7by §aBaraban4ik",
                    ""
            );

    @Override
    public void onEnable() {
        config.loadConfigurations();
        ENABLE_MESSAGE.forEach(System.out::println);

        int pluginId = 14978;
        Metrics metrics = new Metrics(this, pluginId);

        // Register Listeners
        getServer().getPluginManager().registerEvents(new JoinListener(config, this), this);
        getServer().getPluginManager().registerEvents(new HideStreamListener(config), this);
        getServer().getPluginManager().registerEvents(new MoveListener(config), this);
        getServer().getPluginManager().registerEvents(new RestrictionsListener(config), this);
        getServer().getPluginManager().registerEvents(new KickListener(config), this);
        // Register Commands
        Objects.requireNonNull(getServer().getPluginCommand("ecolobby")).setExecutor(new LobbyCommand(config, this));
        Objects.requireNonNull(getServer().getPluginCommand("ecolobby")).setTabCompleter(new LobbyTabCompleter());

        setRules();
        checkConfig();
    }
    @Override
    public void onDisable() {
        config = null;
    }


    public void reload() {
        config.reloadConfigurations();
        checkConfig();
    }
    public void checkConfig() {
        if (!Objects.equals(config.get("config.yml").getString("main.config-version"), this.getDescription().getVersion())){
            File file = new File(this.getDataFolder(), "config.yml");
            file.renameTo(new File(this.getDataFolder(), "config.yml.old"));
            config.reloadConfigurations();
        }
    }
    public static Float getVersion() {
        String version = Bukkit.getVersion();
        String pattern = "[^0-9\\.\\:]";
        String versionMinecraft = version.replaceAll(pattern, "");
        return Float.parseFloat(versionMinecraft.substring(versionMinecraft.indexOf(":") + 1, versionMinecraft.lastIndexOf(".")));
    }

    public void setRules() {
        for (World w : Bukkit.getWorlds()) {
            w.setTime(config.get("config.yml").getLong("settings.time-set"));
            if (getVersion() < 1.13) {
                w.setGameRuleValue("doDaylightCycle", String.valueOf(config.get("config.yml").getBoolean("settings.rules.day-light-cycle")));
                w.setGameRuleValue("doWeatherCycle", String.valueOf(config.get("config.yml").getBoolean("settings.rules.weather-cycle")));
                w.setGameRuleValue("doMobSpawning", String.valueOf(config.get("config.yml").getBoolean("settings.rules.mob-spawning")));
                w.setGameRuleValue("doFireTick", String.valueOf(config.get("config.yml").getBoolean("settings.rules.fire-spread")));
            }
            else {
                w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, config.get("config.yml").getBoolean("settings.rules.day-light-cycle"));
                w.setGameRule(GameRule.DO_WEATHER_CYCLE, config.get("config.yml").getBoolean("settings.rules.weather-cycle"));
                w.setGameRule(GameRule.DO_MOB_SPAWNING, config.get("config.yml").getBoolean("settings.rules.mob-spawning"));
                w.setGameRule(GameRule.DO_FIRE_TICK, config.get("config.yml").getBoolean("settings.rules.fire-spread"));
            }
        }
    }
}
