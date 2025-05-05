package main.hideandseek.Static;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.entity.Player;

public class ModelEnginePlay {
    public static void ModelPlay(Player player, String animation){
        ModelEngineAPI modelEngine = ModelEngineAPI.getAPI();
        if (modelEngine == null) {
            player.sendMessage("플러그인찾을 수 없음");
            return;
        }

        ModeledEntity modeledEntity = modelEngine.getModelUpdaters().getModeledEntity(player.getUniqueId());
        if (modeledEntity == null) {
            player.sendMessage("적용된모델없음");
            return;
        }

        ActiveModel model = modeledEntity.getModels().values().stream().findFirst().orElse(null);
        if (model == null) {
            player.sendMessage("적용된모델없음");
            return;
        }

        AnimationHandler handler = model.getAnimationHandler();
        handler.playAnimation(animation, 0.3, 0.3, 1, false);
        player.sendMessage("play / " +animation);
    }
}
