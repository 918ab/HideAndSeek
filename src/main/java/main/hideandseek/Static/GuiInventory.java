package main.hideandseek.Static;

import main.hideandseek.Command.HideAndSeekCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiInventory {

    public static void openRandom(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, HideAndSeekCommand.pr + "Random");

        for (int i = 45; i < 54; i++) {
            ItemStack item = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .setDisplayName(" ")
                    .build();
            inv.setItem(i, item);
        }

        ItemStack item = new ItemBuilder(Material.IRON_INGOT)
                .setDisplayName(HideAndSeekCommand.pr + "Random("+HideAndSeekStorage.get("Random")+")")
                .build();
        inv.setItem(49, item);

        Set<String> modelIds = new HashSet<>();
        for (String key : HideAndSeekStorage.Storage.keySet()) {
            if (!key.startsWith("[Animation]")) continue;
            String pureKey = key.substring("[Animation]".length());
            String[] split = pureKey.split(",", 2);
            if (split.length < 1) continue;
            String modelId = split[0];
            modelIds.add(modelId);
        }

        int slot = 0;
        for (String modelId : modelIds) {
            item = new ItemBuilder(Material.RED_CONCRETE)
                    .setDisplayName("§f" + modelId)
                    .build();
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }
}
