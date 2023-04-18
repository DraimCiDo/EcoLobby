package me.baraban4ik.ecolobby.commands;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.baraban4ik.ecolobby.utils.Chat.sendMessageSection;
import static me.baraban4ik.ecolobby.utils.Utils.hasPermission;
import static me.baraban4ik.ecolobby.utils.Utils.isPlayer;
import static me.baraban4ik.ecolobby.utils.Utils.teleportSpawn;

public class SpawnCommand implements CommandExecutor {

    private final Configurations config;

    public SpawnCommand(Configurations configurations) {
        config = configurations;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasPermission("ecolobby.spawn", sender)) return true;

        if (config.get("spawn.yml").getString("spawn.x") == null &&
                config.get("spawn.yml").getString("spawn.y") == null) {

            sendMessageSection(sender, "null-spawn");
            return true;
        }

        Player player = (Player) sender;

        teleportSpawn(player, "spawn");
        sendMessageSection(sender, "successfully-spawn");

        return true;
    }
}
