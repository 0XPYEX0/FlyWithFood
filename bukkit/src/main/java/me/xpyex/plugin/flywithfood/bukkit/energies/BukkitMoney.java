package me.xpyex.plugin.flywithfood.bukkit.energies;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.MoneyEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitMoney extends MoneyEnergy {
    private static Economy ECON;

    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.doubleValue() == 0) {  //+-0没有变化
            return;
        }
        if (getEcon() != null) {
            Player target = user.getPlayer();
            EconomyResponse result = value.doubleValue() > 0 ?
                    getEcon().withdrawPlayer(target, value.doubleValue()) :  //Ess不允许存入负数的钱款
                    getEcon().depositPlayer(target, -value.doubleValue());  //存款
            if (!result.transactionSuccess()) {
                FlyWithFood.getLogger().severe("处理玩家 " + target.getName() + " 的游戏币时出现错误: " + result.errorMessage);
            }
        }
    }

    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        return getEcon().getBalance(user.<Player>getPlayer());
        //
    }

    public static Economy getEcon() {
        return ECON;
        //
    }

    public static void setEcon(Economy econ) {
        ECON = econ;
        //
    }

}
