package me.xpyex.plugin.flywithfood.bukkit;

import me.xpyex.plugin.flywithfood.bukkit.commands.FlyCmd;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFPlayerBeenDisableFlyEvent;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.bukkit.utils.VersionUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class FlyWithFood extends JavaPlugin {
    public static FlyWithFood INSTANCE;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = getLogger();
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

        HandleConfig.functionWL = HandleConfig.config.getJSONObject("FunctionsWhitelist").getBoolean("Enable");
        HandleConfig.noCostWL = HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getBoolean("Enable");

        startCheck();

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
        int cost = HandleConfig.config.getInteger("FoodCost"); //每秒消耗的饱食度,20为满,奇数即半格
        int disable = HandleConfig.config.getInteger("FoodDisable"); //饱食度消耗至多少关闭飞行
        int howLongCheck = HandleConfig.config.getInteger("CheckSeconds");  //间隔多少秒检查一次
        Bukkit.getScheduler().runTaskTimerAsynchronously(INSTANCE, () -> {
            boolean FunctionWLEnable = HandleConfig.config.getJSONObject("FunctionsWhitelist").getBoolean("Enable");
            boolean NoCostFoodWLEnable = HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getBoolean("Enable");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (FunctionWLEnable && !HandleConfig.config.getJSONObject("FunctionsWhitelist").getJSONArray("Worlds").contains(player.getLocation().getWorld().getName())) {
                    continue;  //如果这个世界并未启用插件，则没有处理的必要
                }
                if (NoCostFoodWLEnable && HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getJSONArray("Worlds").contains(player.getLocation().getWorld().getName())) {
                    continue;  //如果这个世界不需要消耗饥饿值，则没有处理的必要
                }
                if (!player.isFlying()) {  //玩家不在飞行则没有处理他的必要
                    continue;
                }
                if ("CREATIVE, SPECTATOR".contains(player.getGameMode().toString())) {  //1.7没有旁观者模式，创造模式与旁观者模式没有处理的必要
                    continue;
                }
                if (player.hasPermission("fly.nohunger")) {  //若玩家拥有权限无视消耗，则没有处理的必要
                    continue;
                }
                if (player.hasPotionEffect(PotionEffectType.SATURATION)) {  //若玩家拥有饱和Buff，则禁止飞行
                    Bukkit.getScheduler().runTask(INSTANCE, () -> {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                    });
                    Utils.sendFWFMsg(player, FWFMsgType.HasEffect);
                    continue;
                }
                int nowFood = player.getFoodLevel();
                Bukkit.getScheduler().runTask(INSTANCE, () ->
                        player.setFoodLevel(Math.max((nowFood - cost), 0))  //扣除饥饿值
                );
                if ((nowFood - cost) < disable) {  //检查扣除后是否足够飞行，否则关闭
                    FWFPlayerBeenDisableFlyEvent event = new FWFPlayerBeenDisableFlyEvent(player);
                    Bukkit.getScheduler().runTask(INSTANCE, () -> {
                        Bukkit.getPluginManager().callEvent(event);
                        player.setAllowFlight(false);
                        player.setFlying(false);
                    });
                    Utils.sendFWFMsg(player, FWFMsgType.CanNotFly);
                    new BukkitRunnable() {  //为玩家免疫掉落伤害
                        @Override
                        public void run() {
                            if ((!player.isOnline()) || player.getAllowFlight() || player.isOnGround()) {
                                cancel();
                                return;
                            }
                            player.setFallDistance(0f);
                        }
                    }.runTaskTimer(INSTANCE, 4L, 4L);
                }
            }
        }, 0L, howLongCheck * 20L);
    }
}