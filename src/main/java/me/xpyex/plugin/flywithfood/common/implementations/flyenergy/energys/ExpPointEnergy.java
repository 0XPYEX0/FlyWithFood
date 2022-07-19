package me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys;

import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.FlyEnergy;
import org.jetbrains.annotations.NotNull;

public abstract class ExpPointEnergy extends FlyEnergy {  //方便代码内直接用common包下判断消耗类型
    @Override
    public @NotNull String getName() {
        return "ExpPoint";
        //
    }
}
