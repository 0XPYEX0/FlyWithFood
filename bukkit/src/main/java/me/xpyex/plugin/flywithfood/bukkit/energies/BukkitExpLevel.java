package me.xpyex.plugin.flywithfood.bukkit.energies;

import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.ExpLevelEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitExpLevel extends ExpLevelEnergy {
    @Override
    public void cost(@NotNull FWFUser user, @NotNull Number value) {
        if (value.intValue() == 0) {
            return;
        }
        Player target = user.getPlayer();
        FlyWithFood.getInstance().getAPI().runTask(() ->
                target.setLevel(Math.max(target.getLevel() - value.intValue(), 0))
        );
    }

    @Override
    public @NotNull Number getNow(@NotNull FWFUser user) {
        return user.<Player>getPlayer().getLevel();
        //
    }
}
