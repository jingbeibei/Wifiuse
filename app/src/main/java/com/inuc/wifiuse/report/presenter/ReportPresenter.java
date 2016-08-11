package com.inuc.wifiuse.report.presenter;

/**
 * Created by 景贝贝 on 2016/7/7.
 */
public interface ReportPresenter {

    void loadReport( int pageindex,int pagesize,String times,String code,long applicationID,String username,int type);
}
