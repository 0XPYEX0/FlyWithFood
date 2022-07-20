package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.ExpPointEnergy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitExpPoint extends ExpPointEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.doubleValue() == 0) {
            return;
        }
        Player target = user.getPlayer();
        if (value.floatValue() < -1f || value.floatValue() > 1f) {
            throw new IllegalArgumentException("在 " + getName() + " 模式中，Cost项∈[-1.0, 1.0].小数位可精确到6位.当前为 " + value.floatValue() + " , 您也可尝试 " + new BukkitExpLevel().getName() + " 模式");
        }
        if (target.getLevel() <= 0) {
            return;
        }
        if (target.getExp() - value.floatValue() < 0f) {
            Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () -> {
                target.setLevel(target.getLevel() - 1);
                target.setExp(1f + target.getExp() - value.floatValue());
            });
        } else if (target.getExp() - value.floatValue() > 1f) {
            Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () -> {
                target.setLevel(target.getLevel() + 1);
                target.setExp(target.getExp() - value.floatValue() - 1f);
            });
        } else {
            Bukkit.getScheduler().runTask(FlyWithFood.INSTANCE, () ->
                    target.setExp(target.getExp() - value.floatValue())
            );
        }
    }

    @Override
    public @NotNull Integer getNow(FWFUser user) {
        Player target = user.getPlayer();
        return target.getLevel();
    }
}
