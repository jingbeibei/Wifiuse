package com.inuc.wifiuse.report;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.inuc.wifiuse.R;
import com.inuc.wifiuse.beans.ReportBean;

import java.util.List;

/**
 * Created by 景贝贝 on 2016/7/7.
 */
public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ReportBean> mData;
    private boolean mShowFooter = true;
    private Context mContext;
    private static final int TYPE_ITEM = 0;  //普通Item
    private static final int FOOTER_ITEM = 1;  //底部FooterView

    private OnItemClickListener mOnItemClickListener;

    public ReportAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmDate(List<ReportBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_report, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            ReportBean reports = mData.get(position);
            if (reports == null) {
                return;
            }
            ((ItemViewHolder) holder).mTitle.setText(reports.getTitle());
            ((ItemViewHolder) holder).Contents.setText(reports.getContents());
//            Uri uri = Uri.parse(news.getImgsrc());
//            ((ItemViewHolder) holder).mNewsImg.setImageURI(uri);
//            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).mNewsImg, news.getImgsrc());
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (mData == null) {
            return begin;
        }
        return mData.size() + begin;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return FOOTER_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    public ReportBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }
    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;
        public TextView mTitle, Contents, EntryTime, AnswerTime, Instruction, reporttypet;

        public ItemViewHolder(View v) {
            super(v);
//            mTitle = (TextView) v.findViewById(R.id.tvTitle);
//            mDesc = (TextView) v.findViewById(R.id.tvDesc);
//            mNewsImg = (ImageView) v.findViewById(R.id.ivNews);
            mTitle = (TextView) v.findViewById(R.id.repporttitle);
            reporttypet = (TextView) v.findViewById(R.id.reporttypet);//类型
            Contents = (TextView) v.findViewById(R.id.reportcontent);
            EntryTime = (TextView) v.findViewById(R.id.reportEntryTime);
            Instruction = (TextView) v.findViewById(R.id.reportresult);//审批结果
            AnswerTime = (TextView) v.findViewById(R.id.rAnswerTime);
            v.setOnClickListener(this);
        }
      //  很多事情都是很简单的，你应该给我机会让我走进去，你的一个点头或者一个默许都会让你逐渐的走出来，点头默许很容易嘛，你呀就是懒得去想，懒得去做，就想简简单单，什么都不去考虑，
   //喜欢这种感觉，但却模模糊糊，你不放开自己，怎么接受我呢
        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }
}
