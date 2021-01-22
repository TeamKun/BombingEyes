package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import org.bukkit.entity.Player;

public class ModeController {
    /**　実行中のモード */
    static String runningMode = Const.MODE_NEUTRAL;

    /** キラープレイヤー */
    static Player killer;

    /** 視錐台オブジェクト */
    static Frustum frustum;

    public static void controller(String runningMode) {

        // モードを設定
        ModeController.runningMode = runningMode;

        switch (runningMode) {
            case Const.COMMAND_BE_IN:
                InMode inMode = InMode.getInstance();
                inMode.settingPlayerList();
                inMode.process();
                break;
            case Const.COMMAND_BE_OUT:
                OutMode outMode = OutMode.getInstance();
                outMode.settingPlayerList();
                outMode.process();
                break;
            case Const.COMMAND_BE_OFF:
                // キラープレイヤーの設定を消去
                killer = null;
                frustum = null;
                break;
        }
    }

    /**
     * キラープレイヤーを設定する
     * @param killer
     */
    public static void setKiller(Player killer) {
        ModeController.killer = killer;
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
