package main.hideandseek.Static;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import main.hideandseek.HideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ModelEnginePlay {

    // 커스텀 모드가 활성화된 플레이어 UUID를 저장할 Set
    private static final Set<UUID> customModePlayers = new HashSet<>();

    // 애니메이션 핸들러를 가져오는 함수
    private static AnimationHandler getHandler(Player player) {
        ModeledEntity entity = ModelEngineAPI.getAPI().getModelUpdaters().getModeledEntity(player.getUniqueId());
        if (entity == null) return null;
        ActiveModel model = entity.getModels().values().stream().findFirst().orElse(null);
        if (model == null) return null;
        return model.getAnimationHandler();
    }

    // 기본 애니메이션을 중지하는 함수
    private static void stopBaseAnimations(AnimationHandler handler) {
        String[] baseAnims = {"idle", "walk", "run", "jump", "sit"};
        for (String anim : baseAnims) {
            handler.stopAnimation(anim);
        }
    }

    // 커스텀 모드를 활성화하거나 비활성화하는 함수
    public static void toggleCustomMode(Player player, boolean enable) {
        AnimationHandler handler = getHandler(player);
        if (handler == null) return;

        if (enable) {
            // 커스텀 모드 ON
            customModePlayers.add(player.getUniqueId());

            // 기본 애니메이션 차단
            stopBaseAnimations(handler);

            // 커스텀 애니메이션 실행
            handler.playAnimation("shift", 0.3, 0.3, 1, false);
            player.sendMessage("커스텀 모드 ON");

        } else {
            // 커스텀 모드 OFF
            customModePlayers.remove(player.getUniqueId());

            // 커스텀 애니메이션 중지
            handler.stopAnimation("shift");

            // idle 같은 자동 애니메이션 다시 허용
            // 필요시 playAnimation("idle") 호출 가능
            player.sendMessage("커스텀 모드 OFF");
        }
    }

    // 주기적으로 기본 애니메이션을 막기 위한 작업 (커스텀 모드일 때만)
    public static void startCustomModeMonitor() {
        HideAndSeek plugin = JavaPlugin.getPlugin(HideAndSeek.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : customModePlayers) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null || !player.isOnline()) continue;

                    AnimationHandler handler = getHandler(player);
                    if (handler != null) {
                        Bukkit.broadcastMessage("막음");
                        stopBaseAnimations(handler);  // 기본 애니메이션을 계속해서 막음
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // 10틱마다 반복
    }

    // 애니메이션을 플레이하는 함수 (예: shift 애니메이션)
    public static void ModelPlay(Player player, String animation){
        AnimationHandler handler = getHandler(player);
        if (handler != null) {
            handler.playAnimation(animation, 0.3, 0.3, 1, false);
            player.sendMessage("애니메이션 실행: " + animation);
        } else {
            player.sendMessage("애니메이션 핸들러를 찾을 수 없습니다.");
        }
    }

    // 애니메이션을 멈추는 함수 (예: shift 애니메이션 멈춤)
    public static void ModelStop(Player player, String animation) {
        AnimationHandler handler = getHandler(player);
        if (handler != null) {
            handler.stopAnimation(animation);
            player.sendMessage("애니메이션 중지: " + animation);
        } else {
            player.sendMessage("애니메이션 핸들러를 찾을 수 없습니다.");
        }
    }
}