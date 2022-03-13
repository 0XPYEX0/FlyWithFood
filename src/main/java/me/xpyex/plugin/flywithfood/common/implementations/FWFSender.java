package me.xpyex.plugin.flywithfood.common.implementations;

import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;

public interface FWFSender {
    void sendFWFMsg(FWFMsgType msg);  //向玩家发送FWF的信息

    boolean hasNoCostPerm();  //玩家是否有不消耗点数的权限

    void autoSendMsg(String... msg);  //发送信息

    boolean hasPermission(String perm);  //检查有无权限

    String getName();  //返回玩家名

    boolean isBukkit();  //是不是Bukkit

    boolean isSponge();  //是不是Sponge

    boolean equals(Object o);  //相等

    void sendMessage(String s);  //发送信息
}
