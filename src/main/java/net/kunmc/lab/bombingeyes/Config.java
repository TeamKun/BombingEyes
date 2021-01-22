package net.kunmc.lab.bombingeyes;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    /** 処理のスパン */
    public static int tick;

    /** 視界外爆破モードのタイムリミット */
    public static int timeLimit;

    /** FOV */
    public static int fov;
    /** アスペクト比 */
    public static double aspectRatio;
    /** プレイヤーからニアクリップまでの距離*/
    public static double nearClip;
    /** プレイヤーからファークリップまでの距離*/
    public static double farClip;

    /** 爆発の威力 */
    public static int power;
    /** 爆発時の着火の有無 */
    public static boolean isSetFire;
    /** 爆発時のブロック破壊の有無*/
    public static boolean isBreakBlock;

    /**
     * コンフィグをロードする
     * @param isReload リロードフラグ
     * */
    public static void loadConfig(boolean isReload) {

        /** プラグインオブジェクト */
        BombingEyes plugin = BombingEyes.getPlugin();

        plugin.saveDefaultConfig();

        // リロード処理
        if (isReload) {
            plugin.reloadConfig();
        }

        //　コンフィグファイルを取得
        FileConfiguration config = plugin.getConfig();

        // 各値を代入
        tick = config.getInt("tick");
        fov = config.getInt("fov");
        aspectRatio = config.getDouble("aspectRatioWide") / config.getDouble("aspectRatioHeight");
        nearClip = config.getDouble("nearClip");
        farClip = config.getDouble("farClip");

        power = config.getInt("power");
        isSetFire = config.getBoolean("isSetFire");
        isBreakBlock = config.getBoolean("isBreakBlock");
        timeLimit = config.getInt("timeLimit");
    }
}
