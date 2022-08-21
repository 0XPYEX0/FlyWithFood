package me.xpyex.plugin.flywithfood.common.implementation;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.common.utils.MsgUtil;

public interface FWFSender {

    /**
     * 获取该类包装的Sender实例
     *
     * @param <T> 泛型T为CommandSender类型
     * @return 获取该类包装的Sender实例
     */
    public <T> T getSender();

    /**
     * 检查Sender是否拥有传入的所有权限
     *
     * @param perms 需检查的权限
     * @return 是否拥有
     */
    public default boolean hasPermissionAll(String... perms) {
        for (String perm : perms)
            if (!hasPermission(perm))
                return false;

        return true;
    }

    /**
     * 检查Sender是否拥有传入权限的其中之一
     *
     * @param perms 需检查的权限
     * @return 是否拥有
     */
    public default boolean hasPermissionOr(String... perms) {
        for (String perm : perms)
            if (hasPermission(perm))
                return true;

        return false;
    }

    public boolean hasPermission(String perm);

    @SuppressWarnings("all")
    public default boolean hasNoCostPerm() {
        return hasPermissionOr("fly.nohunger", "fly.nocost");
        //
    }

    public void sendMessage(String... messages);

    public default void autoSendMsg(String... messages) {
        for (String s : messages)
            sendMessage(MsgUtil.getColorMsg(s));
    }

    public String getName();

    public default void sendFWFMsg(FWFMsgType msgType) {
        switch (msgType) {
            case DisableInThisWorld:
            case NoPermission:
            case PlayerOnly:
            case PlayerNotOnline:
                autoSendMsg(FWFConfig.CONFIG.languages.get(msgType.getValue()).getAsString());
                return;
        }
        if (FWFConfig.CONFIG.enableRawMsg) {
            String rawMsg = FWFConfig.CONFIG.languages.get("RawMsg").getAsJsonObject().get(msgType.getValue()).getAsString();
            if (!rawMsg.isEmpty()) {
                autoSendMsg(rawMsg);
            }
        }
        if (!(this instanceof FWFUser)) {
            return;
        }
        if (FWFConfig.CONFIG.enableAction) {
            String actionMsg = MsgUtil.formatMsg(this, FWFConfig.CONFIG.languages.get("ActionMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!actionMsg.isEmpty()) {
                ((FWFUser) this).sendActionBar(actionMsg);
            }
        }
        if (FWFConfig.CONFIG.enableTitle) {
            String titleMsg = MsgUtil.formatMsg(this, FWFConfig.CONFIG.languages.get("TitleMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!titleMsg.isEmpty()) {
                String[] titles = titleMsg.split("\\u005c\\u006e");
                if (titles.length > 2) {
                    if (FWFConfig.CONFIG.language.equalsIgnoreCase("Chinese")) {
                        FlyWithFood.getLogger().error("Title数量错误!最多仅有2行!");
                    } else {
                        FlyWithFood.getLogger().error("Title Messages can only have two at most!");
                    }
                    return;
                }
                ((FWFUser) this).sendTitle(MsgUtil.getColorMsg(titles[0]), titles.length == 2 ? MsgUtil.getColorMsg(titles[1]) : "");
            }
        }
    }

}
