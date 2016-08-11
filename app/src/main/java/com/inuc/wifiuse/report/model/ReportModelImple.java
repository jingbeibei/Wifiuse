package com.inuc.wifiuse.report.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.utils.JsonUtils;
import com.inuc.wifiuse.utils.OkHttpUtils;

import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/7.
 */
public class ReportModelImple implements ReportModel{

    /**
     * 获取新闻列表
     * @param url
     * @param listener
     */
    @Override
    public void loadReports(String url, final ReportModelImple.OnLoadReportListListener listener) {

        OkHttpUtils.ResultCallback<String> loadReportCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
//                List<ReportBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
             //   List<ReportBean> reportBeanList= JsonUtils.deserialize(response,new TypeToken< List<ReportBean>>(){}.getType());
                if(response.indexOf("ReportType")>0){
                List<ReportBean> reportBeanList=new Gson().fromJson(response,new TypeToken< List<ReportBean>>(){}.getType());
                    listener.onSuccess( reportBeanList);
                }else {
                    listener.onFailure("load report list failure.");
                }

            }


            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load report list failure.");
            }
        };
        OkHttpUtils.post(url,loadReportCallback,null);
       // OkHttpUtils.get(url, loadReportCallback);
    }

    public interface OnLoadReportListListener {
        void onSuccess(List<ReportBean> list);
        void onFailure(String msg);
    }
}
