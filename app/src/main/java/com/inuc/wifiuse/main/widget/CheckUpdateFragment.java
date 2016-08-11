package com.inuc.wifiuse.main.widget;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.LastApp;
import com.inuc.wifiuse.beans.Personnel;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.main.receiver.UpdateReceiver;
import com.inuc.wifiuse.utils.GetTimesAndCode;
import com.inuc.wifiuse.utils.JsonUtils;
import com.inuc.wifiuse.utils.LogUtils;
import com.inuc.wifiuse.utils.OkHttpUtils;


public class CheckUpdateFragment extends Fragment {
    private SharedPreferences pref;
    private long applicationid = 1;
    private String times;
    private String code;

    private String myGetLastAppUrl;
    private Button updateBtn;
    OkHttpUtils.ResultCallback<String> GetLastAPPCallback;
    LastApp lastApp;
    UpdateReceiver mUpdateReceiver;
    IntentFilter mIntentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
        times = GetTimesAndCode.getTimes();
        code = GetTimesAndCode.getCode(times);
        applicationid = pref.getLong("applicationID", 1);

        myGetLastAppUrl = Urls.GetLastAPPURL + "times=" + times + "&code=" + code + "&applicationID=" + applicationid;
        GetLastAPPCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                if (response.indexOf("VersionID") > 0) {
                    lastApp = JsonUtils.deserialize(response, LastApp.class);
                    Intent intent=new Intent(UpdateReceiver.UPDATE_ACTION);
                    intent.putExtra("LastApp",lastApp);
                    getActivity().sendBroadcast(intent);
                } else {
                    Snackbar.make(updateBtn, response, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                //  LogUtils.e("modelImple","加载个人信息失败");
                e.printStackTrace();
                Snackbar.make(updateBtn, "网络连接错误", Snackbar.LENGTH_SHORT).show();
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_update, container, false);
        updateBtn = (Button) view.findViewById(R.id.update_button);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.post(myGetLastAppUrl, GetLastAPPCallback,null);

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadcast();
    }

    @Override
    public void onStop() {
        super.onStop();
        unRegisterBroadcast();
    }

    /**
     * 广播注册
     */
    private void registerBroadcast() {
        mUpdateReceiver = new UpdateReceiver(false);
        mIntentFilter = new IntentFilter(UpdateReceiver.UPDATE_ACTION);
        getActivity().registerReceiver(mUpdateReceiver, mIntentFilter);
    }

    /**
     * 广播卸载
     */
    private void unRegisterBroadcast() {
        try {
            getActivity().unregisterReceiver(mUpdateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
