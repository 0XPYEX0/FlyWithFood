package me.xpyex.plugin.flywithfood.bukkit.reflections;

import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSUtil {
    /*
     * 代码来源ActionBarAPI，为了兼容旧版本
     * 其开源地址为 https://github.com/connorlinfoot/ActionBarAPI
     * 非常感谢这位作者
     */
    public static void sendActionBar(Player player, String text) {

        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + NMSAll.nmsVer + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object packet;
            Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".PacketPlayOutChat");
            Class<?> packetClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".Packet");
            if (NMSAll.isOldVer) {
                Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".ChatSerializer");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".IChatBaseComponent");
                Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + text + "\"}"));
                packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".ChatComponentText");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".IChatBaseComponent");
                try {
                    Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + NMSAll.nmsVer + ".ChatMessageType");
                    Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                    Object chatMessageType = null;
                    for (Object obj : chatMessageTypes) {
                        if (obj.toString().equals("GAME_INFO")) {
                            chatMessageType = obj;
                        }
                    }
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(text);
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(chatCompontentText, chatMessageType);
                } catch (ClassNotFoundException cnfe) {
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(text);
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(chatCompontentText, (byte) 2);
                }
            }
            Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
            Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(craftPlayerHandle);
            Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
            HandleConfig.enableAction = false;
            FlyWithFood.logger.warning("你的服务器不支持发送Action信息！");
            FlyWithFood.logger.warning("请在配置文件禁用Action信息！");
        }
    }
}
