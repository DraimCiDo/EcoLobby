package me.baraban4ik.ecolobby.listeners;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import me.baraban4ik.ecolobby.EcoLobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import static me.baraban4ik.ecolobby.EcoLobby.config;

public class LeaveListeners implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (config.getBoolean("Join_and_Leave.clear_items")) {
            player.getInventory().clear();
        }
        if (config.getBoolean("Join_and_Leave.music.enabled") && EcoLobby.noteBlockAPI) {
            NoteBlockAPI.stopPlaying(event.getPlayer());
        }
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }
}
