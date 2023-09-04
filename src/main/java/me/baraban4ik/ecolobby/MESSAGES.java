package me.baraban4ik.ecolobby;



import java.util.Arrays;
import java.util.List;

public interface MESSAGES {
    List<String> ENABLE_MESSAGE = Arrays.asList
            (
                    "&f",
                    "   &f&lᴇᴄᴏ&a&lʟᴏʙʙʏ &7| &fMultifunctional lobby manager",
                    "&f",
                    "    &fAuthor &7— &aBaraban4ik",
                    "    &fVersion &7— &a" + EcoLobby.instance.getDescription().getVersion(),
                    "&f"
            );
    String NO_PLAYER = "  &f&lᴇ&a&lʟᴏʙʙʏ  &7|  &fThis command is only available to players!";
    String NEW_VERSION = "  &f&lᴇ&a&lʟᴏʙʙʏ  &7|  &fThere is a new update available. &ahttps://www.spigotmc.org/101547/";
    String PLUGIN_RELOADED = "%NL%  &f&lᴇ&a&lʟᴏʙʙʏ  &7|  &fPlugin successfully reloaded! %NL% ";

    String ITEM_NOT_FOUND = "%NL%  &f&lᴇ&a&lʟᴏʙʙʏ  &7|  &fItem not found! %NL% ";
    String NBTAPI_NOT_FOUND = "%NL%  &f&lᴇ&a&lʟᴏʙʙʏ  &7|  &fCustom join item disabled. NBTAPI not found! %NL% ";
}
