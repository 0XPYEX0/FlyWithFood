package me.xpyex.plugin.flywithfood.bukkit.implementation;

import me.xpyex.plugin.flywithfood.bukkit.extra.ResidenceHook;
import me.xpyex.plugin.flywithfood.bukkit.tasks.DisableFly;
import me.xpyex.plugin.flywithfood.bukkit.tasks.EnableFly;
import me.xpyex.plugin.flywithfood.bukkit.tasks.ProtectFromFall;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class BukkitUser extends BukkitSender implements FWFUser {

    private final Player player;

    public BukkitUser(Player player) throws IllegalArgumentException {
        super(player);
        this.player = player;
    }

    public BukkitUser(String name) throws IllegalArgumentException {
        this(Bukkit.getPlayerExact(name));
        //
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull T getPlayer() {
        return (T) player;
        //
    }

    @Override
    public boolean hasSaturationEff() {
        return player.hasPotionEffect(PotionEffectType.SATURATION);
        //
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        if (Util.checkEmpty(title + subTitle)) return;

        try {
            player.sendTitle(title, subTitle, 10, 70, 20);
        } catch (NoSuchMethodError ignored) {
            try {
                player.sendTitle(title, subTitle);  //该方法已被Bukkit标记为过时方法
            } catch (Exception ignored2) {
                FlyWithFood.getInstance().getAPI().getLogger().error("你的服务器不支持发送Title信息，请在配置文件中关闭它");
            }
        }
    }

    @Override
    public void sendActionBar(String actionBar) {
        if (Util.checkEmpty(actionBar)) return;

        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
        } catch (Exception ignored) {
            FlyWithFood.getLogger().error("你的服务器不支持发送ActionBar信息，请在配置文件中关闭它");
        }
    }

    @Override
    public boolean canFly() {
        return player.getAllowFlight();
        //
    }

    @Override
    public void enableFly() {
        new EnableFly(this).start();
        //
    }

    @Override
    public void disableFly() {
        new DisableFly(this).start();
        //
    }

    @Override
    public void protectFromFall() {
        new ProtectFromFall(this).start();
        //
    }

    @Override
    @NotNull
    public String getWorldName() {
        return player.getLocation().getWorld().getName();
        //
    }
    
    @Override
    @NotNull
    public String getGameModeName() {
        return player.getGameMode().toString();
        //
    }
    
    @Override
    public boolean isFlying() {
        return player.isFlying();
        //
    }

    @Override
    public boolean isWearingElytra() {
        return player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA;
        //getChestplate可为空 getType必非空
    }

    @Override
    @SuppressWarnings("all")
    public boolean hasAdditionalExempt() {
        if (player.hasPermission("fly.noCost.inLand") && ResidenceHook.isInResidence(player)) {
            return true;  //在领地内飞行，且拥有权限，不扣除
        }
        return false;
    }
}
