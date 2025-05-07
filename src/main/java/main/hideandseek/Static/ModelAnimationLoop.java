package main.hideandseek.Static;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ModelAnimationLoop {
    public String pr = "§x§0§0§E§2§2§2H§x§0§0§E§5§3§7i§x§0§0§E§8§4§Cd§x§0§0§E§B§6§1e§x§0§0§E§E§7§6A§x§0§0§F§1§8§Cn§x§0§0§F§3§A§1d§x§0§0§F§6§B§6S§x§0§0§F§9§C§Be§x§0§0§F§C§E§0e§x§0§0§F§F§F§5k §f>> ";
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