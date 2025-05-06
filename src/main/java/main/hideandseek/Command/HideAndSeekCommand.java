package main.hideandseek.Command;

import main.hideandseek.Static.ModelEnginePlay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HideAndSeekCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 이 명령어를 실행할 수 있습니다.");
            return false;
        }

        Player player = (Player) sender;

        // 애니메이션 실행
        ModelEnginePlay.ModelPlay(player, "shift");

        // 인자가 존재하는지 확인
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("true")) {
                // idle 애니메이션을 멈추고 새로운 애니메이션 실행
                ModelEnginePlay.ModelStop(player, "idle");
            } else if (args[0].equalsIgnoreCase("false")) {
                // 기본 idle 애니메이션 실행
                ModelEnginePlay.ModelStop(player, "shift");
            } else {
                player.sendMessage("유효한 인자를 입력해주세요. 'true' 또는 'false'만 가능합니다.");
            }
        } else {
            player.sendMessage("인자를 입력하세요. 예: /명령어 true 또는 /명령어 false");
        }
        return true;
    }
}
