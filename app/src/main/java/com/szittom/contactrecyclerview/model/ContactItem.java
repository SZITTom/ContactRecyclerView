package com.szittom.contactrecyclerview.model;

import android.view.View;
import android.widget.TextView;

import com.szittom.contactrecyclerview.R;

import kale.adapter.item.AdapterItem;

public class ContactItem implements AdapterItem<SortModel> {

    TextView tv_name;
    TextView tv_phone;

    @Override
    public int getLayoutResId() {
        return R.layout.item_contact;
    }

    @Override
    public void bindViews(View root) {
        tv_name = (TextView)root.findViewById(R.id.tv_name);
        tv_phone = (TextView)root.findViewById(R.id.tv_phone);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void handleData(SortModel sortModel, int position) {
        tv_name.setText(sortModel.name);
        tv_phone.setText(sortModel.number);
    }
}
