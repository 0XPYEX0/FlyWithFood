package me.xpyex.plugin.flywithfood.common.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import me.xpyex.plugin.flywithfood.common.config.ConfigUtil;

public class NetWorkUtil {
    public static String newVer;
    public static String PLUGIN_VERSION;
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
            JsonArray array = ConfigUtil.GSON.fromJson(ba.toString("UTF-8"), JsonArray.class);
            JsonObject latestVer = array.get(0).getAsJsonObject();
            String name = latestVer.get("name").getAsString();
            if (!name.equals("v" + PLUGIN_VERSION)) {
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
