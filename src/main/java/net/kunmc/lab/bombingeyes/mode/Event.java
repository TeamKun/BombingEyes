package net.kunmc.lab.bombingeyes.mode;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.kunmc.lab.bombingeyes.mode.CommonProcess.checkValid;

public class Event implements Listener {

    /**
     * ログイン時に爆破モード実行中の場合プレイヤーを爆破対象リストに追加する(途中参加が可能になる)
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!checkValid(event.getPlayer()))
            return;

        GameLogic.instance.onPlayerRestart(event.getPlayer(), Player::teleport);
    }

    /**
     * プレイヤーがリスポーンしたとき
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPostRespawn(PlayerPostRespawnEvent event) {
        if (!checkValid(event.getPlayer()))
            return;

        GameLogic.instance.onPlayerRestart(event.getPlayer(), Player::teleport);
    }

    /**
     * ゲームモードがスペクテイターが解除された際に再び爆破対象に含む
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (!checkValid(event.getPlayer()))
            return;

        GameLogic.instance.onPlayerRestart(event.getPlayer(), Player::teleport);
    }
}
