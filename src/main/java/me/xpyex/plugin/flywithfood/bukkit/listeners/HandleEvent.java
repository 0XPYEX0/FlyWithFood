package me.xpyex.plugin.flywithfood.bukkit.listeners;

import com.google.gson.JsonPrimitive;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.implementations.BukkitUser;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;
import me.xpyex.plugin.flywithfood.common.implementations.FWFUser;
import me.xpyex.plugin.flywithfood.common.utils.NetWorkUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class HandleEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("fly.admin")) {
            if (NetWorkUtil.newVer != null) {
                if (ConfigUtil.CONFIG.isChinese) {
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&b服务器当前运行的FlyWithFood版本为: &fv" + FlyWithFood.INSTANCE.getDescription().getVersion()));
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&a找到一个更新的版本: &f" + NetWorkUtil.newVer));
                    TextComponent msg = new TextComponent(Utils.getColorfulMsg("&e点击打开下载界面"));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://gitee.com/xpyex/FlyWithFood/releases"));
                    event.getPlayer().spigot().sendMessage(msg);
                }
                if (ConfigUtil.CONFIG.isEnglish) {
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
            if (HandleConfig.functionWL && ConfigUtil.CONFIG.functionWL.get("Worlds").getAsJsonArray().contains(new JsonPrimitive(event.getTo().getWorld().getName()))) {
                player.setAllowFlight(false);
            }
        }
    }
}
