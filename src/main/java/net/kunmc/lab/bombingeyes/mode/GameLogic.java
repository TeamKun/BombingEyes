package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.BombingEyes;
import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;

import java.util.function.BiConsumer;

import static net.kunmc.lab.bombingeyes.mode.ModeController.playerList;
import static net.kunmc.lab.bombingeyes.mode.ModeController.runningMode;

public class GameLogic extends CommonProcess {

    public static final GameLogic instance = new GameLogic();

    /**
     * メイン処理
     */
    public void registerTicks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (runningMode == Const.GameLogicMode.MODE_NEUTRAL)
                    return;

                int limit = runningMode == Const.GameLogicMode.MODE_BE_IN ? Config.timeLimitIn : Config.timeLimit;

                // プレイヤーのタイマーを確認
                Bukkit.getOnlinePlayers().stream()
                        .filter(CommonProcess::checkValid)
                        .map(e -> playerList.computeIfAbsent(e.getPlayerProfile().getId(), p -> new PlayerState(e)))
                        .filter(p -> p.getTime() >= limit)
                        .forEach(p -> bombing(p.getPlayer()));

                // 視界内判定
                ModeController.killerList.forEach(killer -> {
                    Bukkit.getOnlinePlayers().stream()
                            .filter(CommonProcess::checkValid)
                            .map(e -> playerList.computeIfAbsent(e.getPlayerProfile().getId(), p -> new PlayerState(e)))
                            .forEach(p -> {
                                if (shouldBeKilled(killer, p.getPlayer())) {
                                    // 視界内に入ったプレイヤーのタイマーをリセット
                                    p.clearTime();

                                    p.getPlayer().sendActionBar(" ");
                                } else {
                                    // 視界外のプレイヤーのタイマーを進める
                                    p.incrementTime();

                                    p.getPlayer().sendActionBar(String.format("%.1f秒以内に%sの視界に%sください", (limit - p.getTime()) / 20d, killer.getName(), runningMode == Const.GameLogicMode.MODE_BE_OUT ? "入って" : "出て"));
                                }
                            });
                });
            }
        }.runTaskTimer(BombingEyes.getPlugin(), 0L, Config.tick);
    }

    public void onPlayerRestart(Player player, BiConsumer<Player, Location> teleportFunc) {
        PlayerState state = playerList.computeIfAbsent(player.getPlayerProfile().getId(), p -> new PlayerState(player));

        state.clearTime();

        if (runningMode == Const.GameLogicMode.MODE_BE_OUT) {
            boolean isSeenByNobody = ModeController.killerList.stream().noneMatch(killer -> shouldBeKilled(killer, player));
            if (isSeenByNobody) {
                ModeController.killerList.stream().findFirst().ifPresent(killer -> {
                    RayTraceResult result = killer.rayTraceBlocks(Config.farClip);
                    if (result != null)
                        teleportFunc.accept(player, result.getHitPosition().toLocation(killer.getWorld()));
                });
            }
        }
    }

}
