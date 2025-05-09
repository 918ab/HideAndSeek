package main.hideandseek;



import com.ticxo.modelengine.api.ModelEngineAPI;
import main.hideandseek.Command.HideAndSeekCommand;
import main.hideandseek.Command.HideAndSeekCommandTab;
import main.hideandseek.Static.DataManager;
import main.hideandseek.Static.ModelEngineAnimation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public final class HideAndSeek extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("[HideAndSeek] Plugin Enable");
        getCommand("숨바꼭질").setExecutor(new HideAndSeekCommand());
        getCommand("숨바꼭질").setTabCompleter(new HideAndSeekCommandTab());
        if (ModelEngineAPI.getAPI() == null) {
            getLogger().warning("모델엔진 플러그인 찾을 수 없음");
            getServer().getPluginManager().disablePlugin(this);
        }
        DataManager f = new DataManager(this, "Data.yml");
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
