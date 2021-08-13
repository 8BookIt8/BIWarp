package com.bookit.BIWarp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WarpOption {
    String description = null;
    String title = null;
    String subtitle = null;
    int delay = 0;
    int cooldown = 0;

    public WarpOption(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (i <= 1) {
                continue;
            }

            String arg = args[i];
            if (arg.contains("설명")) {
                this.description = makeSpace(arg.split(":")[1]);
            } else if (arg.contains("타이틀") && !arg.contains("서브타이틀")) {
                this.title = makeSpace(arg.split(":")[1]);
            } else if (arg.contains("서브타이틀")) {
                this.subtitle = makeSpace(arg.split(":")[1]);
            } else if (arg.contains("딜레이")) {
                this.delay = Integer.parseInt(arg.split(":")[1]);
            } else if (arg.contains("쿨다운")) {
                this.cooldown = Integer.parseInt(arg.split(":")[1]);
            }
        }
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("description", this.description != null ? this.description : null);
        map.put("title", this.title != null ? this.title : null);
        map.put("subtitle", this.subtitle != null ? this.subtitle : null);
        map.put("delay", this.delay != 0 ? this.delay : 0);
        map.put("cooldown", this.cooldown != 0 ? this.cooldown : 0);
        return map;
    }

    public static String makeSpace(String str) {
        return str.replace("&^", " ");
    }

    public static String getOptions() {
        return "설명, 타이틀, 서브타이틀, 딜레이, 쿨다운";
    }

    public static List<String> getOptionList() {
        return Arrays.asList("설명:", "타이틀:", "서브타이틀:", "딜레이:", "쿨다운:");
    }
}
