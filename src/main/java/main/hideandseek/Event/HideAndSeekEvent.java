package main.hideandseek.Event;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class HideAndSeekEvent implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.IRON_NUGGET) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            if(HideAndSeekStorage.get("[Player]"+ player.getName()+",Animation") == null
                    ||(Boolean) HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") != true){
                setCustomModelData(player,item);
                player.getInventory().close();
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.IRON_NUGGET) {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if(HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") == null
                    ||(Boolean) HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") != true){
                setCustomModelData(player,item);
                player.getInventory().close();
            }
        }
    }

    public void setCustomModelData(Player player,ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.getCustomModelData() == 1){
            itemMeta.setCustomModelData(2);
        }else if(itemMeta.getCustomModelData() == 3){
            itemMeta.setCustomModelData(4);
        }else{
            return;
        }
        item.setItemMeta(itemMeta);
        HideAndSeekStorage.put("[Player]"+player.getName()+",Animation",true);
        String animation = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) return;
        if(itemMeta.getCustomModelData() == 2){
            //auto
            ModelEngineAnimation.Stuck(player);
            handler.playAnimation(animation, 0.3, 0.3, 1, false);
            String modelID = HideAndSeekStorage.get("[Player]"+player.getName()+",Model").toString();
            double count = Math.round((double) HideAndSeekStorage.get("[Animation]"+modelID+","+animation));
            long time = (long) count * 20 + 3;
            new BukkitRunnable() {
                @Override
                public void run() {
                    itemMeta.setCustomModelData(1);
                    item.setItemMeta(itemMeta);
                    ModelEngineAnimation.unStuck(player);
                    handler.stopAnimation(animation);
                    HideAndSeekStorage.remove("[Player]"+player.getName()+",Animation");
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), time);
        }else{
            //switch

        }
    }


    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        HideAndSeekStorage.remove("[Player]"+event.getPlayer()+",Animation");
        HideAndSeekStorage.remove("[Player]"+event.getPlayer()+",Model");
    }
}