package main.hideandseek.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HideAndSeekCommandTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> candidates = new ArrayList<>();
        if(sender.isOp()) {
            if (args.length == 1) {
                String input = args[0].toLowerCase();
                if (label.equalsIgnoreCase("has") || label.equalsIgnoreCase("hideandseek")) {
                    candidates.add("reload");
                    candidates.add("info");
                    candidates.add("disguise");
                    candidates.add("setting");
                }else{
                    candidates.add("리로드");
                    candidates.add("정보");
                    candidates.add("변신");
                    candidates.add("설정");
                }
                //candidates.remove(sender.getName());
                for (String candidate : candidates) {
                    if (candidate.toLowerCase().startsWith(input)) {
                        completions.add(candidate);
                    }
                }
            }
        }
        return completions;
    }
}
