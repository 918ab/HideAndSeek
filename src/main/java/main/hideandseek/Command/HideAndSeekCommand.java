package main.hideandseek.Command;

import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelAnimationLoop;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HideAndSeekCommand implements CommandExecutor {
    private final Map<Player, ModelAnimationLoop> playerTasks = new HashMap<>();
    public String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        Player player = (Player) sender;
        String arg = null;
        if (args.length == 0) {
            arg = "default";
        } else {
            arg = args[0];
        }
        switch (arg) {
            case "정보":
            case "info":
                HideAndSeekStorage.printAll(player,true);
                break;
            case "리로드":
            case "reload":
                ModelEngineAnimation.ModelReload();
                HideAndSeekStorage.printAll(player,false);
                break;
            default:
                if (label.equalsIgnoreCase("has") || label.equalsIgnoreCase("hideandseek")){
                    player.sendMessage(pr + "/hideandseek reload");
                    player.sendMessage(pr + "/hideandseek info");
                    player.sendMessage(pr + "/hideandseek disguise (ModelId) (Name)");
                    player.sendMessage(pr + "/hideandseek setting (ModelId) (Value)");
                }else{
                    player.sendMessage(pr + "/숨바꼭질 리로드");
                    player.sendMessage(pr + "/숨바꼭질 정보");
                    player.sendMessage(pr + "/숨바꼭질 변신 (모델ID) (닉네임)");
                    player.sendMessage(pr + "/숨바꼭질 설정 (모델ID) (값)");
                }
        }


//        if (args.length == 1) {
//            if (args[0].equalsIgnoreCase("시작")) {
//
//                if (playerTasks.containsKey(player) && playerTasks.get(player).isRunning()) {
//                    return true;//이미 시작됨
//                }
//                ModelAnimationLoop modelEnginePlay = new ModelAnimationLoop(player);
//                modelEnginePlay.startRepeating(new String[]{"walk", "idle","jump"});
//                playerTasks.put(player, modelEnginePlay);
//                ModelEngineAnimation.ModelPlayAuto(player,"slash",modelEnginePlay);
//                //ModelEngineAnimation.ModelPlaySwitch(player,"deatha");
//                //반복 시작
//            } else if (args[0].equalsIgnoreCase("중지")) {
//                //Player player = (Player) sender;
//                if (playerTasks.containsKey(player)) {
//                    ModelAnimationLoop modelEnginePlay = playerTasks.get(player);
//                    modelEnginePlay.stopRepeating();
//                    playerTasks.remove(player);
//                    ModelEngineAnimation.ModelStop(player,"deatha");
//                    ModelEngineAnimation.ModelPlaySwitch(player,"idle");
//                    //반복 중단
//                }//반복시작안함
//            }
//        }
        return false;
    }
}
