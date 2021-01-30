package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.BombingEyes;
import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class OutMode extends CommonProcess {

    private static OutMode instance;
    List<OutModePlayer> playerList = new ArrayList<>();

    public static OutMode getInstance() {
        if (instance == null) {
            instance = new OutMode();
        }
        return instance;
    }

    /**
     * メイン処理
     */
    void process() {
        BukkitRunnable process = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // プレイヤーのタイマーを確認
                    for (OutModePlayer player : playerList) {
                        if (player.getTime() >= Config.timeLimit) {
                            // プレイヤーの爆破処理
                            OutMode.super.bombing(player.getPlayer());
                            // リストから除外
                            playerList.remove(player);
                        }
                    }

                    // 視界内判定
                    for (Player killer : ModeController.killerList) {
                        for (OutModePlayer player : playerList) {
                            if (isInSight(killer, player.getPlayer())) {
                                // 視界内に入ったプレイヤーのタイマーをリセット
                                player.clearTime();
                            } else {
                                // 視界外のプレイヤーのタイマーを進める
                                player.incrementTime();
                            }
                        }
                    }

                    // モードが切り替わったとき
                    if (!ModeController.runningMode.equals(Const.MODE_BE_OUT)) {
                        // プロセス終了
                        cancel();
                        playerList.clear();
                        return;
                    }
                } catch (Exception e) {
                }

            }
        };
        process.runTaskTimer(BombingEyes.getPlugin(), 0L, Config.tick);
    }

    /**
     * プレイヤーリストをセット
     */
    void settingPlayerList() {
        // キラー以外のプレイヤーをリストに格納
        for (Player player : Bukkit.getOnlinePlayers()) {
            // ゲームモードがクリエイティブorスぺクテイターの場合は追加しない
            if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                continue;
            }
            if (!ModeController.killerList.contains(player)) {
                playerList.add(new OutModePlayer(player));
            }
        }
    }

    /**
     * リストにプレイヤーを追加する
     *
     * @param player
     */
    void addPlayer(Player player) {
        if (!ModeController.killerList.contains(player)) {
            playerList.add(new OutModePlayer(player));
        }
    }

    /**
     * リストからプレイヤーを削除する
     *
     * @param player
     */
    void removePlayer(Player player) {
        for (OutModePlayer p : playerList) {
            if (p.getPlayer().equals(player)) {
                playerList.remove(p);
            }
        }
    }
}
