package com.inuc.wifiuse.report;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.utils.JsonUtils;
import com.inuc.wifiuse.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/14.
 */
public class ReportJsonUtils {
    private final static String TAG = "ReportJsonUtils";
    public static List<ReportBean> readJsonNewsBeans(String res, String value) {


        List<ReportBean> beans = new ArrayList<ReportBean>();
      //  List<ReportBean> beans=JsonUtils.
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if(jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                if (!jo.has("imgextra")) {
                    ReportBean news = JsonUtils.deserialize(jo, ReportBean.class);
                    beans.add(news);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "readJsonNewsBeans error" , e);
        }
        return beans;
    }
}
