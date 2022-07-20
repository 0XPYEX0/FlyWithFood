package me.xpyex.plugin.flywithfood.bukkit.implementations.energys;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.MoneyEnergy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitMoney extends MoneyEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.doubleValue() == 0) {  //+-0没有变化
            return;
        }
        if (FlyWithFood.ECON != null) {
            Player target = user.getPlayer();
            EconomyResponse result = value.doubleValue() > 0 ?
                    FlyWithFood.ECON.withdrawPlayer(target, value.doubleValue()) :  //Ess不允许存入负数的钱款
                    FlyWithFood.ECON.depositPlayer(target, -value.doubleValue());  //存款
            if (!result.transactionSuccess()) {
                FlyWithFood.LOGGER.severe("处理玩家 " + target.getName() + " 的游戏币时出现错误: " + result.errorMessage);
            }
        }
    }

    @Override
    public @NotNull Double getNow(FWFUser user) {
        Player target = user.getPlayer();
        return FlyWithFood.ECON.getBalance(target);
    }
}
