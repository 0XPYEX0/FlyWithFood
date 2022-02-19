package me.xpyex.plugin.flywithfood.common.implementations;

import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;

public interface FWFUser {
    void sendFWFMsg(FWFMsgType msg);  //向玩家发送FWF的信息

    boolean hasSaturationEff();  //玩家是否拥有饱和Buff

    void cost(double value);  //扣除点数

    void disableFly();  //关闭其飞行

    Object getPlayer();  //此处Object均为玩家，不可能存在其它情况，调用时可直接强转

    FWFInfo getInfo();  //获取基本信息

    boolean hasNoCostPerm();  //玩家是否有不消耗点数的权限

    Number getNow();  //玩家当前的点数[能量]

    boolean inNoCost();  //玩家所在世界是否不消耗点数[能量]

    boolean inNoFunction();  //玩家所在世界是否不启用插件

    boolean needCheck();  //玩家是否需要被检测

    void protectFromFall();  //保护玩家免受掉落伤害

    void autoSendMsg(String... msg);  //发送信息

    boolean hasPermission(String perm);  //检查有无权限

    String getName();  //返回玩家名

    boolean isPlayer();  //是否为玩家
}
