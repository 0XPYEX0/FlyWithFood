package me.xpyex.plugin.flywithfood.bukkit.extra;

import java.lang.reflect.Method;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResidenceHook {
    private static Method GET_RES_METHOD;
    private static Object RES_MANAGER;
    static {
        try {
            RES_MANAGER = Class.forName("com.bekvon.bukkit.residence.api.ResidenceApi").getMethod("getResidenceManager").invoke(null);
            GET_RES_METHOD = RES_MANAGER.getClass().getMethod("getByLoc", Location.class);
        } catch (ReflectiveOperationException ignored) {}
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
