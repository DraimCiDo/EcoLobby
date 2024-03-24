package me.baraban4ik.ecolobby.commands;

import me.baraban4ik.ecolobby.commands.base.BaseCommand;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends BaseCommand {
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!isPlayer(sender)) return;

        Player player = (Player) sender;
        if (!hasPermission(player, "ecolobby.spawn")) return;

        Location spawn = Spawn.get("main");

        if (spawn == null) {
            Chat.sendPathMessage("null_spawn", player);
            return;
        }
        Chat.sendPathMessage("teleported_spawn", player);
        player.teleport(spawn);
    }
}
