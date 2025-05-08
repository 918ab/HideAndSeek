package main.hideandseek.Static;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileReader;
import java.util.Map;
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

    public static void ModelStop(Player player, String animation) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;

        handler.stopAnimation(animation);
        player.sendMessage("stop / " + animation);
    }
    //gitignore

    public static void printAnimationNames(Player player, String modelId) {
        try (FileReader reader = new FileReader("plugins/ModelEngine/blueprints/" + modelId + ".bbmodel")) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonElement animationsElement = json.get("animations");

            if (animationsElement == null) {
                player.sendMessage("§c[" + modelId + "] 모델에 animations 필드가 없습니다.");
                return;
            }

            if (animationsElement.isJsonObject()) {
                JsonObject animations = animationsElement.getAsJsonObject();
                if (animations.size() == 0) {
                    player.sendMessage("§e[" + modelId + "] 모델에 애니메이션이 없습니다.");
                    return;
                }
                player.sendMessage("§a[" + modelId + "] 모델 애니메이션 목록:");
                for (Map.Entry<String, JsonElement> entry : animations.entrySet()) {
                    player.sendMessage(" §f- " + entry.getKey());
                }

            } else if (animationsElement.isJsonArray()) {
                JsonArray animations = animationsElement.getAsJsonArray();
                if (animations.size() == 0) {
                    player.sendMessage("§e[" + modelId + "] 모델에 애니메이션이 없습니다.");
                    return;
                }
                player.sendMessage("§a[" + modelId + "] 모델 애니메이션 목록:");
                for (JsonElement animationElement : animations) {

                    JsonObject animationObj = animationElement.getAsJsonObject();
                    if (animationObj.has("name")) {
                        player.sendMessage(" §f- " + animationObj.get("name").getAsString());
                    }
                    if(animationObj.has("length")){
                        player.sendMessage(" §f- " + animationObj.get("length").getAsString());
                    }
                }
            }

        } catch (Exception e) {
            Bukkit.broadcastMessage(pr+"애니메이션 불러오는 중 오류("+modelId+" : " + e.getMessage());
        }
    }

}

