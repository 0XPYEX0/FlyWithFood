package me.xpyex.plugin.flywithfood.common.utils;

import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.sponge.implementations.SpongeUser;

public class PlayerUtil {
    public static FWFUser getUser(String name, String serverCore) {
        try {
            if (serverCore.equalsIgnoreCase("Bukkit")) {
                return new BukkitUser(name);
            } else if (serverCore.equalsIgnoreCase("Sponge")) {
                return new SpongeUser(name);
            } else {
                throw new IllegalArgumentException("serverCore出错，仅能为Bukkit/Sponge");
            }
        } catch (NullPointerException e) {
            return null;
        }
    }
}
