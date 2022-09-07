package me.xpyex.plugin.flywithfood.common.flyenergy;

import java.util.HashMap;
import org.jetbrains.annotations.Nullable;

public class EnergyManager {
    private static final HashMap<String, FlyEnergy> ENERGIES = new HashMap<>();

    public static @Nullable FlyEnergy getEnergy(String type) {
        return ENERGIES.get(type);
        //
    }

    public static void registerEnergy(String type, FlyEnergy energy) {
        ENERGIES.put(type, energy);
        //
    }

    public static String[] getEnergies() {
        return ENERGIES.keySet().toArray(new String[0]);
        //
    }

    public static boolean hasEnergy(String type) {
        return ENERGIES.containsKey(type);
        //
    }
}