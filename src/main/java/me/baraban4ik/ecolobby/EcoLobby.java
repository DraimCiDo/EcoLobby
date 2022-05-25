package me.baraban4ik.ecolobby;

import me.baraban4ik.ecolobby.command.EcoLobbyCommand;
import me.baraban4ik.ecolobby.command.EcoTabCompleter;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Events;
import me.baraban4ik.ecolobby.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class EcoLobby extends JavaPlugin {

    private Configurations configurations = new Configurations(this, "config.yml", "lang/en.yml", "lang/ru.yml", "spawn.yml");

    private final Chat chat = new Chat(configurations.get("lang/" + configurations.get("config.yml").get("main.lang") + ".yml"), configurations.get("config.yml"));
    private final Events events = new Events(configurations.get("config.yml"), configurations.get("spawn.yml"));

    public final List<String> ENABLE_MESSAGE = Arrays.asList
            (
                    "§7=-=-=-=-§a§lEco§f§lLobby§7-=-=-=-=",
                    "",
                    " Version: §a" + this.getDescription().getVersion(),
                    " Author: §aBaraban4ik",
                    "",
                    "§7=-=-=-=-§a§lEco§f§lLobby§7-=-=-=-="
            );

    @Override
    public void onEnable() {
        configurations.loadConfigurations();
        ENABLE_MESSAGE.forEach(System.out::println);

        Events.setRules();

        getServer().getPluginCommand("ecolobby").setExecutor(new EcoLobbyCommand(configurations.get("lang/" + configurations.get("config.yml").get("main.lang") + ".yml"),configurations.get("spawn.yml"), this));
        getServer().getPluginManager().registerEvents(new EcoListener(configurations.get("config.yml"), configurations.get("lang/" + configurations.get("config.yml").get("main.lang") + ".yml"), configurations.get("spawn.yml"), this), this);
        getServer().getPluginCommand("ecolobby").setTabCompleter(new EcoTabCompleter());

        int pluginId = 14978;
        Metrics metrics = new Metrics(this, pluginId);
        checkConfig();

    }
    @Override
    public void onDisable() {
        configurations = null;
    }

    public void reload() {
        configurations.reloadConfigurations();
        checkConfig();
    }
    public void checkConfig() {
        if (!configurations.get("config.yml").getString("main.config-version").equals("2.0")){
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
}
