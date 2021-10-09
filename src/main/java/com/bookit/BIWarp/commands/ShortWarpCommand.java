package com.bookit.BIWarp.commands;

import com.bookit.BIWarp.BIWarp;
import com.bookit.BIWarp.Warp;
import com.bookit.BIWarp.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShortWarpCommand extends Command {
    public ShortWarpCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "인게임에서만 사용할 수 있습니다.");
            return false;
        }

        String name = BIWarp.format(getName(), false);
        Warp warp = WarpManager.getWarp(name);
        if (warp == null || !warp.use((Player) commandSender)) {
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명이거나 이동할 수 없는 목적지입니다.");
            return false;
        }
        return true;
    }
}
