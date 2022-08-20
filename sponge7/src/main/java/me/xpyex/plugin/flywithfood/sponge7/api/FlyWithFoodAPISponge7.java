package me.xpyex.plugin.flywithfood.sponge7.api;

import java.util.List;
import me.xpyex.plugin.flywithfood.common.api.FlyWithFoodAPI;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.sponge7.implementation.SpongeUser;
import org.spongepowered.api.Sponge;

public class FlyWithFoodAPISponge7 implements FlyWithFoodAPI {
    @Override
    public FWFUser getUser(String name) {
        return new SpongeUser(Sponge.getServer().getPlayer(name).isPresent() ? Sponge.getServer().getPlayer(name).get() : null);
    }

    @Override
    public List<FWFUser> getOnlineUsers() {
        return null;
    }

    @Override
    public FWFLogger getLogger() {
        return null;
    }

    @Override
    public String getServerSoftware() {
        return null;
    }

    @Override
    public int getServerMainVer() {
        return 0;
    }

    @Override
    public void registerEnergies() {

    }

    @Override
    public void register_bStats() {

    }
}
