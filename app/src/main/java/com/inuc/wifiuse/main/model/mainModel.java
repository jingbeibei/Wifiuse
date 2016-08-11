package com.inuc.wifiuse.main.model;

import com.inuc.wifiuse.report.model.ReportModelImple;

/**
 * Created by 景贝贝 on 2016/7/30.
 */
public interface mainModel {
    void loadPersonnel(String url, mainModelImple.OnLoadPersonnelListener listener);
}
