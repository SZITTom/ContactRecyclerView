package com.szittom.contactrecyclerview.model;

import android.view.View;
import android.widget.TextView;

import com.szittom.contactrecyclerview.R;

import kale.adapter.item.AdapterItem;

public class HotCityItem implements AdapterItem<String> {

    TextView mTextView;

    @Override
    public int getLayoutResId() {
        return R.layout.contact_header;
    }

    @Override
    public void bindViews(View root) {
        mTextView = (TextView)root.findViewById(R.id.tv_header);
    }

    @Override
    public void setViews() {
        mTextView.setBackgroundResource(R.drawable.sidebar_background);
        mTextView.setTextColor(0xFFFFFFFF);
    }

    @Override
    public void handleData(String s, int position) {
        mTextView.setText(s);
    }
}
