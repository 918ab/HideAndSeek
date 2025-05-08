package main.hideandseek.Static;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class HideAndSeekStorage {
    private static final String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";
    private static Map<String,Object> Storage = new HashMap<>();
    public static void put(String key, Object value){
        Storage.put(key,value);
    }
    public static Object get(String key){
        return Storage.get(key);
    }
    public static void remove(String key){
        Storage.remove(key);
    }

    public static void printAll(Player player,Boolean tf) {
        if (Storage.isEmpty()){
            player.sendMessage(pr+"아직 초기화되지 않음");
            return;
        };
        Map<String, List<String>> grouped = new HashMap<>();
        for (String key : Storage.keySet()) {
            if (!key.startsWith("[ModelEngine]")) continue;
            String pureKey = key.substring("[ModelEngine]".length());
            String[] split = pureKey.split(",", 2);
            if (split.length < 2) continue;
            String modelId = split[0];
            String animation = split[1];
            grouped.computeIfAbsent(modelId, k -> new ArrayList<>()).add(animation);
        }

        for (String modelId : grouped.keySet()) {
            List<String> animations = grouped.get(modelId);
            if(tf == true) {
                player.sendMessage(pr + "[§a" + modelId + "§f] 애니메이션 목록 (§a" + animations.size() + "개§f):");
                String allAnimations = String.join(", ", animations);
                player.sendMessage(" §f- " + allAnimations);
            }else{
                player.sendMessage(pr + "[§a" + modelId + "§f] 애니메이션 목록 (§a" + animations.size() + "개§f)");
            }
        }
    }
}
