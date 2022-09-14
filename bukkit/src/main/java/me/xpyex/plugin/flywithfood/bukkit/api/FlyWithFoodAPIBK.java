package me.xpyex.plugin.flywithfood.bukkit.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFoodBukkit;
import me.xpyex.plugin.flywithfood.bukkit.bstats.Metrics;
import me.xpyex.plugin.flywithfood.bukkit.energy.BukkitDurability;
import me.xpyex.plugin.flywithfood.bukkit.energy.BukkitExpLevel;
import me.xpyex.plugin.flywithfood.bukkit.energy.BukkitExpPoint;
import me.xpyex.plugin.flywithfood.bukkit.energy.BukkitFood;
import me.xpyex.plugin.flywithfood.bukkit.energy.BukkitMoney;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitUser;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class FlyWithFoodAPIBK implements FlyWithFoodAPI {
    private static final String SERVER_SOFTWARE;
    private static final int SERVER_MAIN_VERSION;
    private final FWFLogger LOGGER;

    static {
        String softwareResult;
        try {
            Object MinecraftServer = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());  //此时取到MCServer实例
            String serverName = (String) MinecraftServer.getClass().getMethod("getServerModName").invoke(MinecraftServer);  //调用MCServer类的实例方法getServerModName -> Spigot
            String serverVersion = (String) MinecraftServer.getClass().getMethod("getVersion").invoke(MinecraftServer);  //调用MCServer类的实例方法getVersion -> 1.12.2
            softwareResult = serverName + "-" + serverVersion;  //拼接 -> Spigot-1.12.2
            //该方法CraftBukkit也包含，不会出错罢
            //项目使用SpigotAPI，里面没有MinecraftServer的方法，使用反射获取方法

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            softwareResult = "Unknown-" + Bukkit.getName() + "-" + Bukkit.getBukkitVersion();
        }
        SERVER_SOFTWARE = softwareResult;
        SERVER_MAIN_VERSION = Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1]);
    }

    public FlyWithFoodAPIBK() {
        LOGGER = new FWFLogger(new BukkitSender(Bukkit.getConsoleSender()));
        //
    }

    @Override
    public FWFSender getSender(Object sender) {
        if (!Util.checkNull(sender)) {
            if (sender instanceof Player) {
                return new BukkitUser((Player) sender);
            } else if (sender instanceof CommandSender) {
                return new BukkitSender((CommandSender) sender);
            }
        }
        return null;
    }

    @Override
    public FWFUser getUser(String name) {
        if (!USER_MAP.containsKey(name)) {
            try {
                USER_MAP.put(name, new BukkitUser(name));
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }
        return USER_MAP.get(name);
    }

    @Override
    public List<FWFUser> getOnlineUsers() {
        return Bukkit.getOnlinePlayers().stream().map(BukkitUser::new).collect(Collectors.toList());
        //
    }

    @Override
    public FWFLogger getLogger() {
        return LOGGER;
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
        new BukkitExpPoint().register();

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                BukkitMoney.setEcon(rsp.getProvider());
                new BukkitMoney().register();
                FlyWithFood.getLogger().info("已与Vault挂钩");
                FlyWithFood.getLogger().info("Hooked with Vault successfully");
                FlyWithFood.getLogger().info(" ");
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

        if (FlyWithFood.getInstance().getAPI().getServerMainVer() >= 9) {
            new BukkitDurability().register();
        }
    }

    @Override
    public void register_bStats() {
        long now = System.currentTimeMillis();
        try {
            Metrics metrics = new Metrics(FlyWithFoodBukkit.getInstance(), 15311);
            metrics.addCustomChart(new Metrics.DrilldownPie("game_version", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> entry = new HashMap<>();
                try {
                    entry.put(getServerSoftware(), 1);
                    map.put(getServerSoftware(), entry);
                    if (FWFConfig.CONFIG.isChinese) {
                        FlyWithFood.getLogger().info("与bStats挂钩成功", "共消耗 " + (System.currentTimeMillis() - now) + "ms");
                    } else {
                        FlyWithFood.getLogger().info("Hooked with bStats successfully", "It spent " + (System.currentTimeMillis() - now) + "ms");
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
    public void stopTasks() {
        Bukkit.getScheduler().cancelTasks(FlyWithFoodBukkit.getInstance());
        //
    }
    
    @Override
    public void runTask(Runnable r) {
        Bukkit.getScheduler().runTask(FlyWithFoodBukkit.getInstance(), r);
        //
    }
    
    @Override
    public void runTaskAsync(Runnable r) {
        Bukkit.getScheduler().runTaskAsynchronously(FlyWithFoodBukkit.getInstance(), r);
        //
    }
    
    @Override
    public void runTaskTimerAsync(Runnable r, long waitSeconds, long periodSeconds) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(FlyWithFoodBukkit.getInstance(), r, waitSeconds * 20L, periodSeconds * 20L);
        //
    }

    @Override
    public void runTask(Consumer<?> c) {
        //该方法为Sponge方法，Bukkit侧不实现
    }

    @Override
    public File getDataFolder() {
        return FlyWithFoodBukkit.getInstance().getDataFolder();
        //
    }

    @Override
    public String getPluginVersion() {
        return FlyWithFoodBukkit.getInstance().getDescription().getVersion();
        //
    }

    /*
    从此方法往下，与经验值相关的代码均来源于EssentialsX
    其相关代码开源页面为: https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/com/earth2me/essentials/craftbukkit/SetExpFix.java
     */

    //This method is used to update both the recorded total experience and displayed total experience.
    //We reset both types to prevent issues.
    @Override
    public <P> void setTotalExperience(P p, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        Player player = (Player) p;
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        //This following code is technically redundant now, as bukkit now calulcates levels more or less correctly
        //At larger numbers however... player.getExp(3000), only seems to give 2999, putting the below calculations off.
        int amount = exp;
        while (amount > 0) {
            final int expToLevel = getExpAtLevel(player);
            amount -= expToLevel;
            if (amount >= 0) {
                // give until next level
                player.giveExp(expToLevel);
            } else {
                // give the rest
                amount += expToLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

    @Override
    public <P> int getExpAtLevel(P p) {
        Player player = (Player) p;
        return getExpAtLevel(player.getLevel());
    }

    //new Exp Math from 1.8
    @Override
    public int getExpAtLevel(int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        }
        if (level <= 30) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;

    }

    @Override
    public int getExpToLevel(int level) {
        int currentLevel = 0;
        int exp = 0;

        while (currentLevel < level) {
            exp += getExpAtLevel(currentLevel);
            currentLevel++;
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    //This method is required because the bukkit player.getTotalExperience() method, shows exp that has been 'spent'.
    //Without this people would be able to use exp and then still sell it.
    @Override
    public <P> int getTotalExperience(P p) {
        Player player = (Player) p;
        int exp = Math.round(getExpAtLevel(player) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    @Override
    public <P> int getExpUntilNextLevel(P p) {
        Player player = (Player) p;
        final int exp = Math.round(getExpAtLevel(player) * player.getExp());
        final int nextLevel = player.getLevel();
        return getExpAtLevel(nextLevel) - exp;
    }
}
