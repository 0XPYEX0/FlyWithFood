package me.xpyex.plugin.flywithfood.common.api;

import java.io.File;
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
    public <T, S extends FWFSender> S getSender(T sender);

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
        
                if (info.getEnergy() instanceof FoodEnergy) {
                    if (user.hasSaturationEff()) {  //若玩家拥有饱和Buff，则禁止飞行
                        user.disableFly();  //关闭玩家的飞行
                        user.sendFWFMsg(FWFMsgType.HasEffect);
                        continue;
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
}
