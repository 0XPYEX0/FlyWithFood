package me.xpyex.plugin.flywithfood.bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import me.xpyex.plugin.flywithfood.bukkit.commands.FlyCmd;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitExpLevel;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitExpPoint;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitFood;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitMoney;
import me.xpyex.plugin.flywithfood.bukkit.listeners.HandleEvent;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.FWFInfo;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.common.utils.NetWorkUtil;
import net.milkbowl.vault.economy.Economy;
import me.xpyex.plugin.flywithfood.bukkit.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyWithFood extends JavaPlugin {
    public static FlyWithFood INSTANCE;
    public static Logger LOGGER;
    public static Economy ECON;
    public static String SERVER_TYPE;

    static {
        try {
            Object MinecraftServer = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());  //此时取到MCServer实例
            String serverName = (String) MinecraftServer.getClass().getMethod("getServerModName").invoke(MinecraftServer);  //调用MCServer类的实例方法getServerModName -> Spigot
            String serverVersion = (String) MinecraftServer.getClass().getMethod("getVersion").invoke(MinecraftServer);  //调用MCServer类的实例方法getVersion -> 1.12.2
            SERVER_TYPE = serverName + "-" + serverVersion;  //拼接 -> Spigot-1.12.2
            //该方法CraftBukkit也包含，不会出错罢
            //别问我为什么用反射，球球了
            //看看build.gradle罢，我导的是SpigotAPI，里面没有MinecraftServer的方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = getLogger();

        LOGGER.info(" ");

        LOGGER.info("你使用的服务端核心: " + SERVER_TYPE);

        NetWorkUtil.PLUGIN_VERSION = INSTANCE.getDescription().getVersion();
        LOGGER.info(" ");
        LOGGER.info("感谢使用FlyWithFood.");
        LOGGER.info("本项目在GitHub开源: https://github.com/0XPYEX0/FlyWithFood");
        LOGGER.info("本项目在Gitee开源: https://gitee.com/xpyex/FlyWithFood");
        LOGGER.info(" ");
        LOGGER.info("Thank you for using FlyWithFood");
        LOGGER.info("The plugin has been open source in GitHub: https://github.com/0XPYEX0/FlyWithFood");
        LOGGER.info("Have a nice trip with this plugin  :)");
        LOGGER.info(" ");
        getCommand("FlyWithFood").setExecutor(new FlyCmd());

        if (!HandleConfig.loadConfig()) {
            LOGGER.severe("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
            LOGGER.severe("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
            LOGGER.severe(" ");
            LOGGER.severe("ERROR!! The plugin loading has been terminated. Please check your config file.");
            LOGGER.severe("If you can not solve this problem, please check wrong messages in console and open a Issue to my GitHub.");
            LOGGER.severe("If you are sure that the config file has something wrong, you can use '/fly reload' after you fix that problem.");
            return;
        }
        if (ConfigUtil.needUpdate()) {
            LOGGER.info("本次插件更新修改了配置文件格式，正在备份原文件并转化新文件");
            LOGGER.info("The configuration file is modified in this update, and the original file is being backed up and a new file is being generated.");
            LOGGER.info(" ");
            HandleConfig.updateConfigFile();
            if (!HandleConfig.reloadConfig()) {
                LOGGER.severe("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
                LOGGER.severe("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
                LOGGER.severe(" ");
                LOGGER.severe("ERROR!! The plugin loading has been terminated. Please check your config file.");
                LOGGER.severe("If you can not solve this problem, please check wrong messages in console and open a Issue to my GitHub.");
                LOGGER.severe("If you are sure that the config file has something wrong, you can use '/fly reload' after you fix that problem.");
                return;
            }
        }
        LOGGER.info("成功加载配置文件");
        LOGGER.info("Load config file successfully");
        LOGGER.info(" ");

        HandleConfig.functionWL = ConfigUtil.CONFIG.functionWL.get("Enable").getAsBoolean();
        HandleConfig.noCostWL = ConfigUtil.CONFIG.noCostWL.get("Enable").getAsBoolean();

        {
            new BukkitExpPoint().register();
            new BukkitExpLevel().register();
            new BukkitFood().register();
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp != null) {
                    ECON = rsp.getProvider();
                    new BukkitMoney().register();
                    if (ConfigUtil.CONFIG.isChinese) {
                        LOGGER.info("已与Vault挂钩");
                    } else {
                        LOGGER.info("Hooked with Vault successfully");
                    }
                } else {
                    if (ConfigUtil.CONFIG.isChinese) {
                        LOGGER.severe("你的Vault貌似出了点问题？无法与Vault挂钩");
                        LOGGER.severe("或许是你没有安装任何经济插件！Vault并非经济插件，仅作为桥的功能出现！");
                        LOGGER.severe("请放心，这个问题不会影响FWF的正常运作，但您无法使用Money模式");
                    } else {
                        LOGGER.severe("There is something wrong with Vault...FlyWithFood cannot hook with Vault");
                        LOGGER.severe("This may because you did not install any plugin to control economy of your server.");
                        LOGGER.severe("Don't worry about this, FlyWithFood will work perfectly. But you can not use \"Money\" mode");
                    }
                }
            }
        }

        startCheck();

        Bukkit.getScheduler().runTaskAsynchronously(INSTANCE, () -> {
            NetWorkUtil.newVer = NetWorkUtil.checkUpdate();
            if (NetWorkUtil.newVer != null) {
                if (ConfigUtil.CONFIG.isChinese) {
                    LOGGER.info("你当前运行的版本为 v" + INSTANCE.getDescription().getVersion());
                    LOGGER.info("找到一个更新的版本: " + NetWorkUtil.newVer);
                    LOGGER.info("前往 https://gitee.com/xpyex/FlyWithFood/releases 下载");
                } else {
                    LOGGER.info("You are running FlyWithFood v" + NetWorkUtil.PLUGIN_VERSION);
                    LOGGER.info("There is a newer version: " + NetWorkUtil.newVer);
                    LOGGER.info("Download it at: https://github.com/0XPYEX0/FlyWithFood/releases");
                }
            } else {
                if (ConfigUtil.CONFIG.isChinese) {
                    LOGGER.info("当前已是最新版本");
                } else {
                    LOGGER.info("You are running the newest FlyWithFood");
                }
            }
        });

        Bukkit.getPluginManager().registerEvents(new HandleEvent(), INSTANCE);  //😅居然这么久才发现我压根没注册监听器，哈哈了
        LOGGER.info("已注册事件监听器");

        try {
            Metrics metrics = new Metrics(INSTANCE, 15311);
            metrics.addCustomChart(new Metrics.DrilldownPie("game_version", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> entry = new HashMap<>();
                entry.put(SERVER_TYPE, 1);
                map.put(SERVER_TYPE, entry);
                return map;
            }));
            if (ConfigUtil.CONFIG.isChinese) {
                LOGGER.info("与bStats挂钩成功");
            } else {
                LOGGER.info("Hooked with bStats successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (ConfigUtil.CONFIG.isChinese) {
                LOGGER.warning("与bStats挂钩失败");
            } else {
                LOGGER.warning("Failed to hook with bStats");
            }
        }

        LOGGER.info("已成功加载!");
        LOGGER.info("Plugin is loaded!!");
        LOGGER.info(" ");

    }

    @Override
    public void onDisable() {
        LOGGER.info("已卸载");
        LOGGER.info("Plugin is unloaded. Thanks for your using  :)");
    }

    public static void startCheck() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(INSTANCE, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                BukkitUser user = new BukkitUser(player);
                if (!user.needCheck()) {
                    continue;
                }
                FWFInfo info = user.getInfo();  //直接存起来稍微节省一点性能?
                //别问我为什么不在User实现类存实例，你有没有考虑过服主实时变动玩家权限(
                if (info.getEnergy() instanceof FoodEnergy) {
                    if (user.hasSaturationEff()) {  //若玩家拥有饱和Buff，则禁止飞行
                        user.disableFly();  //关闭玩家的飞行
                        user.sendFWFMsg(FWFMsgType.HasEffect);
                        continue;
                    }
                }
                double cost = info.getCost();  //每秒消耗的数值，可为饥饿值或经验值
                double disable = info.getDisable(); //消耗至多少关闭飞行
                double now = user.getNow().doubleValue();  //内部实现已不再new，故可以使用
                user.cost(cost);  //扣除数值
                if ((now - cost) < disable) {  //检查扣除后是否足够飞行，否则关闭
                    user.sendFWFMsg(FWFMsgType.CanNotFly);
                    user.disableFly();  //关闭玩家的飞行
                    user.protectFromFall();  //为玩家免疫掉落伤害
                }
            }
        }, 0L, ConfigUtil.CONFIG.howLongCheck * 20L);  //间隔多少秒检查一次
    }
}