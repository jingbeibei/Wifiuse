package com.inuc.wifiuse.main.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inuc.wifiuse.beans.Personnel;
import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.report.model.ReportModelImple;
import com.inuc.wifiuse.utils.JsonUtils;
import com.inuc.wifiuse.utils.LogUtils;
import com.inuc.wifiuse.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/30.
 */
public class mainModelImple implements mainModel{

    @Override
    public void loadPersonnel(String url, final mainModelImple.OnLoadPersonnelListener listener) {
        OkHttpUtils.ResultCallback<String> loadPersonnelCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                if(response.indexOf("PictureUrl")>0){
                   Personnel personnel= JsonUtils.deserialize(response,Personnel.class);
                    listener.onSuccess( personnel);
                }else {
                    listener.onFailure("加载个人信息失败..1.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                LogUtils.e("modelImple","加载个人信息失败");
                e.printStackTrace();
                listener.onFailure("加载个人信息失败...");
            }
        };
        OkHttpUtils.post(url,loadPersonnelCallback,null);

    }

    public interface OnLoadPersonnelListener {
        void onSuccess(Personnel personnel);
        void onFailure(String msg);
    }
}
