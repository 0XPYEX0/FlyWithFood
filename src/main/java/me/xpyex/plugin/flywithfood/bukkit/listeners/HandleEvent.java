package me.xpyex.plugin.flywithfood.bukkit.listeners;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.NetWorkUtil;
import me.xpyex.plugin.flywithfood.common.utils.PlayerUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class HandleEvent implements Listener {
    private static boolean hasSpigotClass;

    static {
        try {
            new TextComponent("检查能不能发Json信息").setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ""));
            hasSpigotClass = true;
        } catch (Exception ignored) { hasSpigotClass = false; }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!hasSpigotClass) {
            return;
        }
        if (event.getPlayer().hasPermission("fly.admin")) {
            if (NetWorkUtil.newVer != null) {
                if (ConfigUtil.CONFIG.isChinese) {
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&b服务器当前运行的FlyWithFood版本为: &fv" + FlyWithFood.INSTANCE.getDescription().getVersion()));
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&a找到一个更新的版本: &f" + NetWorkUtil.newVer));
                    TextComponent msg = new TextComponent(Utils.getColorfulMsg("&e点击打开下载界面"));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://gitee.com/xpyex/FlyWithFood/releases"));
                    event.getPlayer().spigot().sendMessage(msg);
                } else {
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&bThe server is running FlyWithFood &fv" + FlyWithFood.INSTANCE.getDescription().getVersion()));
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&aThere is a newer version: &f" + NetWorkUtil.newVer));
                    TextComponent msg = new TextComponent(Utils.getColorfulMsg("&eClick me to download it!"));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/0XPYEX0/FlyWithFood/releases"));
                    event.getPlayer().spigot().sendMessage(msg);
                }
            }
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerTeleportEvent event) {  //传送事件可阻止(虽然好像没什么用)，且更方便判断所在位置
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {  //当玩家在阅读终末之诗时，玩家的实例会消失，直到阅读完成后创建新的实例，故旧实例无法使用
            PlayerUtil.removeUser(event.getPlayer().getName());
        }
        if (event.getFrom().getWorld() != event.getTo().getWorld()) {  //跨世界传送的情况
            Player player = event.getPlayer();
            if (!player.getAllowFlight()) {
                return;
            }
            FWFUser user = new BukkitUser(player);
            if ("CREATIVE, SPECTATOR".contains(player.getGameMode().toString())) {  //1.7没有旁观者模式，创造模式与旁观者模式没有处理的必要
                return;
            }
            if (user.hasNoCostPerm()) {  //若玩家拥有权限无视消耗，则没有处理的必要
                return;
            }
            if (HandleConfig.functionWL && ConfigUtil.jsonArrayContains(ConfigUtil.CONFIG.functionWL.get("Worlds").getAsJsonArray(), event.getTo().getWorld().getName())) {
                player.setAllowFlight(false);
            }
        }
    }

    @EventHandler
    public void onRespawnEvent(PlayerRespawnEvent event) {
        PlayerUtil.removeUser(event.getPlayer().getName());
        //
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerUtil.removeUser(event.getPlayer().getName());
        //玩家退出服务器后应移除实例
    }
}
