package me.xpyex.plugin.flywithfood.bukkit.commands;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFPlayerBeenDenyCmdEvent;
import me.xpyex.plugin.flywithfood.common.types.DenyReason;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFDisableFlyEvent;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFEnableFlyEvent;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashSet;

public class FlyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (HandleConfig.config == null) {
            if (!sender.hasPermission("fly.admin")) {
                Utils.autoSendMsg(sender, "&c插件载入时出错，无法使用，请联系管理员处理");
                Utils.autoSendMsg(sender, "&cThere is something wrong with the plugin. Please ask admin of this server for help");
                return true;
            }
            if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                Utils.autoSendMsg(sender,
                        "&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件",
                        "&9你目前可用的命令: ",
                        "&a/" + label + " &breload &f- &e重载配置");
            } else {
                Utils.autoSendMsg(sender,
                        "&eYou can execute &a/fly &e, &a/fwf &eor &a/flywithfood",
                        "&9Valid commands: ",
                        "&a/" + label + " &breload &f- &eReload config");
            }
            return true;
        }
        if (args.length == 0) { //执行/fly时帮助
            Bukkit.getScheduler().runTaskAsynchronously(FlyWithFood.INSTANCE, () -> {  //发送信息允许异步
                for (Object o : HandleConfig.config.getJSONObject("Languages").getJSONObject("HelpMsgList").getJSONArray("Start")) {
                    if (o instanceof String) {
                        Utils.autoSendMsg(sender, ((String) o));
                    }
                }
                if (sender.hasPermission("fly.fly")) {
                    for (Object o : HandleConfig.config.getJSONObject("Languages").getJSONObject("HelpMsgList").getJSONArray("Fly")) {
                        if (o instanceof String) {
                            Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/" + label));
                        }
                    }
                }
                if (sender.hasPermission("fly.other")) {
                    for (Object o : HandleConfig.config.getJSONObject("Languages").getJSONObject("HelpMsgList").getJSONArray("Other")) {
                        if (o instanceof String) {
                            Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/" + label));
                        }
                    }
                }
                if (sender.hasPermission("fly.admin")) {
                    for (Object o : HandleConfig.config.getJSONObject("Languages").getJSONObject("HelpMsgList").getJSONArray("Admin")) {
                        if (o instanceof String) {
                            Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/" + label));
                        }
                    }
                }
                for (Object o : HandleConfig.config.getJSONObject("Languages").getJSONObject("HelpMsgList").getJSONArray("End")) {
                    if (o instanceof String) {
                        Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/" + label));
                    }
                }
            });
            return true;
        }
        if (args.length == 1) {  //当参数数量等于1时
            if (args[0].equalsIgnoreCase("reload")) { //重载
                if (!sender.hasPermission("fly.admin")) {
                    Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                    return true;
                }
                if (HandleConfig.reloadConfig()) {
                    if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                        Utils.autoSendMsg(sender, "&a重载成功");
                    } else {
                        Utils.autoSendMsg(sender, "&aReload successfully!");
                    }
                } else {
                    if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                        Utils.autoSendMsg(sender, "&c重载失败!请检查配置文件!无法解决请报告开发者.&f QQ:1723275529");
                    } else {
                        Utils.autoSendMsg(sender, "&cFailed to reload! Please check your config file!");
                        Utils.autoSendMsg(sender, "&cIf you can not solve this problem, please open a issue in my GitHub");
                    }
                }
                return true;
            }
            Bukkit.dispatchCommand(sender, label + " " + args[0] + " " + sender.getName());
            return true;  //令命令重新执行，定义目标
        }
        //往下参数数量必然>=2，无需考虑数组越界
        HashSet<String> checkArgs = new HashSet<>();
        checkArgs.add("on");
        checkArgs.add("off");
        checkArgs.add("toggle");
        if (checkArgs.contains(args[0].toLowerCase())) {  //如果是调整飞行模式
            Player target = Bukkit.getPlayerExact(args[1]); //获取目标玩家
            if ((!(sender instanceof Player)) && args[1].equals(sender.getName())) { //sender不是玩家，且对象是sender的时候
                if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                    Utils.autoSendMsg(sender, "&c该命令仅允许玩家使用");
                } else {
                    Utils.autoSendMsg(sender, "&cThis command only allows players to use");
                }
                return true;
            }
            if (target == null) {
                if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                    Utils.autoSendMsg(sender, args[1] + " &c不在线(或不存在)");
                } else {
                    Utils.autoSendMsg(sender, "&cPlayer &f" + args[1] + " &cis not online");
                }
                return true;
            }
            if ((target != sender && !sender.hasPermission("fly.other"))
                    ||
                    (target == sender && !sender.hasPermission("fly.fly"))) {
                if (sender instanceof Player) {
                    FWFPlayerBeenDenyCmdEvent event = new FWFPlayerBeenDenyCmdEvent((Player) sender, DenyReason.NoPermission, Arrays.toString(args), target != sender ? "fly.other" : "fly.fly");
                    Bukkit.getPluginManager().callEvent(event);
                }
                Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                return true;
            }
            if (HandleConfig.functionWL && !HandleConfig.config.getJSONObject("FunctionsWhitelist").getJSONArray("Worlds").contains(target.getLocation().getWorld().getName())) {
                FWFPlayerBeenDenyCmdEvent event = new FWFPlayerBeenDenyCmdEvent(target, DenyReason.DisableInThisWorld, args[0]);
                Bukkit.getPluginManager().callEvent(event);
                if (sender != target) {
                    if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                        Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c调整飞行模式: 玩家所在世界禁止此功能");
                    } else {
                        Utils.autoSendMsg(sender, "&cUnable to turn flight for player &f" + target.getName() + " &c because: The world is in blacklist");
                    }
                    return true;
                }
                Utils.sendFWFMsg(target, FWFMsgType.DisableInThisWorld);
                return true;
            }
            if (args[0].equalsIgnoreCase("on")) {
                if (target.hasPotionEffect(PotionEffectType.SATURATION) && !target.hasPermission("fly.nohunger")) {
                    FWFPlayerBeenDenyCmdEvent event = new FWFPlayerBeenDenyCmdEvent(target, DenyReason.HasEffect, "on");
                    Bukkit.getPluginManager().callEvent(event);
                    if (target != sender) {
                        if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                            Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家拥有饱和Buff");
                        } else {
                            Utils.autoSendMsg(sender, "&cUnable to turn flight for player &f" + target.getName() + " &c because: The player has Saturation Effect");
                        }
                        return true;
                    }
                    Utils.sendFWFMsg(target, FWFMsgType.HasEffect);
                    return true;
                }
                if ((target.getFoodLevel() < HandleConfig.config.getInteger("FoodDisable")) && !target.hasPermission("fly.nohunger")) {
                    FWFPlayerBeenDenyCmdEvent event = new FWFPlayerBeenDenyCmdEvent(target, DenyReason.NotEnoughFood, "on");
                    Bukkit.getPluginManager().callEvent(event);
                    if (target != sender) {
                        if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                            Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家饱食度不足");
                        } else {
                            Utils.autoSendMsg(sender, "&cUnable to turn flight for player &f" + target.getName() + " &c because: The saturation of player is not enough to fly");
                        }
                        return true;
                    }
                    Utils.sendFWFMsg(target, FWFMsgType.CanNotEnable);
                    return true;
                }
                FWFEnableFlyEvent event = new FWFEnableFlyEvent(target);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return true;
                }
                target.setAllowFlight(true);
                Utils.sendFWFMsg(target, FWFMsgType.EnableFly);
                if (target != sender) {
                    if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                        Utils.autoSendMsg(sender, "&9成功打开 &f" + target.getName() + " &9的飞行");
                    } else {
                        Utils.autoSendMsg(sender, "&9Successfully turn on the flight for player" + target.getName());
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("off")) {
                FWFDisableFlyEvent event = new FWFDisableFlyEvent(target);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return true;
                }
                target.setFlying(false);
                target.setAllowFlight(false);
                Utils.sendFWFMsg(target, FWFMsgType.DisableFly);
                if (target != sender) {
                    if (HandleConfig.config.getString("Language").equalsIgnoreCase("Chinese")) {
                        Utils.autoSendMsg(sender, "&9成功打开 &f" + target.getName() + " &9的飞行");
                    } else {
                        Utils.autoSendMsg(sender, "&9Successfully turn off the flight for player" + target.getName());
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (target.getAllowFlight()) {
                    Bukkit.dispatchCommand(sender, label + " off " + args[1]);
                } else {
                    Bukkit.dispatchCommand(sender, label + " on " + args[1]);
                }
                return true;
            }
        }
        Bukkit.dispatchCommand(sender, label);
        return true;
    }
}