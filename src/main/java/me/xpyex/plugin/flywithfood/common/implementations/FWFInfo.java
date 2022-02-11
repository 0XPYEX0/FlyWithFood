package me.xpyex.plugin.flywithfood.common.implementations;

import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.EnergyManager;
import me.xpyex.plugin.flywithfood.common.implementations.flyenergy.FlyEnergy;

public class FWFInfo {
    private final double cost;
    private final double disable;
    private final FlyEnergy energy;

    public FWFInfo(double cost, double disable, String energy) {
        this.cost = cost;
        this.disable = disable;
        this.energy = EnergyManager.getEnergy(energy);
    }

    public double getCost() {
        return this.cost;
    }

    public double getDisable() {
        return this.disable;
    }

    public FlyEnergy getEnergy() {
        return this.energy;
    }
}
