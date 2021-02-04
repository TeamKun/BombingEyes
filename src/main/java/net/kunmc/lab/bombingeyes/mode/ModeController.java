package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.entity.Player;

import java.util.*;

public class ModeController {
    /**
     * 　実行中のモード
     */
    public static Const.GameLogicMode runningMode = Const.GameLogicMode.MODE_NEUTRAL;

    /**
     * キラープレイヤー
     */
    public static List<Player> killerList = new ArrayList<Player>();

    /**
     * 視錐台オブジェクト
     */
    public static Frustum frustum;

    /**
     * プレイヤーステート
     */
    public static final Map<UUID, PlayerState> playerList = new HashMap<>();

    public static void controller(Const.GameLogicMode runningMode) {
        // モードを設定
        ModeController.runningMode = runningMode;

        switch (runningMode) {
            case MODE_BE_IN:
            case MODE_BE_OUT:
                // 視錐台オブジェクトをセット
                ModeController.createFrustum();
                break;
            case MODE_NEUTRAL:
                frustum = null;
                break;
        }
    }

    /**
     * 視錐台オブジェクトを生成する
     */
    public static void createFrustum() {
        frustum = new Frustum(Config.fov,
                Config.aspectRatio,
                Config.nearClip,
                Config.farClip);
    }
}
