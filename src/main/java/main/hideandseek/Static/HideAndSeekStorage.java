package main.hideandseek.Static;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class HideAndSeekStorage {
    private static final String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";
    public static Map<String,Object> Storage = new HashMap<>();
    public static void put(String key, Object value){
        Storage.put(key,value);
    }
    public static Object get(String key){
        return Storage.get(key);
    }
    public static void remove(String key){
        Storage.remove(key);
    }


    public static void print(Player player, String modelId) {
        DataManager f = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
        List<String> list = f.getNames("HideAndSeek");
        if(list.contains(modelId)){
            player.sendMessage(pr+"Name : §a"+f.get("HideAndSeek."+modelId+"Name"));
            player.sendMessage(pr+"PlayerScale : §a"+f.get("HideAndSeek."+modelId+".PlayerScale"));
            player.sendMessage(pr+"ModelScale : §a"+f.get("HideAndSeek."+modelId+".ModelScale"));
            player.sendMessage(pr+"HitboxScale : §a"+f.get("HideAndSeek."+modelId+".HitboxScale"));
            player.sendMessage(pr+"PlayerHealth : §a"+f.get("HideAndSeek."+modelId+".PlayerHealth"));
            List<String> animations = f.getNames("HideAndSeek."+modelId+".Animation");
            if(animations !=null){
                for (String animation : animations) {
                    String mode = f.get("HideAndSeek." + modelId + ".Animation." + animation).toString();
                    String durationKey = "[Animation]" + modelId + "," + animation;
                    Object durationObj = HideAndSeekStorage.get(durationKey);
                    if (durationObj != null) {
                        player.sendMessage(pr + "§f" + animation + " : §a" + mode + " §7(" + durationObj + "초)");
                    } else {
                        player.sendMessage(pr + "§f" + animation + " : §a" + mode);
                    }
                }
            }
        }
    }
    public static void printInfo(Player player,String name) {
        for (String key : Storage.keySet()) {
            if (!key.contains(name)) continue;
            player.sendMessage(pr+"Key : "+key +", Value : "+ Storage.get(key));
        }

    }


    public static List<String> getList(String name) {
        List<String> list = new ArrayList<>();
        for (String key : Storage.keySet()) {
            if (key.contains(name)) {
                list.add(key);
            }
        }
        return list;
    }
}
