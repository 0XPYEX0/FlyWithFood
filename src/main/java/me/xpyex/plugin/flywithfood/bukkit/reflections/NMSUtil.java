package me.xpyex.plugin.flywithfood.bukkit.reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;
import me.xpyex.plugin.flywithfood.bukkit.config.HandleConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSUtil {

    /*
     * CraftBukkit类下面的getHandle方法 用于获取NMS对象
     */
    private static final Method getHandleMethod;

    /*
     * NMS的EntityPlayer类中的playerConnection 用于获取PlayerConnection对象
     */
    private static final Field playerConnectionField;

    /*
     * NMS的PlayerConnection类中的sendPacket 用于发包
     */
    private static final Method sendPacketMethod;

    /*
     * NMS的ChatComponentText类中的构造方法 发包的内容
     * ChatComponentText(String)
     */
    private static final Constructor<?> chatComponentTextConstructor;

    /*
     * NMS的PacketPlayOutChatClass类中的构造方法 数据包的构造方法
     * >1.8 PacketPlayOutChat(IChatBaseComponent, ChatMessageType)
     * =1.8 PacketPlayOutChat(IChatBaseComponent, byte)
     * <1.8 PacketPlayOutChat(IChatBaseComponent, int)
     */
    private static Constructor<?> packetPlayOutChatClassConstructor;

    /*
     * NMS的PacketPlayOutChatClass构造方法中的第二个参数
     * ChatMessageType消息类型 在低版本中使用的是byte类型
     */
    private static Object chatMessageType;

    /*
     * 初始化
     */
    static {
        try {
            // 这三个是发包需要的
            Class<?> craftPlayerClass = getClass("org.bukkit.craftbukkit." + NMSAll.NMS_VERSION + ".entity.CraftPlayer");
            getHandleMethod = craftPlayerClass.getMethod("getHandle");
            Class<?> entityPlayerClass = getNMSClass("EntityPlayer");
            playerConnectionField = entityPlayerClass.getField("playerConnection");
            Class<?> playerConnectionClass = getNMSClass("PlayerConnection");
            sendPacketMethod = playerConnectionClass.getMethod("sendPacket", getNMSClass("Packet"));
            // 下面是创建数据包需要的
            Class<?> chatComponentTextClass = getNMSClass("ChatComponentText");
            chatComponentTextConstructor = chatComponentTextClass.getConstructor(String.class);
            Class<?> packetPlayOutChatClass = getNMSClass("PacketPlayOutChat");
            Class<?> iChatBaseComponentClass = getNMSClass("IChatBaseComponent");
            // 这个try只捕获 getNMSClass 方法内的异常 Class Not Found.
            try {
                // 如果有这个类 那么版本 > 1.8
                Class<?> chatMessageTypeClass = getNMSClass("ChatMessageType");
                chatMessageType = chatMessageTypeClass.getField("GAME_INFO").get(null);
                packetPlayOutChatClassConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass);
            } catch (IllegalArgumentException e) {
                // 否则就是 <= 1.8
                try {
                    chatMessageType = (byte) 2;
                    packetPlayOutChatClassConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class);
                } catch (NoSuchMethodException e1) { //1.7
                    chatMessageType = 2;
                    packetPlayOutChatClassConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, int.class);
                }
            }
        } catch (ReflectiveOperationException e) {
            HandleConfig.enableAction = false;
            FlyWithFood.LOGGER.warning("你的服务器不支持发送Action信息！");
            FlyWithFood.LOGGER.warning("请在配置文件禁用Action信息！");
            throw new IllegalStateException(e);
        }
    }

    private NMSUtil() {
        throw new UnsupportedOperationException("此类不允许实例化");
        //
    }

    /**
     * 发送ActionBar信息
     *
     * @param player 玩家
     * @param text   文本
     */
    public static void sendActionBar(Player player, String text) {
        Bukkit.getScheduler().runTaskAsynchronously(FlyWithFood.INSTANCE, () -> {
            try {
                // 正常流程 -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Packet)
                Object packet = generateActionBarPacket(text);
                Object nmsPlayer = getHandleMethod.invoke(player);
                Object playerConnection = playerConnectionField.get(nmsPlayer);
                sendPacketMethod.invoke(playerConnection, packet);
            } catch (Exception e) {
                e.printStackTrace();
                HandleConfig.enableAction = false;
                FlyWithFood.LOGGER.warning("你的服务器不支持发送Action信息！");
                FlyWithFood.LOGGER.warning("请在配置文件禁用Action信息！");
            }
        });
    }

    /**
     * 生成一个ActionBar包
     *
     * @param text 文本
     * @return 数据包
     * @throws ReflectiveOperationException 失败
     */
    public static Object generateActionBarPacket(String text) throws ReflectiveOperationException {
        Object chatComponentText = chatComponentTextConstructor.newInstance(text);
        return packetPlayOutChatClassConstructor.newInstance(chatComponentText, chatMessageType);
    }

    /**
     * 获取一个NMS的类
     *
     * @param className 类名
     * @return Class
     */
    private static Class<?> getNMSClass(String className) {
        return getClass("net.minecraft.server." + NMSAll.NMS_VERSION + "." + className);
    }

    /**
     * 获取类
     *
     * @param name 完整类名
     * @return 类
     */
    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class Not Found.", e);
        }
    }
}
