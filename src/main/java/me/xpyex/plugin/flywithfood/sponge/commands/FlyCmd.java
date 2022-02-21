package me.xpyex.plugin.flywithfood.sponge.commands;

import java.util.Optional;
import me.xpyex.plugin.flywithfood.common.commands.FWFCmd;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import me.xpyex.plugin.flywithfood.sponge.implementations.SpongeSender;
import me.xpyex.plugin.flywithfood.sponge.implementations.SpongeUser;
import me.xpyex.plugin.flywithfood.sponge.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class FlyCmd {
    public static void registerCmd() {
        CommandSpec cmd = CommandSpec.builder()
                .description(Text.of("FlyWithFood"))
                .executor((sender, args) -> {
                    FWFCmd.onCmd(new SpongeSender(sender), "fly");
                    return CommandResult.success();
                })
                .child(CommandSpec.builder()
                        .executor(((sender, args) -> {
                            FWFCmd.onCmd(sender instanceof Player ? new SpongeUser((Player) sender) : new SpongeSender(sender), "fly", "reload");
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
                                Utils.sendFWFMsg(sender, FWFMsgType.PlayerOnly);
                                return CommandResult.success();
                            }
                            FWFCmd.onCmd(sender instanceof Player ? new SpongeUser((Player) sender) : new SpongeSender(sender), "fly", "on", target.getName());
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
                            FWFCmd.onCmd(sender instanceof Player ? new SpongeUser((Player) sender) : new SpongeSender(sender), "fly", "off", target.getName());
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
                            FWFCmd.onCmd(sender instanceof Player ? new SpongeUser((Player) sender) : new SpongeSender(sender), "fly", "toggle", target.getName());
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
