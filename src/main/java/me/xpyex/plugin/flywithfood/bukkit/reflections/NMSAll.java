package me.xpyex.plugin.flywithfood.bukkit.reflections;

import org.bukkit.Bukkit;

public class NMSAll {
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    public static boolean shouldUseNMSAction = false;
}
