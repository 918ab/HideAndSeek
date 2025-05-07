package main.hideandseek.Static;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

    public static void ModelPlay(Player player, String animation, ModelAnimationLoop loop) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;

        handler.playAnimation(animation, 0.3, 0.3, 1, false);
        player.sendMessage("play / " + animation);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (loop != null && loop.isRunning()) {
                    loop.stopRepeating();
                    ModelPlayf(player, "idle");
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), 40);
    }

    public static void ModelPlayf(Player player, String animation) {
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
}
