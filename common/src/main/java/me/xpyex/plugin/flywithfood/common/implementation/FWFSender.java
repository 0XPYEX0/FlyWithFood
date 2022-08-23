package me.xpyex.plugin.flywithfood.common.implementation;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.common.utils.MsgUtil;
import me.xpyex.plugin.flywithfood.common.utils.Util;

public interface FWFSender {

    /**
     * 获取该类包装的Sender实例
     *
     * @param <T> 泛型T为Bukkit的CommandSender类型，或为Sponge的CommandSource类型
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

    /**
     * 检查Sender是否拥有单个权限
     *
     * @param perm 需检查的权限
     * @return 是否拥有
     */
    public boolean hasPermission(String perm);

    /**
     * 检查Sender是否拥有无需计算点数的权限
     *
     * @return 是否拥有
     */
    @SuppressWarnings("all")
    public default boolean hasNoCostPerm() {
        return hasPermissionOr("fly.nohunger", "fly.nocost");
        //
    }

    /**
     * 给Sender发送文本信息
     *
     * @param messages 文本信息
     */
    public void sendMessage(String... messages);

    /**
     * 给Sender发送染色过后的信息
     *
     * @param messages 文本信息
     */
    public default void autoSendMsg(String... messages) {
        for (String s : messages)
            sendMessage(MsgUtil.getColorMsg(s));
    }

    /**
     * 获取Sender实例的名字
     *
     * @return 名字
     */
    public String getName();

    /**
     * 发送FlyWithFood的信息，自动从配置文件中获取并转换
     *
     * @param msgType 需要发送的消息类型
     */
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
            String rawMsg = MsgUtil.formatMsg(this, FWFConfig.CONFIG.languages.get("RawMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!Util.checkNull(rawMsg)) {
                autoSendMsg(rawMsg);
            }
        }
        if (!(this instanceof FWFUser)) {
            return;
        }
        if (FWFConfig.CONFIG.enableAction) {
            String actionMsg = MsgUtil.formatMsg(this, FWFConfig.CONFIG.languages.get("ActionMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!Util.checkNull(actionMsg)) {
                ((FWFUser) this).sendActionBar(actionMsg);
            }
        }
        if (FWFConfig.CONFIG.enableTitle) {
            String titleMsg = MsgUtil.formatMsg(this, FWFConfig.CONFIG.languages.get("TitleMsg").getAsJsonObject().get(msgType.getValue()).getAsString());
            if (!Util.checkNull(titleMsg)) {
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
