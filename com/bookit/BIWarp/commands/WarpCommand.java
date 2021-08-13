package com.bookit.BIWarp.commands;

import com.bookit.BIWarp.BIWarp;
import com.bookit.BIWarp.WarpOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class WarpCommand implements CommandExecutor, TabExecutor {
    private BIWarp plugin = (BIWarp) Bukkit.getPluginManager().getPlugin("BIWarp");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 생성 <워프명> [옵션...]" + ChatColor.WHITE +  " - 새로운 워프를 생성합니다.");
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 제거 <워프명>" + ChatColor.WHITE +  " - 워프를 제거합니다.");
            return false;
        }

        if (strings[0].equals("생성")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 생성 <워프명> [옵션...]" + ChatColor.WHITE +  " - 새로운 워프를 생성합니다.");
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 사용 가능 옵션" + ChatColor.WHITE +  " : " + WarpOption.getOptions());
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 옵션을 사용하려면..." + ChatColor.WHITE +  " 타이틀:스폰 서브타이틀:환영합니다!");
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 띄어쓰기는..." + ChatColor.WHITE +  " &^로 사용할 수 있습니다.");
                return false;
            }

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "인게임에서만 사용할 수 있습니다.");
                return false;
            }

            if (!commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "권한이 부족합니다.");
                return false;
            }

            String name = WarpOption.makeSpace(strings[1]);
            if (this.plugin.getConfig().getString(name) != null) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "이미 존재하는 워프명입니다.");
                return false;
            }

            HashMap<String, Object> options = new WarpOption(strings).toMap();
            Location loc = ((Player) commandSender).getLocation();
            options.put("world", loc.getWorld().getName());
            options.put("x", loc.getX());
            options.put("y", loc.getY());
            options.put("z", loc.getZ());

            this.plugin.getConfig().set(name, options);

            this.plugin.saveConfig();
            this.plugin.reloadConfig();
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + name + " 워프를 생성했습니다.");
            return true;
        } else if(strings[0].equals("제거")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 제거 <워프명>" + ChatColor.WHITE +  " - 워프를 제거합니다.");
                return false;
            }

            if (!commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "권한이 부족합니다.");
                return false;
            }

            String name = strings[1];
            for (int i = 0; i < strings.length; i++) {
                if (i < 2) {
                    continue;
                }

                name = name + " " + strings[i];
            }

            if (this.plugin.getConfig().getString(name) == null) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명입니다.");
                return false;
            }

            this.plugin.getConfig().set(name, null);
            this.plugin.saveConfig();
            this.plugin.reloadConfig();
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + name + " 워프를 제거했습니다.");
            return true;
        } else if(strings[0].equals("이동")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 이동 <워프명>" + ChatColor.WHITE +  " - 목적지로 순간이동합니다.");
                return false;
            }

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "인게임에서만 사용할 수 있습니다.");
                return false;
            }

            String name = strings[1];
            for (int i = 0; i < strings.length; i++) {
                if (i < 2) {
                    continue;
                }

                name = name + " " + strings[i];
            }

            if (this.plugin.getConfig().getString(name) == null) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명입니다.");
                return false;
            }

            Bukkit.broadcastMessage(name);
            Map<String, Object> options = this.plugin.getConfig().getValues(true);
            ((Player) commandSender).teleport(new Location(Bukkit.getWorld((String) options.get(name + ".world")), (double) options.get(name + ".x"), (double) options.get(name + ".y"), (double) options.get(name + ".z")));
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + name + " 성공적으로 이동했습니다.");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> arguments = new ArrayList<String>();
        if (strings.length == 1) {
            List<String> all = Arrays.asList("생성", "제거", "목록", "이동");
            for (String arg : all) {
                if (arg.toLowerCase().startsWith(strings[0].toLowerCase())) {
                    arguments.add(arg);
                }
            }
            return arguments;
        } else if(strings.length == 2) {
            if (strings[0].equals("제거") || strings[0].equals("이동")) {
                Set<String> all = this.plugin.getConfig().getKeys(false);
                for (String arg : all) {
                    if (arg.toLowerCase().startsWith(strings[1].toLowerCase())) {
                        arguments.add(arg);
                    }
                }

                return arguments;
            }

        } else if(strings.length >= 3) {
            if (strings[0].equals("생성")) {
                List<String> all = WarpOption.getOptionList();
                for (String arg : all) {
                    if (arg.toLowerCase().startsWith(strings[strings.length - 1].toLowerCase())) {
                        arguments.add(arg);
                    }
                }

                return arguments;
            }

        }

        return null;
    }
}
