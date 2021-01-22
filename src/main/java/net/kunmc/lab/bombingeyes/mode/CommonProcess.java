package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        // 爆発を起こす
        player.getLocation()
                .createExplosion(Config.power,
                        Config.isSetFire
                        ,Config.isBreakBlock);

        // ダメージを与える
        player.damage(1000);
        Bukkit.broadcastMessage(player.getName() + "は爆殺された" );
    }

    /**
     * キラーの視界内に存在するプレイヤーのリストを取得する
     * @return true: 視界内　false: 視界外
     */
    protected boolean isInSight(Player target) {

        /** キラープレイヤー */
        Player killer = ModeController.killer;
        /** キラープレイヤーの座標 */
        Vector killerLocation = killer.getLocation().toVector();
        /** キラープレイヤーのピッチ */
        double killerPitch = killer.getLocation().getPitch();
        /** キラープレイヤーのヨー */
        double killerYaw = killer.getLocation().getYaw();
        /** ワールドオブジェクト */
        World world = ModeController.killer.getWorld();
        /** ターゲットの座標 */
        Vector targetVector = target.getLocation().toVector();

        // 遮蔽物の有無を判定
        if (world.rayTraceBlocks(killerLocation.toLocation(world),target.getLocation().toVector(),killerLocation.distance(targetVector))==null) {
            return false;
        }

        // 視錐台内外判定
        Frustum frustum = ModeController.frustum.rotatePitch(killerPitch).rotateYaw(killerYaw).translate(killerLocation);
        if (frustum.isInSight(target.getLocation().toVector())) {
            return false;
        }
        // 視錐台オブジェクトを初期化
        ModeController.createFrustum();

        return true;
    }
}
