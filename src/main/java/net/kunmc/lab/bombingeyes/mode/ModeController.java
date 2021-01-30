package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ModeController {
    /**
     * 　実行中のモード
     */
    static String runningMode = Const.MODE_NEUTRAL;

    /**
     * キラープレイヤー
     */
    static List<Player> killerList = new ArrayList<Player>();

    /**
     * 視錐台オブジェクト
     */
    static Frustum frustum;

    public static void controller(String runningMode) {
        // モードを設定
        ModeController.runningMode = runningMode;


        switch (runningMode) {
            case Const.MODE_BE_IN:
                InMode inMode = InMode.getInstance();
                inMode.settingPlayerList();
                inMode.process();
                break;
            case Const.MODE_BE_OUT:
                OutMode outMode = OutMode.getInstance();
                outMode.settingPlayerList();
                outMode.process();
                break;
            case Const.MODE_NEUTRAL:
                // キラープレイヤーの設定を消去
                killerList.clear();
                frustum = null;
                break;
        }
    }

    /**
     * キラープレイヤーを設定する
     *
     * @param killer
     */
    public static void setKiller(Player killer) {
        killerList.add(killer);
    }

    public static void initializeKiller() {
        killerList.clear();
    }

    public static String getRunningMode() {
        return runningMode;
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
