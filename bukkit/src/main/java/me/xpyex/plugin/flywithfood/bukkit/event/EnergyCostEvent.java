package me.xpyex.plugin.flywithfood.bukkit.event;

import me.xpyex.plugin.flywithfood.bukkit.implementation.BukkitUser;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EnergyCostEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final FWFUser user;
    private boolean cancelled = false;
    private final Number cost;

    public EnergyCostEvent(Player who, @NotNull Number cost) {
        super(who);
        user = new BukkitUser(who);
        this.cost = cost;
    }

    public EnergyCostEvent(FWFUser who, @NotNull Number cost) {
        this(who.<Player>getPlayer(), cost);
        //
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
        //
    }


    /**
     * 获取被扣除能量的用户
     * @return 被扣除能量的用户
     */
    public FWFUser getUser() {
        return user;
        //
    }

    /**
     * 获取扣除能量之前，玩家剩余的能量
     * @return 扣除能量之前，玩家剩余的能量
     */
    public @NotNull Number getBefore() {
        return user.getNow();
        //
    }

    /**
     * 获取扣除能量之后，玩家剩余的能量
     * @return 扣除能量之后，玩家剩余的能量
     */
    public @NotNull Number getAfter() {
        return user.getNow().doubleValue() - getCost().doubleValue();
        //
    }

    /**
     * 获取用户将被扣除多少能量
     * @return 用户将被扣除多少能量
     */
    public @NotNull Number getCost() {
        return cost;
        //
    }

    /**
     * 事件是否被取消，如被取消则不往下执行
     * @return  是否被取消
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
        //
    }

    /**
     * 是否取消事件，如被取消则不往下执行
     * @param cancel true if you wish to cancel this event <br> 若为true则取消事件
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        //
    }
}
