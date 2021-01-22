package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.BombingEyes;
import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InMode extends CommonProcess {

    private static InMode instance;

    /** プレイヤーリスト */
    List<Player> playerList = new ArrayList<Player>();

    private InMode(){}
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
            @Override
            public void run() {
                // 視界内判定
                for (Player player : playerList) {
                    if (isInSight(player)) {
                        // 視界内のプレイヤーを爆殺
                        bombing(player);
                    }
                }

                // モード切替時
                if (!(ModeController.runningMode == Const.MODE_BE_IN)) {
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
                playerList.add(player);
            }
        }
    }

    /**
     * リストにプレイヤーを追加する
     * @param player
     */
    void addPlayer(Player player) {
        for (Player p : playerList) {
            if (!p.getPlayer().equals(player) && !p.equals(ModeController.killer)) {
                playerList.add(player);
            }
        }
    }

    /**
     * リストからプレイヤーを削除する
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
