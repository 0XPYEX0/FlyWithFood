package me.xpyex.plugin.flywithfood.bukkit;

import me.xpyex.plugin.flywithfood.bukkit.commands.FlyCmd;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.events.FWFPlayerBeenDisableFlyEvent;
import me.xpyex.plugin.flywithfood.bukkit.events.HandleEvent;
import me.xpyex.plugin.flywithfood.bukkit.reflections.NMSAll;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.bukkit.utils.VersionUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.logging.Logger;

public final class FlyWithFood extends JavaPlugin {
    public static FlyWithFood INSTANCE;
    public static Logger logger;
    public static HashSet<Player> antiFallDamage = new HashSet<>();

    @Override
    public void onEnable() {
        INSTANCE = this;
        logger = getLogger();
        logger.info("感谢使用FlyWithFood. 本插件在GitHub开源: https://github.com/0XPYEX0/FlyWithFood");
        getCommand("FlyWithFood").setExecutor(new FlyCmd());
        Bukkit.getPluginManager().registerEvents(new HandleEvent(), INSTANCE);
        if (!HandleConfig.loadConfig()) {
            logger.warning("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
            logger.warning("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
            return;
        }
        if (VersionUtil.getPluginConfigVersion() != VersionUtil.getLocalConfigVersion()) {
            logger.info("本次插件更新修改了配置文件格式，正在备份原文件并转化新文件");
            HandleConfig.updateConfigFile();
            if (!HandleConfig.reloadConfig()) {
                logger.warning("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
                logger.warning("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
                return;
            }
        }
        NMSAll.nmsVer = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        logger.info("当前服务端NMS版本: " + NMSAll.nmsVer); //v1_8_R3

        if (NMSAll.nmsVer.equalsIgnoreCase("v1_8_R1") || NMSAll.nmsVer.startsWith("v1_7_")) {
            NMSAll.isOldVer = true;
        }

        HandleConfig.functionWL = HandleConfig.config.getJSONObject("FunctionsWhitelist").getBoolean("Enable");
        HandleConfig.noCostWL = HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getBoolean("Enable");

        logger.info("成功加载配置文件");
        logger.info("已成功加载!");

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(INSTANCE);
        logger.info("已卸载");
    }

    public static void startCheck() {
        int cost = HandleConfig.config.getInteger("FoodCost"); //每秒消耗的饱食度,20为满,奇数即半格
        int disable = HandleConfig.config.getInteger("FoodDisable"); //饱食度消耗至多少关闭飞行
        Bukkit.getScheduler().runTaskTimerAsynchronously(INSTANCE, () -> {
            boolean FunctionWLEnable = HandleConfig.config.getJSONObject("FunctionsWhitelist").getBoolean("Enable");
            boolean NoCostFoodWLEnable = HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getBoolean("Enable");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (FunctionWLEnable && !HandleConfig.config.getJSONObject("FunctionsWhitelist").getJSONArray("Worlds").contains(player.getLocation().getWorld().getName())) {
                    continue;
                }
                if (NoCostFoodWLEnable && HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getJSONArray("Worlds").contains(player.getLocation().getWorld().getName())) {
                    continue;
                }
                if (!player.isFlying()) {
                    continue;
                }
                if ("CREATIVE, SPECTATOR".contains(player.getGameMode().toString())) {
                    continue;
                }
                if (player.hasPermission("fly.nohunger")) {
                    continue;
                }
                if (player.hasPotionEffect(PotionEffectType.SATURATION)) {
                    Bukkit.getScheduler().runTask(INSTANCE, () -> {
                        player.setFlying(false);
                        player.setAllowFlight(false);
                    });
                    Utils.sendFWFMsg(player, FWFMsgType.HasEffect);
                    continue;
                }
                int nowFood = player.getFoodLevel();
                Bukkit.getScheduler().runTask(INSTANCE, () ->
                        player.setFoodLevel(Math.max((nowFood - cost), 0))
                );
                if ((nowFood - cost) < disable) {
                    FWFPlayerBeenDisableFlyEvent event = new FWFPlayerBeenDisableFlyEvent(player);
                    Bukkit.getScheduler().runTask(INSTANCE, () -> {
                        Bukkit.getPluginManager().callEvent(event);
                        player.setAllowFlight(false);
                        player.setFlying(false);
                    });
                    Utils.sendFWFMsg(player, FWFMsgType.CanNotFly);
                    antiFallDamage.add(player);
                }
            }
        }, 0L, 20L);
    }
}
