package me.baraban4ik.ecolobby.command;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.command.LobbyTabCompleter;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.command.CommandExecutor;

import com.google.common.collect.Lists;
import me.baraban4ik.ecolobby.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyCommand extends LobbyTabCompleter implements CommandExecutor {

    private final EcoLobby plugin;
    private final Configurations config;
    private static final List<String> HELP = Arrays.asList
            (
                    "",
                    "           §a§lEco§f§lLobby §8— §7Helper commands",
                    "",
                    "    §7/ecolobby reload §8— §aReload config plugin.",
                    "        §7/ecolobby setspawn §8— §aSet spawn.",
                    "      §7/ecolobby spawn §8— §aTeleport to spawn.",
                    "",
                    "             §a§lEco§f§lLobby §7by §aBaraban4ik",
                    ""
            );

    public LobbyCommand(Configurations configurations, EcoLobby plugin) {
        config = configurations;
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("ecolobby.reload")) {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("no-permission", "You don't have permission!"));
                return true;
            }
            Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("plugin-reloaded", "Plugin successfully reloaded!"));
            plugin.reload();
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("no-player", "This command is only available to players!"));
                return true;
            }
            if (!sender.hasPermission("ecolobby.setspawn")) {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("no-permission", "You don't have permission!"));
                return true;
            }
            Player player = (Player) sender;
            try {
                Spawn.getLocation(player);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("successfully-setspawn", "Spawn has been successfully installed."));
        }
        else if ((args.length == 1 && args[0].equalsIgnoreCase("spawn"))) {
            if (!(sender instanceof Player)) {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("no-player", "This command is only available to players!"));
                return true;
            }
            if (!sender.hasPermission("ecolobby.spawn")) {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("no-permission", "You don't have permission!"));
                return true;
            }
            if (config.get("spawn.yml").getString("spawn.x") == null && config.get("spawn.yml").getString("spawn.y") == null) {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("spawn-null", "Spawn doesn't exist, I can't teleport you!"));
                return true;
            }
            Player player = (Player) sender;
            Spawn.tpSpawn(player);

            Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("successfully-spawn", "You have been teleported to spawn."));
        }
        else {
            if (!sender.hasPermission("ecolobby.help"))
            {
                Chat.sendMessage(sender, config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getString("no-permissions", "You don't have permission!"));
                return true;
            }
            List<String> help = config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getStringList("help");
            if (help.isEmpty()) {
                HELP.forEach((x) -> Chat.sendMessage(sender, x));
                return true;
            }
            help.forEach((x) -> Chat.sendMessage(sender, x));
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("reload", "setspawn", "spawn");
        return new ArrayList<>();
    }
}
