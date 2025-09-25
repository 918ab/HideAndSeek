package main.hideandseek.Static;

import com.ticxo.modelengine.api.animation.ModelState;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import org.bukkit.entity.Player;

public class AnimationController {
    public static void disableAutomaticAnimations(Player player) {
        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) {
            return;
        }

        for (ModelState state : ModelState.values()) {
            handler.setDefaultProperty(new AnimationHandler.DefaultProperty(state, "blank", 0, 0, 1));
        }

        handler.forceStopAllAnimations();
    }
    public static void enableAutomaticAnimations(Player player) {
        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) {
            return;
        }
        for (ModelState state : ModelState.values()) {
            handler.setDefaultProperty(new AnimationHandler.DefaultProperty(state, state.getString(), 0.25, 0.25, 1.0));
        }
        handler.playAnimation("idle", 0.3, 0.3, 1, true);
    }
}