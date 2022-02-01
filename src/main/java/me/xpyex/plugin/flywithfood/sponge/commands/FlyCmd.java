package me.xpyex.plugin.flywithfood.sponge.commands;

import java.util.Optional;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import me.xpyex.plugin.flywithfood.sponge.config.HandleConfig;
import me.xpyex.plugin.flywithfood.sponge.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.sponge.implementations.energys.FoodEnergy;
import me.xpyex.plugin.flywithfood.sponge.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class FlyCmd {
    public static void registerCmd() {
        CommandSpec cmd = CommandSpec.builder()
                .description(Text.of("FlyWithFood"))
                .executor((sender, args) -> {
                    if (HandleConfig.config == null) {
                        if (!sender.hasPermission("fly.admin")) {
                            Utils.autoSendMsg(sender, "&c插件载入时出错，无法使用，请联系管理员处理");
                            return CommandResult.success();
                        }
                        Utils.autoSendMsg(sender, "&e你可以执行 &a/fly &e, &a/fwf &e或 &a/flywithfood &e来使用本插件");
                        Utils.autoSendMsg(sender, "&9你目前可用的命令: ");
                        Utils.autoSendMsg(sender, "&a/fly &breload &f- &e重载配置");
                        return CommandResult.success();
                    }
                    //执行/fly时帮助
                for (Object o : HandleConfig.config.languages.getJSONObject("HelpMsgList").getJSONArray("Start")) {
                    if (o instanceof String) {
                        Utils.autoSendMsg(sender, ((String) o));
                    }
                }
                if (sender.hasPermission("fly.fly")) {
                    for (Object o : HandleConfig.config.languages.getJSONObject("HelpMsgList").getJSONArray("Fly")) {
                        if (o instanceof String) {
                            Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/fly"));
                        }
                    }
                }
                if (sender.hasPermission("fly.other")) {
                    for (Object o : HandleConfig.config.languages.getJSONObject("HelpMsgList").getJSONArray("Other")) {
                        if (o instanceof String) {
                            Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/fly"));
                        }
                    }
                }
                if (sender.hasPermission("fly.admin")) {
                    for (Object o : HandleConfig.config.languages.getJSONObject("HelpMsgList").getJSONArray("Admin")) {
                        if (o instanceof String) {
                            Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/fly"));
                        }
                    }
                }
                for (Object o : HandleConfig.config.languages.getJSONObject("HelpMsgList").getJSONArray("End")) {
                    if (o instanceof String) {
                        Utils.autoSendMsg(sender, ((String) o).replace("%command%", "/fly"));
                    }
                }
                    return CommandResult.success();
                })
                .child(CommandSpec.builder()
                        .executor(((sender, args) -> {
                            if (!sender.hasPermission("fly.admin")) {
                                Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                                return CommandResult.success();
                            }
                            if (HandleConfig.reloadConfig()) {
                                Utils.autoSendMsg(sender, "&a重载成功");
                            } else {
                                Utils.autoSendMsg(sender, "&c重载失败!请检查配置文件!无法解决请报告开发者.&f QQ:1723275529");
                            }
                            return CommandResult.success();
                        }))
                        .build(), "reload")
                .child(CommandSpec.builder()
                        .arguments(GenericArguments.optional(GenericArguments.player(Text.of("Player"))))
                        .executor((sender, args) -> {
                            Player target;
                            Optional<Player> optionalPlayer = args.getOne("Player");
                            if (optionalPlayer.isPresent()) {
                                // 如果传入了玩家参数 则 使用传入的玩家
                                target = optionalPlayer.get();
                            } else if (sender instanceof Player) {
                                // 如果没有则使用命令发送者
                                target = (Player) sender;
                            } else {
                                // 如果命令发送者不是玩家 则发送警告 终止命令
                                Utils.autoSendMsg(sender, "&c该命令仅允许玩家使用");
                                return CommandResult.success();
                            }
                            FWFUser targetUser = new FWFUser(target);
                            if ((!sender.hasPermission("fly.other") && target != sender) || (!sender.hasPermission("fly.fly") && target == sender)) {
                                Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                                return CommandResult.success();
                            }
                            if (targetUser.inNoFunction()) {
                                if (sender != target) {
                                    Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c调整飞行模式: 玩家所在世界禁止此功能");
                                    return CommandResult.success();
                                }
                                targetUser.sendFWFMsg(FWFMsgType.DisableInThisWorld);
                                return CommandResult.success();
                            }
                            if (targetUser.getInfo().getEnergy() instanceof FoodEnergy) {
                                if (targetUser.hasSaturationEff()) {
                                    if (!target.hasPermission("fly.nohunger") && !target.hasPermission("fly.nocost")) {
                                        if (sender != target) {
                                            Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家拥有饱和Buff");
                                            return CommandResult.success();
                                        }
                                        targetUser.sendFWFMsg(FWFMsgType.HasEffect);
                                        return CommandResult.success();
                                    }
                                }
                            }
                            if ((target.foodLevel().get() < targetUser.getInfo().getDisable()) && !target.hasPermission("fly.nohunger")) {
                                if (target != sender) {
                                    Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c开启飞行: 玩家饱食度不足");
                                    return CommandResult.success();
                                }
                                Utils.sendFWFMsg(target, FWFMsgType.CanNotEnable);
                                return CommandResult.success();
                            }
                            target.offer(Keys.CAN_FLY, true);
                            Utils.sendFWFMsg(target, FWFMsgType.EnableFly);
                            if (target != sender) {
                                Utils.autoSendMsg(sender, "&9成功打开 &f" + target.getName() + " &9的飞行");
                            }
                            return CommandResult.success();
                        })
                        .build(), "on")
                .child(CommandSpec.builder()
                        .arguments(GenericArguments.optional(GenericArguments.player(Text.of("Player"))))
                        .executor((sender, args) -> {
                            Player target;
                            Optional<Player> optionalPlayer = args.getOne("Player");
                            if (optionalPlayer.isPresent()) {
                                // 如果传入了玩家参数 则 使用传入的玩家
                                target = optionalPlayer.get();
                            } else if (sender instanceof Player) {
                                // 如果没有则使用命令发送者
                                target = (Player) sender;
                            } else {
                                // 如果命令发送者不是玩家 则发送警告 终止命令
                                Utils.autoSendMsg(sender, "&c该命令仅允许玩家使用");
                                return CommandResult.success();
                            }
                            if ((!sender.hasPermission("fly.other") && target != sender) || (!sender.hasPermission("fly.fly") && target == sender)) {
                                Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                                return CommandResult.success();
                            }
                            FWFUser targetUser = new FWFUser(target);
                            if (targetUser.inNoFunction()) {
                                if (sender != target) {
                                    Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c调整飞行模式: 玩家所在世界禁止此功能");
                                    return CommandResult.success();
                                }
                                targetUser.sendFWFMsg(FWFMsgType.DisableInThisWorld);
                                return CommandResult.success();
                            }
                            targetUser.disableFly();
                            targetUser.sendFWFMsg(FWFMsgType.DisableFly);
                            if (target != sender) {
                                Utils.autoSendMsg(sender, "&9成功关闭 &f" + target.getName() + " &9的飞行");
                            }
                            return CommandResult.success();
                        })
                        .build(), "off")
                .child(CommandSpec.builder()
                        .arguments(GenericArguments.optional(GenericArguments.player(Text.of("Player"))))
                        .executor((sender, args) -> {
                            Player target;
                            Optional<Player> optionalPlayer = args.getOne("Player");
                            if (optionalPlayer.isPresent()) {
                                // 如果传入了玩家参数 则 使用传入的玩家
                                target = optionalPlayer.get();
                            } else if (sender instanceof Player) {
                                // 如果没有则使用命令发送者
                                target = (Player) sender;
                            } else {
                                // 如果命令发送者不是玩家 则发送警告 终止命令
                                Utils.autoSendMsg(sender, "&c该命令仅允许玩家使用");
                                return CommandResult.success();
                            }
                            if ((!sender.hasPermission("fly.other") && target != sender) || (!sender.hasPermission("fly.fly") && target == sender)) {
                                Utils.sendFWFMsg(sender, FWFMsgType.NoPermission);
                                return CommandResult.success();
                            }
                            FWFUser targetUser = new FWFUser(target);
                            if (targetUser.inNoFunction()) {
                                if (sender != target) {
                                    Utils.autoSendMsg(sender, "&c无法为玩家 &f" + target.getName() + " &c调整飞行模式: 玩家所在世界禁止此功能");
                                    return CommandResult.success();
                                }
                                targetUser.sendFWFMsg(FWFMsgType.DisableInThisWorld);
                                return CommandResult.success();
                            }
                            if (target.get(Keys.CAN_FLY).orElse(false)) {
                                Sponge.getCommandManager().process(sender, "fly off " + target.getName());
                            } else {
                                Sponge.getCommandManager().process(sender, "fly on " + sender.getName());
                            }
                            return CommandResult.success();
                        })
                        .build(), "toggle")
                .build();
        Sponge.getCommandManager().register(FlyWithFood.INSTANCE,
                cmd,
                "FlyWithFood".toLowerCase(), //傻逼IDEA一直提示我拼写错误
                "fly",
                "fwf");
    }
}
