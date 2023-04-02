package me.baraban4ik.ecolobby;

import me.baraban4ik.ecolobby.command.LobbyCommand;
import me.baraban4ik.ecolobby.command.LobbyTabCompleter;
import me.baraban4ik.ecolobby.command.SpawnCommand;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.listeners.*;
import me.baraban4ik.ecolobby.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class EcoLobby extends JavaPlugin {

    private Configurations config = new Configurations(this, "config.yml", "lang/en.yml", "lang/ru.yml", "spawn.yml");

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

        // BStats:
        int pluginId = 14978;
        Metrics metrics = new Metrics(this, pluginId);
        // Load utils:
        chatUtils chat = new chatUtils(config);
        spawnUtils spawn = new spawnUtils(config);

        if (config.get("config.yml").getBoolean("check-update")) {
            new UpdateChecker(this, 101547).getVersion(version -> {
                if (this.getDescription().getVersion().equals(version)) {
                    getLogger().info("There is not a §anew update §ravailable.");
                } else {
                    getLogger().info("There is a §anew update §ravailable.");
                }
            });
        }

        // Register Listeners:
        getServer().getPluginManager().registerEvents(new MainListener(config, this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(config), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(config), this);
        getServer().getPluginManager().registerEvents(new WorldListener(config), this);
        getServer().getPluginManager().registerEvents(new ItemsListener(config), this);
        // Register Commands
        getServer().getPluginCommand("ecolobby").setExecutor(new LobbyCommand(this));
        getServer().getPluginCommand("ecolobby").setTabCompleter(new LobbyTabCompleter());
        getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand(config));

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
        if (!Objects.equals(config.get("config.yml").getString("config-version"), this.getDescription().getVersion())) {
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

    public static boolean getPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }
}
