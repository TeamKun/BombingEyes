package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

abstract public class CommonProcess implements Listener {
    /**
     * メイン処理
     */
    abstract void process();

    /**
     * リストにプレイヤーをセット
     */
    abstract void settingPlayerList();

    /**
     * リストにプレイヤーを追加する
     * @param player
     */
    abstract void addPlayer(Player player);

    /**
     * リストからプレイヤーを削除する
     * @param player
     */
    abstract void removePlayer(Player player);

    /**
     * 爆破処理
     * @param player
     */
    protected void bombing(Player player) {

        // ゲームモードがクリエイティブorスぺクテイターの場合は爆破しない
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        // 爆発を起こす
        player.getEyeLocation()
                .createExplosion(Config.power,
                        Config.isSetFire
                        ,Config.isBreakBlock);

        // ダメージを与える
        player.damage(1000);
    }

    /**
     * キラーの視界内にプレイヤーが存在するか判定する
     * @return true:視界内　false:視界外
     */
    protected boolean isInSight(Player killer,Player target) {

        /** プレイヤーの視界 */
        Frustum killerFrustum = ModeController.frustum.clone().getFieldOfView(killer.getEyeLocation());

        if (killerFrustum.isInSight(killer,target)) {
            return true;
        }
        return false;
    }
}
