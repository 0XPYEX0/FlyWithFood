package me.xpyex.plugin.flywithfood.common.implementation;

import me.xpyex.plugin.flywithfood.common.utils.MsgUtil;

public interface FWFSender {

    /**
     * 获取该类包装的Sender实例
     * @return 获取该类包装的Sender实例
     * @param <T> 泛型T为CommandSender类型
     */
    public <T> T getSender();

    /**
     * 检查Sender是否拥有传入的所有权限
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
    }

    public void sendMessage(String... messages);

    public default void autoSendMsg(String... messages) {
        for (String s : messages)
            sendMessage(MsgUtil.getColorMsg(s));
    }

    public String getName();

    @Override
    public boolean equals(Object o);
}
