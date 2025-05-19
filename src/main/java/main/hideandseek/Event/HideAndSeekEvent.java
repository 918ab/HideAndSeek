package main.hideandseek.Event;

import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HideAndSeekEvent implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.IRON_NUGGET) {
            ModelEngineAnimation.ModelStop(event.getPlayer(),item.getItemMeta().getDisplayName());
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.IRON_NUGGET) {
            event.setCancelled(true);
            ModelEngineAnimation.ModelStop((Player) event.getWhoClicked(),item.getItemMeta().getDisplayName());
        }
    }

}