package me.xpyex.plugin.flywithfood.bukkit.extra;

import java.lang.reflect.Method;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResidenceHook {
    private static Method GET_RES_METHOD;
    private static Object RES_MANAGER;
    static {
        try {
            RES_MANAGER = Class.forName("com.bekvon.bukkit.residence.api.ResidenceApi").getMethod("getResidenceManager").invoke(null);
            GET_RES_METHOD = RES_MANAGER.getClass().getMethod("getByLoc", Location.class);
            FlyWithFood.getLogger().info("检测到Residence，已挂钩");
        } catch (ReflectiveOperationException ignored) {
            FlyWithFood.getLogger().info("未检测到Residence，相关功能将不生效");
        }
    }

    public static boolean isInResidence(Player p) {
        if (RES_MANAGER == null || GET_RES_METHOD == null) {
            return false;
        }
        try {
            return GET_RES_METHOD.invoke(RES_MANAGER, p.getLocation()) != null;
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
}
