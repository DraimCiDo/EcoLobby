package me.baraban4ik.ecolobby.managers;

import me.baraban4ik.ecolobby.utils.Format;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabManager {

    private List<String> header = new ArrayList<>();
    private List<String> footer = new ArrayList<>();

    public void update(Player player) {
        player.setPlayerListHeader(convert(header, player));
        player.setPlayerListFooter(convert(footer, player));
    }


    private String convert(List<String> list, Player player) {
        String convertString = String.join("\n", list);
        return Format.format(convertString, player);
    }

    public void setHeader(List<String> list) {
        header = list;
    }

    public void setFooter(List<String> list) {
        footer = list;
    }


}
