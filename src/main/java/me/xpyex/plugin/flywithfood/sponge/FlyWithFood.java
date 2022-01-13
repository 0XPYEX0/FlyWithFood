package me.xpyex.plugin.flywithfood.sponge;

import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;
import me.xpyex.plugin.flywithfood.sponge.commands.FlyCmd;
import me.xpyex.plugin.flywithfood.sponge.config.HandleConfig;
import me.xpyex.plugin.flywithfood.sponge.utils.Utils;
import me.xpyex.plugin.flywithfood.sponge.utils.VersionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Plugin(
        id = "flywithfood-sponge",
        name = "FlyWithFood",
        description = "FlyWithFood",
        authors = {
                "XPYEX"
        },
        version = "1.0.0"
)
public class FlyWithFood {
    public static FlyWithFood INSTANCE;
    public static Logger LOGGER;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        INSTANCE = this;
        LOGGER = LoggerFactory.getLogger("FlyWithFood");
        FlyCmd.registerCmd();
        if (!HandleConfig.loadConfig()) {
            LOGGER.error("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
            LOGGER.error("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
            return;
        }
        if (VersionUtil.getPluginConfigVersion() != VersionUtil.getLocalConfigVersion()) {
            LOGGER.info("本次插件更新修改了配置文件格式，正在备份原文件并转化新文件");
            HandleConfig.updateConfigFile();
            if (!HandleConfig.reloadConfig()) {
                LOGGER.error("载入配置文件出错!插件加载已终止,请检查配置文件，如无法解决请查看后台报错并报告开发者. QQ:1723275529");
                LOGGER.error("若确认是由配置文件错误导致加载出错，可在修改完毕后使用 /fly reload 重载以恢复");
                return;
            }
        }

        HandleConfig.functionWL = HandleConfig.config.getJSONObject("FunctionsWhitelist").getBoolean("Enable");
        HandleConfig.noCostWL = HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getBoolean("Enable");

        startCheck();

        LOGGER.info("成功加载配置文件");
        LOGGER.info("已成功加载!");
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        cancelTasks();
    }

    public static void cancelTasks() {
        for (Task task : Sponge.getScheduler().getScheduledTasks(INSTANCE)) {
            task.cancel();
        }
    }

    public static void startCheck() {
        Task.Builder Scheduler = Task.builder();
        int cost = HandleConfig.config.getInteger("FoodCost"); //每秒消耗的饱食度,20为满,奇数即半格
        int disable = HandleConfig.config.getInteger("FoodDisable"); //饱食度消耗至多少关闭飞行
        Scheduler.execute(() -> {
            boolean FunctionWLEnable = HandleConfig.config.getJSONObject("FunctionsWhitelist").getBoolean("Enable");
            boolean NoCostFoodWLEnable = HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getBoolean("Enable");
            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                if (FunctionWLEnable && !HandleConfig.config.getJSONObject("FunctionsWhitelist").getJSONArray("Worlds").contains(player.getWorld().getName())) {
                    continue;
                }
                if (NoCostFoodWLEnable && HandleConfig.config.getJSONObject("NoCostFoodWhitelist").getJSONArray("Worlds").contains(player.getWorld().getName())) {
                    continue;
                }
                if (!player.get(Keys.IS_FLYING).orElse(false)) {
                    continue;
                }
                if ("CREATIVE, SPECTATOR".toLowerCase().contains(player.gameMode().get().getName().toLowerCase())) {
                    continue;
                }
                if (player.hasPermission("fly.nohunger")) {
                    continue;
                }
                if (Utils.hasPotionEffect(player, PotionEffectTypes.SATURATION)) {
                    player.offer(Keys.CAN_FLY, false);
                    player.offer(Keys.IS_FLYING, false);
                    Utils.sendFWFMsg(player, FWFMsgType.HasEffect);
                    continue;
                }
                int nowFood = player.foodLevel().get();
                player.offer(Keys.FOOD_LEVEL, Math.max((nowFood - cost), 0));
                if ((nowFood - cost) < disable) {
                    player.offer(Keys.CAN_FLY, false);
                    player.offer(Keys.IS_FLYING, false);
                    Utils.sendFWFMsg(player, FWFMsgType.CanNotFly);
                    Scheduler.execute(() ->
                            new FallDamageTimer(player)
                    )
                            .delayTicks(4)
                            .intervalTicks(4)
                            .submit(INSTANCE);
                }
            }
        })
                .interval(1, TimeUnit.SECONDS)
                .delay(0, TimeUnit.SECONDS)
                .submit(INSTANCE);
    }
}

class FallDamageTimer implements Consumer<Task> {
    private final Player player;
    public FallDamageTimer(Player player) {
        this.player = player;
    }
    @Override
    public void accept(Task task) {
        if (!player.isOnline()) {
            task.cancel();
            return;
        }
        if (player.get(Keys.CAN_FLY).orElse(false)) {
            task.cancel();
            return;
        }
        if (player.isOnGround()) {
            task.cancel();
            return;
        }
        player.offer(Keys.FALL_DISTANCE, 0f);
    }
}