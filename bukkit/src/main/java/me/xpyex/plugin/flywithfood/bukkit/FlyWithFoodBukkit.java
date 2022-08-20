package me.xpyex.plugin.flywithfood.bukkit;

import me.xpyex.plugin.flywithfood.bukkit.api.FlyWithFoodAPIBK;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.command.FWFCmdExecutor;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyWithFoodBukkit extends JavaPlugin {
    public static Economy ECON;
    public static FlyWithFoodBukkit INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        new FlyWithFood(new FlyWithFoodAPIBK());

        FlyWithFood.getInstance().enable();

        FWFConfig.ROOT = FlyWithFoodBukkit.INSTANCE.getDataFolder();

        INSTANCE.getCommand("FlyWithFood").setExecutor((sender, cmd, label, args) -> {
            FWFCmdExecutor.onCmd(new BukkitSender(sender), label, args);
            return true;
        });
    }
}
