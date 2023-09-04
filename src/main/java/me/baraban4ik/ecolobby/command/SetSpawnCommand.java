package me.baraban4ik.ecolobby.command;

import me.baraban4ik.ecolobby.command.base.BaseCommand;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand extends BaseCommand {

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        if (!hasPermission(player, "ecolobby.setspawn")) return true;

        Chat.sendMessage("successfully_setspawn", sender);
        Spawn.set(player);

        return true;
    }

}
