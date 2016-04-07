package com.szittom.contactrecyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szittom.contactrecyclerview.R;
import com.szittom.contactrecyclerview.adapter.expandRecyclerviewadapter.StickyRecyclerHeadersAdapter;
import com.szittom.contactrecyclerview.model.CityItem;
import com.szittom.contactrecyclerview.model.CityModel;
import com.szittom.contactrecyclerview.widget.SideBar;

import java.util.List;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

import static android.support.v7.widget.RecyclerView.OnScrollListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class CityListAdapter extends CommonRcvAdapter<CityModel.CityBean.CityListBean>
        implements StickyRecyclerHeadersAdapter<ViewHolder> {

    private int mIndex;
    private boolean move;
    private RecyclerView mRecyclerView;

    public CityListAdapter(@Nullable List<CityModel.CityBean.CityListBean> data) {
        super(data);
    }

    @Override
    public long getHeaderId(int position) {
        return getData().get(position).getSimpleSpell().charAt(0);
    }

    @Override
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_header, parent, false);
        return new ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        String showValue = String.valueOf(getData().get(position).getSimpleSpell().charAt(0));
        if ("#".equals(showValue)) {
            textView.setText("#");
        } else if("@".equals(showValue)){
            textView.setText("定位");
        } else if("%".equals(showValue)){
            textView.setText("热门");
        }else {
            textView.setText(showValue);
        }
    }


    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getData().get(i).getSimpleSpell();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public void setSideBar(SideBar sideBar) {
        if (null != sideBar) {
            sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
                @Override
                public void onTouchingLetterChanged(String s) {
                    int position = getPositionForSection(s.charAt(0));
                    if ("定位".equals(s)){
                        position =  0;
                    }else if("热门".equals(s)) {
                        position = 1;
                    }
                    mIndex = position;
                    if (position != -1) {
                        moveToPosition(position);
                    }
                }
            });
        }
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        if (null != recyclerView) {
            recyclerView.addOnScrollListener(mOnScrollListener);
        }
    }

    private OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动（最后的100米！）
            if (move) {
                move = false;
                LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = mRecyclerView.getChildAt(n).getTop();
                    //最后的移动
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    };

    private void moveToPosition(int n) {
        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }

    }

    @NonNull
    @Override
    public AdapterItem createItem(Object type) {
        return new CityItem();
    }
}
