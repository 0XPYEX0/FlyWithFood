package me.xpyex.plugin.flywithfood.bukkit.utils;

import java.util.Arrays;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.reflections.NMSAll;
import me.xpyex.plugin.flywithfood.bukkit.reflections.NMSUtil;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.common.utils.ColorMsg;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {
    public static String getColorfulMsg(String msg) {
        return ColorMsg.getColorMsg(msg);
        //
    }

    public static String formatMsg(CommandSender target, String msg) {
        String mode = ConfigUtil.CONFIG.mode;  //消耗什么数值
        for (String groupName : ConfigUtil.getKeysOfJsonObject(ConfigUtil.CONFIG.groups)) {
            if (target.hasPermission("fly.groups." + groupName)) {
                mode = ConfigUtil.CONFIG.groups.get(groupName).getAsJsonObject().get("CostMode").getAsString();  //重新分配分组中的值
                break;
            }
        }
        if (!ConfigUtil.CONFIG.languages.get("Modes").getAsJsonObject().has(mode)) {
            throw new IllegalStateException(target.getName() + " 的CostMode出现错误！无法正确替换信息！请检查配置文件！CostMode只应出现" + Arrays.toString(EnergyManager.getEnergys()));
        }
        return msg.replace("%mode%", ConfigUtil.CONFIG.languages.get("Modes").getAsJsonObject().get(mode).getAsString());
    }

    public static void autoSendMsg(CommandSender target, String... msg) {
        for (String s : msg) {
            target.sendMessage(getColorfulMsg(formatMsg(target, s)));
        }
    }

    public static void sendFWFMsg(CommandSender target, FWFMsgType msgType) {
        switch (msgType) {
            case DisableInThisWorld:
            case NoPermission:
            case PlayerOnly:
            case PlayerNotOnline:
                autoSendMsg(target, ConfigUtil.CONFIG.languages.get(msgType.getValue()).getAsString());
                return;
        }
        if (HandleConfig.enableRawMsg) {
            String rawMsg = ConfigUtil.CONFIG.languages.get("RawMsg").getAsJsonObject().get(msgType.getValue()).getAsString();
            if (!rawMsg.isEmpty()) {
                autoSendMsg(target, rawMsg);
            }
        }
        if (!(target instanceof Player)) {
            return;
        }
        if (HandleConfig.enableAction) {
            String actionMsg = formatMsg(target, ConfigUtil.CONFIG.languages.get("ActionMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!actionMsg.isEmpty()) {
                if (NMSAll.shouldUseNMSAction) {
                    NMSUtil.sendActionBar((Player) target, getColorfulMsg(actionMsg));
                } else {
                    ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getColorfulMsg(actionMsg)));
                }
            }
        }
        if (HandleConfig.enableTitle) {
            String titleMsg = formatMsg(target, ConfigUtil.CONFIG.languages.get("TitleMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!titleMsg.isEmpty()) {
                String[] titles = titleMsg.split("\\u005c\\u006e");
                if (titles.length > 2) {
                    if (ConfigUtil.CONFIG.language.equalsIgnoreCase("Chinese")) {
                        FlyWithFood.LOGGER.warning("Title数量错误!最多仅有2行!");
                    } else {
                        FlyWithFood.LOGGER.warning("Title Messages can only have two at most!");
                    }
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