package me.xpyex.plugin.flywithfood.sponge.tasks;

import java.util.function.Consumer;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class FallDamageTimer implements Consumer<Task> {
    private final Player player;

    public FallDamageTimer(Player player) {
        this.player = player;
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
        player.offer(Keys.FALL_DISTANCE, 0f);
    }

    public void start() {
        Task.builder()
                .execute(() ->
                        new FallDamageTimer(player)
                )
                .delayTicks(4)
                .intervalTicks(4)
                .submit(FlyWithFood.INSTANCE);
    }
}
