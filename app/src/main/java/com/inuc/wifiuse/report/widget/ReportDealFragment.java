package com.inuc.wifiuse.report.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.ReportBean;
import com.inuc.wifiuse.commons.Urls;
import com.inuc.wifiuse.report.ReportAdapter;
import com.inuc.wifiuse.report.presenter.ReportPresenter;
import com.inuc.wifiuse.report.presenter.ReportPresenterImple;
import com.inuc.wifiuse.report.view.ReportView;
import com.inuc.wifiuse.utils.GetTimesAndCode;
import com.inuc.wifiuse.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class ReportDealFragment extends Fragment implements ReportView, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_PARAMETER = "parameter";//fragment传值
    // TODO: Customize parameters
    private int parameter = 1;//不同报告的参数区分
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private List<ReportBean> mData;
    private int pageIndex = 1;
    private ReportAdapter mAdapter;
    private ReportPresenter mReportPresenter;
    private LinearLayoutManager mLayoutManager;

    private SharedPreferences pref;
    private long applicationid = 1;
    private String times;
    private String code;
    private String username;



    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReportDealFragment newInstance(int columnCount) {
        ReportDealFragment fragment = new ReportDealFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAMETER, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref=getActivity().getSharedPreferences("data",getContext().MODE_PRIVATE);
        times= GetTimesAndCode.getTimes();
        code=GetTimesAndCode.getCode(times);
        applicationid=pref.getLong("applicationID",1);
        username=pref.getString("username","");
        if (getArguments() != null) {
            parameter = getArguments().getInt(ARG_PARAMETER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reportdeal_list, container, false);
//
//        TextView textView = (TextView)view.findViewById(R.id.id_test);
//        textView.setText("Fragment #" +mColumnCount);
        mReportPresenter=new ReportPresenterImple(this);
        mSwipeRefreshWidget = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_widget);

        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.primary_light, R.color.colorAccent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_View);
        mLayoutManager = new LinearLayoutManager(getActivity());//设置布局管理器,默认垂直
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//增加或删除条目动画

        mAdapter = new ReportAdapter(getActivity());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        onRefresh();
        return view;
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
        if (pageIndex == 1) {
            mAdapter.setmDate(mData);
        } else {
            //如果没有更多数据了,则隐藏footer布局
            if (reportList == null || reportList.size() == 0) {
                mAdapter.isShowFooter(false);
                Snackbar.make(mRecyclerView, "暂无更多...", Snackbar.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();
        }
        pageIndex +=1;
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        if(pageIndex == 0) {
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
//        View view = this == null ? mRecyclerView.getRootView() : findViewById(R.id.drawer_layout);
//        View v=view;
        View v1= mRecyclerView.getRootView();
//        View v2=findViewById(R.id.drawer_layout);
        Snackbar.make(v1, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        if (mData != null) {
            mData.clear();
        }
        mReportPresenter.loadReport( pageIndex,Urls.PAZE_SIZE,times,code,applicationid,username,parameter);
    }

    private ReportAdapter.OnItemClickListener mOnItemClickListener = new ReportAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ReportBean report = mAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), ReportItemActivity.class);
            intent.putExtra("report", report);
            intent.putExtra("tijiao", "1");

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
              //  LogUtils.d(TAG, "loading more data");
                mReportPresenter.loadReport( pageIndex ,Urls.PAZE_SIZE,times,code,applicationid,username,parameter);
            }
        }
    };



}
