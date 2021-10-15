package me.xpyex.plugin.flywithfood.bukkit.commands;

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
                return true;
            }
            Utils.autoSendMsg(sender, "&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件");
            Utils.autoSendMsg(sender, "&9你目前可用的命令: ");
            Utils.autoSendMsg(sender, "&a/" + label + " &breload &f- &e重载配置");
            return true;
        }
        if (args.length == 0) { //执行/fly时帮助
            if (HandleConfig.config.getInteger("HelpMsgType") == 1) {
                Utils.autoSendMsg(sender, "&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件");
                Utils.autoSendMsg(sender, "&9你目前可用的命令: ");
                if (sender.hasPermission("fly.fly")) {
                    Utils.autoSendMsg(sender, "&a/" + label + " &b<on|off|toggle> &f- &e为你自己开启或关闭飞行");
                }
                if (sender.hasPermission("fly.other")) {
                    Utils.autoSendMsg(sender, "&a/" + label + " &b<on|off|toggle> <在线玩家> &f- &e为指定玩家开启或关闭飞行");
                }
                if (sender.hasPermission("fly.admin")) {
                    Utils.autoSendMsg(sender, "&a/" + label + " &breload &f- &e重载配置");

                    Utils.autoSendMsg(sender, "&d以下为权限列表: ");
                    Utils.autoSendMsg(sender, "&afly.fly &f- &e允许玩家开启或关闭飞行");
                    Utils.autoSendMsg(sender, "&afly.nohunger &f- &e允许玩家飞行时不消耗饥饿值");
                    Utils.autoSendMsg(sender, "&afly.other &f- &e允许玩家开启或关闭他人的飞行");
                    Utils.autoSendMsg(sender, "&afly.admin &f- &e可收到权限列表");
                }
            } else if (HandleConfig.config.getInteger("HelpMsgType") == 2) {
                Utils.autoSendMsg(sender, "&7你可以使用&8/Fly|Fwf|FlyWithFood&7来使用本插件");
                Utils.autoSendMsg(sender, "&8╔══════════════════════════════");
                if (sender.hasPermission("fly.fly")) {
                    Utils.autoSendMsg(sender, "&8║ &7/" + label + " [ON|OFF|Toggle] &f- &8为你自己开启或关闭飞行");
                }
                if (sender.hasPermission("fly.other")) {
                    Utils.autoSendMsg(sender, "&8║ &7/" + label + " [ON|OFF|Toggle] [玩家] &f- &8为指定玩家开启或关闭飞行");
                }
                if (sender.hasPermission("fly.admin")) {
                    Utils.autoSendMsg(sender, "&8║ &7/" + label + " Reload &f- &8重载配置");
                    Utils.autoSendMsg(sender, "&8╠══════════════════════════════");
                    Utils.autoSendMsg(sender, "&8║ 权限列表: ");
                    Utils.autoSendMsg(sender, "&8║ &7fly.fly &f- &8允许玩家开启或关闭飞行");
                    Utils.autoSendMsg(sender, "&8║ &7fly.nohunger &f- &8允许玩家飞行时不消耗饥饿值");
                    Utils.autoSendMsg(sender, "&8║ &7fly.other &f- &8允许玩家开启或关闭他人的飞行");
                    Utils.autoSendMsg(sender, "&8║ &7fly.admin &f- &8可收到权限列表");
                }
                Utils.autoSendMsg(sender, "&8╚══════════════════════════════");
            } else {
                Utils.autoSendMsg(sender, "&c插件配置出现错误，请联系服务器管理员操作.错误原因: &fHelpMsgType");
            }
            return true;
        }
        if (args.length == 1) {  //当参数数量等于1时
            if (args[0].equalsIgnoreCase("reload")) { //重载
                if (!sender.hasPermission("fly.admin")) {
                    Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                    return true;
                }
                if (HandleConfig.reloadConfig()) {
                    Utils.autoSendMsg(sender, "&a重载成功");
                } else {
                    Utils.autoSendMsg(sender, "&c重载失败!请检查配置文件!无法解决请报告开发者.&f QQ:1723275529");
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
                Utils.autoSendMsg(sender, "&c该命令仅允许玩家使用");
                return true;
            }
            if (target == null) {
                Utils.autoSendMsg(sender, args[1] + " &c不在线(或不存在)");
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
                    Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c调整飞行模式: 玩家所在世界禁止此功能");
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
                        Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家拥有饱和Buff");
                        return true;
                    }
                    Utils.sendFWFMsg(target, FWFMsgType.HasEffect);
                    return true;
                }
                if ((target.getFoodLevel() < HandleConfig.config.getInteger("FoodDisable")) && !target.hasPermission("fly.nohunger")) {
                    FWFPlayerBeenDenyCmdEvent event = new FWFPlayerBeenDenyCmdEvent(target, DenyReason.NotEnoughFood, "on");
                    Bukkit.getPluginManager().callEvent(event);
                    if (target != sender) {
                        Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家饱食度不足");
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
                    Utils.autoSendMsg(sender, "&9成功打开 &f" + target.getName() + " &9的飞行");
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
                    Utils.autoSendMsg(sender, "&9成功关闭 &f" + target.getName() + " &9的飞行");
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
