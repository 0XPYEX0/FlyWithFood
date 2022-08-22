package me.xpyex.plugin.flywithfood.bukkit.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import me.xpyex.plugin.flywithfood.bukkit.bstats.Metrics;
import me.xpyex.plugin.flywithfood.bukkit.energies.BukkitExpLevel;
import me.xpyex.plugin.flywithfood.bukkit.energies.BukkitFood;
import me.xpyex.plugin.flywithfood.bukkit.energies.BukkitMoney;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitUser;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class FlyWithFoodAPIBK implements FlyWithFoodAPI {
    private static final String SERVER_SOFTWARE;
    private static final int SERVER_MAIN_VERSION;
    private final FWFLogger logger;

    static {
        String softwareResult;
        int versionResult;
        try {
            Object MinecraftServer = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());  //此时取到MCServer实例
            String serverName = (String) MinecraftServer.getClass().getMethod("getServerModName").invoke(MinecraftServer);  //调用MCServer类的实例方法getServerModName -> Spigot
            String serverVersion = (String) MinecraftServer.getClass().getMethod("getVersion").invoke(MinecraftServer);  //调用MCServer类的实例方法getVersion -> 1.12.2
            softwareResult = serverName + "-" + serverVersion;  //拼接 -> Spigot-1.12.2
            //该方法CraftBukkit也包含，不会出错罢
            //项目使用SpigotAPI，里面没有MinecraftServer的方法，使用反射获取方法

            versionResult = Integer.parseInt(serverVersion.split("\\.")[1]);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            versionResult = 0;
            softwareResult = "Unknown-" + Bukkit.getName() + "-" + Bukkit.getBukkitVersion();
        }
        SERVER_SOFTWARE = softwareResult;
        SERVER_MAIN_VERSION = versionResult;
    }

    public FlyWithFoodAPIBK() {
        logger = new FWFLogger(new BukkitSender(Bukkit.getConsoleSender()));
        //
    }

    @Override
    public FWFUser getUser(String name) {
        return new BukkitUser(name);
        //
    }

    @Override
    public List<FWFUser> getOnlineUsers() {
        return Bukkit.getOnlinePlayers().stream().map(BukkitUser::new).collect(Collectors.toList());
        //
    }

    @Override
    public FWFLogger getLogger() {
        return logger;
        //
    }

    @Override
    public String getServerSoftware() {
        return SERVER_SOFTWARE;
        //
    }

    @Override
    public int getServerMainVer() {
        return SERVER_MAIN_VERSION;
        //
    }

    @Override
    public void registerEnergies() {
        new BukkitFood().register();
        new BukkitExpLevel().register();

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                BukkitMoney.setEcon(rsp.getProvider());
                //new BukkitMoney().register();
                FlyWithFood.getLogger().info("已与Vault挂钩");
                FlyWithFood.getLogger().info(" ");
                FlyWithFood.getLogger().info("Hooked with Vault successfully");
            } else {
                FlyWithFood.getLogger().error("你的Vault貌似出了点问题？无法与Vault挂钩");
                FlyWithFood.getLogger().error("或许是你没有安装任何经济插件！Vault并非经济插件，仅作为桥的功能出现！");
                FlyWithFood.getLogger().error("请放心，这个问题不会影响FWF的正常运作，但您无法使用Money模式");
                FlyWithFood.getLogger().error(" ");
                FlyWithFood.getLogger().error("There is something wrong with Vault...FlyWithFood cannot hook with Vault");
                FlyWithFood.getLogger().error("This may because you did not install any plugin to control economy of your server.");
                FlyWithFood.getLogger().error("Don't worry about this, FlyWithFood will work perfectly. But you can not use \"Money\" mode");
            }
        }
    }

    @Override
    public void register_bStats() {
        try {
            Metrics metrics = new Metrics(FlyWithFoodBukkit.INSTANCE, 15311);
            metrics.addCustomChart(new Metrics.DrilldownPie("game_version", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> entry = new HashMap<>();
                try {
                    entry.put(getServerSoftware(), 1);
                    map.put(getServerSoftware(), entry);
                    if (FWFConfig.CONFIG.isChinese) {
                        FlyWithFood.getLogger().info("与bStats挂钩成功");
                    } else {
                        FlyWithFood.getLogger().info("Hooked with bStats successfully");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (FWFConfig.CONFIG.isChinese) {
                        FlyWithFood.getLogger().warning("添加饼状图失败");
                    } else {
                        FlyWithFood.getLogger().warning("Failed to load bStats");
                    }
                    entry.put(Bukkit.getName() + "-" + Bukkit.getBukkitVersion(), 1);
                    map.put(Bukkit.getName() + "-" + Bukkit.getBukkitVersion(), entry);
                }
                return map;
            }));
        } catch (Exception e) {
            e.printStackTrace();
            if (FWFConfig.CONFIG.isChinese) {
                FlyWithFood.getLogger().warning("与bStats挂钩失败");
            } else {
                FlyWithFood.getLogger().warning("Failed to hook with bStats");
            }
        }
    }
    
    @Override
    public void startCheck() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(FlyWithFoodBukkit.INSTANCE,
            getCheckTask(),
            0L,
            FWFConfig.CONFIG.howLongCheck * 20L
        );
    }
    
    @Override
    public void stopTasks() {
        Bukkit.getScheduler().cancelTasks(FlyWithFoodBukkit.INSTANCE);
        //
    }
}
