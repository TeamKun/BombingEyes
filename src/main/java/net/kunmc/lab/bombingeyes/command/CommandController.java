package net.kunmc.lab.bombingeyes.command;

import net.kunmc.lab.bombingeyes.Config;
import net.kunmc.lab.bombingeyes.Const;
import net.kunmc.lab.bombingeyes.common.DecolationConst;
import net.kunmc.lab.bombingeyes.common.MessageConst;
import net.kunmc.lab.bombingeyes.mode.ModeController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandController implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /** コマンド名 */
        String commandName = command.getName();

        // コンフィグリロード
        if (commandName.equals(Const.COMMAND_BE_RELOAD)) {
            if (!ModeController.runningMode.equals(Const.GameLogicMode.MODE_NEUTRAL)) {
                sender.sendMessage(DecolationConst.RED + "コンフィグファイルをリロードするには実行中のbe-offコマンドで実行中のモードを終了してください");
            } else {
                Config.loadConfig(true);
                sender.sendMessage(DecolationConst.GREEN + "コンフィグファイルをリロードしました");
            }
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはゲーム内からのみ実行できます");
            return true;
        }

        // コマンドを判別
        switch (commandName) {
            // 視界内爆破モードON
            case Const.COMMAND_BE_IN:
                startBeIn(sender, args);
                break;

            // 視界外爆破モードON
            case Const.COMMAND_BE_OUT:
                startBeOut(sender, args);
                break;

            // 爆破モードOFF
            case Const.COMMAND_BE_OFF:
                beOff();
                break;
        }
        return true;
    }

    /**
     * BE_INコマンド実行処理
     *
     * @param sender
     * @param args
     */
    private static void startBeIn(CommandSender sender, String[] args) {

        // 引数の数をチェック
        if (args.length < 1) {
            sender.sendMessage(DecolationConst.RED + MessageConst.ERROR_MSG_LACK_ARGS);
            return;
        }


        // 指定されたプレイヤーの存在チェック
        for (String arg : args) {
            if (Bukkit.selectEntities(sender, arg).isEmpty()) {
                sender.sendMessage(DecolationConst.RED + MessageConst.ERROR_MSG_PLAYER_NOT_FOUND);
                return;
            }
        }

        if (ModeController.runningMode.equals(Const.GameLogicMode.MODE_BE_IN)) {
            sender.sendMessage("すでに実行中です");
            return;
        }

        // キラープレイヤーのセット
        ModeController.killerList = Arrays.stream(args)
                .flatMap(arg -> Bukkit.selectEntities(sender, arg).stream())
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList());

        // モードの設定をして実行
        ModeController.controller(Const.GameLogicMode.MODE_BE_IN);

        Bukkit.broadcastMessage(DecolationConst.RED + "==============================");
        Bukkit.broadcastMessage(DecolationConst.RED + "視界内爆破モードを開始しました");
        for (String arg : args) {
            Bukkit.broadcastMessage(DecolationConst.RED + arg);
        }
        Bukkit.broadcastMessage(DecolationConst.RED + "の視界に入らないでください");
        Bukkit.broadcastMessage(DecolationConst.RED + "==============================");
    }

    /**
     * BE_OUTコマンド実行処理
     *
     * @param sender
     * @param args
     */
    private static void startBeOut(CommandSender sender, String[] args) {
        // 引数の数をチェック
        if (args.length < 1) {
            sender.sendMessage(DecolationConst.RED + MessageConst.ERROR_MSG_LACK_ARGS);
            return;
        }

        // 指定されたプレイヤーの存在チェック
        for (String arg : args) {
            if (Bukkit.selectEntities(sender, arg).isEmpty()) {
                sender.sendMessage(DecolationConst.RED + MessageConst.ERROR_MSG_PLAYER_NOT_FOUND);
                return;
            }
        }

        if (ModeController.runningMode.equals(Const.GameLogicMode.MODE_BE_OUT)) {
            sender.sendMessage("すでに実行中です");
            return;
        }

        // キラープレイヤーのセット
        ModeController.killerList = Arrays.stream(args)
                .flatMap(arg -> Bukkit.selectEntities(sender, arg).stream())
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .collect(Collectors.toList());

        // モードの設定をして実行
        ModeController.controller(Const.GameLogicMode.MODE_BE_OUT);

        Bukkit.broadcastMessage(DecolationConst.RED + "==============================");
        Bukkit.broadcastMessage(DecolationConst.RED + "視界外爆破モードを開始しました");
        for (String arg : args) {
            Bukkit.broadcastMessage(DecolationConst.RED + arg);
        }
        Bukkit.broadcastMessage(DecolationConst.RED + "の視界に入ってください");
        Bukkit.broadcastMessage(DecolationConst.RED + "==============================");
    }

    /**
     * BE_OFFコマンド実行処理
     */
    private static void beOff() {
        // モードの設定をして実行
        ModeController.controller(Const.GameLogicMode.MODE_NEUTRAL);
        Bukkit.broadcastMessage(DecolationConst.YELLOW + "実行中のモードが終了しました");
    }
}
