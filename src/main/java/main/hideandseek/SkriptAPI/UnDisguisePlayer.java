package main.hideandseek.SkriptAPI;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class UnDisguisePlayer extends Effect {

    private Expression<Player> playerExpr;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        playerExpr = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Player player = playerExpr.getSingle(e);
        if (player == null) {
            Bukkit.getLogger().info("플레이어 null");
            return;
        }
        AnimationHandler handler = ModelEngineAnimation.getHandler(player);
        if (handler == null) {
            player.sendMessage("변신중아님");
            return;
        }
        ModelEngineAnimation.undisguisePlayer(player);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set undisguise model of " + playerExpr.toString(e, debug);
    }
}
