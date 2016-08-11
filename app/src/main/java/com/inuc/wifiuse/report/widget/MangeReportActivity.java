package com.inuc.wifiuse.report.widget;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.utils.ActivityCollector;

public class MangeReportActivity extends AppCompatActivity   {
    private ReportMangeFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mange_report);
        ActivityCollector.addActivity(this);

        pagerAdapter = new ReportMangeFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.report_tabs);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


}
