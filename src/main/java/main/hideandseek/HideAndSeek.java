package main.hideandseek;



import com.ticxo.modelengine.api.ModelEngineAPI;
import main.hideandseek.Command.HideAndSeekCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class HideAndSeek extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("[HideAndSeek] Plugin Enable");
        getCommand("숨바꼭질").setExecutor(new HideAndSeekCommand());
        if (ModelEngineAPI.getAPI() == null) {
            getLogger().warning("모델엔진 플러그인 찾을 수 없음");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
