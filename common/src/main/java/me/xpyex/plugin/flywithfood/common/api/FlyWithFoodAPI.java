package me.xpyex.plugin.flywithfood.common.api;

import java.util.List;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;

public interface FlyWithFoodAPI {
    public FWFUser getUser(String name);

    public List<FWFUser> getOnlineUsers();

    public FWFLogger getLogger();

    public String getServerSoftware();

    public int getServerMainVer();

    public void registerEnergies();

    public void register_bStats();
}
