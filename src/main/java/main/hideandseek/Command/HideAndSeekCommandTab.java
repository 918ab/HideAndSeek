package main.hideandseek.Command;

import main.hideandseek.Static.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
                    candidates.add("setting");
                } else {
                    candidates.add("리로드");
                    candidates.add("정보");
                    candidates.add("변신");
                    candidates.add("설정");
                }

            }
            if (args.length == 2) {
                if(args[0].equals("설정") || args[0].equals("setting")) {
                    List<String> list = f.getNames("HideAndSeek");
                    candidates.addAll(list);
                }
            }
            if (args.length == 3) {
                if(args[0].equals("설정") || args[0].equals("setting")) {
                    List<String> Animations = f.getNames("HideAndSeek."+args[1]+".Animation");
                    for(String Animation : Animations){
                        candidates.add("Animation."+Animation);
                    }
                    candidates.add("PlayerScale");
                    candidates.add("ModelScale");
                }
            }
            if (args.length == 4) {
                if (args[0].equals("설정") || args[0].equals("setting")) {
                    if(args[2].contains("Animation.")){
                        candidates.add("auto");
                        candidates.add("switch");
                    }else{
                        candidates.add("(int)");
                    }
                }
            }
            for (String candidate : candidates) {
                if (candidate.toLowerCase().startsWith(input)) {
                    completions.add(candidate);
                }
            }
            return completions;
        }
        return null;
    }
}
