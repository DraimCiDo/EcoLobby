package me.baraban4ik.ecolobby.item;

import me.baraban4ik.ecolobby.configurations.Configurations;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.baraban4ik.ecolobby.utils.Chat.format;

public class ItemManager {
    public static ItemStack joinItem;

    public static void init(Configurations configurations, Player player) {
        FileConfiguration config = configurations.get("config.yml");

        createJoin(config.getString("Items.join-item.material"), config.getString("Items.join-item.name"),
                config.getStringList("Items.join-item.lore"), player);
    }

    private static void createJoin(String material, String name, List<String> lore, Player player) {
        if (material == null) return;

        ItemStack item = new ItemStack(Material.getMaterial(material), 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(format(name, player));

        List<String> formatLore = new ArrayList<>();
        for (String s : lore) {
            formatLore.add(format(s, player));
        }
        meta.setLore(formatLore);

        item.setItemMeta(meta);
        joinItem = item;
    }
}
