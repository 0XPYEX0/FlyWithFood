package me.xpyex.plugin.flywithfood.sponge7.implementation;

import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.MsgUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

public class SpongeUser extends SpongeSender implements FWFUser {
    private final Player player;

    public SpongeUser(Player player) throws IllegalArgumentException {
        super(player);
        this.player = player;
    }

    public SpongeUser(String name) throws IllegalArgumentException {
        this(Sponge.getServer().getPlayer(name).orElse(null));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPlayer() {
        return (T) player;
        //
    }

    @Override
    public boolean hasSaturationEff() {
        return (player.get(Keys.POTION_EFFECTS).isPresent() && player.get(Keys.POTION_EFFECTS).get().stream().noneMatch(potionEffect ->
                potionEffect.getType() == PotionEffectTypes.SATURATION));
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        player.sendTitle(
                Title.of(
                        Text.of(MsgUtil.getColorMsg(MsgUtil.formatMsg(this, title))),
                        Text.of(MsgUtil.getColorMsg(MsgUtil.formatMsg(this, subTitle)))
                )
        );
    }

    @Override
    public void sendActionBar(String actionBar) {
        player.sendTitle(
                Title.builder().actionBar(
                        Text.of(MsgUtil.getColorMsg(MsgUtil.formatMsg(this, actionBar)))
                ).build());
    }

    @Override
    public void enableFly() {

    }

    @Override
    public void disableFly() {

    }

    @Override
    public void protectFromFall() {

    }

    @Override
    public boolean canFly() {
        return player.get(Keys.CAN_FLY).orElse(false);
        //
    }

    @Override
    public String getWorldName() {
        return player.getWorld().getName();
        //
    }

    @Override
    public String getGameModeName() {
        return player.get(Keys.GAME_MODE).orElse(GameModes.NOT_SET).getName();
        //
    }

    @Override
    public boolean isFlying() {
        return player.get(Keys.IS_FLYING).orElse(false);
        //
    }

    @Override
    public boolean isWearingElytra() {
        return player.getChestplate().orElse(ItemStack.empty()).getType() == ItemTypes.ELYTRA;
        //
    }
}
