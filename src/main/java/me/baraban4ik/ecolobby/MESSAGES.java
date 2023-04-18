package me.baraban4ik.ecolobby;

import java.util.Arrays;
import java.util.List;

public interface MESSAGES {
    List<String> HELP_EN = Arrays.asList
            (
                    "&f",
                    "   &f&lᴇᴄᴏ&a&lʟᴏʙʙʏ &8| &7Commands helper",
                    "&f",
                    "      &7/ecolobby reload &8— &aReloads plugin configuration",
                    "&f",
                    "      &7/ecolobby setspawn &8— &aSets the player spawn",
                    "      &7/ecolobby setfirstspawn &8— &aSets the first player spawn",
                    "      &7/spawn &8— &aTeleports to the spawn",
                    "&f",
                    "      &7/ecolobby give &8— &aGives the item when connected",
                    "&f"
            );
    List<String> HELP_RU = Arrays.asList
            (
                    "&f",
                    "   &f&lᴇᴄᴏ&a&lʟᴏʙʙʏ &8| &7Помощник по командам",
                    "&f",
                    "      &7/ecolobby reload &8— &aПерезагружает конфигурацию плагина",
                    "&f",
                    "      &7/ecolobby setspawn &8— &aУстанавливает спавн игрока",
                    "      &7/ecolobby setfirstspawn &8— &aУстанавливает первый спавн игрока",
                    "      &7/spawn &8— &aТелепортирует к спавну",
                    "&f",
                    "      &7/ecolobby give &8— &aВыдаёт предмет при подключении",
                    "&f"
            );
    List<String> ENABLE_MESSAGE_RU = Arrays.asList
            (
                    "&f",
                    "   &f&lᴇᴄᴏ&a&lʟᴏʙʙʏ &8| &7Многофункциональное лобби",
                    "&f",
                    "      &7Автор &8— &aBaraban4ik",
                    "      &7Версия &8— &a1.0.2",
                    "&f"
            );
    List<String> ENABLE_MESSAGE_EN = Arrays.asList
            (
                    "&f",
                    "   &f&lᴇᴄᴏ&a&lʟᴏʙʙʏ &8| &7Multifunctional Lobby",
                    "&f",
                    "      &7Author &8— &aBaraban4ik",
                    "      &7Version &8— &a1.0.2",
                    "&f"
            );
    String NO_PLAYER_EN = "  &l&fᴇ&a&lʟ &8|  &7This command is only available to players!";
    String NO_PLAYER_RU = "  &l&fᴇ&a&lʟ &8|  &7Эта команда доступна только игрокам!";
    String NEW_VERSION_RU = "  &l&fᴇ&a&lʟ &8|  &7Доступно новое обновление. &ahttps://spigotmc.ru/resources/998/";
    String NEW_VERSION_EN = "  &l&fᴇ&a&lʟ &8|  &7There is a new update available. &ahttps://www.spigotmc.org/101547/";
}
