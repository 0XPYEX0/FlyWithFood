package me.xpyex.plugin.flywithfood.bukkit;

import java.util.logging.Logger;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitUser;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.command.FWFCmdExecutor;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyWithFoodBukkit extends JavaPlugin {
    public static FlyWithFoodBukkit INSTANCE;
    public static Logger LOGGER;
    public static Economy ECON;
    public static final int SERVER_MAIN_VERSION;

    static {
        int result;
        try {
            Object MinecraftServer = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());  //此时取到MCServer实例
            String serverName = (String) MinecraftServer.getClass().getMethod("getServerModName").invoke(MinecraftServer);  //调用MCServer类的实例方法getServerModName -> Spigot
            String serverVersion = (String) MinecraftServer.getClass().getMethod("getVersion").invoke(MinecraftServer);  //调用MCServer类的实例方法getVersion -> 1.12.2
            FlyWithFood.SERVER_TYPE = serverName + "-" + serverVersion;  //拼接 -> Spigot-1.12.2
            //该方法CraftBukkit也包含，不会出错罢
            //项目使用SpigotAPI，里面没有MinecraftServer的方法，使用反射获取方法

            result = Integer.parseInt(serverVersion.split("\\.")[1]);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            result = 0;
        }
        SERVER_MAIN_VERSION = result;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = INSTANCE.getLogger();

        FWFUser.GET_PLAYER_FUNC[0] = BukkitUser::new;
        FWFConfig.ROOT = FlyWithFoodBukkit.INSTANCE.getDataFolder();

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
