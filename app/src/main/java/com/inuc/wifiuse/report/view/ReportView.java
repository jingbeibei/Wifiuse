package com.inuc.wifiuse.report.view;

import com.inuc.wifiuse.beans.ReportBean;

import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/6.
 */
public  interface ReportView {
    void showProgress();

    void addReports(List<ReportBean> reportList);

    void hideProgress();

    void showLoadFailMsg();
}
