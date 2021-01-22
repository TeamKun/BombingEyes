package net.kunmc.lab.bombingeyes.mode;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import net.kunmc.lab.bombingeyes.BombingEyes;
import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutMode extends CommonProcess {

    private static OutMode instance;
    private static List<OutModePlayer> playerList = new ArrayList<>();

    private OutMode(){}

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
                // プレイヤーのタイマーを確認
                for (OutModePlayer p : playerList) {
                    if (p.getTime() > Config.timeLimit) {
                        // プレイヤーの爆破処理
                        OutMode.super.bombing(p.getPlayer());
                        // リストから除外
                        playerList.remove(p);
                    }
                }

                // 視界内判定
                for (OutModePlayer player : playerList) {
                    if (isInSight(player.getPlayer())) {
                        // 視界内に入ったプレイヤーのタイマーをリセット
                        player.clearTime();
                    } else {
                        // 視界外のプレイヤーのタイマーを進める
                        player.incrementTime();
                    }
                }

                if (ModeController.runningMode != Const.MODE_BE_IN) {
                    // プロセス終了
                    playerList.clear();
                    cancel();
                    return;
                }
            }
        };
        process.runTaskTimer(BombingEyes.getPlugin(),0L, Config.tick);
    }

    /**
     * プレイヤーリストをセット
     */
     void settingPlayerList() {
        // キラー以外のプレイヤーをリストに格納
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != ModeController.killer) {
                playerList.add(new OutModePlayer(player));
            }
        }
    }

    /**
     * リストにプレイヤーを追加する
     * @param player
     */
    void addPlayer(Player player) {
        for (OutModePlayer p : playerList) {
            if (p.getPlayer().equals(player)) {
                playerList.remove(p);
            }
            if (player.equals(ModeController.killer)) {
                playerList.add(new OutModePlayer(player));
            }
        }
    }

    /**
     * リストからプレイヤーを削除する
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
