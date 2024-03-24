package me.baraban4ik.ecolobby.commands;

import com.google.common.collect.Lists;
import me.baraban4ik.ecolobby.MESSAGES;
import me.baraban4ik.ecolobby.commands.base.BaseCommand;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand extends BaseCommand {
    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!isPlayer(sender)) return;

        Player player = (Player) sender;
        if (!hasPermission(player, "ecolobby.setspawn")) return;

        if (args.length == 0) {
            Chat.sendMessage(MESSAGES.SUCCESSFULLY_SETSPAWN, player);
            Spawn.set(player, "main");
        }
        else if (args.length == 1) {
            String spawnType = args[0];

            if (!spawnType.equalsIgnoreCase("main") && !spawnType.equalsIgnoreCase("first")) {
                Chat.sendMessage(MESSAGES.SETSPAWN_USAGE, player);
                return;
            }
            Chat.sendMessage(MESSAGES.SUCCESSFULLY_SETSPAWN, player);
            Spawn.set(player, spawnType);
        }
    }
    @Override
    public List<String> complete(CommandSender sender, String[] args) {

        if (args.length == 1) {
            return Lists.newArrayList("main", "first");
        }

        return new ArrayList<>();
    }
}
