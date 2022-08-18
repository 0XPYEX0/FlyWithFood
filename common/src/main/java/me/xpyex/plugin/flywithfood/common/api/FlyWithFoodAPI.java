package me.xpyex.plugin.flywithfood.common.api;

import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;

public interface FlyWithFoodAPI {
    public FWFUser getUser(String name);

    public FWFUser[] getOnlineUsers();

    public FWFLogger getLogger();

    public String getServerSoftware();

    public int getServerMainVer();
}
