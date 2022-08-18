package me.xpyex.plugin.flywithfood.bukkit;

import me.xpyex.plugin.flywithfood.bukkit.api.FlyWithFoodAPIBK;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.command.FWFCmdExecutor;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyWithFoodBukkit extends JavaPlugin {
    public static FlyWithFood INSTANCE;
    public static Economy ECON;
    public static FlyWithFoodBukkit BUKKIT_INSTANCE;

    @Override
    public void onEnable() {
        BUKKIT_INSTANCE = this;
        INSTANCE = new FlyWithFood(new FlyWithFoodAPIBK());

        FWFConfig.ROOT = FlyWithFoodBukkit.BUKKIT_INSTANCE.getDataFolder();

        if (BUKKIT_INSTANCE.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                ECON = rsp.getProvider();
                //new BukkitMoney().register();
                INSTANCE.getAPI().getLogger().info("已与Vault挂钩");
                INSTANCE.getAPI().getLogger().info(" ");
                INSTANCE.getAPI().getLogger().info("Hooked with Vault successfully");
            } else {
                INSTANCE.getAPI().getLogger().error("你的Vault貌似出了点问题？无法与Vault挂钩");
                INSTANCE.getAPI().getLogger().error("或许是你没有安装任何经济插件！Vault并非经济插件，仅作为桥的功能出现！");
                INSTANCE.getAPI().getLogger().error("请放心，这个问题不会影响FWF的正常运作，但您无法使用Money模式");
                INSTANCE.getAPI().getLogger().error(" ");
                INSTANCE.getAPI().getLogger().error("There is something wrong with Vault...FlyWithFood cannot hook with Vault");
                INSTANCE.getAPI().getLogger().error("This may because you did not install any plugin to control economy of your server.");
                INSTANCE.getAPI().getLogger().error("Don't worry about this, FlyWithFood will work perfectly. But you can not use \"Money\" mode");
            }
        }

        BUKKIT_INSTANCE.getCommand("FlyWithFood").setExecutor((sender, cmd, label, args) -> {
            FWFCmdExecutor.onCmd(new BukkitSender(sender), label, args);
            return true;
        });
    }
}
