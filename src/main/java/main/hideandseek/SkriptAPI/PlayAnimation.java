package main.hideandseek.SkriptAPI;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.Static.HideAndSeekStorage;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayAnimation extends Effect {

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
            return;
        }

        HideAndSeekStorage.put("[Player]"+player.getName()+",Animation",true);
        ModelEngineAnimation.Stuck(player);
        handler.playAnimation(animation, 0.3, 0.3, 1, false);
        String modelID = ModelEngineAnimation.getCurrentModelName(player);
        double count = Math.round((double) HideAndSeekStorage.get("[Animation]"+modelID+","+animation));
        long time = (long) count * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                ModelEngineAnimation.unStuck(player);
                handler.stopAnimation(animation);
                handler.playAnimation("idle", 0.3, 0.3, 1, false);
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HideAndseek"), time);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play animation of " + playerExpr.toString(e, debug) + " to " + animationExpr.toString(e, debug);
    }
}
