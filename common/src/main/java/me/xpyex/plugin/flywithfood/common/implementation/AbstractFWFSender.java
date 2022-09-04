package me.xpyex.plugin.flywithfood.common.implementation;

public abstract class AbstractFWFSender implements FWFSender {
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o) {
            return false;
        }
        if (o instanceof FWFSender) {
            return this.getName().equals(((FWFSender) o).getName());
        }
        return false;
    }
}
