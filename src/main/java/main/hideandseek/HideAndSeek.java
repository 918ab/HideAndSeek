package main.hideandseek;


import ch.njol.skript.Skript;
import com.ticxo.modelengine.api.ModelEngineAPI;
import main.hideandseek.Command.HideAndSeekCommand;
import main.hideandseek.Command.HideAndSeekCommandTab;
import main.hideandseek.Event.HideAndSeekEvent;
import main.hideandseek.SkriptAPI.PlayAnimation;
import main.hideandseek.SkriptAPI.StopAnimation;
import main.hideandseek.Static.DataManager;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class HideAndSeek extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("[HideAndSeek] Plugin Enable");


        if (ModelEngineAPI.getAPI() == null) {
            getLogger().warning("모델엔진 플러그인 찾을 수 없음");
            getServer().getPluginManager().disablePlugin(this);
        }
        getCommand("숨바꼭질").setExecutor(new HideAndSeekCommand());
        getCommand("숨바꼭질").setTabCompleter(new HideAndSeekCommandTab());
        getServer().getPluginManager().registerEvents(new HideAndSeekEvent(), this);
        DataManager f = new DataManager(this, "Data.yml");

        Skript.registerEffect(PlayAnimation.class, "play animation of %player% to %string%");
        Skript.registerEffect(StopAnimation.class, "stop animation of %player% to %string%");

        f.createFileIfNotExists();
        new BukkitRunnable() {
            @Override
            public void run() {
                ModelEngineAnimation.ModelReload();
            }
        }.runTaskLater(this, 40);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
