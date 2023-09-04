package me.baraban4ik.ecolobby;

import de.tr7zw.nbtapi.NBT;
import me.baraban4ik.ecolobby.bstats.Metrics;
import me.baraban4ik.ecolobby.command.LobbyCommand;
import me.baraban4ik.ecolobby.command.SetSpawnCommand;
import me.baraban4ik.ecolobby.command.SpawnCommand;
import me.baraban4ik.ecolobby.command.base.BaseTabCompleter;
import me.baraban4ik.ecolobby.listeners.*;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Update;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class EcoLobby extends JavaPlugin {

    Configurations configurations = new Configurations(this, "config.yml", "messages.yml", "spawn.yml");

    public Configurations getConfigurations() {
        return configurations;
    }

    public static EcoLobby instance;

    public static FileConfiguration config;
    public static FileConfiguration messages;
    public static FileConfiguration spawn;

    Logger logger = this.getLogger();


    @Override
    public void onLoad() {
        instance = this;

        this.configurations.load();
        this.loadConfigurations();
    }

    @Override
    public void onEnable() {
        logger.info("NBTAPI hook: " + this.getServer().getPluginManager().isPluginEnabled("NBTAPI"));
        logger.info("PlaceholderAPI hook: " + this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"));

        Chat.sendPluginMessage(MESSAGES.ENABLE_MESSAGE, Bukkit.getConsoleSender());

        this.registerCommands();
        this.registerListeners();

        new Metrics(this, 14978);

        if (config.getBoolean("check_update")) this.update();
    }
    @Override
    public void onDisable() {
        this.configurations = null;
    }

    public void reload() {
        this.configurations.reload();
        this.loadConfigurations();
    }

    public Float getVersion() {
        String version = Bukkit.getVersion();
        String pattern = "[^0-9\\.\\:]";
        String versionMinecraft = version.replaceAll(pattern, "");

        return Float.parseFloat(versionMinecraft.substring(versionMinecraft.indexOf(":") + 1, versionMinecraft.lastIndexOf(".")));
    }


    private void update() {
        new Update().getVersion(version ->
        {
            if (!this.getDescription().getVersion().equals(version)) {
                Chat.sendPluginMessage(MESSAGES.NEW_VERSION, Bukkit.getConsoleSender());
            }
        });
    }


    private void registerCommands() {
        this.getServer().getPluginCommand("ecolobby").setExecutor(new LobbyCommand());
        this.getServer().getPluginCommand("ecolobby").setTabCompleter(new BaseTabCompleter());

        this.getServer().getPluginCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new ItemsListener(), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void loadConfigurations() {
        config = this.configurations.get("config.yml");
        messages = this.configurations.get("messages.yml");
        spawn = this.configurations.get("spawn.yml");

        if (!Objects.equals(config.get("config_version"), this.getDescription().getVersion())) {

            File file = new File(this.getDataFolder(), "config.yml");
            file.renameTo(new File(this.getDataFolder(), "config.yml-old"));

            this.configurations.reload();
        }
    }
}
