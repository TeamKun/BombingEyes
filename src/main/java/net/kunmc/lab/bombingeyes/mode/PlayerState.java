package net.kunmc.lab.bombingeyes.mode;

import net.kunmc.lab.bombingeyes.Config;
import org.bukkit.entity.Player;

public class PlayerState {
    private Player player;
    private int time;

    public PlayerState(Player player) {
        this.player = player;
        this.time = 0;
    }

    public void clearTime() {
        this.time = 0;
    }

    public void incrementTime() {
        this.time += Config.tick;
    }

    public int getTime() {
        return time;
    }

    public Player getPlayer() {
        return player;
    }
}
