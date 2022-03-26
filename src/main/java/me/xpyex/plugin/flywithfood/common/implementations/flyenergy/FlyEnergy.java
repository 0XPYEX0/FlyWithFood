package me.xpyex.plugin.flywithfood.common.implementations.flyenergy;

import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import org.jetbrains.annotations.NotNull;

public interface FlyEnergy {

    @NotNull String getName();

    void cost(@NotNull FWFUser target, @NotNull Number value);  //对target扣除value的点数

    @NotNull Number getNow(FWFUser target);  //获取target现在的点数

    void register();

    //boolean canFly();  //是否允许飞行
}
