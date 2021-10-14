package me.xpyex.plugin.flywithfood.common.types;

public enum DenyReason {
    Other(999),
    NoPermission(0),
    HasEffect(1),
    NotEnoughFood(2),
    DisableInThisWorld(3);

    private int denyType;

    DenyReason(int denyType) {
        this.denyType = denyType;
    }
}
