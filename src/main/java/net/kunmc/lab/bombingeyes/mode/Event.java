package net.kunmc.lab.bombingeyes.mode;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Event implements Listener {

    /**
     * ログイン時に爆破モード実行中の場合プレイヤーを爆破対象リストに追加する(途中参加が可能になる)
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // モード確認
        switch (ModeController.runningMode) {
            // 視界内爆破モード実行時
            case Const.MODE_BE_IN:
                // リストにプレイヤーを追加する
                InMode.getInstance().addPlayer(player);
                break;
            // 視界外爆破モード実行時
            case Const.MODE_BE_OUT:
                // リストにプレイヤーを追加する
                OutMode.getInstance().addPlayer(player);
                break;
            // ニュートラル時
            case Const.MODE_NEUTRAL:
                // 何もしない
                break;
        }
    }

    /**
     * プレイヤーがリスポーンしたとき
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPostRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        // モード確認
        switch (ModeController.runningMode) {
            // 視界内爆破モード実行時
            case Const.MODE_BE_IN:
                // リストにプレイヤーを追加する
                InMode.getInstance().addPlayer(player);
                break;
            // 視界外爆破モード実行時
            case Const.MODE_BE_OUT:
                // リストにプレイヤーを追加する
                OutMode.getInstance().addPlayer(player);
                break;
            // ニュートラル時
            case Const.MODE_NEUTRAL:
                // 何もしない
                break;
        }
    }
}
