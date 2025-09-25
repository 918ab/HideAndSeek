package main.hideandseek.SkriptAPI;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerModel extends SimpleExpression<String> {

    private Expression<Player> players;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        players = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        Player player = players.getSingle(e);
        if (player == null) {
            Bukkit.getLogger().info("플레이어 null");
            return null;
        }

        String modelName = ModelEngineAnimation.getCurrentModelName(player);

        if (modelName == null || modelName.isEmpty()) {
            player.sendMessage("변신중아님");
            return null;
        }
        return new String[]{modelName};
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "model of " + players.toString(e, debug);
    }
}