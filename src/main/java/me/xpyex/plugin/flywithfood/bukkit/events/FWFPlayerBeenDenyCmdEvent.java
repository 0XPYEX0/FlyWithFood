package me.xpyex.plugin.flywithfood.bukkit.events;

import me.xpyex.plugin.flywithfood.common.types.DenyReason;
import org.bukkit.entity.Player;

public class FWFPlayerBeenDenyCmdEvent extends FWFEvent {

    DenyReason reason;
    String doWhat;
    String perm;
    Player player;
    public Player getPlayer() {
        return this.player;
    }

    public FWFPlayerBeenDenyCmdEvent(Player player, DenyReason reason, String doWhat) {
        this.player = player;
        this.reason = reason;
        this.doWhat = doWhat;
    }

    public FWFPlayerBeenDenyCmdEvent(Player player, DenyReason reason, String doWhat, String perm) {
        this.player = player;
        if (reason != DenyReason.NoPermission) {
            throw new IllegalArgumentException("DenyReason should not be NoPermission when you put Perm Argument into this method");
        }
        this.reason = reason;
        this.doWhat = doWhat;
        this.perm = perm;
    }

    public DenyReason getReason() {
        return this.reason;
    }

    public String getDoWhat() {
        return this.doWhat;
    }

    public String getPerm() {
        return this.perm != null ? this.perm : "";
    }
}
