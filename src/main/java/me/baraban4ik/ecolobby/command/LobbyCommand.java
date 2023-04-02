package me.baraban4ik.ecolobby.command;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.utils.spawnUtils;
import org.bukkit.command.CommandExecutor;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.baraban4ik.ecolobby.utils.chatUtils.getLang;
import static me.baraban4ik.ecolobby.utils.chatUtils.sendMessage;

public class LobbyCommand extends LobbyTabCompleter implements CommandExecutor {

    private final EcoLobby plugin;
    private static final List<String> HELP = Arrays.asList
            (
                    "",
                    "           §a§lEco§f§lLobby §8— §7Helper commands",
                    "",
                    "    §7/ecolobby reload §8— §aReload config plugin.",
                    "        §7/ecolobby setspawn §8— §aSet spawn.",
                    "            §7/spawn §8— §aTeleport to spawn.",
                    "",
                    "             §a§lEco§f§lLobby §7by §aBaraban4ik",
                    ""
            );

    public LobbyCommand(EcoLobby plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            checkPerm("ecolobby.reload", sender);
            sendMessage(sender, getLang().getString("plugin-reloaded", "Plugin successfully reloaded!"));

            plugin.reload();
        } else if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
            if (!isPlayer(sender)) { return true; }
            checkPerm("ecolobby.setspawn", sender);

            Player player = (Player) sender;
            try {
                spawnUtils.getLocation(player);
            } catch (IOException e) {
                e.printStackTrace();
            }

            sendMessage(sender, getLang().getString("successfully-setspawn", "Spawn has been successfully installed."));
        } else {
            checkPerm("ecolobby.help", sender);

            List<String> help = getLang().getStringList("help");
            if (help.isEmpty()) {
                HELP.forEach((x) -> sendMessage(sender, x));
                return true;
            }
            help.forEach((x) -> sendMessage(sender, x));
        }
        return true;
    }

    private void checkPerm(String permission, CommandSender sender) {
        if (sender.hasPermission(permission)) {
            return;
        }
        sendMessage(sender, getLang().getString("no-permission"));
    }

    private Boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }
        sendMessage(sender, getLang().getString("no-player", "This command is only available to players!"));
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload", "setspawn");
        return new ArrayList<>();
    }
}
