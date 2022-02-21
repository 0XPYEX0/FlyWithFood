package me.xpyex.plugin.flywithfood.sponge.utils;

import me.xpyex.plugin.flywithfood.common.utils.ColorMsg;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import me.xpyex.plugin.flywithfood.sponge.config.HandleConfig;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

public class Utils {
    public static String getColorfulMsg(String msg) {
        return ColorMsg.getColorMsg(msg);
    }
    public static void autoSendMsg(CommandSource sender, String... msg) {
        for (String s : msg) {
            sender.sendMessage(Text.of(getColorfulMsg(s)));
        }
    }
    public static void sendFWFMsg(CommandSource target, FWFMsgType msgType) {
        if (msgType == FWFMsgType.DisableInThisWorld || msgType == FWFMsgType.NoPermission) {
            target.sendMessage(Text.of(getColorfulMsg(HandleConfig.config.languages.getString(msgType.getValue()))));
            return;
        }
        if (HandleConfig.enableRawMsg) {
            String rawDisableMsg = HandleConfig.config.languages.getJSONObject("RawMsg").getString(msgType.getValue());
            if (!rawDisableMsg.isEmpty()) {
                target.sendMessage(Text.of(getColorfulMsg(rawDisableMsg)));
            }
        }
        if (!(target instanceof Viewer)) {
            return;
        }
        if (HandleConfig.enableAction) {
            String actionDisableMsg = HandleConfig.config.languages.getJSONObject("ActionMsg").getString(msgType.getValue());
            if (!actionDisableMsg.isEmpty()) {
                ((Viewer) target).sendTitle(Title.builder().actionBar(Text.of(actionDisableMsg)).build());
            }
        }
        if (HandleConfig.enableTitle) {
            String titleDisableMsg = HandleConfig.config.languages.getJSONObject("TitleMsg").getString(msgType.getValue());
            if (!titleDisableMsg.isEmpty()) {
                String[] titles = titleDisableMsg.split("\\u005c\\u006e");
                if (titles.length > 2) {
                    FlyWithFood.LOGGER.warn("Title数量错误!最多仅有2行!");
                    HandleConfig.enableTitle = false;
                    return;
                }
                ((Viewer) target).sendTitle(Title.of(Text.of(getColorfulMsg(titles[0])), Text.of(titles.length == 2 ? getColorfulMsg(titles[1]) : "")));
            }
        }
    }
    public static boolean hasPotionEffect(Living entity, PotionEffectType type) {
        return (entity.get(Keys.POTION_EFFECTS).isPresent() && entity.get(Keys.POTION_EFFECTS).get().stream().noneMatch(potionEffect ->
                potionEffect.getType() == type));
    }
}
