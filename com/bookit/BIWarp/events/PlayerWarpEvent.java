package com.bookit.BIWarp.events;

import com.bookit.BIWarp.Warp;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerWarpEvent extends BIWarpEvent implements Cancellable {

    protected boolean isCancel = false;

    protected Player player;
    protected Warp warp;

    public PlayerWarpEvent(Player player, Warp warp) {
        this.player = player;
        this.warp = warp;
    }

    /**
     * Get event player
     *
     * @return player Event player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get event warp
     *
     * @return warp Event warp
     */
    public Warp getWarp() { return warp; }

    @Override
    public boolean isCancelled() {
        return isCancel;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancel = b;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}
