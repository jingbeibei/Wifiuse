package com.inuc.wifiuse.main.presenter;


import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.Personnel;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.main.model.mainModel;
import com.inuc.wifiuse.main.model.mainModelImple;
import com.inuc.wifiuse.main.view.MainView;

/**
 * Created by 景贝贝 on 2016/6/27.
 */
public class MainPresenterImpl implements MainPresenter, mainModelImple.OnLoadPersonnelListener {

    private MainView mMainView;
    private mainModel mmainModel;

    public MainPresenterImpl(MainView mainView) {
        this.mMainView = mainView;
        this.mmainModel = new mainModelImple();
    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.navigation_item_news:
                mMainView.switch2News();
                break;
            case R.id.navigation_item_images:
                mMainView.switch2Images();
                break;
            case R.id.navigation_item_weather:
                mMainView.switch2Weather();
                break;
            case R.id.navigation_item_about:
                mMainView.switch2About();
                break;
            default:
                mMainView.switch2News();
                break;
        }
    }

    @Override
    public void loadPersonnel(String times, String code, long applicationID, String username) {
        String url = Urls.GetPsersonnelURL + "times=" + times + "&code=" + code + "&applicationID=" + applicationID + "&username=" + username;
        mmainModel.loadPersonnel(url, this);
    }

    @Override
    public void onSuccess(Personnel personnel) {
        mMainView.loadPersonnel(personnel);
    }

    @Override
    public void onFailure(String msg) {
        mMainView.showLoadFailmsg(msg);
    }
}
