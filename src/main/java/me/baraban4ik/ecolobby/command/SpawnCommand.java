package me.baraban4ik.ecolobby.command;

import me.baraban4ik.ecolobby.command.base.BaseCommand;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends BaseCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (!hasPermission(player, "ecolobby.spawn")) return true;

        Location spawn = Spawn.get();
        if (spawn == null) {
            Chat.sendMessage("null_spawn", player);
            return true;
        }
        Chat.sendMessage("teleported_spawn", sender);
        player.teleport(spawn);

        return true;
    }
}
