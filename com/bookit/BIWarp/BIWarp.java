package com.bookit.BIWarp;

import com.bookit.BIWarp.commands.WarpCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BIWarp extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getLogger().info("BIWarp 플러그인이 정상적으로 활성화되었습니다.");
        this.saveDefaultConfig();

        this.getCommand("워프").setExecutor(new WarpCommand());
    }

    public static String format(String str , boolean color) {
        str = str.replace("&^", " ");
        if (color) {
            str = ChatColor.translateAlternateColorCodes('&', str);
        }
        return str;
    }
}
