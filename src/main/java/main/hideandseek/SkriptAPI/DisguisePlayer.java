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

public class DisguisePlayer extends Effect {

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
        if (handler != null) {
            player.sendMessage("변신중임");
            return;
        }
        ModelEngineAnimation.disguisePlayer(player, animation);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set disguise model of " + playerExpr.toString(e, debug) + " to " + animationExpr.toString(e, debug);
    }
}
