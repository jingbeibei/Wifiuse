package com.inuc.wifiuse.report.presenter;

import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.report.model.ReportModel;
import com.inuc.wifiuse.report.model.ReportModelImple;
import com.inuc.wifiuse.report.view.ReportView;
import com.inuc.wifiuse.utils.LogUtils;

import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/7.
 */
public class ReportPresenterImple implements ReportPresenter,ReportModelImple.OnLoadReportListListener{
    private static final String TAG = "ReportPresenterImple";
   private  ReportView mReportView;
    private ReportModel mReportModel;

    public ReportPresenterImple(ReportView mReportView) {
        this.mReportView = mReportView;
        this.mReportModel=new ReportModelImple();
    }

    @Override
    public void loadReport(int pageIndex,int pageSize,String times,String code,long applicationID,String username,int type) {
        String url="";
        if(type==0){
            url= Urls.GetReportsURL+"times="+times+"&code="+code+"&applicationID="+applicationID+"&username="+username+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
        }else{
        url= Urls.GetReportsForAdminURL+"times="+times+"&code="+code+"&applicationID="+applicationID+"&username="+username+"&type="+type+"&pageIndex="+pageIndex+"&pageSize="+pageSize;
        }
        LogUtils.d(TAG, url);
        //只有第一页的或者刷新的时候才显示刷新进度条
        if(pageIndex == 0) {
            mReportView.showProgress();
        }
        mReportModel.loadReports(url,this);
    }

    @Override
    public void onSuccess(List<ReportBean> list) {
       mReportView.hideProgress();
        mReportView.addReports(list);
    }

    @Override
    public void onFailure(String msg) {
       mReportView.hideProgress();
        mReportView.showLoadFailMsg();
    }
}
