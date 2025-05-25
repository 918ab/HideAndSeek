package main.hideandseek.Command;

import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.Static.DataManager;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HideAndSeekCommandTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()) {
            List<String> completions = new ArrayList<>();
            List<String> candidates = new ArrayList<>();
            DataManager f = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");

            String input = args[args.length-1].toLowerCase();
            if (args.length == 1) {
                if (label.equalsIgnoreCase("has") || label.equalsIgnoreCase("hideandseek")) {
                    candidates.add("reload");
                    candidates.add("info");
                    candidates.add("disguise");
                    candidates.add("undisguise");
                    candidates.add("setting");
                    candidates.add("play");
                    candidates.add("stop");
                } else {
                    candidates.add("리로드");
                    candidates.add("정보");
                    candidates.add("변신");
                    candidates.add("변신풀기");
                    candidates.add("설정");
                    candidates.add("실행");
                    candidates.add("정지");
                }

            }
            if (args.length == 2) {
                if(args[0].equals("설정") || args[0].equals("setting") || args[0].equals("변신") || args[0].equals("disguise")) {
                    List<String> list = f.getNames("HideAndSeek");
                    candidates.addAll(list);
                }
                if(args[0].equals("변신풀기") || args[0].equals("undisguise") || args[0].equals("실행") || args[0].equals("play") || args[0].equals("정지") || args[0].equals("stop")) {
                    for(Player player : Bukkit.getOnlinePlayers()){
                        candidates.add(player.getName());
                    }
                }

            }
            if (args.length == 3) {
                if(args[0].equals("변신") || args[0].equals("disguise")) {
                    for(Player player : Bukkit.getOnlinePlayers()){
                        candidates.add(player.getName());
                    }
                }
                if(args[0].equals("실행") || args[0].equals("play") || args[0].equals("정지") || args[0].equals("stop")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null){
                        completions.add("["+args[1]+"] null");
                        return completions;
                    }
                    AnimationHandler handler = ModelEngineAnimation.getHandler(target);
                    if (handler == null){
                        completions.add("["+args[1]+"] 변신중아님");
                        return completions;
                    }else{
                        String model = HideAndSeekStorage.get("[Player]"+target.getName()+",Model").toString();
                        List<String> list = f.getNames("HideAndSeek."+model+".Animation");
                        candidates.addAll(list);
                    }
                }
                if(args[0].equals("설정") || args[0].equals("setting")) {
                    candidates.add("PlayerScale");
                    candidates.add("PlayerHealth");
                    candidates.add("ModelScale");
                    candidates.add("HitboxScale");
                    candidates.add("Name");
                }
            }
            if (args.length == 4) {
                if (args[0].equals("설정") || args[0].equals("setting")) {
                    if(args[2].equals("Name")){
                        candidates.add("(Name)");
                    }else if(args[2].equals("PlayerHealth")) {
                        candidates.add("(int)");
                    }else{
                        candidates.add("(double)");
                    }
                }
            }
            for (String candidate : candidates) {
                if (candidate.toLowerCase().contains(input)) {
                    completions.add(candidate);
                }
            }
            return completions;
        }
        return null;
    }
}
