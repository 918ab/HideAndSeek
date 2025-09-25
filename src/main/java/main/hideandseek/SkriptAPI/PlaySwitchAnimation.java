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

public class PlaySwitchAnimation extends Effect {

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
        AnimationController.disableAutomaticAnimations(player);
        handler.playAnimation(animation, 0.3, 0.3, 1, true);
        HideAndSeekStorage.put("[Player]"+player.getName()+",Animation", true);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !((Boolean) HideAndSeekStorage.get("[Player]" + player.getName() + ",Animation"))) {
                    this.cancel();
                }
            }
        };
        runnable.runTaskTimer(Bukkit.getPluginManager().getPlugin("HideAndseek"), 0, 5);
        HideAndSeekStorage.put("[Player]"+player.getName()+",ActionbarRunnable", runnable);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play switch animation of " + playerExpr.toString(e, debug) + " to " + animationExpr.toString(e, debug);
    }
}