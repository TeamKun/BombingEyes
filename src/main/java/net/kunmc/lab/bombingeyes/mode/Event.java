package net.kunmc.lab.bombingeyes.mode;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Event implements Listener {

    //TODO プレイヤーログイン時　リストに追加する


    /**
     * プレイヤーがリスポーンしたとき
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPostRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        // モード確認
        switch (ModeController.runningMode) {
            // 視界内爆破モード実行時
            case Const.MODE_BE_IN:
                InMode inMode = InMode.getInstance();
                // リストにプレイヤーを追加
                inMode.addPlayer(player);
                break;

            // 視界外爆破モード実行時
            case Const.MODE_BE_OUT:
                OutMode outMode = OutMode.getInstance();
                // リストにプレイヤーを追加
                outMode.addPlayer(player);
                break;

            // ニュートラル時
            case Const.MODE_NEUTRAL:
                // 何もしない
                break;
        }
    }
}
