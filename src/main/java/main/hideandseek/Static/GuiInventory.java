package main.hideandseek.Static;

import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.Command.HideAndSeekCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GuiInventory {

    public static void openRandom(Player player, int page, @Nullable ItemStack iron) {
        Inventory inv = Bukkit.createInventory(null, 54, HideAndSeekCommand.pr + "Random [" + page + "]");

        for (int i = 45; i < 54; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build());
        }

        if (iron == null || iron.getType() != Material.IRON_INGOT) {
            iron = new ItemBuilder(Material.IRON_INGOT)
                    .setDisplayName(HideAndSeekCommand.pr + "Random(" + HideAndSeekStorage.get("Random") + ")")
                    .build();
        }
        inv.setItem(49, iron);

        Set<String> modelIds = new HashSet<>();
        for (String key : HideAndSeekStorage.Storage.keySet()) {
            if (!key.startsWith("[Animation]")) continue;
            String pureKey = key.substring("[Animation]".length());
            String[] split = pureKey.split(",", 2);
            if (split.length < 1) continue;
            modelIds.add(split[0]);
        }
        List<String> modelList = new ArrayList<>(modelIds);
        Collections.sort(modelList);
        int start = page * 45;
        int end = Math.min(start + 45, modelList.size());

        for (int i = start; i < end; i++) {
            String modelId = modelList.get(i);
            ItemStack item = new ItemBuilder(Material.RED_CONCRETE)
                    .setDisplayName("§f" + modelId)
                    .build();

            if (iron.hasItemMeta() && iron.getItemMeta().hasLore()) {
                List<String> selected = iron.getItemMeta().getLore();
                if (selected.contains("§f" + modelId)) {
                    item.setType(Material.GREEN_CONCRETE);
                }
            }
            inv.setItem(i - start, item);
        }
        if (page > 0) {
            inv.setItem(45, new ItemBuilder(Material.ARROW).setDisplayName("§e이전 페이지").build());
        }
        if (end < modelList.size()) {
            inv.setItem(53, new ItemBuilder(Material.ARROW).setDisplayName("§e다음 페이지").build());
        }

        player.openInventory(inv);
    }
    public static final Map<UUID, Map<Integer, Boolean>> playerSettings = new HashMap<>();

    public static boolean getSetting(UUID uuid, int slot) {
        return playerSettings.getOrDefault(uuid, Collections.emptyMap()).getOrDefault(slot, false);
    }
    public static void toggleSetting(UUID uuid, int slot) {
        Map<Integer, Boolean> settings = playerSettings.computeIfAbsent(uuid, k -> new HashMap<>());
        settings.put(slot, !getSetting(uuid, slot));
    }
    public static void resetSettings(UUID uuid) {
        playerSettings.remove(uuid);
    }


    public static void openSetting(Player player) {
        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) {
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, HideAndSeekCommand.pr + "Setting");

        boolean setting1_status = getSetting(player.getUniqueId(), 11);
        boolean setting2_status = getSetting(player.getUniqueId(), 13);
        //boolean setting3_status = getSetting(player.getUniqueId(), 15);

        inv.setItem(11, createToggleButton("§aModel Hide", "§7활성화시 자신에게만 모델이 보이지 않습니다",setting1_status));
        inv.setItem(13, createToggleButton("§bAnimation", "§7활성화시 자동 애니메이션을 중지합니다",setting2_status));
        //inv.setItem(15, createToggleButton("§c세 번째 설정", "3",setting3_status));

        player.openInventory(inv);
    }

    private static ItemStack createToggleButton(String name, String lore, boolean enabled) {
        Material type = enabled ? Material.GREEN_CONCRETE : Material.RED_CONCRETE;
        String status = enabled ? "§a활성화" : "§7비활성화";

        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore,"§f현재 상태: " + status));
            item.setItemMeta(meta);
        }
        return item;
    }
}
