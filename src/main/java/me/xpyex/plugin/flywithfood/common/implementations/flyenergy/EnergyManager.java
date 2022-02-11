package me.xpyex.plugin.flywithfood.common.implementations.flyenergy;

import java.util.HashMap;
import org.jetbrains.annotations.Nullable;

public class EnergyManager {
    private static final HashMap<String, FlyEnergy> energys = new HashMap<>();

    @Nullable
    public static FlyEnergy getEnergy(String type) {
        return energys.get(type);
        //
    }

    public static void registerEnergy(String type, FlyEnergy energy) {
        energys.put(type, energy);
        //
    }

    public static String[] getEnergys() {
        return energys.keySet().toArray(new String[0]);
        //
    }

    public static boolean hasEnergy(String type) {
        return energys.containsKey(type);
        //
    }
}
