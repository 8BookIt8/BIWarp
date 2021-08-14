package com.bookit.BIWarp.events;

import com.bookit.BIWarp.WarpOption;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class PlayerWarpEvent extends BIWarpEvent implements Cancellable {

    protected boolean isCancel = false;

    protected Player player;
    protected String name;
    protected WarpOption option;

    public PlayerWarpEvent(Player player, String name, WarpOption option) {
        this.player = player;
        this.name = name;
        this.option = option;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getWarpName() {
        return this.name;
    }

    public WarpOption getOptions() {
        return this.option;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancel = b;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}
