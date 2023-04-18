package me.baraban4ik.ecolobby.commands;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.MESSAGES;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.item.ItemManager;
import me.baraban4ik.ecolobby.utils.Utils;
import org.bukkit.command.CommandExecutor;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.baraban4ik.ecolobby.utils.Chat.sendMessage;
import static me.baraban4ik.ecolobby.utils.Chat.sendMessageSection;

public class LobbyCommand extends LobbyTabCompleter implements CommandExecutor {

    private final EcoLobby plugin;
    private final Configurations config;

    public LobbyCommand(Configurations config, EcoLobby plugin) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
        {
            if (!Utils.hasPermission("ecolobby.reload", sender)) return true;

            sendMessageSection(sender, "plugin-reloaded");
            plugin.reload();
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("setspawn"))
        {
            if (!Utils.isPlayer(sender)) return true;
            if (!Utils.hasPermission("ecolobby.setspawn", sender)) return true;

            Player player = (Player) sender;

            Utils.setSpawn(player, "spawn");
            sendMessageSection(sender, "successfully-setspawn");

        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("setfirstspawn"))
        {
            if (!Utils.isPlayer(sender)) return true;
            if (!Utils.hasPermission("ecolobby.setspawn", sender)) return true;

            Player player = (Player) sender;
            player.getInventory().addItem(ItemManager.joinItem);

            Utils.setSpawn(player, "firstSpawn");
            sendMessageSection(sender, "successfully-setspawn");
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("give"))
        {
            if (!Utils.isPlayer(sender)) return true;
            if (!Utils.hasPermission("ecolobby.give", sender)) return true;

            Player player = (Player) sender;

            ItemManager.init(config, player);
            player.getInventory().addItem(ItemManager.joinItem);
        }
        else {
            if (!Utils.hasPermission("ecolobby.help", sender)) return true;

            if (Objects.equals(config.get("config.yml").getString("Main.lang"), "ru")) {
                MESSAGES.HELP_RU.forEach((x) -> sendMessage(sender, x));
                return true;

            }
            MESSAGES.HELP_EN.forEach((x) -> sendMessage(sender, x));
            return true;
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload", "setspawn", "setfirstspawn", "give");
        return new ArrayList<>();
    }
}
