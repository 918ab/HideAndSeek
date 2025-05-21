package main.hideandseek.Event;

import com.ticxo.modelengine.api.animation.ModelState;
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
            Player player = event.getPlayer();
            AnimationHandler handler = ModelEngineAnimation.getHandler(player);
            if (handler != null){
                event.setCancelled(true);
            }else{
                return;
            }

            if (HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") != null
                    && (Boolean) HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") == true) {
                if(item.getItemMeta().getCustomModelData() == 4){
                    setCustomModelData(player, item);
                    player.getInventory().close();
                }
            } else {
                setCustomModelData(player, item);
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
            AnimationHandler handler = ModelEngineAnimation.getHandler(player);
            if (handler != null){
                event.setCancelled(true);
            }else{
                return;
            }

            if (HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") != null
                    && (Boolean) HideAndSeekStorage.get("[Player]"+player.getName()+",Animation") == true) {
                if(item.getItemMeta().getCustomModelData() == 4){
                    setCustomModelData(player, item);
                    player.getInventory().close();
                }
            } else {
                setCustomModelData(player, item);
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
        }else if(itemMeta.getCustomModelData() == 4){
            itemMeta.setCustomModelData(3);
        }else {
            return;
        }

        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) return;
        item.setItemMeta(itemMeta);
        String animation = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        if(itemMeta.getCustomModelData() == 2){
            //auto
            HideAndSeekStorage.put("[Player]"+player.getName()+",Animation",true);
            ModelEngineAnimation.Stuck(player);
            handler.playAnimation(animation, 0.3, 0.3, 1, false);
            String modelID = HideAndSeekStorage.get("[Player]"+player.getName()+",Model").toString();
            double count = Math.round((double) HideAndSeekStorage.get("[Animation]"+modelID+","+animation));
            long time = (long) count * 20;
            new BukkitRunnable() {
                @Override
                public void run() {
                    itemMeta.setCustomModelData(1);
                    item.setItemMeta(itemMeta);
                    ModelEngineAnimation.unStuck(player);
                    handler.stopAnimation(animation);
                    handler.playAnimation("idle", 0.3, 0.3, 1, false);
                    HideAndSeekStorage.remove("[Player]"+player.getName()+",Animation");
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), time);
        }if(itemMeta.getCustomModelData() == 4){
            //switch on
            handler.playAnimation(animation, 0.3, 0.3, 1, false);
            HideAndSeekStorage.put("[Player]"+player.getName()+",Animation", true);
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || !((Boolean) HideAndSeekStorage.get("[Player]"+player.getName()+",Animation"))) {
                        this.cancel();
                        return;
                    }
                    player.sendActionBar("§a애니메이션 실행중");
                }
            };
            runnable.runTaskTimer(Bukkit.getPluginManager().getPlugin("HideAndseek"), 0, 5);
            HideAndSeekStorage.put("[Player]"+player.getName()+",ActionbarRunnable", runnable);
        }if(itemMeta.getCustomModelData() == 3){
            //switch off
            ModelEngineAnimation.unStuck(player);
            handler.stopAnimation(animation);
            handler.playAnimation("idle", 0.3, 0.3, 1, false);
            HideAndSeekStorage.remove("[Player]"+player.getName()+",Animation");

            Object runnableObj = HideAndSeekStorage.get("[Player]"+player.getName()+",ActionbarRunnable");
            if (runnableObj instanceof BukkitRunnable) {
                ((BukkitRunnable) runnableObj).cancel();
            }
            player.sendActionBar(" ");
            HideAndSeekStorage.remove("[Player]"+player.getName()+",ActionbarRunnable");
        }
    }
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        HideAndSeekStorage.remove("[Player]"+event.getPlayer()+",Animation");
        HideAndSeekStorage.remove("[Player]"+event.getPlayer()+",Model");
    }
}