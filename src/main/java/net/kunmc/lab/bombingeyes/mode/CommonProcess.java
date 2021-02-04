package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

abstract public class CommonProcess implements Listener {
    /**
     * メイン処理
     */
    public abstract void registerTicks();

    /**
     * チェック処理
     */
    public static boolean checkValid(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR))
            return false;
        if (ModeController.killerList.contains(player))
            return false;
        return player.isValid();
    }

    /**
     * ゲームモードに応じたkill判定
     */
    protected boolean shouldBeKilled(Player killer, Player target) {
        if (ModeController.runningMode == Const.GameLogicMode.MODE_BE_OUT)
            return isInSight(killer, target);
        if (ModeController.runningMode == Const.GameLogicMode.MODE_BE_IN)
            return !isInSight(killer, target);
        return false;
    }

    /**
     * 爆破処理
     *
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
                        , Config.isBreakBlock);

        // ダメージを与える
        player.damage(1000);
    }

    /**
     * キラーの視界内にプレイヤーが存在するか判定する
     *
     * @return true:視界内　false:視界外
     */
    protected boolean isInSight(Player killer, Player target) {
        /** プレイヤーの視界 */
        Frustum killerFrustum = ModeController.frustum.clone().getFieldOfView(killer.getEyeLocation());

        return killerFrustum.isInSight(killer, target);
    }
}
