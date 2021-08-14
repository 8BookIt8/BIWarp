package com.bookit.BIWarp.commands;

import com.bookit.BIWarp.BIWarp;
import com.bookit.BIWarp.WarpOption;
import com.bookit.BIWarp.events.PlayerWarpEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class WarpCommand implements CommandExecutor, TabExecutor {
    private BIWarp plugin = (BIWarp) Bukkit.getPluginManager().getPlugin("BIWarp");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 생성 <워프명> [옵션...]" + ChatColor.WHITE +  " - 새로운 워프를 생성합니다.");
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 제거 <워프명>" + ChatColor.WHITE +  " - 워프를 제거합니다.");
            commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 이동 <워프명>" + ChatColor.WHITE +  " - 목적지로 순간이동합니다.");
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

            if (this.createWarp(strings, (Player) commandSender)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "워프를 생성했습니다.");
                return true;
            } else {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "이미 존재하는 워프명입니다.");
                return false;
            }
        } else if(strings[0].equals("제거")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 제거 <워프명>" + ChatColor.WHITE +  " - 워프를 제거합니다.");
                return false;
            }

            if (!commandSender.isOp()) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "권한이 부족합니다.");
                return false;
            }

            if (this.removeWarp(strings, (Player) commandSender)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "워프를 제거했습니다.");
                return false;
            } else {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명입니다.");
                return true;
            }
        } else if(strings[0].equals("이동")) {
            if (strings.length == 1) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "[ BIWarp ] 워프 이동 <워프명>" + ChatColor.WHITE +  " - 목적지로 순간이동합니다.");
                return false;
            }

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "인게임에서만 사용할 수 있습니다.");
                return false;
            }

            if (!this.useWarp(strings, (Player) commandSender)) {
                commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "존재하지 않는 워프명입니다.");
                return false;
            }
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

    protected boolean createWarp(String[] strings, Player player) {
        String name = BIWarp.format(strings[1], false);

        if (this.plugin.getConfig().getString(name) != null) {
            return false;
        }

        HashMap<String, Object> options = new WarpOption(strings).toMap();
        Location loc = player.getLocation();
        options.put("world", loc.getWorld().getName());
        options.put("x", loc.getX());
        options.put("y", loc.getY());
        options.put("z", loc.getZ());

        this.plugin.getConfig().set(name, options);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
        return true;
    }

    protected boolean removeWarp(String[] strings, Player player) {
        String name = strings[1];
        for (int i = 0; i < strings.length; i++) {
            if (i < 2) {
                continue;
            }

            name = name + " " + strings[i];
        }

        if (this.plugin.getConfig().getString(name) == null) {
            return false;
        }

        this.plugin.getConfig().set(name, null);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
        return true;
    }

    protected boolean useWarp(String[] strings, Player player) {
        String name = strings[1];
        for (int i = 0; i < strings.length; i++) {
            if (i < 2) {
                continue;
            }

            name = name + " " + strings[i];
        }

        if (this.plugin.getConfig().getString(name) == null) {
            return false;
        }

        MemorySection warp = (MemorySection) this.plugin.getConfig().get(name);
        Map<String, Object> options = warp.getValues(true);

        PlayerWarpEvent event = new PlayerWarpEvent(player, name, new WarpOption(options));
        this.plugin.getServer().getPluginManager().callEvent(event);
//
        WarpTask task = new WarpTask(event);
        int delay = event.getOptions().getDelay();
        if (delay == 0) {
            task.run();
            return true;
        }

        task.runTaskLater(this.plugin, event.getOptions().getDelay() * 20);
        player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + delay + "초 후 워프합니다.");
        return true;
    }
}

class WarpTask extends BukkitRunnable {

    private PlayerWarpEvent event;

    public WarpTask(PlayerWarpEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        WarpOption option = event.getOptions();
        Location loc = option.getLocation();
        Player player = event.getPlayer();

        event.getPlayer().teleport(option.getLocation());

        event.getPlayer().sendTitle(option.getTitle(), option.getSubtitle());
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "성공적으로 이동했습니다.");
    }
}
