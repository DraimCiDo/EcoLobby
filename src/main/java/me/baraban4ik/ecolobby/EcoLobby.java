package me.baraban4ik.ecolobby;

import me.baraban4ik.ecolobby.commands.LobbyCommand;
import me.baraban4ik.ecolobby.commands.LobbyTabCompleter;
import me.baraban4ik.ecolobby.commands.SpawnCommand;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.item.ItemManager;
import me.baraban4ik.ecolobby.listeners.*;
import me.baraban4ik.ecolobby.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

import static me.baraban4ik.ecolobby.utils.Chat.sendMessage;

public final class EcoLobby extends JavaPlugin {

    private Configurations configurations = new Configurations(this, "config.yml", "lang/en.yml", "lang/ru.yml", "spawn.yml");

    @Override
    public void onEnable() {
        configurations.loadConfigurations();

        this.updateConfig();
        this.checkUpdate();

        // BStats:
        int pluginId = 14978;
        new Metrics(this, pluginId);
        // Load utils:
        new Chat(configurations);
        new Utils(configurations);

        this.registerListeners();
        this.registerCommands();

        if (Objects.equals(configurations.get("config.yml").getString("Main.lang"), "ru")) {
            MESSAGES.ENABLE_MESSAGE_RU.forEach((x) -> sendMessage(Bukkit.getConsoleSender(), x));
            return;
        }
        MESSAGES.ENABLE_MESSAGE_EN.forEach((x) -> sendMessage(Bukkit.getConsoleSender(), x));
    }

    @Override
    public void onDisable() {
        configurations = null;
    }

    public void reload() {
        configurations.reloadConfigurations();
        updateConfig();
    }
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(configurations, this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(configurations, this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(configurations), this);
        getServer().getPluginManager().registerEvents(new ItemsListener(configurations, this), this);
    }
    private void registerCommands() {
        getServer().getPluginCommand("ecolobby").setExecutor(new LobbyCommand(configurations,this));
        getServer().getPluginCommand("ecolobby").setTabCompleter(new LobbyTabCompleter());
        getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand(configurations));
    }
    private void checkUpdate() {
        if (configurations.get("config.yml").getBoolean("Main.check-update")) {
            new UpdateChecker(this, 101547).getVersion(version -> {
                if (!this.getDescription().getVersion().equals(version)) {

                    if (Objects.equals(configurations.get("config.yml").getString("Main.lang"), "ru")) {
                        sendMessage(Bukkit.getConsoleSender(), MESSAGES.NEW_VERSION_RU);
                        return;
                    }
                    sendMessage(Bukkit.getConsoleSender(), MESSAGES.NEW_VERSION_EN);
                }
            });
        }
    }

    public void updateConfig() {
        if (!Objects.equals(configurations.get("config.yml").get("Main.config-version"), this.getDescription().getVersion())) {
            File file = new File(this.getDataFolder(), "config.yml");
            file.renameTo(new File(this.getDataFolder(), "config.yml.old"));

            configurations.reloadConfigurations();
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
