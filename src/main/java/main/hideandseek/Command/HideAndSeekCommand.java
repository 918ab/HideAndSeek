package main.hideandseek.Command;

import main.hideandseek.Static.ModelAnimationLoop;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class HideAndSeekCommand implements CommandExecutor {
    private final Map<Player, ModelAnimationLoop> playerTasks = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ModelEngineAnimation.printAnimationNames(player,"bakezori");
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("시작")) {

                if (playerTasks.containsKey(player) && playerTasks.get(player).isRunning()) {
                    return true;//이미 시작됨
                }
                ModelAnimationLoop modelEnginePlay = new ModelAnimationLoop(player);
                modelEnginePlay.startRepeating(new String[]{"walk", "idle","jump"});
                playerTasks.put(player, modelEnginePlay);
                ModelEngineAnimation.ModelPlayAuto(player,"slash",modelEnginePlay);
                //ModelEngineAnimation.ModelPlaySwitch(player,"deatha");
                //반복 시작
            } else if (args[0].equalsIgnoreCase("중지")) {
                //Player player = (Player) sender;
                if (playerTasks.containsKey(player)) {
                    ModelAnimationLoop modelEnginePlay = playerTasks.get(player);
                    modelEnginePlay.stopRepeating();
                    playerTasks.remove(player);
                    ModelEngineAnimation.ModelStop(player,"deatha");
                    ModelEngineAnimation.ModelPlaySwitch(player,"idle");
                    //반복 중단
                }//반복시작안함
            }
        }
        return false;
    }
}
