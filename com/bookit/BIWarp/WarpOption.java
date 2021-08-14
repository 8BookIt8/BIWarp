package com.bookit.BIWarp;


import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpOption {
    private String world = null;
    private double x = 0;
    private double y = 0;
    private double z = 0;

    private String description = null;
    private String title = null;
    private String subtitle = null;
    private int delay = 0;

    public WarpOption(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (i <= 1) {
                continue;
            }

            String arg = args[i];
            if (arg.contains("설명")) {
                this.description = BIWarp.format(arg.split(":")[1], false);
            } else if (arg.contains("타이틀") && !arg.contains("서브타이틀")) {
                this.title = BIWarp.format(arg.split(":")[1], true);
            } else if (arg.contains("서브타이틀")) {
                this.subtitle = BIWarp.format(arg.split(":")[1], true);
            } else if (arg.contains("딜레이")) {
                this.delay = Math.abs(Integer.parseInt(arg.split(":")[1]));
            }
        }
    }

    public WarpOption(Map<String, Object> map) {
        this.world = (String) map.get("world");
        this.x = (double) map.get("x");
        this.y = (double) map.get("y");
        this.z = (double) map.get("z");

        this.description = (String) map.get("description");
        this.title = (String) map.get("title");
        this.subtitle = (String) map.get("subtitle");
        this.delay = Math.abs((int) map.get("delay"));
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getDelay() {
        return delay;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("world", this.world != null ? this.world : null);
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("z", this.z);

        map.put("description", this.description != null ? this.description : null);
        map.put("title", this.title != null ? this.title : null);
        map.put("subtitle", this.subtitle != null ? this.subtitle : null);
        map.put("delay", this.delay);
        return map;
    }

    public static String getOptions() {
        return "설명, 타이틀, 서브타이틀, 딜레이";
    }

    public static List<String> getOptionList() {
        return Arrays.asList("설명:", "타이틀:", "서브타이틀:", "딜레이:");
    }
}
