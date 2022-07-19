package me.xpyex.plugin.flywithfood.common.utils;

import java.util.HashMap;
import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.sponge.implementations.SpongeUser;

public class PlayerUtil {
    private static final HashMap<String, FWFUser> USER_MAP_NAME = new HashMap<>();

    public static FWFUser getUser(String name, String serverCore) {
        if (USER_MAP_NAME.containsKey(name)) {  //这个方法要遍历所有在线玩家，所以存一下好，内存换性能
            return USER_MAP_NAME.get(name);
        }
        try {
            FWFUser toBack;
            if (serverCore.equalsIgnoreCase("Bukkit")) {
                toBack = new BukkitUser(name);
            } else if (serverCore.equalsIgnoreCase("Sponge")) {
                toBack = new SpongeUser(name);
            } else {
                throw new IllegalArgumentException("serverCore出错，仅能为Bukkit/Sponge");
            }
            USER_MAP_NAME.put(name, toBack);
            return toBack;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void removeUser(String name) {
        USER_MAP_NAME.remove(name);
        //
    }
}
