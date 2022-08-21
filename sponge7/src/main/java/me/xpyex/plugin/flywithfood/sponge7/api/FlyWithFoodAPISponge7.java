package me.xpyex.plugin.flywithfood.sponge7.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.sponge7.FlyWithFoodSponge7;
import me.xpyex.plugin.flywithfood.sponge7.energies.SpongeExpLevel;
import me.xpyex.plugin.flywithfood.sponge7.energies.SpongeFood;
import me.xpyex.plugin.flywithfood.sponge7.implementation.SpongeSender;
import me.xpyex.plugin.flywithfood.sponge7.implementation.SpongeUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class FlyWithFoodAPISponge7 implements FlyWithFoodAPI {
    private final FWFLogger logger;
    private static final String SERVER_SOFTWARE = "Sponge-1.12";
    private static final int SERVER_MAIN_VERSION = 12;
    
    public FlyWithFoodAPISponge7() {
        this.logger = new FWFLogger(new SpongeSender(Sponge.getServer().getConsole()));
        //
    }
    
    @Override
    public FWFUser getUser(String name) {
        return new SpongeUser(Sponge.getServer().getPlayer(name).isPresent() ? Sponge.getServer().getPlayer(name).get() : null);
        //
    }

    @Override
    public List<FWFUser> getOnlineUsers() {
        List<FWFUser> list = new ArrayList<>();
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            list.add(new SpongeUser(p));
        }
        return list;
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
        new SpongeFood().register();
        new SpongeExpLevel().register();
    }

    @Override
    public void register_bStats() {
        //Sponge的bStats注册在主类，因为需要Inject注解，无法分离
    }
    
    @Override
    public void startCheck() {
        Task.Builder scheduler = Task.builder();
        scheduler.execute(getCheckTask())
            .interval(FWFConfig.CONFIG.howLongCheck, TimeUnit.SECONDS)
            .submit(FlyWithFoodSponge7.getInstance());
    }
    
    @Override
    public void stopTasks() {
        Sponge.getScheduler().getScheduledTasks(FlyWithFoodSponge7.getInstance()).forEach(Task::cancel);
        //
    }
}
