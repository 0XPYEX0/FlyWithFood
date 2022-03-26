package me.xpyex.plugin.flywithfood.common.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.HashSet;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.FWFSender;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.common.utils.PlayerUtil;

public class FWFCmd {
    private static final HashSet<String> CHECK_ARGS = new HashSet<>();
    static {
        CHECK_ARGS.add("on");
        CHECK_ARGS.add("off");
        CHECK_ARGS.add("toggle");
    }  //写成常量节省一丢丢性能

    public static void onCmd(FWFSender sender, String label, String... args) {
        if (ConfigUtil.CONFIG == null || ConfigUtil.CONFIG.config == null) {  //未雨绸缪一下
            if (!sender.hasPermission("fly.admin")) {
                sender.autoSendMsg(
                        "&c插件载入时出错，无法使用，请联系管理员处理",
                        "&cThere is something wrong with the plugin. Please ask admin of this server for help");
                return;
            }
            sender.autoSendMsg(
                    "&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件",
                    "&9你目前可用的命令: ",
                    "&a/" + label + " &breload &f- &e重载配置",
                    "&eYou can execute &a/fly &e, &a/fwf &eor &a/flywithfood",
                    "&9Valid commands: ",
                    "&a/" + label + " &breload &f- &eReload config");
            return;
        }
        if (args.length == 0) {
            for (JsonElement o : ConfigUtil.CONFIG.languages.get("HelpMsgList").getAsJsonObject().get("Start").getAsJsonArray()) {
                if (o.isJsonPrimitive() && ((JsonPrimitive) o).isString()) {
                    sender.autoSendMsg(o.getAsString());
                }
            }
            if (sender.hasPermission("fly.fly")) {
                for (JsonElement o : ConfigUtil.CONFIG.languages.get("HelpMsgList").getAsJsonObject().get("Fly").getAsJsonArray()) {
                    if (o.isJsonPrimitive() && ((JsonPrimitive) o).isString()) {
                        sender.autoSendMsg(o.getAsString().replace("%command%", "/" + label));
                    }
                }
            }
            if (sender.hasPermission("fly.other")) {
                for (JsonElement o : ConfigUtil.CONFIG.languages.get("HelpMsgList").getAsJsonObject().get("Other").getAsJsonArray()) {
                    if (o.isJsonPrimitive() && ((JsonPrimitive) o).isString()) {
                        sender.autoSendMsg(o.getAsString().replace("%command%", "/" + label));
                    }
                }
            }
            if (sender.hasPermission("fly.admin")) {
                for (JsonElement o : ConfigUtil.CONFIG.languages.get("HelpMsgList").getAsJsonObject().get("Admin").getAsJsonArray()) {
                    if (o.isJsonPrimitive() && ((JsonPrimitive) o).isString()) {
                        sender.autoSendMsg(o.getAsString().replace("%command%", "/" + label));
                    }
                }
            }
            for (JsonElement o : ConfigUtil.CONFIG.languages.get("HelpMsgList").getAsJsonObject().get(("End")).getAsJsonArray()) {
                if (o.isJsonPrimitive() && ((JsonPrimitive) o).isString()) {
                    sender.autoSendMsg(o.getAsString().replace("%command%", "/" + label));
                }
            }
            return;
        }
        if (args.length == 1) {  //当参数数量等于1时
            if (args[0].equalsIgnoreCase("reload")) { //重载
                if (!sender.hasPermission("fly.admin")) {
                    sender.sendFWFMsg(FWFMsgType.NoPermission);
                    return;
                }
                if (ConfigUtil.CONFIG.reload()) {
                    if (ConfigUtil.CONFIG.isChinese) {
                        sender.autoSendMsg("&a重载成功");
                    } else {
                        sender.autoSendMsg("&aReload successfully!");
                    }
                } else {
                    sender.autoSendMsg(
                            "&c重载失败!请检查配置文件!无法解决请报告开发者.&f QQ:1723275529",
                            "&cFailed to reload! Please check your config file!",
                            "&cIf you can not solve this problem, please open a issue in my GitHub");
                }
                return;
            }
            //Bukkit.dispatchCommand(sender, label + " " + args[0] + " " + sender.getName());
            onCmd(sender, label, args[0], sender.getName());
            return;  //令命令重新执行，定义目标
        }
        if (CHECK_ARGS.contains(args[0].toLowerCase())) {  //如果是调整飞行模式
            if ((!(sender instanceof FWFUser)) && sender.getName().equals(args[1])) { //sender不是玩家，且对象是sender的时候
                sender.sendFWFMsg(FWFMsgType.PlayerOnly);
                return;
            }
            FWFUser target = PlayerUtil.getUser(args[1], sender.isBukkit() ? "Bukkit" : "Sponge");
            if (target == null) {
                sender.sendFWFMsg(FWFMsgType.PlayerNotOnline);
                return;
            }
            if ((!target.equals(sender) && !sender.hasPermission("fly.other"))
                    ||
                    (target.equals(sender) && !sender.hasPermission("fly.fly"))) {
                sender.sendFWFMsg(FWFMsgType.NoPermission);
                return;
            }
            if (target.inNoFunction()) {
                if (!target.equals(sender)) {
                    if (ConfigUtil.CONFIG.isChinese) {
                        sender.autoSendMsg("&c无法为玩家 &f" + target.getName() + " &c调整飞行模式: 玩家所在世界禁止此功能");
                    } else {
                        sender.autoSendMsg("&cUnable to turn flight for player &f" + target.getName() + " &c because: The world is in blacklist");
                    }
                    return;
                }
                target.sendFWFMsg(FWFMsgType.DisableInThisWorld);
                return;
            }
            if (args[0].equalsIgnoreCase("on")) {
                if (target.getInfo().getEnergy() instanceof FoodEnergy) {
                    if (target.hasSaturationEff() && !target.hasNoCostPerm()) {
                        if (!target.equals(sender)) {
                            if (ConfigUtil.CONFIG.isChinese) {
                                sender.autoSendMsg("&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家拥有饱和Buff");
                            } else {
                                sender.autoSendMsg("&cUnable to turn flight for player &f" + target.getName() + " &c because: The player has Saturation Effect");
                            }
                            return;
                        }
                        target.sendFWFMsg(FWFMsgType.HasEffect);
                        return;
                    }
                }
                if ((target.getNow().doubleValue() < target.getInfo().getDisable()) && !target.hasNoCostPerm()) {
                    if (!target.equals(sender)) {
                        if (ConfigUtil.CONFIG.isChinese) {
                            sender.autoSendMsg("&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家的点数不足");
                        } else {
                            sender.autoSendMsg("&cUnable to turn flight for player &f" + target.getName() + " &c because: The points of player is not enough to fly");
                        }
                        return;
                    }
                    target.sendFWFMsg(FWFMsgType.CanNotEnable);
                    return;
                }
                target.enableFly();
                target.sendFWFMsg(FWFMsgType.EnableFly);
                if (!target.equals(sender)) {
                    if (ConfigUtil.CONFIG.isChinese) {
                        sender.autoSendMsg("&9成功打开 &f" + target.getName() + " &9的飞行");
                    } else {
                        sender.autoSendMsg("&9Successfully turn on the flight for player" + target.getName());
                    }
                }
                return;  //你妈的，下面会执行help啊，不要再删这个了
            }
            if (args[0].equalsIgnoreCase("off")) {
                target.sendFWFMsg(FWFMsgType.DisableFly);
                target.disableFly();
                if (!target.equals(sender)) {
                    if (ConfigUtil.CONFIG.isChinese) {
                        sender.autoSendMsg("&9成功关闭 &f" + target.getName() + " &9的飞行");
                    } else {
                        sender.autoSendMsg("&9Successfully turn off the flight for player" + target.getName());
                    }
                }
                return;  //你妈的，下面会执行help啊，不要再删这个了
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (target.canFly()) {
                    onCmd(sender, label, "off", args[1]);
                } else {
                    onCmd(sender, label, "on", args[1]);
                }
                return;  //你妈的，下面会执行help啊，不要再删这个了
            }
        }
        onCmd(sender, label);
    }
}