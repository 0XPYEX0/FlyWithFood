package me.xpyex.plugin.flywithfood.sponge7.tasks;

import java.util.function.Consumer;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class ProtectFromFall implements Consumer<Task> {
    private final Player player;

    public ProtectFromFall(Player player) {
        this.player = player;
        //
    }

    @Override
    public void accept(Task task) {
        if (!player.isOnline()) {
            task.cancel();
            return;
        }
        if (player.get(Keys.CAN_FLY).orElse(false)) {
            task.cancel();
            return;
        }
        if (player.isOnGround()) {
            task.cancel();
            return;
        }
        if (player.get(Keys.IS_ELYTRA_FLYING).orElse(false)) {
            task.cancel();
            return;
        }
        player.offer(Keys.FALL_DISTANCE, 0f);
    }

    public void start() {
        FlyWithFood.getInstance().getAPI().runTask(this);
        //
    }
}
