package com.inuc.wifiuse.main.widget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.report.widget.ReportListActivity;
import com.inuc.wifiuse.utils.GetTimesAndCode;

/**
 * Created by 景贝贝 on 2016/7/6.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private Button reportBtn;
    private  Button applyBtn;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, null);

        initview();
        initEvent();
        return view;
    }

    private void initEvent() {
        reportBtn.setOnClickListener(this);
        applyBtn.setOnClickListener(this);
    }

    private void initview() {
        reportBtn = (Button) view.findViewById(R.id.report_btn);
        applyBtn= (Button) view.findViewById(R.id.apply_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_btn:
                Intent reportIntent = new Intent(getActivity(), ReportListActivity.class);
               reportIntent.putExtra("flag","1");
                startActivity( reportIntent);

                break;
            case R.id.apply_btn:
                Intent applyIntent = new Intent(getActivity(), ReportListActivity.class);
                applyIntent.putExtra("flag","2");
                startActivity(applyIntent);

                break;

        }

    }
}
