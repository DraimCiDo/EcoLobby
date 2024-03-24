package me.baraban4ik.ecolobby.managers;


import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableItemNBT;
import me.baraban4ik.ecolobby.EcoLobby;
import me.baraban4ik.ecolobby.MESSAGES;
import me.baraban4ik.ecolobby.utils.Chat;
import me.baraban4ik.ecolobby.utils.Format;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.baraban4ik.ecolobby.EcoLobby.*;

public class ItemManager {
    private static ItemStack createItem(@NotNull Player player, @NotNull String itemName) {
        ConfigurationSection itemSection = items.getConfigurationSection("Items." + itemName);
        if (itemSection == null) return null;

        String materialSection = itemSection.getString("material");
        int amount = itemSection.getInt("amount");
        int data = itemSection.getInt("data");

        String displayName = Format.format(itemSection.getString("name"), player);
        List<String> formatLore = itemSection.getStringList("lore").stream()
                .map(line -> Format.format(line, player))
                .collect(Collectors.toList());

        boolean isHead = false;
        boolean isBaseHead = false;
        Material itemMaterial = Material.getMaterial(materialSection);

        if (materialSection.startsWith("head-")) {
            if (!legacyItems)
                itemMaterial = Material.PLAYER_HEAD;
            else
                itemMaterial = Material.LEGACY_SKULL_ITEM;
            isHead = true;
        }
        if (materialSection.startsWith("basehead-")) {
            if (!legacyItems)
                itemMaterial = Material.PLAYER_HEAD;
            else
                itemMaterial = Material.LEGACY_SKULL_ITEM;
            isBaseHead = true;
        }
        ItemStack item = new ItemStack(itemMaterial, amount, (short) data);
        ItemMeta itemMeta = item.getItemMeta();

        if (isHead) {
            if (legacyItems) item.setDurability((short) 3);

            String owner = materialSection.replace("head-", "");
            owner = Format.replacePlaceholders(owner, player);

            SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(Bukkit.getPlayer(owner).getUniqueId()));
        }
        if (isBaseHead) {
            if (legacyItems) item.setDurability((short) 3);

            String base64 = materialSection.replace("basehead-", "");
            NBT.modify(item, nbt -> {
                final ReadWriteNBT skullOwnerCompound = nbt.getOrCreateCompound("SkullOwner");

                skullOwnerCompound.setUUID("Id", UUID.randomUUID());

                skullOwnerCompound.getOrCreateCompound("Properties")
                        .getCompoundList("textures")
                        .addCompound()
                        .setString("Value", base64);
            });
        }

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(formatLore);

        if (!legacyItems) {
            NamespacedKey ECO_ITEM = new NamespacedKey(EcoLobby.instance, "ECO_ITEM");
            itemMeta.getPersistentDataContainer().set(ECO_ITEM, PersistentDataType.STRING, itemName);
        }
        else {
            NBT.modify(item, nbt -> {
                nbt.setString("ECO_ITEM", itemName);
            });
        }
        item.setItemMeta(itemMeta);

        return item;
    }

    public static boolean isEcoItem(ItemStack item, String itemName) {
        if (!legacyItems) {
            NamespacedKey ECO_ITEM = new NamespacedKey(EcoLobby.instance, "ECO_ITEM");
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if (container.has(ECO_ITEM, PersistentDataType.STRING))
                return Objects.equals(container.get(ECO_ITEM, PersistentDataType.STRING), itemName);
        }
        else {
            String tag = NBT.get(item, (Function<ReadableItemNBT, String>) nbt -> nbt.getString("ECO_ITEM"));
            return tag.equals(itemName);
        }
        return false;
    }

    public static void setItems(Player player) {
        ConfigurationSection itemsSection = items.getConfigurationSection("Items");
        if (itemsSection == null) return;

        for (String itemName : itemsSection.getKeys(false)) {
            int slot = itemsSection.getInt(itemName + ".slot");

            player.getInventory().setItem(slot, createItem(player, itemName));
        }
    }
    public static void giveItem(Player player, String itemName) {
        ConfigurationSection itemsSection = items.getConfigurationSection("Items");

        ItemStack item = createItem(player, itemName);
        if (item == null || itemsSection == null) {
            Chat.sendMessage(MESSAGES.ITEM_NOT_FOUND, player);
            return;
        }

        player.getInventory().addItem(item);
    }
}

