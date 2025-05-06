package main.hideandseek;



import main.hideandseek.Command.HideAndSeekCommand;
import main.hideandseek.Static.ModelEnginePlay;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HideAndSeek extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[HideAndSeek] Plugin Enable");
        getCommand("숨바꼭질").setExecutor(new HideAndSeekCommand());
        ModelEnginePlay.startCustomModeMonitor();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
