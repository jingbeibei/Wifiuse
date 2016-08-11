package com.inuc.wifiuse.main.view;

import com.inuc.wifiuse.beans.Personnel;

/**
 * Created by 景贝贝 on 2016/6/27.
 */
public interface MainView {
    void switch2News();
    void switch2Images();
    void switch2Weather();
    void switch2About();
    void showLoadFailmsg(String msg);
    void loadPersonnel(Personnel personnel);
}
