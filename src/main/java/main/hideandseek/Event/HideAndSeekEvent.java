package main.hideandseek.Event;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.ModelBone;
import main.hideandseek.Command.HideAndSeekCommand;
import main.hideandseek.HideAndSeek;
import main.hideandseek.Static.AnimationController;
import main.hideandseek.Static.GuiInventory;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class HideAndSeekEvent implements Listener {
    public HideAndSeek plugin;
    public HideAndSeekEvent(HideAndSeek plugin){
        this.plugin = plugin;
    }
    private final Map<String, Boolean> invisibleState = new HashMap<>();
    private final Map<String, BukkitRunnable> actionBarRunnables = new HashMap<>();
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.getType() == Material.IRON_INGOT) {
                ItemMeta meta = item.getItemMeta();
                if(!meta.getDisplayName().contains(HideAndSeekCommand.pr+"Random")){
                    return;
                }
                event.setCancelled(true);
                String name = meta.getDisplayName();
                name = name.replace(HideAndSeekCommand.pr+"Random(","");
                name = name.replace(")","");
                if (meta == null || !meta.hasLore()) return;
                List<String> lore = meta.getLore();
                if (lore.isEmpty()) return;

                Random random = new Random();
                String randomLine = ChatColor.stripColor(lore.get(random.nextInt(lore.size())));
                EntityType entityType;
                try {
                    entityType = EntityType.valueOf(name);
                    HideAndSeekStorage.put("Random",entityType);
                } catch (IllegalArgumentException e) {
                    event.getPlayer().sendMessage("§c잘못된 이름 : "+name);
                    return;
                }
                ModelEngineAnimation.spawnModeledEntity(event.getPlayer(),randomLine,entityType);
                event.getPlayer().sendActionBar(HideAndSeekCommand.pr+"Type : "+name +" / Model : "+ randomLine);

            }
        }
        if (item != null && item.getType() == Material.IRON_NUGGET) {
            Player player = event.getPlayer();
            AnimationHandler handler = ModelEngineAnimation.getHandler(player);
            if (handler != null){
                if(event.getAction().toString().equals("HOTBAR_SWAP")){
                    return;
                }
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
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        AnimationHandler handler = ModelEngineAnimation.getHandler(event.getPlayer());
        if (handler != null) {
            event.setCancelled(true);
            if (event.getPlayer().isSneaking()) {
                GuiInventory.openSetting(event.getPlayer());
            }
        }

    }
    public void updateActionBar(Player player) {
        boolean invisible = HideAndSeekStorage.get("[Player]" + player.getName() + ",Invisible") != null
                && (Boolean) HideAndSeekStorage.get("[Player]" + player.getName() + ",Invisible");
        boolean animating = HideAndSeekStorage.get("[Player]" + player.getName() + ",Animation") != null
                && (Boolean) HideAndSeekStorage.get("[Player]" + player.getName() + ",Animation");
        boolean prevInvisible = invisibleState.getOrDefault(player.getName(), false);
        invisibleState.put(player.getName(), invisible);
        BukkitRunnable runnable = actionBarRunnables.get(player.getName());
        if (invisible || animating) {
            if (runnable == null) {
                runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            this.cancel();
                            actionBarRunnables.remove(player.getName());
                            player.sendActionBar(" ");
                            return;
                        }
                        boolean currentInvisible = HideAndSeekStorage.get("[Player]" + player.getName() + ",Invisible") != null
                                && (Boolean) HideAndSeekStorage.get("[Player]" + player.getName() + ",Invisible");
                        boolean currentAnimating = HideAndSeekStorage.get("[Player]" + player.getName() + ",Animation") != null
                                && (Boolean) HideAndSeekStorage.get("[Player]" + player.getName() + ",Animation");
                        if (!currentInvisible && !currentAnimating) {
                            this.cancel();
                            actionBarRunnables.remove(player.getName());
                            player.sendActionBar(" ");
                            return;
                        }

                        StringBuilder msg = new StringBuilder();
                        if (currentInvisible) {
                            msg.append("§7보이지않음");
                        }
                        if (currentAnimating) {
                            if (msg.length() > 0) {
                                msg.append(" §f/ ");
                            }
                            msg.append("§a애니메이션 실행중");
                        }
                        player.sendActionBar(msg.toString());
                    }
                };
                runnable.runTaskTimer(plugin, 0L, 5L);
                actionBarRunnables.put(player.getName(), runnable);
            }
        } else {
            if (runnable != null) {
                runnable.cancel();
                actionBarRunnables.remove(player.getName());
                player.sendActionBar(" ");
            }
        }
        if (invisible != prevInvisible) {
            if (invisible) {
                ModelEngineAnimation.invisible(player);
            } else {
                ModelEngineAnimation.uninvisible(player);
            }
        }
    }
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event){
        AnimationHandler handler = ModelEngineAnimation.getHandler(event.getPlayer());
        if (handler != null){
            if(event.getItemDrop().getItemStack().getType() == Material.IRON_NUGGET) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryClick2(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(HideAndSeekCommand.pr+"Setting")) {
            return;
        }
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int clickedSlot = event.getSlot();
        if (clickedSlot == 11 || clickedSlot == 13 || clickedSlot == 15) {
            GuiInventory.toggleSetting(player.getUniqueId(), clickedSlot);

            boolean isEnabled = GuiInventory.getSetting(player.getUniqueId(), clickedSlot);
            if (clickedSlot == 11) {
                if (isEnabled) {
                    HideAndSeekStorage.put("[Player]" +player.getName() + ",Invisible", true);
                } else {
                    HideAndSeekStorage.remove("[Player]" + player.getName() + ",Invisible");
                }
                updateActionBar(player);
            }

            if (clickedSlot == 13) {
                if (isEnabled) {
                    AnimationController.disableAutomaticAnimations(player);
                    player.sendMessage(HideAndSeekCommand.pr+"애니메이션을 중지합니다");
                } else {
                    AnimationController.enableAutomaticAnimations(player);
                    player.sendMessage(HideAndSeekCommand.pr+"애니메이션을 시작합니다");
                }
            }

//            if (clickedSlot == 15) {
//                if (isEnabled) {
//                    player.sendMessage("15번 슬롯 킴");
//                } else {
//                    player.sendMessage("15번 슬롯 끔");
//                }
//            }

            GuiInventory.openSetting(player);
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        String title = event.getView().getTitle();
        if (!title.startsWith(HideAndSeekCommand.pr + "Random")) return;

        event.setCancelled(true);

        int page = 0;
        try {
            page = Integer.parseInt(title.replace(HideAndSeekCommand.pr + "Random", "")
                    .replace("[", "").replace("]", "").trim());
        } catch (Exception ignored) {}

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        Inventory inv = event.getInventory();
        ItemStack ironIngot = inv.getItem(49);

        if (event.getSlot() == 45 && clickedItem.getType() == Material.ARROW) {
            GuiInventory.openRandom(player, page - 1, ironIngot);
            return;
        }
        if (event.getSlot() == 53 && clickedItem.getType() == Material.ARROW) {
            GuiInventory.openRandom(player, page + 1, ironIngot);
            return;
        }

        if (event.getSlot() == 49 && clickedItem.getType() == Material.IRON_INGOT) {
            player.getInventory().addItem(clickedItem);
            player.closeInventory();
            return;
        }

        Material clickedType = clickedItem.getType();
        if (ironIngot == null) return;

        if (clickedType == Material.RED_CONCRETE || clickedType == Material.GREEN_CONCRETE) {
            ItemMeta ironMeta = ironIngot.getItemMeta();
            List<String> lore = ironMeta.hasLore() ? new ArrayList<>(ironMeta.getLore()) : new ArrayList<>();
            String clickedName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            if (lore.contains("§f" + clickedName)) {
                lore.remove("§f" + clickedName);
                clickedItem.setType(Material.RED_CONCRETE);
            } else {
                lore.add("§f" + clickedName);
                clickedItem.setType(Material.GREEN_CONCRETE);
            }
            ironMeta.setLore(lore);
            ironIngot.setItemMeta(ironMeta);
            inv.setItem(49, ironIngot);
            inv.setItem(event.getSlot(), clickedItem);
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
                if(event.getAction().toString().equals("HOTBAR_SWAP")){
                    return;
                }
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
            AnimationController.disableAutomaticAnimations(player);
            handler.playAnimation(animation, 0.3, 0.3, 1, true);
            String modelID = ModelEngineAnimation.getCurrentModelName(player);
            double count = Math.round((double) HideAndSeekStorage.get("[Animation]"+modelID+","+animation));
            long time = (long) count * 20;
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack currentItem = null;
                    for (ItemStack invItem : player.getInventory().getContents()) {
                        if (invItem != null && invItem.hasItemMeta()) {
                            if (ChatColor.stripColor(invItem.getItemMeta().getDisplayName())
                                    .equals(animation)) {
                                currentItem = invItem;
                                break;
                            }
                        }
                    }

                    if (currentItem != null) {
                        ItemMeta meta = currentItem.getItemMeta();
                        meta.setCustomModelData(1);
                        currentItem.setItemMeta(meta);
                    }
                    boolean isEnabled = GuiInventory.getSetting(player.getUniqueId(), 13);
                    if(!isEnabled){
                        AnimationController.enableAutomaticAnimations(player);
                    }
                    handler.stopAnimation(animation);
                    HideAndSeekStorage.remove("[Player]"+player.getName()+",Animation");
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), time);
        }if(itemMeta.getCustomModelData() == 4){
            //switch on
            AnimationController.disableAutomaticAnimations(player);
            handler.playAnimation(animation, 0.3, 0.3, 1, true);
            HideAndSeekStorage.put("[Player]"+player.getName()+",Animation", true);
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || !((Boolean) HideAndSeekStorage.get("[Player]" + player.getName() + ",Animation"))) {
                        this.cancel();
                        return;
                    }
                    updateActionBar(player);
                }
            };
            runnable.runTaskTimer(Bukkit.getPluginManager().getPlugin("HideAndseek"), 0, 5);
            HideAndSeekStorage.put("[Player]"+player.getName()+",ActionbarRunnable", runnable);
        }if(itemMeta.getCustomModelData() == 3){
            //switch off
            boolean isEnabled = GuiInventory.getSetting(player.getUniqueId(), 13);
            if(!isEnabled){
                AnimationController.enableAutomaticAnimations(player);
            }
            handler.stopAnimation(animation);
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
        invisibleState.remove(event.getPlayer().getName());
        HideAndSeekStorage.remove("[Player]"+event.getPlayer().getName()+",Animation");
        HideAndSeekStorage.remove("[Player]"+event.getPlayer().getName() + ",Invisible");
        HideAndSeekStorage.remove("[Player]"+event.getPlayer().getName()+",ActionbarRunnable");
        ModelEngineAnimation.undisguisePlayer(event.getPlayer());
        GuiInventory.resetSettings(event.getPlayer().getUniqueId());
    }
}