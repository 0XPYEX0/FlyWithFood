package me.xpyex.plugin.flywithfood.common.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import me.xpyex.plugin.flywithfood.common.config.FWFConfig;
import me.xpyex.plugin.flywithfood.common.config.FWFInfo;
import me.xpyex.plugin.flywithfood.common.flyenergy.energies.FoodEnergy;
import me.xpyex.plugin.flywithfood.common.implementation.FWFLogger;
import me.xpyex.plugin.flywithfood.common.implementation.FWFSender;
import me.xpyex.plugin.flywithfood.common.implementation.FWFUser;
import me.xpyex.plugin.flywithfood.common.types.FWFMsgType;

public interface FlyWithFoodAPI {
    public static final HashMap<String, FWFUser> USER_MAP = new HashMap<>();

    public FWFSender getSender(Object sender);

    public FWFUser getUser(String name);

    public List<FWFUser> getOnlineUsers();

    public FWFLogger getLogger();

    public String getServerSoftware();

    public int getServerMainVer();

    public void registerEnergies();

    public void register_bStats();

    public default void startCheck() {
        runTaskTimerAsync(() -> {
            for (FWFUser user : this.getOnlineUsers()) {
                if (!user.needCheck()) continue;  //玩家是否需要被计算飞行

                FWFInfo info = user.getInfo();  //直接存起来稍微节省一点性能?

                if (FWFConfig.CONFIG.checkSaturation) {  //如果要检查“饱和”Buff的话
                    if (info.getEnergy() instanceof FoodEnergy) {
                        if (user.hasSaturationEff()) {  //若玩家拥有“饱和”Buff，则禁止飞行
                            user.disableFly();  //关闭玩家的飞行
                            user.sendFWFMsg(FWFMsgType.HasEffect);
                            continue;
                        }
                    }
                }
                double cost = info.getCost();  //每秒消耗的数值，可为饥饿值或经验值
                double disable = info.getDisable(); //消耗至多少关闭飞行
                double now = info.getEnergy().getNow(user).doubleValue();  //玩家现在的点数
                user.cost(cost);  //扣除数值
                if ((now - cost) < disable) {  //检查扣除后是否足够飞行，否则关闭
                    user.sendFWFMsg(FWFMsgType.CanNotFly);
                    user.disableFly();  //关闭玩家的飞行
                    user.protectFromFall();  //为玩家免疫掉落伤害
                }
            }
        }, 0L, FWFConfig.CONFIG.howLongCheck);
    }
    
    public void stopTasks();
    
    public void runTask(Runnable r);
    
    public void runTaskAsync(Runnable r);
    
    public void runTaskTimerAsync(Runnable r, long waitSeconds, long periodSeconds);

    public void runTask(Consumer<?> c);

    public File getDataFolder();

    public String getPluginVersion();

    public default void checkUpdate() {
        try {
            HttpURLConnection huc = (HttpURLConnection) new URL("https://gitee.com/api/v5/repos/xpyex/FlyWithFood/tags").openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            ByteArrayOutputStream ba = new ByteArrayOutputStream(16384);
            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = huc.getInputStream().read(data, 0, data.length)) != -1) {
                ba.write(data, 0, nRead);
            }
            JsonArray array = FWFConfig.GSON.fromJson(ba.toString("UTF-8"), JsonArray.class);
            JsonObject latestVer = array.get(0).getAsJsonObject();
            String name = latestVer.get("name").getAsString();
            if (!name.equals("v" + getPluginVersion())) {
                getLogger().info("");
                if (FWFConfig.CONFIG.isChinese) {
                    getLogger().info("你当前运行的版本为 v" + getPluginVersion());
                    getLogger().info("找到一个更新的版本: " + name);
                    getLogger().info("前往 https://gitee.com/xpyex/FlyWithFood/releases 下载");
                } else {
                    getLogger().info("You are running FlyWithFood v" + getPluginVersion());
                    getLogger().info("There is a newer version: " + name);
                    getLogger().info("Download it at: https://github.com/0XPYEX0/FlyWithFood/releases");
                }
            } else {
                if (FWFConfig.CONFIG.isChinese) {
                    getLogger().info("当前已是最新版本");
                } else {
                    getLogger().info("You are running the newest FlyWithFood");
                }
            }

        } catch (Exception e) {
            getLogger().error("检查更新失败");
            e.printStackTrace();
        }
    }
}
