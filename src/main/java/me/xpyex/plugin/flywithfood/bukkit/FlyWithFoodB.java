package me.xpyex.plugin.flywithfood.bukkit;

import java.util.logging.Logger;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.common.command.FWFCmdExecutor;
import me.xpyex.plugin.flywithfood.common.utils.ReflectUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyWithFoodB extends JavaPlugin {
    public static FlyWithFoodB INSTANCE;
    public static Logger LOGGER;
    public static Economy ECON;
    public static final String SERVER_SOFTWARE;
    static {
        Object MCServer = ReflectUtil.runMethod(Bukkit.getServer(), "getServer", Object.class);
        SERVER_SOFTWARE = ReflectUtil.<String>runMethod(MCServer, "getServerModName") + ReflectUtil.<String>runMethod(MCServer, "getVersion");
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = INSTANCE.getLogger();

        if (INSTANCE.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                ECON = rsp.getProvider();
                //new BukkitMoney().register();
                LOGGER.info("已与Vault挂钩");
                LOGGER.info(" ");
                LOGGER.info("Hooked with Vault successfully");
            } else {
                LOGGER.severe("你的Vault貌似出了点问题？无法与Vault挂钩");
                LOGGER.severe("或许是你没有安装任何经济插件！Vault并非经济插件，仅作为桥的功能出现！");
                LOGGER.severe("请放心，这个问题不会影响FWF的正常运作，但您无法使用Money模式");
                LOGGER.severe(" ");
                LOGGER.severe("There is something wrong with Vault...FlyWithFood cannot hook with Vault");
                LOGGER.severe("This may because you did not install any plugin to control economy of your server.");
                LOGGER.severe("Don't worry about this, FlyWithFood will work perfectly. But you can not use \"Money\" mode");
            }
        }

        INSTANCE.getCommand("FlyWithFood").setExecutor((sender, cmd, label, args) -> {
            FWFCmdExecutor.onCmd(new BukkitSender(sender), label, args);
            return true;
        });
    }
}
