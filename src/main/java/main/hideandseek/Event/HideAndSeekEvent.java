package main.hideandseek.Event;

import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HideAndSeekEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.IRON_NUGGET) return;
        ModelEngineAnimation.ModelStop(player,"flame");
    }
}