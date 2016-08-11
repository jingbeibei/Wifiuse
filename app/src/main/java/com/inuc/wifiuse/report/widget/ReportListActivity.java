package com.inuc.wifiuse.report.widget;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.report.ReportAdapter;
import com.inuc.wifiuse.report.presenter.ReportPresenter;
import com.inuc.wifiuse.report.presenter.ReportPresenterImple;
import com.inuc.wifiuse.report.view.ReportView;
import com.inuc.wifiuse.utils.ActivityCollector;
import com.inuc.wifiuse.utils.GetTimesAndCode;
import com.inuc.wifiuse.utils.LogUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/6.
 */
public class ReportListActivity extends AppCompatActivity implements ReportView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ReportListActivity";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private List<ReportBean> mData;
    private int pageIndex = 0;
    private ReportAdapter mAdapter;
    private ReportPresenter mReportPresenter;
    private LinearLayoutManager mLayoutManager;
    private TextView BarRightTv;
    private TextView BarTitle;
    private ImageView BackImage;
    FloatingActionButton reportFAB;

    private SharedPreferences pref;
    private long applicationid = 1;
    private String times;
    private String code;
    private String username;
    private int flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportlist);
        ActivityCollector.addActivity(this);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        times = GetTimesAndCode.getTimes();
        code = GetTimesAndCode.getCode(times);
        applicationid = pref.getLong("applicationID", 1);
        username = pref.getString("username", "");
         flag=Integer.parseInt(getIntent().getStringExtra("flag"));

        mReportPresenter = new ReportPresenterImple(this);
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        BarRightTv = (TextView) findViewById(R.id.bar_right_tv);
        BarTitle = (TextView) findViewById(R.id.id_bar_title);
        if (flag==1){
        BarTitle.setText("报告审批");}else {
            BarTitle.setText("来队申请");
        }
        BackImage = (ImageView) findViewById(R.id.id_back_arrow_image);
        reportFAB = (FloatingActionButton) findViewById(R.id.report_fab);

        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.primary_light, R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_View);
        mLayoutManager = new LinearLayoutManager(this);//设置布局管理器,默认垂直
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//增加或删除条目动画

        mAdapter = new ReportAdapter(this);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        onRefresh();
        BarRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportListActivity.this, MangeReportActivity.class);
                startActivity(intent);
            }
        });
        BackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.removeActivity(ReportListActivity.this);
            }
        });
        reportFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //184350  mRecyclerView.setSelected(false);
                Intent intent = new Intent(ReportListActivity.this, ReleaseRepportActivity.class);
                intent.putExtra("flag",flag+"");
                startActivity(intent);
            }
        });
    }

    @Override
    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addReports(List<ReportBean> reportList) {
        mAdapter.isShowFooter(true);
        if (mData == null) {
            mData = new ArrayList<ReportBean>();
        }
        mData.addAll(reportList);
        if (pageIndex == 0) {
            mAdapter.setmDate(mData);
        } else {
            //如果没有更多数据了,则隐藏footer布局
            if (reportList == null || reportList.size() == 0) {
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        pageIndex += Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        if (pageIndex == 0) {
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
//        View view = this == null ? mRecyclerView.getRootView() : findViewById(R.id.drawer_layout);
//        View v=view;
        View v1 = mRecyclerView.getRootView();
//        View v2=findViewById(R.id.drawer_layout);
        Snackbar.make(v1, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        if (mData != null) {
            mData.clear();
        }
        mReportPresenter.loadReport(pageIndex, times, code, applicationid, username, 0);
    }

    private ReportAdapter.OnItemClickListener mOnItemClickListener = new ReportAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ReportBean report = mAdapter.getItem(position);
            Intent intent = new Intent(ReportListActivity.this, ReportItemActivity.class);
            intent.putExtra("report", report);
            intent.putExtra("tijiao", "2");

            startActivity(intent);

        }
    };
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter()) {
                //加载更多
                LogUtils.d(TAG, "loading more data");
                mReportPresenter.loadReport(pageIndex + Urls.PAZE_SIZE, times, code, applicationid, username, 0);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
