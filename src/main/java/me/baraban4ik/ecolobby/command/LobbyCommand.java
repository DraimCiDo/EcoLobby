package me.baraban4ik.ecolobby.command;

import com.google.common.collect.Lists;
import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.MESSAGES;
import me.baraban4ik.ecolobby.command.base.BaseCommand;
import me.baraban4ik.ecolobby.managers.ItemManager;
import me.baraban4ik.ecolobby.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.baraban4ik.ecolobby.EcoLobby.config;
import static me.baraban4ik.ecolobby.EcoLobby.messages;

public class LobbyCommand extends BaseCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (hasPermission(sender, "ecolobby.reload")) {
                    this.reload(sender);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (!isPlayer(sender)) return true;

                Player player = (Player) sender;

                if (hasPermission(player, "ecolobby.give"))
                    Chat.sendMessage("give_usage", player);
				
				return true;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give")) {
                if (!isPlayer(sender)) return true;
                Player player = (Player) sender;

				if (EcoLobby.instance.getVersion() < 1.14f) {
					if (!Bukkit.getPluginManager().isPluginEnabled("NBTAPI")) {
						Chat.sendPluginMessage(MESSAGES.NBTAPI_NOT_FOUND, player);
						return true;
					}
				}
                if (hasPermission(player, "ecolobby.give"))
                    this.give(player, args[1]);

                return true;
            }
        }
        if (hasPermission(sender, "ecolobby.help")) {
            this.sendHelp(sender);
        }
        return true;
    }

    private void reload(@NotNull CommandSender sender) {
        Chat.sendPluginMessage(MESSAGES.PLUGIN_RELOADED, sender);
        EcoLobby.instance.reload();
    }

    private void sendHelp(@NotNull CommandSender sender) {
        Chat.sendMessage(messages.getStringList("help"), sender);
    }

    private void give(@NotNull Player player, @NotNull String section) {
        ConfigurationSection items = config.getConfigurationSection("join_settings.custom_join_items.items." + section);
        ItemStack item = ItemManager.get(player, section);

        if (items == null) {
            Chat.sendPluginMessage(MESSAGES.ITEM_NOT_FOUND, player);
            return;
        }
        if (item != null) {
            player.getInventory().setItem(items.getInt(section + ".slot"), item);
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        ConfigurationSection items = config.getConfigurationSection("join_settings.custom_join_items.items");

        if (args.length == 1) return Lists.newArrayList("reload", "help", "give");
        if (args.length == 2 && args[0].equalsIgnoreCase("give") && items != null) return Lists.newArrayList(items.getKeys(false));

        return new ArrayList<>();
    }
}
