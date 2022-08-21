package me.xpyex.plugin.flywithfood.sponge7;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import me.xpyex.plugin.flywithfood.sponge7.api.FlyWithFoodAPISponge7;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
    id = "flywithfood-sponge",
    name = "FlyWithFood",
    description = "FlyWithFood",
    authors = {
        "XPYEX"
    },
    version = "1.4.8"
)
public class FlyWithFoodSponge7 {
    private static FlyWithFoodSponge7 INSTANCE;
    @Listener
    public void onGameStart(GameStartedServerEvent event) {
        INSTANCE = this;
        new FlyWithFood(new FlyWithFoodAPISponge7());
        
        FlyWithFood.getInstance().enable();
    }
    public static FlyWithFoodSponge7 getInstance() {
        if (Util.checkNull(INSTANCE)) throw new IllegalStateException("插件尚未加载完成");
        
        return INSTANCE;
    }
    
    @Listener
    public void onGameStop(GameStoppingServerEvent event) {
        FlyWithFood.getInstance().getAPI().stopTasks();
        FlyWithFood.getLogger().info("已取消所有任务");
        FlyWithFood.getLogger().info("插件已卸载");
    }
}