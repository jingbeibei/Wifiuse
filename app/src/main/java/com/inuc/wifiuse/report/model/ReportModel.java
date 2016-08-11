package com.inuc.wifiuse.report.model;

/**
 * Created by 景贝贝 on 2016/7/7.
 */
public interface ReportModel {
    void loadReports(String url, ReportModelImple.OnLoadReportListListener listener);
}
