package com.inuc.wifiuse.report.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 景贝贝 on 2016/7/24.
 */
public class ReportMangeFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private static final String[] mTitles = {"已审批", "未审批"};

    public ReportMangeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ReportDealFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        /**
         *  重写Adapter的这个方法，使TabLayout可以关联显示ViewPager每一页的Title
         */
        return mTitles[position];
    }

}
