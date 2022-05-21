package me.baraban4ik.ecolobby.command;

import com.google.common.collect.Lists;
import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Events;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EcoLobbyCommand extends EcoTabCompleter implements CommandExecutor {

    private final EcoLobby plugin;
    private final FileConfiguration m;
    private final FileConfiguration s;

    public EcoLobbyCommand(FileConfiguration mess, FileConfiguration spawn, EcoLobby plugin) {
        this.plugin = plugin;
        this.m = mess;
        this.s = spawn;
    }

    private static final List<String> HELP = Arrays.asList
            (
                    "      &7&m=-=-=-=-&a&lEco&f&lLobby&7&m-=-=-=-=",
                    "",
                    "  /ecolobby reload &8- &aReload plugin.",
                    "  /ecolobby setspawn &8- &aSet spawn.",
                    "  /ecolobby spawn &8- &aTeleport to spawn.",
                    "",
                    "      &7&m=-=-=-=-&a&lEco&f&lLobby&7&m-=-=-=-="
            );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("ecolobby.reload")) {
                Chat.sendMessage(sender, m.getString("no-permission", "You don't have permission!"));
                return true;
            }
            Chat.sendMessage(sender, m.getString("plugin-reloaded", "Plugin successfully reloaded!"));
            plugin.reload();
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) {
                Chat.sendMessage(sender, m.getString("no-player", "This command is only available to players!"));
                return true;
            }
            if (!sender.hasPermission("ecolobby.setspawn")) {
                Chat.sendMessage(sender, m.getString("no-permission", "You don't have permission!"));
                return true;
            }
            Player player = (Player) sender;
            try {
                Events.getLocation(player);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Chat.sendMessage(sender, m.getString("successfully-setspawn", "Spawn has been successfully installed."));
        }
        else if ((args.length == 1 && args[0].equalsIgnoreCase("spawn"))) {
            if (!(sender instanceof Player)) {
                Chat.sendMessage(sender, m.getString("no-player", "This command is only available to players!"));
                return true;
            }
            if (!sender.hasPermission("ecolobby.spawn")) {
                Chat.sendMessage(sender, m.getString("no-permission", "You don't have permission!"));
                return true;
            }
            if (s.getString("spawn.x") == null && s.getString("spawn.y") == null) {
                Chat.sendMessage(sender, m.getString("spawn-null", "Spawn doesn't exist, I can't teleport you!"));
                return true;
            }
            Player player = (Player) sender;
            Events.tpSpawn(player);

            Chat.sendMessage(sender, m.getString("successfully-spawn", "You have been teleported to spawn."));
        }
        else {
            if (!sender.hasPermission("ecolobby.help"))
            {
                Chat.sendMessage(sender, m.getString("no-permissions", "You don't have permission!"));
                return true;
            }
            List<String> help = m.getStringList("help");
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
