package main.hideandseek.Command;

import com.ticxo.modelengine.api.ModelEngineAPI;
import main.hideandseek.Static.DataManager;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelAnimationLoop;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class HideAndSeekCommand implements CommandExecutor {
    private final Map<Player, ModelAnimationLoop> playerTasks = new HashMap<>();
    public String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.isOp()) {
            return true;
        }
        Player player = (Player) sender;
        ModelEngineAnimation.ModelDisguise(player,"bakezori");
        String arg = null;
        if (args.length == 0) {
            arg = "default";
        } else {
            arg = args[0];
        }
        switch (arg) {
            case "정보":
            case "info":
                if(args.length >= 2){
                    HideAndSeekStorage.print(player,args[1]);
                    return true;
                }
                HideAndSeekStorage.printAll(player);
                break;
            case "리로드":
            case "reload":
                ModelEngineAnimation.ModelReload();
                player.sendMessage(pr+"리로드 완료");
                break;
            case "설정":
            case "setting":
                DataManager f = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
                List<String> list = f.getNames("HideAndSeek");
                if(args.length >= 2){
                    if(list.contains(args[1])){
                        List<String> keyList = new ArrayList<>(Arrays.asList("PlayerScale", "PlayerHealth","ModelScale", "Name"));
                        List<String> Animations = f.getNames("HideAndSeek."+args[1]+".Animation");
                        for(String Animation : Animations){
                            keyList.add("Animation."+Animation);
                        }
                        if(keyList.contains(args[2])){
                            String modelId = args[1];
                            String key = args[2];
                            if(args[2].contains("Animation.")){
                                if(args[3].equals("auto") || args[3].equals("switch") ){
                                    String value = args[3];
                                    player.sendMessage(pr+modelId+" / "+key+" / "+value +" §a설정완료");
                                    f.set("HideAndSeek."+modelId+"."+key,value);
                                    HideAndSeekStorage.put("",value);
                                }else{
                                    player.sendMessage(pr+"auto, switch 중에 입력해주세요");
                                }
                            }else if(args[2].equals("Name")) {
                                if(args.length >= 4){
                                    String value = args[3];
                                    player.sendMessage(pr+modelId+" / "+key+" / "+value +" §a설정완료");
                                    f.set("HideAndSeek."+modelId+"."+key,value);
                                }else {
                                    player.sendMessage(pr + "이름을 입력해주세요");
                                }
                            }else{
                                try {
                                    double value = Double.parseDouble(args[3]);
                                    player.sendMessage(pr+modelId+" / "+key+" / "+value +" §a설정완료");
                                    f.set("HideAndSeek."+modelId+"."+key,value);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(pr+"숫자만 입력해주세요");
                                }
                            }
                        }else{
                            player.sendMessage(pr+"잘못된 값");
                        }
                    }else {
                        player.sendMessage(pr + "모델 ID를 알 수 없습니다");
                    }
                }else{
                    player.sendMessage(pr+"모델 ID를 입력해주세요");
                }
                break;
            default:
                if (label.equalsIgnoreCase("has") || label.equalsIgnoreCase("hideandseek")){
                    player.sendMessage(pr + "/hideandseek reload");
                    player.sendMessage(pr + "/hideandseek info");
                    player.sendMessage(pr + "/hideandseek disguise (ModelId) (Name)");
                    player.sendMessage(pr + "/hideandseek setting (ModelId) (Key) (Value)");
                }else{
                    player.sendMessage(pr + "/숨바꼭질 리로드");
                    player.sendMessage(pr + "/숨바꼭질 정보");
                    player.sendMessage(pr + "/숨바꼭질 변신 (모델ID) (닉네임)");
                    player.sendMessage(pr + "/숨바꼭질 설정 (모델ID) (이름) (값)");
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
