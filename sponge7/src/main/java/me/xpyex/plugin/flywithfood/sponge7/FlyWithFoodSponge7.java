package me.xpyex.plugin.flywithfood.sponge7;

import com.google.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.command.FWFCmdExecutor;
import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import me.xpyex.plugin.flywithfood.sponge7.api.FlyWithFoodAPISponge7;
import me.xpyex.plugin.flywithfood.sponge7.bstats.Metrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Plugin(
    id = "flywithfood-sponge",
    name = "FlyWithFood",
    description = "FlyWithFood",
    authors = {
        "XPYEX"
    },
    version = "2.0.0"
)
public class FlyWithFoodSponge7 {
    private static FlyWithFoodSponge7 INSTANCE;
    @Inject
    private PluginContainer plugin;
    public final Metrics metrics;

    @Inject
    public FlyWithFoodSponge7(Metrics.Factory metricsFactory) {
        int pluginId = 15311;
        metrics = metricsFactory.make(pluginId);
    }

    @Listener
    public void onGameStart(GameStartedServerEvent event) {
        INSTANCE = this;
        new FlyWithFood(new FlyWithFoodAPISponge7());
        
        FlyWithFood.getInstance().enable();

        Sponge.getCommandManager().register(INSTANCE, new CommandCallable() {
            @Override
            public @NotNull CommandResult process(@NotNull CommandSource source, @NotNull String arguments) throws CommandException {
                String[] args;
                if (arguments.trim().isEmpty()) {
                    args = new String[0];
                } else {
                    args = arguments.split(" ");
                }
                FWFCmdExecutor.onCmd(FWFSender.of(source), "fly", args);
                return CommandResult.success();
            }

            @Override
            public @NotNull List<String> getSuggestions(@NotNull CommandSource source, @NotNull String arguments, @Nullable Location<World> targetPosition) throws CommandException {
                return Collections.emptyList();
                //
            }

            @Override
            public boolean testPermission(@NotNull CommandSource source) {
                return true;
                //
            }

            @Override
            public @NotNull Optional<Text> getShortDescription(@NotNull CommandSource source) {
                return Optional.of(Text.of("FlyWithFood的基础命令"));
                //
            }

            @Override
            public @NotNull Optional<Text> getHelp(@NotNull CommandSource source) {
                return Optional.of(Text.of("这是可以帮助的吗"));
                //
            }

            @Override
            public @NotNull Text getUsage(@NotNull CommandSource source) {
                return Text.of("没有Usage");
                //
            }
        }, "flywithfood", "fly", "fwf");
    }

    public static FlyWithFoodSponge7 getInstance() {
        if (Util.checkNull(INSTANCE)) throw new IllegalStateException(FlyWithFood.PLUGIN_NOT_LOADED_MSG);
        
        return INSTANCE;
    }
    
    @Listener
    public void onGameStop(GameStoppingServerEvent event) {
        INSTANCE = null;

        FlyWithFood.disable();
    }

    public static PluginContainer getPlugin() {
        return getInstance().plugin;
        //
    }
}
