package me.xpyex.plugin.flywithfood.sponge.tasks;

import java.util.function.Consumer;
import me.xpyex.plugin.flywithfood.sponge.FlyWithFood;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class EnableFly implements Consumer<Task> {
    private final Player player;

    public EnableFly(Player player) {
        this.player = player;
        //
    }

    @Override
    public void accept(Task task) {
        player.offer(Keys.CAN_FLY, true);
        //
    }

    public void start() {
        Task.builder()
                .execute(this)
                .delayTicks(4)
                .intervalTicks(4)
                .submit(FlyWithFood.INSTANCE);
    }
}
