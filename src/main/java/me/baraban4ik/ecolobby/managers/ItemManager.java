package me.baraban4ik.ecolobby.managers;

import de.tr7zw.nbtapi.NBT;
import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.baraban4ik.ecolobby.EcoLobby.config;

public class ItemManager {
    public static ItemStack get(@NotNull Player player, @NotNull String section) {

        ConfigurationSection item_section = config.getConfigurationSection("join_settings.custom_join_items.items." + section);
        if (item_section == null) return null;

        Material material = Material.getMaterial(item_section.getString("material"));
        String name = item_section.getString("name", "");

        List<String> lore = item_section.getStringList("lore");
        List<String> format_lore = new ArrayList<>();

        int data = item_section.getInt("data", 0);
        int amount = item_section.getInt("amount", 1);

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        item.setDurability((byte) data);
        meta.setDisplayName(Chat.format(name, player));

        for (String s : lore) {
            format_lore.add(Chat.format(s, player));
        }
        meta.setLore(format_lore);

        if (EcoLobby.instance.getVersion() >= 1.14f) {
            NamespacedKey key = new NamespacedKey(EcoLobby.instance, "JOIN_ITEM");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, section);
        }
        item.setItemMeta(meta);

        if (EcoLobby.instance.getVersion() < 1.14f) {
            if (!Bukkit.getPluginManager().isPluginEnabled("NBTAPI")) {
                return null;
            }
            NBT.modify(item, nbt -> {
                nbt.setString("JOIN_ITEM", section);
            });
        }
        return item;
    }

    public static void run(Player player, List<String> actions) {
        actions.forEach(action -> {
            if (actionType(action, "[CMD]"))
                player.performCommand(replaceVoid(action, player, "[CMD]"));

            if (actionType(action, "[CONSOLE]"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replaceVoid(action, player, "[CONSOLE]"));

            if (actionType(action, "[MSG]"))
                Chat.sendPluginMessage(replaceVoid(action, player, "[MSG]"), player);
        });
    }
    private static boolean actionType(@NotNull String actions, String action) {
        return actions.startsWith(action) || actions.startsWith(action + " ");
    }
    private static @NotNull String replaceVoid(@NotNull String actions, @NotNull Player player, String target) {
        actions = actions.replace(target + " ", "").replace(target, "");
        return Chat.format(actions, player);
    }

    public static boolean checkNBT(ItemStack item, String section) {
        if (EcoLobby.instance.getVersion() < 1.14f && Bukkit.getPluginManager().isPluginEnabled("NBTAPI")) {
            String tag = NBT.get(item, nbt -> nbt.getString("JOIN_ITEM"));
            return tag.equals(section);
        }
        if (EcoLobby.instance.getVersion() >= 1.14f) {
            NamespacedKey key = new NamespacedKey(EcoLobby.instance, "JOIN_ITEM");
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if (container.has(key, PersistentDataType.STRING))
                return Objects.equals(container.get(key, PersistentDataType.STRING), section);
        }
        return false;
    }
}
