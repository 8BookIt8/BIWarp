package com.bookit.BIWarp.commands;

import com.bookit.BIWarp.BIWarp;
import com.bookit.BIWarp.Warp;
import com.bookit.BIWarp.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class WarpCommand implements CommandExecutor, TabExecutor {
    private final BIWarp plugin = BIWarp.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 생성 <워프명> [옵션...]" + ChatColor.WHITE + " - 새로운 워프를 생성합니다.");
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 제거 <워프명>" + ChatColor.WHITE + " - 워프를 제거합니다.");
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 목록 [페이지]" + ChatColor.WHITE + " - 워프 목록을 확인합니다.");
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 이동 <워프명>" + ChatColor.WHITE + " - 목적지로 순간이동합니다.");
            return false;
        }

        if (strings[0].equals("생성")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 생성 <워프명> [옵션...]" + ChatColor.WHITE + " - 새로운 워프를 생성합니다.");
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 사용 가능 옵션" + ChatColor.WHITE + " : " + WarpManager.getOption());
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 옵션을 사용하려면..." + ChatColor.WHITE + " 타이틀:스폰 서브타이틀:환영합니다!");
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 띄어쓰기는..." + ChatColor.WHITE + " &^로 사용할 수 있습니다.");
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

            if (createWarp(strings, (Player) commandSender)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "워프를 생성했습니다.");
                return true;
            } else {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "이미 존재하는 워프명입니다.");
                return false;
            }
        } else if (strings[0].equals("제거")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 제거 <워프명>" + ChatColor.WHITE + " - 워프를 제거합니다.");
                return false;
            }

            if (!commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "권한이 부족합니다.");
                return false;
            }

            if (removeWarp(strings)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "워프를 제거했습니다.");
                return false;
            } else {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명입니다.");
                return true;
            }
        } else if (strings[0].equals("목록")) {
            int page = strings.length >= 2 ? Integer.parseInt(strings[1]) - 1 : 0;
            showWarpList(commandSender, page);
            return true;
        } else if (strings[0].equals("이동")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 이동 <워프명>" + ChatColor.WHITE + " - 목적지로 순간이동합니다.");
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
            Warp warp = WarpManager.getWarp(name);
            if (warp == null || !warp.use((Player) commandSender)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명이거나 이동할 수 없는 목적지입니다.");
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> arguments = new ArrayList<>();
        if (strings.length == 1) {
            List<String> all = Arrays.asList("생성", "제거", "목록", "이동");
            for (String arg : all) {
                if (arg.toLowerCase().startsWith(strings[0].toLowerCase())) {
                    arguments.add(arg);
                }
            }
            return arguments;
        } else if (strings.length == 2) {
            if (strings[0].equals("제거") || strings[0].equals("이동")) {
                List<String> list = new ArrayList<>(WarpManager.getMap().keySet());
                for (String arg : list) {
                    if (arg.toLowerCase().startsWith(strings[1].toLowerCase())) {
                        arguments.add(arg);
                    }
                }

                return arguments;
            }

        } else if (strings.length >= 3) {
            if (strings[0].equals("생성")) {
                List<String> all = WarpManager.getOptionList();
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

    /**
     * Create new warp
     *
     * @param strings Command args
     * @param player  CommandSender
     * @return True if creation is successful
     */
    protected boolean createWarp(String[] strings, Player player) {
        String name = BIWarp.format(strings[1], false);

        if (WarpManager.isRegistered(name)) {
            return false;
        }

        Map<String, Object> option = getOptionMapByArgs(strings);

        Location loc = player.getLocation();
        option.put("world", loc.getWorld().getName());
        option.put("x", loc.getX());
        option.put("y", loc.getY());
        option.put("z", loc.getZ());

        plugin.getConfig().set(name.toLowerCase(), option);
        plugin.saveConfig();
        plugin.reloadConfig();

        Warp warp = new Warp(name, option);
        WarpManager.registerWarp(warp);
        BIWarp.registerShortCommand(name);
        return true;
    }

    /**
     * Get option map by command args
     *
     * @param args Command args
     * @return option Option of warp
     */
    protected Map<String, Object> getOptionMapByArgs(String[] args) {
        Map<String, Object> option = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (i <= 1) {
                continue;
            }

            String arg = args[i];
            if (arg.contains("설명:")) {
                option.put("description", BIWarp.format(arg.split(":")[1], false));
            } else if (arg.contains("타이틀:") && !arg.contains("서브타이틀")) {
                option.put("title", BIWarp.format(arg.split(":")[1], true));
            } else if (arg.contains("서브타이틀:")) {
                option.put("subtitle", BIWarp.format(arg.split(":")[1], true));
            } else if (arg.contains("딜레이:")) {
                option.put("delay", Math.abs(Integer.parseInt(arg.split(":")[1])));
            }
        }

        return option;
    }

    /**
     * Remove warp
     *
     * @param strings Command args
     * @return True if removing is successful
     */
    protected boolean removeWarp(String[] strings) {
        String name = strings[1];
        for (int i = 0; i < strings.length; i++) {
            if (i < 2) {
                continue;
            }

            name = name + " " + strings[i];
        }

        if (!WarpManager.isRegistered(name)) {
            return false;
        }

        plugin.getConfig().set(name, null);
        plugin.saveConfig();
        plugin.reloadConfig();

        WarpManager.unregisterWarp(WarpManager.getWarp(name));
        BIWarp.unregisterShortCommand(name.replace(" ", "&^"));
        return true;
    }

    /**
     * Show warp list
     *
     * @param commandSender CommandSender
     * @param page          Page to show
     */
    protected void showWarpList(CommandSender commandSender, int page) {
        List<Warp> warps = new ArrayList<>(WarpManager.getMap().values());
        int page_max = (int) Math.ceil((double) warps.size() / 5) - 1;
        page_max = Math.max(0, page_max);
        page = Math.max(0, page);
        page = Math.min(page, page_max);

        StringBuilder str = new StringBuilder("&l&6===== [ &f");
        str.append(page_max + 1);
        str.append(" &6페이지 중 &f");
        str.append(page + 1);
        str.append(" &6] =====\n");
        for (int i = 0; i < 5; i++) {
            int n = i + (page * 5);
            if (warps.size() <= n) {
                break;
            }

            Warp warp = warps.get(n);
            String name = warp.getName();
            int delay = warp.getDelay();
            String description = warp.getDescription() != null ? warp.getDescription() : name;
            StringBuilder inform = new StringBuilder("&l&6[ BIWarp ] &f" + name);
            inform.append(" : &7" + description);
            if (delay != 0) {
                inform.append(" | 딜레이 : " + delay);
            }
            inform.append("\n");

            str.append(inform);
        }
        str.append("&l&6=======================");

        commandSender.sendMessage(BIWarp.format(str.toString(), true));
    }
}
