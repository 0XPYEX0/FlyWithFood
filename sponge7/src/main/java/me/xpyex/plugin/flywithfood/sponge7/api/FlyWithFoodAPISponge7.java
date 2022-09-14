package me.xpyex.plugin.flywithfood.sponge7.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import me.xpyex.plugin.flywithfood.sponge7.FlyWithFoodSponge7;
import me.xpyex.plugin.flywithfood.sponge7.bstats.Metrics;
import me.xpyex.plugin.flywithfood.sponge7.energy.SpongeDurability;
import me.xpyex.plugin.flywithfood.sponge7.energy.SpongeExpLevel;
import me.xpyex.plugin.flywithfood.sponge7.energy.SpongeFood;
import me.xpyex.plugin.flywithfood.sponge7.energy.SpongeMoney;
import me.xpyex.plugin.flywithfood.sponge7.implementation.SpongeSender;
import me.xpyex.plugin.flywithfood.sponge7.implementation.SpongeUser;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;

public class FlyWithFoodAPISponge7 implements FlyWithFoodAPI {
    private final FWFLogger logger;
    private static final String SERVER_SOFTWARE = "Sponge-1.12";
    private static final int SERVER_MAIN_VERSION = 12;
    
    public FlyWithFoodAPISponge7() {
        this.logger = new FWFLogger(new SpongeSender(Sponge.getServer().getConsole()));
        //
    }

    @Override
    public FWFSender getSender(Object sender) {
        if (!Util.checkNull(sender)) {
            if (sender instanceof Player) {
                return new SpongeUser((Player) sender);
            } else if (sender instanceof CommandSource) {
                return new SpongeSender((CommandSource) sender);
            }
        }
        return null;
    }

    @Override
    public FWFUser getUser(String name) {
        if (!USER_MAP.containsKey(name)) {
            try {
                USER_MAP.put(name, new SpongeUser(name));
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        return USER_MAP.get(name);
    }

    @Override
    public List<FWFUser> getOnlineUsers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(SpongeUser::new).collect(Collectors.toList());
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
        new SpongeFood().register();
        new SpongeExpLevel().register();
        new SpongeDurability().register();

        if (Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
            SpongeMoney.setEconomyService(Sponge.getServiceManager().provide(EconomyService.class).get());
            new SpongeMoney().register();
            getLogger().info("已与经济模块挂钩");
            getLogger().info("Hooked with Economy Service successfully");
        }
    }

    @Override
    public void register_bStats() {
        try {
            FlyWithFoodSponge7.getInstance().metrics.addCustomChart(new Metrics.DrilldownPie("game_version", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> entry = new HashMap<>();
                try {
                    entry.put(getServerSoftware(), 1);
                    map.put(getServerSoftware(), entry);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (FWFConfig.CONFIG.isChinese) {
                        getLogger().warning("添加饼状图失败");
                    } else {
                        getLogger().warning("Failed to load bStats");
                    }
                }
                return map;
            }));
            if (FWFConfig.CONFIG.isChinese) {
                getLogger().info("与bStats挂钩成功");
            } else {
                getLogger().info("Hooked with bStats successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (FWFConfig.CONFIG.isChinese) {
                getLogger().warn("与bStats挂钩失败");
            } else {
                getLogger().warn("Failed to hook with bStats");
            }
        }
    }

    @Override
    public void stopTasks() {
        Sponge.getScheduler().getScheduledTasks(FlyWithFoodSponge7.getInstance()).forEach(Task::cancel);
        //
    }

    public Task.Builder getScheduler() {
        return Task.builder();
        //
    }
    
    @Override
    public void runTask(Runnable r) {
        getScheduler().execute(r).submit(FlyWithFoodSponge7.getInstance());
        //
    }
    
    @Override
    public void runTaskAsync(Runnable r) {
        getScheduler().execute(r).async().submit(FlyWithFoodSponge7.getInstance());
        //
    }
    
    @Override
    public void runTaskTimerAsync(Runnable r, long waitSeconds, long periodSeconds) {
        getScheduler().execute(r).interval(periodSeconds, TimeUnit.SECONDS)
            .delay(waitSeconds, TimeUnit.SECONDS).submit(FlyWithFoodSponge7.getInstance());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void runTask(Consumer<?> c) {
        getScheduler().execute((Consumer<Task>) c).submit(FlyWithFoodSponge7.getInstance());
    }

    @Override
    public File getDataFolder() {
        return new File("config/FlyWithFood");
        //
    }

    @Override
    public String getPluginVersion() {
        return Sponge.getPluginManager().getPlugin("FlyWithFood-Sponge".toLowerCase()).get().getVersion().orElse("Unknown");
        //
    }

    @Override
    public <P> void setTotalExperience(P p, int exp) {
        //Sponge侧无法实现
        //
    }

    @Override
    public <P> int getExpAtLevel(P p) {
        return 0;
        //Sponge侧无法实现
    }

    @Override
    public int getExpAtLevel(int level) {
        return 0;
        //Sponge侧无法实现
    }

    @Override
    public int getExpToLevel(int level) {
        return 0;
        //Sponge侧无法实现
    }

    @Override
    public <P> int getTotalExperience(P p) {
        return 0;
        //Sponge侧无法实现
    }

    @Override
    public <P> int getExpUntilNextLevel(P p) {
        return 0;
        //Sponge侧无法实现
    }
}
