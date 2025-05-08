package main.hideandseek.Static;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ModelAnimationLoop {

    private final Player player;
    private BukkitRunnable chatTask;
    private boolean isRunning;

    public ModelAnimationLoop(Player player) {
        this.player = player;
        this.isRunning = false;
    }

    public void startRepeating(String[] Animations) {
        if (!isRunning) {
            chatTask = new BukkitRunnable() {
                @Override
                public void run() {
                    for (String Animation : Animations) {
                        ModelEngineAnimation.ModelStop(player,Animation);
                    }
                }
            };
            chatTask.runTaskTimer(Bukkit.getPluginManager().getPlugin("HideAndseek"), 0, 5);
            isRunning = true;
        }
    }

    public void stopRepeating() {
        if (isRunning && chatTask != null) {
            chatTask.cancel();
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}