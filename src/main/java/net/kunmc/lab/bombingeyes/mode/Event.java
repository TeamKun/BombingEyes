package net.kunmc.lab.bombingeyes.mode;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
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

    /**
     * ゲームモードがスペクテイターが解除された際に再び爆破対象に含む
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (!ModeController.getRunningMode().equals(Const.MODE_BE_IN)) {
            return;
        }

        GameMode beforeGameMode = event.getPlayer().getGameMode();
        GameMode afterGameMode = event.getNewGameMode();

        if (!beforeGameMode.equals(GameMode.SPECTATOR)) {
            return;
        }

        if (afterGameMode.equals(GameMode.SURVIVAL)||afterGameMode.equals(GameMode.ADVENTURE)) {
            InMode.getInstance().playerList.add(event.getPlayer());
        }
    }
}
