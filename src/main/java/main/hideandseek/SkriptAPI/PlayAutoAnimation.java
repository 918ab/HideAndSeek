package main.hideandseek.SkriptAPI;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.Static.AnimationController;
import main.hideandseek.Static.GuiInventory;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayAutoAnimation extends Effect {

    private Expression<Player> playerExpr;
    private Expression<String> animationExpr;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        animationExpr = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Player player = playerExpr.getSingle(e);
        if (player == null) {
            Bukkit.getLogger().info("플레이어 null");
            return;
        }

        String animation = animationExpr.getSingle(e);
        if (animation == null) return;

        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) {
            player.sendMessage("변신중아님");
            return;
        }
        HideAndSeekStorage.put("[Player]"+player.getName()+",Animation",true);
        AnimationController.disableAutomaticAnimations(player);
        handler.playAnimation(animation, 0.3, 0.3, 1, true);
        String modelID = ModelEngineAnimation.getCurrentModelName(player);
        double count = Math.round((double) HideAndSeekStorage.get("[Animation]"+modelID+","+animation));
        long time = (long) count * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean isEnabled = GuiInventory.getSetting(player.getUniqueId(), 13);
                if(!isEnabled){
                    AnimationController.enableAutomaticAnimations(player);
                }
                handler.stopAnimation(animation);
                HideAndSeekStorage.remove("[Player]"+player.getName()+",Animation");
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), time);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play auto animation of " + playerExpr.toString(e, debug) + " to " + animationExpr.toString(e, debug);
    }
}
