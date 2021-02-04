package net.kunmc.lab.bombingeyes;

public class Const {

    /***********
     * コマンド *
     ***********/

    /** 視界内爆破モードON */
    public final static String COMMAND_BE_IN = "be-in";

    /** 視界外爆破モードON */
    public final static String COMMAND_BE_OUT = "be-out";

    /** 爆破モードOFF */
    public final static String COMMAND_BE_OFF = "be-off";

    /** コンフィグリロード */
    public final static String COMMAND_BE_RELOAD = "be-reload";

    /*********
     * モード *
     *********/

    public static enum GameLogicMode {
        /**
         * ニュートラル
         */
        MODE_NEUTRAL,

        /**
         * 視界内爆破モード
         */
        MODE_BE_IN,

        /**
         * 視界外爆破モード
         */
        MODE_BE_OUT,
    }
}
