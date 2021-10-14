package me.xpyex.plugin.flywithfood.bukkit.utils;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.common.colormsg.ColorMsg;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {
    public static String getColorfulMsg(String msg) {
        return ColorMsg.getColorMsg(msg);
    }
    public static void autoSendMsg(CommandSender sender, String msg) {
        sender.sendMessage(getColorfulMsg(msg));
    }
    public static void sendFWFMsg(CommandSender target, FWFMsgType msgType) {
        if (msgType == FWFMsgType.DisableInThisWorld || msgType == FWFMsgType.NoPermission) {
            target.sendMessage(getColorfulMsg(HandleConfig.config.getJSONObject("Languages").getString(msgType.getValue())));
            return;
        }
        if (HandleConfig.enableRawMsg) {
            String rawDisableMsg = HandleConfig.config.getJSONObject("Languages").getJSONObject("RawMsg").getString(msgType.getValue());
            if (!rawDisableMsg.isEmpty()) {
                target.sendMessage(getColorfulMsg(rawDisableMsg));
            }
        }
        if (!(target instanceof Player)) {
            return;
        }
        if (HandleConfig.enableAction) {
            String actionDisableMsg = HandleConfig.config.getJSONObject("Languages").getJSONObject("ActionMsg").getString(msgType.getValue());
            if (!actionDisableMsg.isEmpty()) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getColorfulMsg(actionDisableMsg)));
            }
        }
        if (HandleConfig.enableTitle) {
            String titleDisableMsg = HandleConfig.config.getJSONObject("Languages").getJSONObject("TitleMsg").getString(msgType.getValue());
            if (!titleDisableMsg.isEmpty()) {
                String[] titles = titleDisableMsg.split("\\u005c\\u006e");
                if (titles.length > 2) {
                    FlyWithFood.logger.warning("Title数量错误!最多仅有2行!");
                    HandleConfig.enableTitle = false;
                    return;
                }
                if (HandleConfig.isOldVer) {
                    ((Player) target).sendTitle(getColorfulMsg(titles[0]), titles.length == 2 ? getColorfulMsg(titles[1]) : "");
                } else {
                    ((Player) target).sendTitle(getColorfulMsg(titles[0]), titles.length == 2 ? getColorfulMsg(titles[1]) : "", 10, 70, 20);
                }
            }
        }
    }
}
