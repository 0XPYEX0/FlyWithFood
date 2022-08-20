package me.xpyex.plugin.flywithfood.sponge7;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "flywithfood-sponge",
        name = "FlyWithFood",
        description = "FlyWithFood",
        authors = {
                "XPYEX"
        },
        version = "1.4.8"
)
public class FlyWithFoodSponge7 {
        public static FlyWithFoodSponge7 INSTANCE;

        @Listener
        public void onGameStart(GameStartedServerEvent event) {
                INSTANCE = this;
        }
}
