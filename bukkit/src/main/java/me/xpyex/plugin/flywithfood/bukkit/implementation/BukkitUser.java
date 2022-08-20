package me.xpyex.plugin.flywithfood.bukkit.implementation;

import me.xpyex.plugin.flywithfood.bukkit.tasks.DisableFly;
import me.xpyex.plugin.flywithfood.bukkit.tasks.EnableFly;
import me.xpyex.plugin.flywithfood.bukkit.tasks.ProtectFromFall;
import me.xpyex.plugin.flywithfood.common.FlyWithFood;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BukkitUser extends BukkitSender implements FWFUser {
    private final Player player;

    public BukkitUser(Player player) {
        super(player);
        this.player = player;
    }

    public BukkitUser(String name) {
        this(Bukkit.getPlayerExact(name));
        //
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

    @Override
    public void sendTitle(String title, String subTitle) {
        if (Util.checkNull(title, subTitle)) return;

        try {
            player.sendTitle(title, subTitle, 10, 70, 20);
        } catch (NoSuchMethodError ignored) {
            try {
                player.sendTitle(title, subTitle);  //该方法已弃用
            } catch (Exception ignored2) {
                FlyWithFood.getInstance().getAPI().getLogger().error("你的服务器不支持发送Title信息，请在配置文件中关闭它");
            }
        }
    }

    @Override
    public void sendActionBar(String actionBar) {
        if (Util.checkNull(actionBar)) return;

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
    }

    @Override
    public String getWorldName() {
        return player.getLocation().getWorld().getName();
        //
    }
}
