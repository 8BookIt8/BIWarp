package com.bookit.BIWarp;

import com.bookit.BIWarp.events.PlayerWarpEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class Warp {
    protected String name;

    protected String world;
    protected double x;
    protected double y;
    protected double z;

    protected String description = null;
    protected String title = null;
    protected String subtitle = null;
    protected int delay = 0;

    public Warp(String name, Map<String, Object> option) {
        this.name = name.toLowerCase();

        world = (String) option.get("world");
        x = (double) option.get("x");
        y = (double) option.get("y");
        z = (double) option.get("z");

        if (option.get("description") != null) {
            description = (String) option.get("description");
        }
        if (option.get("title") != null) {
            title = (String) option.get("title");
        }
        if (option.get("subtitle") != null) {
            subtitle = (String) option.get("subtitle");
        }
        if (option.get("delay") != null) {
            delay = Math.abs((int) option.get("delay"));
        }
    }

    /**
     * Return warp name
     *
     * @return name Name of warp
     */
    public String getName() {
        return name;
    }

    /**
     * Return warp location
     *
     * @return Location of warp
     */
    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * Return warp desription
     *
     * @return description Description of warp
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return warp title
     *
     * @return title Title of warp
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return warp subtitle
     *
     * @return subtitle Subtitle of warp
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Return warp delay
     *
     * @return delay Delay of warp
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Teleport by warp
     *
     * @param player Player to Warp
     * @return True if warping is successful
     */
    public boolean use(Player player) {
        if (!WarpManager.isRegistered(name)) {
            return false;
        }

        if (Bukkit.getWorld(world) == null) {
            return false;
        }

        PlayerWarpEvent event = new PlayerWarpEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        WarpTask task = new WarpTask(event);
        int delay = this.delay;
        if (delay == 0) {
            task.run();
            return true;
        }

        task.runTaskLater(BIWarp.getInstance(), delay * 20);
        player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + delay + "초 후 워프합니다.");
        return true;
    }
}

class WarpTask extends BukkitRunnable {

    private final PlayerWarpEvent event;

    public WarpTask(PlayerWarpEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        if (!event.isCancelled() && event.getPlayer() != null && WarpManager.isRegistered(event.getWarp())) {
            Warp warp = event.getWarp();
            Location loc = warp.getLocation();
            Player player = event.getPlayer();

            player.teleport(loc);

            player.sendTitle(warp.getTitle(), warp.getSubtitle());
            loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

            player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "성공적으로 이동했습니다.");
        }
    }
}
