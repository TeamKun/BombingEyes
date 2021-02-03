package net.kunmc.lab.bombingeyes.common;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public class CommonUtil {

    /**
     * 起動時メッセージを生成する
     * @return 起動時メッセージ
     */
    public static String getStartMessage() {
        return MessageConst.PLUGIN_NAME + MessageConst.START;
    }

    /**
     * 終了時メッセージを生成する
     * @return 起動時メッセージ
     */
    public static String getEndMessage() {

        return MessageConst.PLUGIN_NAME + MessageConst.END;
    }

    /**
     * プレイヤー全員に対して音を鳴らす
     * @param sound
     */
    public static void playSoundAll(Sound sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getEyeLocation(),
                    sound,
                    1,
                    1);
        }
    }
}
