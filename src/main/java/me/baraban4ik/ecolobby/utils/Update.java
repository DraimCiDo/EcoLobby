package me.baraban4ik.ecolobby.utils;

import me.baraban4ik.ecolobby.EcoLobby;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class Update {

    private final int resourceId = 101547;

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(EcoLobby.instance, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                EcoLobby.instance.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
