package me.baraban4ik.ecolobby;



import java.util.Arrays;
import java.util.List;
public interface MESSAGES {
    List<String> HELP_MESSAGE = Arrays.asList(
            "",
            "  &aEcoLobby &7( &fHelp page &7)",
            "",
            "  &f/el help &7- Open this page",
            "  &f/el reload &7- Reload plugin configuration",
            "  &f/el setspawn (main / first) &7- Set spawn main or first",
            "  &f/el spawn &7- Teleport to the spawn",
            "  &f/el give (player) <item_name> &7- Give out a custom item",
            " "
    );
    List<String> ENABLE_MESSAGE = Arrays.asList
            (
                    " ",
                    "&a ____  ___  _____    __    _____  ____  ____  _  _",
                    "&a( ___)/ __)(  _  )  (  )  (  _  )(  _ \\(  _ \\( \\/ )",
                    "&a )__)( (__  )(_)(    )(__  )(_)(  ) _ < ) _ < \\  /",
                    "&a(____)\\___)(_____)  (____)(_____)(____/(____/ (__)",
                    "",
                    " &fАвтор: &aBaraban4ik &7| &fФорк: &aDraimGooSe",
                    " &fВерсия: &a" + EcoLobby.instance.getDescription().getVersion(),
                    " &fВы используется спец. версия для &bWILLOW STUDIO &c♥",
                    "",
                    " PlaceholderAPI: &a" + EcoLobby.placeholderAPI,
                    " NoteBlockAPI: &a" + EcoLobby.noteBlockAPI,
                    " ",
                    "ᴇᴄᴏ ᴅᴇᴠ - development studio",
                    " "
            );
    String ONLY_PLAYER = "  &7( &aEcoLobby &7)  &fThis command is only available to players!";
    String NEW_VERSION = "  &7( &aEcoLobby &7)  &fThere is a new update available.";
    String GIVE_USAGE = "  &7( &aUsage &7)  &f/el give (player) <item_name>";
    String SETSPAWN_USAGE = "  &7( &aUsage &7)  &f/el setspawn (main / first)";

    String PLUGIN_RELOADED = "  &7( &aEcoLobby &7)  &fPlugin successfully reloaded!";
    String ITEM_NOT_FOUND = "  &7( &aEcoLobby &7)  &fItem not found!";
    String PLAYER_NOT_FOUND = "  &7( &aEcoLobby &7)  &fPlayer not found!";
    String SUCCESSFULLY_GIVE_ITEM = "  &7( &aEcoLobby &7)  &fItem issued successfully";
    String SUCCESSFULLY_SETSPAWN = "  &7( &aEcoLobby &7)  &fSpawn successfully installed.";
}
