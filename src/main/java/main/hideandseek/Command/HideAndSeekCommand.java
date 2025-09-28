package main.hideandseek.Command;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import main.hideandseek.HideAndSeek;
import main.hideandseek.Static.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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
                }else{
                    player.sendMessage(pr+"모델 이름을 입력해주세요");
                }
                break;
            case "i":
                if(args.length >= 2){
                    HideAndSeekStorage.printInfo(player,args[1]);
                    return true;
                }
                break;
            case "리로드":
            case "reload":
                player.sendMessage(pr+"리로드 중");
                ModelEngineAnimation.ModelReload(player);
                break;
            case "설정":
            case "setting":
                DataManager f = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
                List<String> list = f.getNames("HideAndSeek");
                if(args.length >= 2){
                    if(list.contains(args[1])){
                        List<String> keyList = new ArrayList<>(Arrays.asList("PlayerScale", "PlayerHealth","ModelScale", "HitboxScale","Name"));
                        List<String> Animations = f.getNames("HideAndSeek."+args[1]+".Animation");
                        if (Animations != null) {
                            for(String Animation : Animations){
                                keyList.add("Animation."+Animation);
                            }
                        }
                        if(keyList.contains(args[2])){
                            String modelId = args[1];
                            String key = args[2];
                            if(key.equalsIgnoreCase("Name")) {
                                if(args.length >= 4){
                                    String value = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                                    String before = HideAndSeekStorage.get(modelId + "," + key).toString();
                                    f.set("HideAndSeek." + modelId + "." + key, value);
                                    f.saveConfig();
                                    HideAndSeekStorage.put(modelId + "," + key, value);
                                    player.sendMessage(pr + modelId + " " + key + " §c" + before +" §f-> §a"+value);
                                    for (Player p : Bukkit.getOnlinePlayers()){
                                        AnimationHandler handler = ModelEngineAnimation.getHandler(p);
                                        if (handler != null) {
                                            String model = ModelEngineAnimation.getCurrentModelName(p);
                                            if(model.equals(modelId)){
                                                ModelEngineAnimation.undisguisePlayer(p);
                                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                    p.sendMessage(pr+"값이 변경되었습니다");
                                                    ModelEngineAnimation.disguisePlayer(p, modelId);
                                                }, 5L);
                                            }
                                        }
                                    }
                                }else {
                                    player.sendMessage(pr + "이름을 입력해주세요");
                                }
                            }else{
                                if (args.length >= 4) {
                                    try {
                                        double value = Double.parseDouble(args[3]);
                                        String before = HideAndSeekStorage.get(modelId + "," + key).toString();
                                        f.set("HideAndSeek." + modelId + "." + key, value);
                                        f.saveConfig();
                                        HideAndSeekStorage.put(modelId + "," + key, value);
                                        player.sendMessage(pr + modelId + " " + key + " §c" + before +" §f-> §a"+value);
                                        for (Player p : Bukkit.getOnlinePlayers()){
                                            AnimationHandler handler = ModelEngineAnimation.getHandler(p);
                                            if (handler != null) {
                                                String model = ModelEngineAnimation.getCurrentModelName(p);
                                                if(model.equals(modelId)){
                                                    ModelEngineAnimation.undisguisePlayer(p);
                                                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                        p.sendMessage(pr+"값이 변경되었습니다");
                                                        ModelEngineAnimation.disguisePlayer(p, modelId);
                                                    }, 5L);
                                                }
                                            }
                                        }
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(pr+"숫자만 입력해주세요");
                                    }
                                } else {
                                    player.sendMessage(pr + "값을 입력해주세요");
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
                                    handler.playAnimation(args[2], 0.3, 0.3, 1, true);
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
                GuiInventory.openRandom(player,0,null);
                break;

            case "3":
                ModelEngineAPI api = ModelEngineAPI.getAPI();
                if (api == null) {
                    player.sendMessage("§cModelEngineAPI를 찾을 수 없습니다.");
                    return true;
                }

                ModeledEntity entity = api.getModelUpdaters().getModeledEntity(player.getUniqueId());
                if (entity == null) {
                    player.sendMessage("§c변신한 모델이 없습니다.");
                    return true;
                }

                ActiveModel activeModel = entity.getModels().values().stream().findFirst().orElse(null);
                if (activeModel == null) {
                    player.sendMessage("§c변신한 모델이 없습니다.");
                    return true;
                }

                // 모델의 모든 뼈대 이름(ID)을 가져옵니다.
                Set<String> boneNames = activeModel.getBones().keySet();

                player.sendMessage("§e[현재 모델의 뼈대 목록]");
                player.sendMessage("§f" + String.join(", ", boneNames));
                player.sendMessage("§7이 중에서 몸통 전체를 담당하는 뼈대 이름을 찾으세요.");
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
