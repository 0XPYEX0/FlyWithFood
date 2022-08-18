package me.xpyex.plugin.flywithfood.bukkit.api;

import java.util.ArrayList;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitSender;
import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitUser;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlyWithFoodAPIBK implements FlyWithFoodAPI {
    private static final String SERVER_SOFTWARE;
    private final FWFLogger logger;
    private static final int SERVER_MAIN_VERSION;

    static {
        String softwareResult;
        int versionResult;
        try {
            Object MinecraftServer = Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());  //此时取到MCServer实例
            String serverName = (String) MinecraftServer.getClass().getMethod("getServerModName").invoke(MinecraftServer);  //调用MCServer类的实例方法getServerModName -> Spigot
            String serverVersion = (String) MinecraftServer.getClass().getMethod("getVersion").invoke(MinecraftServer);  //调用MCServer类的实例方法getVersion -> 1.12.2
            softwareResult = serverName + "-" + serverVersion;  //拼接 -> Spigot-1.12.2
            //该方法CraftBukkit也包含，不会出错罢
            //项目使用SpigotAPI，里面没有MinecraftServer的方法，使用反射获取方法

            versionResult = Integer.parseInt(serverVersion.split("\\.")[1]);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            versionResult = 0;
            softwareResult = "Unknown-" + Bukkit.getBukkitVersion();
        }
        SERVER_SOFTWARE = softwareResult;
        SERVER_MAIN_VERSION = versionResult;
    }

    public FlyWithFoodAPIBK() {
        logger = new FWFLogger(new BukkitSender(Bukkit.getConsoleSender()));
    }

    @Override
    public FWFUser getUser(String name) {
        return new BukkitUser(name);
        //
    }

    @Override
    public FWFUser[] getOnlineUsers() {
        ArrayList<FWFUser> list = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            list.add(new BukkitUser(p));
        }
        return list.toArray(new FWFUser[0]);
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
}
