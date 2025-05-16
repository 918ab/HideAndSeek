package main.hideandseek.Static;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.AnimationPropertyRegistry;
import com.ticxo.modelengine.api.animation.ModelState;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.animation.property.SimpleProperty;
import com.ticxo.modelengine.api.entity.Dummy;
import com.ticxo.modelengine.api.generator.blueprint.ModelBlueprint;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.util.List;
public class ModelEngineAnimation {

    private static final String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";

    private static AnimationHandler getHandler(Player player) {
        ModelEngineAPI api = ModelEngineAPI.getAPI();
        if (api == null) {
            Bukkit.broadcastMessage(pr + "ModelEngineAPI 연결 실패");
            return null;
        }

        ModeledEntity entity = api.getModelUpdaters().getModeledEntity(player.getUniqueId());
        if (entity == null) {
            player.sendMessage(pr + "적용된 모델 없음");
            return null;
        }

        ActiveModel model = entity.getModels().values().stream().findFirst().orElse(null);
        if (model == null) {
            player.sendMessage(pr + "적용된 모델 없음");
            return null;
        }

        return model.getAnimationHandler();
    }

    public static void ModelPlayAuto(Player player, String animation, ModelAnimationLoop loop) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;

        handler.playAnimation(animation, 0.3, 0.3, 1, false);
        player.sendMessage("play / " + animation);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (loop != null && loop.isRunning()) {
                    loop.stopRepeating();
                    handler.playAnimation("idle", 0.3, 0.3, 1, false);
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), 40);
    }

    public static void ModelPlaySwitch(Player player, String animation) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;

        handler.playAnimation(animation, 0.3, 0.3, 1, false);
        player.sendMessage("play / " + animation);
    }

    public static void ModelDisguise(Player player, String modelId){

        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(player);
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(modelId);
        modeledEntity.addModel(activeModel, false);
        modeledEntity.setBaseEntityVisible(false);
    }

    public static void ModelStop(Player player, String animation) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.IDLE, "noop", 0.2, 0.2, 1.0));
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.WALK, "noop", 0.2, 0.2, 1.0));
        handler.setDefaultProperty(new AnimationHandler.DefaultProperty(ModelState.JUMP, "noop", 0.2, 0.2, 1.0));
        //handler.setDefaultProperty();
        //handler.stopAnimation(animation);

        handler.playAnimation(animation, 0.3, 0.3, 1, false);
        player.sendMessage("stop / " + animation);
    }
    public static void ModelReload(){

        DataManager f = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
        File dic = new File("plugins/ModelEngine/blueprints/");
        if(dic.exists() && dic.isDirectory()) {
            File[] files = dic.listFiles();
            if (files != null) {
                for (File file : dic.listFiles()) {
                    String modelId = file.getName().substring(0, file.getName().length() - ".bbmodel".length());
                    if(f.get("HideAndSeek."+modelId) == null){
                        Bukkit.getLogger().info("[HideAndSeek] "+modelId+" 자동생성");
                        f.set("HideAndSeek."+modelId+".Name","설정되지않음");
                        f.set("HideAndSeek."+modelId+".PlayerScale",1);
                        f.set("HideAndSeek."+modelId+".ModelScale",1);
                        f.set("HideAndSeek."+modelId+".PlayerHealth",20);
                        HideAndSeekStorage.put("HideAndSeek."+modelId+".ModelScale",1);
                        HideAndSeekStorage.put("HideAndSeek."+modelId+".PlayerScale",1);
                        HideAndSeekStorage.put("HideAndSeek."+modelId+".PlayerHealth",20);
                    }else{
                        HideAndSeekStorage.put(modelId+",PlayerScale",f.get("HideAndSeek."+modelId+".PlayerScale"));
                        HideAndSeekStorage.put(modelId+",ModelScale",f.get("HideAndSeek."+modelId+".ModelScale"));
                        HideAndSeekStorage.put(modelId+",PlayerHealth",f.get("HideAndSeek."+modelId+".PlayerHealth"));
                        if(!f.get("HideAndSeek."+modelId+".Name").equals("설정되지않음")) {
                            HideAndSeekStorage.put(modelId + ",Name", f.get("HideAndSeek." + modelId + ".Name"));
                        }
                    }
                    printAnimationNames(modelId);
                    if(f.get("HideAndSeek."+modelId) != null){
                        List<String> Animations = f.getNames("HideAndSeek."+modelId+".Animation");
                        for(String Animation : Animations){
                            HideAndSeekStorage.put("[ModelEngine]"+modelId+","+Animation,f.get("HideAndSeek."+modelId+".Animation."+Animation));
                        }
                    }
                }
            } else {
                Bukkit.broadcastMessage("§c디렉토리에 파일이 없습니다");
            }
        }


    }

    public static void printAnimationNames(String modelId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                DataManager f = new DataManager(Bukkit.getPluginManager().getPlugin("HideAndseek"), "Data.yml");
                try (FileReader reader = new FileReader("plugins/ModelEngine/blueprints/" + modelId+".bbmodel")) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonElement animationsElement = json.get("animations");
                    if (animationsElement == null) {
                        Bukkit.broadcastMessage("§canimations 존재하지 않음("+modelId+")");
                        return;
                    }
                    if (animationsElement.isJsonArray()) {
                        JsonArray animations = animationsElement.getAsJsonArray();
                        if (animations.size() == 0) {
                            Bukkit.broadcastMessage("§c애니메이션 존재하지 않음("+modelId+")");
                            return;
                        }
                        for (JsonElement animationElement : animations) {
                            JsonObject animationObj = animationElement.getAsJsonObject();
                            if (animationObj.has("name") && animationObj.has("length")) {
                                HideAndSeekStorage.put("[Animation]"+modelId+","+animationObj.get("name").getAsString(),animationObj.get("length").getAsDouble());
                                String animation = animationObj.get("name").getAsString();
                                if(f.get("HideAndSeek."+modelId+".Animation."+animation) == null){
                                    f.set("HideAndSeek."+modelId+".Animation."+animation,"auto");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Bukkit.broadcastMessage("§c모델 불러오는 중 오류("+modelId+") : " + e.getMessage());
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), 40);
    }

}

