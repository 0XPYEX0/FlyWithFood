package me.xpyex.plugin.flywithfood.bukkit.implementation;

import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BukkitUser extends BukkitSender implements FWFUser {
    private final Player player;

    public BukkitUser(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPlayer() {
        return (T) player;
        //
    }

    @Override
    public boolean hasSaturationEff() {
        return player.hasPotionEffect(PotionEffectType.SATURATION);
        //
    }
}
