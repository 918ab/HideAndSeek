package main.hideandseek;

import com.ticxo.modelengine.api.ModelEngineAPI;
import main.hideandseek.Command.HideAndSeekCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HideAndSeek extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[HideAndSeek] Plugin Enable");
        ModelEngineAPI modelEngine = ModelEngineAPI.getAPI();
        if (modelEngine == null) {
            Bukkit.getLogger().info("[HideAndSeek] ModelEngine 플러그인 찾을 수 없음");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("숨바꼭질").setExecutor(new HideAndSeekCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
