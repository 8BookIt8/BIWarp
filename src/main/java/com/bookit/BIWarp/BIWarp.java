package com.bookit.BIWarp;

import com.bookit.BIWarp.commands.ShortWarpCommand;
import com.bookit.BIWarp.commands.WarpCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BIWarp extends JavaPlugin {
    private static BIWarp plugin;

    public static BIWarp getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();

        getCommand("워프").setExecutor(new WarpCommand());

        ArrayList<String> warpList = new ArrayList<>(getConfig().getKeys(false));
        if (warpList.isEmpty()) {
            return ;
        }

        for (String name : warpList) {
            MemorySection section = (MemorySection) getConfig().get(name);
            Map<String, Object> option = section.getValues(true);
            WarpManager.registerWarp(new Warp(name, option));

            if (!registerShortCommand(name)) {
                getServer().getLogger().warning("단축 명령어 등록 중 오류가 발생했습니다 : " + name);
            }
        }

        getServer().getLogger().info("BIWarp 플러그인이 정상적으로 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        List<String> warpList = new ArrayList<>(WarpManager.getMap().keySet());
        if (warpList.isEmpty()) {
            return ;
        }

        for (String name : warpList) {
            WarpManager.unregisterWarp(name);
            if (!unregisterShortCommand(name)) {
                getServer().getLogger().warning("단축 명령어 등록 해제 중 오류가 발생했습니다 : " + name);
            }
        }
    }

    /**
     * Format &@ to space or color
     *
     * @param str String to format
     * @param color Is formatting color
     * @return str Formatted string
     */
    public static String format(String str , boolean color) {
        str = str.replace("&^", " ");
        if (color) {
            str = ChatColor.translateAlternateColorCodes('&', str);
        }
        return str;
    }

    /**
     * Register Short Command
     * @param command Command to register
     * @return True if registration is successful
     */
    public static boolean registerShortCommand(String command) {
        command = command.toLowerCase();

        try {
            Field commandMap = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            SimpleCommandMap smap = (SimpleCommandMap) commandMap.get(plugin.getServer());

            if(smap.getCommand(command) != null) {
                return false;
            }

            smap.register("BIWarp Error", new ShortWarpCommand(command.replace(" ", "&^")));
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            plugin.getServer().getLogger().warning("commandMap not founded");
            return false;
        }
    }

    /**
     * Unregister Short Command
     * @param command Command to unregister
     * @return True if unregistration is successful
     */
    public static boolean unregisterShortCommand(String command) {
        command = command.toLowerCase();

        try {
            Field commandMap = plugin.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            SimpleCommandMap smap = (SimpleCommandMap) commandMap.get(plugin.getServer());
            Field kmap = smap.getClass().getDeclaredField("knownCommands");
            kmap.setAccessible(true);
            Map<String, Command> commands = (Map<String, Command>) kmap.get(smap);

            if(commands.get(command) == null) {
                return false;
            }

            commands.remove(command);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            plugin.getServer().getLogger().warning("commandMap not founded");
            return false;
        }
    }
}
