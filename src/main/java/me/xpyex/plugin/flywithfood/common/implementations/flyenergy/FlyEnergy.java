package me.xpyex.plugin.flywithfood.common.implementations.flyenergy;

import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import org.jetbrains.annotations.NotNull;

public abstract class FlyEnergy {

    public abstract @NotNull String getName();

    public abstract void cost(@NotNull FWFUser target, @NotNull Number value);  //对target扣除value的点数

    public abstract @NotNull Number getNow(FWFUser target);  //获取target现在的点数

    public void register() {
        EnergyManager.registerEnergy(getName(), this);
        //
    }

    //boolean canFly();  //是否允许飞行
}
