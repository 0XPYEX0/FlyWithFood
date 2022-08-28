package me.xpyex.plugin.flywithfood.common.implementation;

public abstract class AbstractFWFSender implements FWFSender {
    @Override
    public final boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (o instanceof FWFUser) {
            return (((FWFUser) o).getName().equals(this.getName()));
        }
        return false;
    }
}
