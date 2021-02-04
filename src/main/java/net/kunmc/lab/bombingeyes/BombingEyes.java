package net.kunmc.lab.bombingeyes;

import net.kunmc.lab.bombingeyes.command.CommandController;
import net.kunmc.lab.bombingeyes.common.CommonUtil;
import net.kunmc.lab.bombingeyes.mode.Event;
import net.kunmc.lab.bombingeyes.mode.GameLogic;
import org.bukkit.plugin.java.JavaPlugin;

public final class BombingEyes extends JavaPlugin {

    /**
     * プラグインオブジェクト
     */
    private static BombingEyes plugin;

    /**
     * プラグインオブジェクトを取得する.
     *
     * @return プラグインオブジェクト
     */
    public static BombingEyes getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getLogger().info(CommonUtil.getStartMessage());

        // イベントクラス読み込み
        getServer().getPluginManager().registerEvents(new Event(), this);

        GameLogic.instance.registerTicks();

        // コマンド読み込み
        getCommand(Const.COMMAND_BE_IN).setExecutor(new CommandController());
        getCommand(Const.COMMAND_BE_OUT).setExecutor(new CommandController());
        getCommand(Const.COMMAND_BE_OFF).setExecutor(new CommandController());
        getCommand(Const.COMMAND_BE_RELOAD).setExecutor(new CommandController());

        // コンフィグファイルを読み込み
        Config.loadConfig(false);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
