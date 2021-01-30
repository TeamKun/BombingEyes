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

public class InMode extends CommonProcess {

    private static InMode instance;

    /**
     * プレイヤーリスト
     */
    List<Player> playerList = new ArrayList<Player>();

    public static InMode getInstance() {
        if (instance == null) {
            instance = new InMode();
        }
        return instance;
    }

    /**
     * メイン処理
     */
    void process() {
        BukkitRunnable process = new BukkitRunnable() {
            public void run() {
                try {
                    // 視界内判定
                    for (Player killer : ModeController.killerList) {
                        for (Player target : playerList) {
                            if (isInSight(killer, target)) {
                                // 視界内のプレイヤーを爆殺
                                bombing(target);
                                removePlayer(target);
                            }
                        }
                    }

                    // モードが切り替わったとき
                    if (!ModeController.runningMode.equals(Const.MODE_BE_IN)) {
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
                playerList.add(player);
            }
        }
    }

    /**
     * リストにプレイヤーを追加する
     *
     * @param player
     */
    void addPlayer(Player player) {
        if (!playerList.contains(player) && !ModeController.killerList.contains(player)) {
            playerList.add(player);
        }
    }

    /**
     * リストからプレイヤーを削除する
     *
     * @param player
     */
    void removePlayer(Player player) {
        for (Player p : playerList) {
            if (p.getPlayer().equals(player)) {
                playerList.remove(p);
            }
        }
    }
}
