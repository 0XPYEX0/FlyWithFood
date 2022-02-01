package me.xpyex.plugin.flywithfood.bukkit.listeners;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import me.xpyex.plugin.flywithfood.bukkit.utils.Utils;
import me.xpyex.plugin.flywithfood.common.networks.NetWorkUtil;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HandleEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("fly.admin")) {
            if (NetWorkUtil.newVer != null) {
                if (HandleConfig.config.isChinese) {
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&b服务器当前运行的FlyWithFood版本为: &fv" + FlyWithFood.INSTANCE.getDescription().getVersion()));
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&a找到一个更新的版本: &f" + NetWorkUtil.newVer));
                    TextComponent msg = new TextComponent(Utils.getColorfulMsg("&e点击打开下载界面"));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://gitee.com/xpyex/FlyWithFood/releases"));
                    event.getPlayer().spigot().sendMessage(msg);
                }
                if (HandleConfig.config.isEnglish) {
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&bThe server is running FlyWithFood &fv" + FlyWithFood.INSTANCE.getDescription().getVersion()));
                    event.getPlayer().sendMessage(Utils.getColorfulMsg("&aThere is a newer version: &f" + NetWorkUtil.newVer));
                    TextComponent msg = new TextComponent(Utils.getColorfulMsg("&eClick me to download it!"));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/0XPYEX0/FlyWithFood/releases"));
                    event.getPlayer().spigot().sendMessage(msg);
                }
            }
        }
    }
}
