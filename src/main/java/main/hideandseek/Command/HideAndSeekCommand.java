package main.hideandseek.Command;

import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.HideAndSeek;
import main.hideandseek.Static.DataManager;
import main.hideandseek.Static.GuiInventory;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HideAndSeekCommand implements CommandExecutor {
    public static String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";
    public HideAndSeek plugin;
    public HideAndSeekCommand(HideAndSeek plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
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
                        List<String> keyList = new ArrayList<>(Arrays.asList("PlayerScale", "PlayerHealth","ModelScale", "HitboxScale","Name"));
                        List<String> Animations = f.getNames("HideAndSeek."+args[1]+".Animation");
                        for(String Animation : Animations){
                            keyList.add("Animation."+Animation);
                        }
                        if(keyList.contains(args[2])){
                            String modelId = args[1];
                            String key = args[2];
                            if(args[2].equals("Name")) {
                                if(args.length >= 4){
                                    String value = args[3];
                                    player.sendMessage(pr+modelId+" / "+key+" / "+value +" §a설정완료");
                                    f.set("HideAndSeek."+modelId+"."+key,value);
                                    ModelEngineAnimation.ModelReload();
                                }else {
                                    player.sendMessage(pr + "이름을 입력해주세요");
                                }
                            }else{
                                try {
                                    double value = Double.parseDouble(args[3]);
                                    player.sendMessage(pr+modelId+" / "+key+" / "+value +" §a설정완료");
                                    f.set("HideAndSeek."+modelId+"."+key,value);
                                    ModelEngineAnimation.ModelReload();
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
            case "disguise":
            case "변신":
                DataManager file = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
                if(args.length >= 2){
                    List<String> listfile = file.getNames("HideAndSeek");
                    if(listfile.contains(args[1])){
                        if(args.length >= 3) {
                            Player target = Bukkit.getPlayer(args[2]);
                            if (target != null) {
                                AnimationHandler handler = ModelEngineAnimation.getHandler(target);
                                if (handler != null){
                                    ModelEngineAnimation.undisguisePlayer(target);
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        ModelEngineAnimation.disguisePlayer(target, args[1]);
                                    }, 5L);
                                }else{
                                    ModelEngineAnimation.disguisePlayer(target, args[1]);
                                }
                            } else {
                                player.sendMessage(pr + "플레이어를 찾을 수 없습니다");
                            }
                        }else {
                            AnimationHandler handler = ModelEngineAnimation.getHandler(player);
                            if (handler != null){
                                ModelEngineAnimation.undisguisePlayer(player);
                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    ModelEngineAnimation.disguisePlayer(player, args[1]);
                                }, 5L);
                            }else{
                                ModelEngineAnimation.disguisePlayer(player, args[1]);
                            }

                        }
                    }else{
                        player.sendMessage(pr + "모델 ID를 알 수 없습니다");
                    }
                }else{
                    player.sendMessage(pr+"모델 ID를 입력해주세요");
                }
                break;
            case "변신풀기":
            case "undisguise":
                if(args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        ModelEngineAnimation.undisguisePlayer(target);
                    } else {
                        player.sendMessage(pr + "플레이어를 찾을 수 없습니다");
                    }
                }else {
                    ModelEngineAnimation.undisguisePlayer(player);
                }
                break;
            case "play":
            case "실행":
                if(args.length >= 2) {
                    if(args.length >= 3) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            AnimationHandler handler = ModelEngineAnimation.getHandler(target);
                            DataManager fa = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
                            if (handler == null) {
                                player.sendMessage(pr + target.getName() + " 변신중 아님");
                            } else {
                                String model = ModelEngineAnimation.getCurrentModelName(target);
                                List<String> Animation = fa.getNames("HideAndSeek." + model + ".Animation");
                                if (Animation.contains(args[2])) {
                                    handler.playAnimation(args[2], 0.3, 0.3, 1, false);
                                } else {
                                    player.sendMessage(pr + "애니메이션 찾을 수 없음");
                                }
                            }
                        } else {
                            player.sendMessage(pr + "플레이어를 찾을 수 없습니다");
                        }
                    }else{
                        player.sendMessage(pr+"애니메이션 이름을 입력해주세요");
                    }
                }else {
                    player.sendMessage(pr + "닉네임을 입력해주세요");
                }
                break;
            case "stop":
            case "정지":
                if(args.length >= 2) {
                    if(args.length >= 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            AnimationHandler handler = ModelEngineAnimation.getHandler(target);
                            DataManager fa = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
                            if (handler == null) {
                                player.sendMessage(pr + target.getName() + " 변신중 아님");
                            } else {
                                String model = ModelEngineAnimation.getCurrentModelName(target);
                                List<String> Animation = fa.getNames("HideAndSeek." + model + ".Animation");
                                if (Animation.contains(args[2])) {
                                    handler.stopAnimation(args[2]);
                                } else {
                                    player.sendMessage(pr + "애니메이션 찾을 수 없음");
                                }
                            }
                        } else {
                            player.sendMessage(pr + "플레이어를 찾을 수 없습니다");
                        }
                    }else{
                        player.sendMessage(pr+"애니메이션 이름을 입력해주세요");
                    }
                }else {
                    player.sendMessage(pr + "닉네임을 입력해주세요");
                }
                break;
            case "random":
            case "랜덤":
                GuiInventory.openRandom(player);
                break;
            default:
                if (label.equalsIgnoreCase("has") || label.equalsIgnoreCase("hideandseek")){
                    player.sendMessage(pr + "/hideandseek reload");
                    player.sendMessage(pr + "/hideandseek info");
                    player.sendMessage(pr + "/hideandseek random");
                    player.sendMessage(pr + "/hideandseek disguise (ModelId) (Player)");
                    player.sendMessage(pr + "/hideandseek undisguise (Player)");
                    player.sendMessage(pr + "/hideandseek play (Player) (AnimationName)");
                    player.sendMessage(pr + "/hideandseek stop (Player) (AnimationName)");
                    player.sendMessage(pr + "/hideandseek setting (ModelId) (Key) (Value)");
                }else{
                    player.sendMessage(pr + "/숨바꼭질 리로드");
                    player.sendMessage(pr + "/숨바꼭질 정보");
                    player.sendMessage(pr + "/숨바꼭질 랜덤");
                    player.sendMessage(pr + "/숨바꼭질 변신 (모델ID) (닉네임)");
                    player.sendMessage(pr + "/숨바꼭질 변신풀기 (닉네임)");
                    player.sendMessage(pr + "/숨바꼭질 실행 (닉네임) (애니메이션이름)");
                    player.sendMessage(pr + "/숨바꼭질 정지 (닉네임) (애니메이션이름)");
                    player.sendMessage(pr + "/숨바꼭질 설정 (모델ID) (이름) (값)");
                }
        }

        return false;
    }

}
