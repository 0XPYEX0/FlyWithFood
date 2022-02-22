package me.xpyex.plugin.flywithfood.bukkit;

import java.util.logging.Logger;
import me.xpyex.plugin.flywithfood.bukkit.commands.FlyCmd;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitExpLevel;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitExpPoint;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitFood;
import me.xpyex.plugin.flywithfood.bukkit.implementations.energys.BukkitMoney;
import me.xpyex.plugin.flywithfood.bukkit.utils.VersionUtil;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.FlyEnergy;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.energys.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.utils.NetWorkUtil;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyWithFood extends JavaPlugin {
    public static FlyWithFood INSTANCE;
    public static Logger LOGGER;
    public static Economy econ;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = getLogger();
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

        {
            new BukkitExpPoint().register();
            new BukkitExpLevel().register();
            new BukkitFood().register();
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                new BukkitMoney().register();

                RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp != null) {
                    econ = rsp.getProvider();
                    LOGGER.info("已与Vault挂钩");
                }
            }
        }

        if (!HandleConfig.loadConfig()) {
            LOGGER.warning("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
            LOGGER.warning("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
            LOGGER.warning(" ");
            LOGGER.warning("Wrong!! The plugin loading has been terminated. Please check your config file.");
            LOGGER.warning("If you can not solve this problem, please check wrong messages in console and open a Issue to my GitHub.");
            LOGGER.warning("If you are sure that the config file has something wrong, you can use '/fly reload' after you fix that problem.");
            return;
        }
        if (VersionUtil.getPluginConfigVersion() != VersionUtil.getLocalConfigVersion()) {
            LOGGER.info("本次插件更新修改了配置文件格式，正在备份原文件并转化新文件");
            LOGGER.info("The configuration file is modified in this update, and the original file is being backed up and a new file is being generated.");
            LOGGER.info(" ");
            HandleConfig.updateConfigFile();
            if (!HandleConfig.reloadConfig()) {
                LOGGER.warning("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
                LOGGER.warning("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
                LOGGER.warning(" ");
                LOGGER.warning("Wrong!! The plugin loading has been terminated. Please check your config file.");
                LOGGER.warning("If you can not solve this problem, please check wrong messages in console and open a Issue to my GitHub.");
                LOGGER.warning("If you are sure that the config file has something wrong, you can use '/fly reload' after you fix that problem.");
                return;
            }
        }
        LOGGER.info("成功加载配置文件");
        LOGGER.info("Load config file successfully");
        LOGGER.info(" ");

        HandleConfig.functionWL = HandleConfig.config.functionWL.getBoolean("Enable");
        HandleConfig.noCostWL = HandleConfig.config.noCostWL.getBoolean("Enable");

        startCheck();

        Bukkit.getScheduler().runTaskAsynchronously(INSTANCE, () -> {
            NetWorkUtil.newVer = NetWorkUtil.checkUpdate();
            if (NetWorkUtil.newVer != null) {
                LOGGER.info("你当前运行的版本为 v" + INSTANCE.getDescription().getVersion());
                LOGGER.info("找到一个更新的版本: " + NetWorkUtil.newVer);
                LOGGER.info("前往 https://gitee.com/xpyex/FlyWithFood/releases 下载");
                LOGGER.info(" ");
                LOGGER.info("You are running FlyWithFood v" + NetWorkUtil.PLUGIN_VERSION);
                LOGGER.info("There is a newer version: " + NetWorkUtil.newVer);
                LOGGER.info("Download it at: https://github.com/0XPYEX0/FlyWithFood/releases");
            } else {
                LOGGER.info("当前已是最新版本");
                LOGGER.info("You are running the newest FlyWithFood");
            }
        });

        LOGGER.info("已成功加载!");
        LOGGER.info("Plugin is loaded!!");

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(INSTANCE);
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
                double cost = user.getInfo().getCost();  //每秒消耗的数值，可为饥饿值或经验值
                double disable = user.getInfo().getDisable(); //消耗至多少关闭飞行
                FlyEnergy mode = user.getInfo().getEnergy();  //消耗什么数值
                if (mode instanceof FoodEnergy) {
                    if (user.hasSaturationEff()) {  //若玩家拥有饱和Buff，则禁止飞行
                        user.disableFly();  //关闭玩家的飞行
                        user.sendFWFMsg(FWFMsgType.HasEffect);
                        continue;
                    }
                }
                double now = user.getNow().doubleValue();
                user.cost(cost);  //扣除数值
                if ((now - cost) < disable) {  //检查扣除后是否足够飞行，否则关闭
                    user.sendFWFMsg(FWFMsgType.CanNotFly);
                    user.disableFly();  //关闭玩家的飞行
                    user.protectFromFall();  //为玩家免疫掉落伤害
                }
            }
        }, 0L, HandleConfig.config.howLongCheck * 20L);  //间隔多少秒检查一次
    }
}