package me.baraban4ik.ecolobby.listeners;

import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.configurations.Configurations;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Spawn;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JoinListener implements Listener {
    private final EcoLobby plugin;
    private final Configurations config;

    private final List<String> MOTD = Arrays.asList
            (
                    "",
                    "                    &8[&a&lLobby&8]",
                    "",
                    "   &fwelcome to the server &a&lserver",
                    "   &fThere are &amany &fadventures waiting for you here.",
                    "",
                    "   &fHope you &alike &fit here!",
                    ""
            );

    public JoinListener(Configurations configurations, EcoLobby plugin) {
        config = configurations;
        this.plugin = plugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (config.get("spawn.yml").getString("spawn.x") != null && config.get("spawn.yml").getString("spawn.y") != null) {
            Spawn.tpSpawn(player);
        }
        if (config.get("config.yml").getBoolean("settings.enable-motd")) {
            List<String> motd = config.get("lang/" + config.get("config.yml").get("main.lang") + ".yml").getStringList("motd");
            if (motd.isEmpty()) {
                MOTD.forEach((x) -> Chat.sendMessage(player, x));
                return;
            }
            motd.forEach((x) -> Chat.sendMessage(player, x));
        }
        if (config.get("config.yml").getBoolean("settings.title-motd.enabled")) {
            String text = config.get("config.yml").getString("settings.title-motd.title-text");
            String subText = config.get("config.yml").getString("settings.title-motd.subtitle-text");
            int duration = config.get("config.yml").getInt("settings.title-motd.duration");

            Chat.sendTitle(player, text, subText, duration);
        }
        if (config.get("config.yml").getBoolean("settings.action-bar-motd.enabled")) {
            Chat.sendActionBar(player, config.get("config.yml").getString("settings.action-bar-motd.text"));
        }
        if (config.get("config.yml").getBoolean("settings.player-hide")) {
            player.hidePlayer(plugin, player);
        }
        else {
            player.showPlayer(plugin, player);
        }
        if (config.get("config.yml").getBoolean("settings.music.enabled")) {
            Sound sound;
            if (EcoLobby.getVersion() < 1.13) {
                sound = Sound.valueOf("RECORD_" + Objects.requireNonNull(config.get("config.yml").getString("settings.music.disk")).toUpperCase());
            } else {
                sound = Sound.valueOf("MUSIC_DISC_" + Objects.requireNonNull(config.get("config.yml").getString("settings.music.disk")).toUpperCase());
            }
            player.getWorld().playSound(player.getLocation(), sound, 1, 0);
        }
        if (config.get("config.yml").getBoolean("settings.join.clear.chat")) {
            for(int i = 0; i < 120; ++i) {
                player.sendMessage("");
            }
        }
        if (config.get("config.yml").getBoolean("settings.join.clear.inventory")) {
            player.getInventory().clear();
        }
        if (config.get("config.yml").getBoolean("settings.player-fly")) {
            player.setFlying(true);
        }
        if (config.get("config.yml").getBoolean("settings.join.glow")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, true, false));
        }
        else {
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
        player.setGameMode(GameMode.valueOf(Objects.requireNonNull(config.get("config.yml").getString("settings.gamemode")).toUpperCase()));
        player.setLevel(config.get("config.yml").getInt("settings.level-exp"));
        player.setHealth(config.get("config.yml").getDouble("settings.player.health"));
        List<String> effects = config.get("config.yml").getStringList("settings.effects");
        effects.forEach((x) -> {
            PotionEffectType types = PotionEffectType.getByName(x.split(":")[0].toUpperCase());
            int level = Integer.parseInt(x.split(":")[1]) - 1;
            assert types != null;
            player.addPotionEffect(new PotionEffect(types, 9999999, level));
        });
    }
}
