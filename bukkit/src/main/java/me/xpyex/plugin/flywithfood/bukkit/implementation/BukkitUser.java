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
    public boolean canFly() {
        return player.getAllowFlight();
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
    @SuppressWarnings("all")
    public boolean hasAdditionalExempt() {
        if (player.hasPermission("fly.noCost.inLand") && ResidenceHook.isInResidence(player)) {
            return true;  //在领地内飞行，且拥有权限，不扣除
        }
        return false;
    }

    @Override
    public boolean isWearingElytra() {
        return player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA;
        //getChestplate可为空 getType必非空
    }

    /*
    从此方法往下，与经验值相关的代码均来源于EssentialsX
    其相关代码开源页面为: https://github.com/EssentialsX/Essentials/blob/2.x/Essentials/src/main/java/com/earth2me/essentials/craftbukkit/SetExpFix.java
     */

    //This method is used to update both the recorded total experience and displayed total experience.
    //We reset both types to prevent issues.
    @Override
    public void setTotalExperience(int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        Player player = getPlayer();
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        //This following code is technically redundant now, as bukkit now calulcates levels more or less correctly
        //At larger numbers however... player.getExp(3000), only seems to give 2999, putting the below calculations off.
        int amount = exp;
        while (amount > 0) {
            final int expToLevel = getExpAtLevel(player.getLevel());
            amount -= expToLevel;
            if (amount >= 0) {
                // give until next level
                player.giveExp(expToLevel);
            } else {
                // give the rest
                amount += expToLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

    @Override
    public int getExpAtLevel() {
        Player player = getPlayer();
        return getExpAtLevel(player.getLevel());
    }

    //new Exp Math from 1.8
    @Override
    public int getExpAtLevel(int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        }
        if (level <= 30) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;

    }

    @Override
    public int getExpToLevel(int level) {
        int currentLevel = 0;
        int exp = 0;

        while (currentLevel < level) {
            exp += getExpAtLevel(currentLevel);
            currentLevel++;
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    //This method is required because the bukkit player.getTotalExperience() method, shows exp that has been 'spent'.
    //Without this people would be able to use exp and then still sell it.
    @Override
    public int getTotalExperience() {
        Player player = getPlayer();
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    @Override
    public int getExpUntilNextLevel() {
        Player player = getPlayer();
        final int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        final int nextLevel = player.getLevel();
        return getExpAtLevel(nextLevel) - exp;
    }
}
