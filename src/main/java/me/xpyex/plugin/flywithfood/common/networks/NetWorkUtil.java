package me.xpyex.plugin.flywithfood.common.networks;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import me.xpyex.plugin.flywithfood.bukkit.FlyWithFood;

public class NetWorkUtil {
    public static String newVer;
    public static String checkUpdate() {
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
            JSONArray array = JSON.parseArray(ba.toString("UTF-8"));
            JSONObject latestVer = array.getJSONObject(0);
            String name = latestVer.getString("name");
            if (!name.equals("v" + FlyWithFood.INSTANCE.getDescription().getVersion())) {
                return name;
            } else {
                newVer = null;
                return null;
            }
        } catch (Exception e) {
            System.out.println("[FlyWithFood] 检查更新失败");
            e.printStackTrace();
        }
        return null;
    }
}
