package me.xpyex.plugin.flywithfood.bukkit;

import me.xpyex.plugin.flywithfood.bukkit.api.FlyWithFoodAPIBK;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.command.FWFCmdExecutor;
import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyWithFoodBukkit extends JavaPlugin {
    private static FlyWithFoodBukkit INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        new FlyWithFood(new FlyWithFoodAPIBK());

        FlyWithFood.getInstance().enable();
    
        INSTANCE.getCommand("FlyWithFood").setExecutor((sender, cmd, label, args) -> {
            FWFCmdExecutor.onCmd(FWFSender.of(sender), label, args);
            return true;
        });

        this.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                FlyWithFoodAPI.USER_MAP.remove(event.getPlayer().getName());
                //
            }

            @EventHandler
            public void onRespawn(PlayerRespawnEvent event) {  //玩家从末地门回主世界也算重生，这样方便
                FlyWithFoodAPI.USER_MAP.remove(event.getPlayer().getName());
                //
            }
        }, FlyWithFoodBukkit.getInstance());
    }

    @Override
    public void onDisable() {
        FlyWithFood.disable();

        INSTANCE = null;
    }
    
    public static FlyWithFoodBukkit getInstance() {
        if (Util.checkNull(INSTANCE)) throw new IllegalStateException(FlyWithFood.PLUGIN_NOT_LOADED_MSG);
        
        return INSTANCE;
    }
}
