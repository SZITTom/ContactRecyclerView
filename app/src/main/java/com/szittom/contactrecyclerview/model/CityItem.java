package com.szittom.contactrecyclerview.model;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szittom.contactrecyclerview.R;
import com.szittom.contactrecyclerview.adapter.HotCityAdapter;
import com.szittom.contactrecyclerview.widget.NoSlideGridView;

import java.util.ArrayList;
import java.util.List;

import kale.adapter.item.AdapterItem;

/**
 * Created by SZITTom on 2016/4/5.
 */
public class CityItem implements AdapterItem<CityModel.CityBean.CityListBean> {

    TextView tv_name;
    TextView tv_phone;
    LinearLayout ll_contact;
    NoSlideGridView gv_hotcity;

    @Override
    public int getLayoutResId() {
        return R.layout.item_contact;
    }

    @Override
    public void bindViews(View root) {
        tv_name = (TextView)root.findViewById(R.id.tv_name);
        tv_phone = (TextView)root.findViewById(R.id.tv_phone);
        ll_contact = (LinearLayout)root.findViewById(R.id.ll_contact);
        gv_hotcity = (NoSlideGridView)root.findViewById(R.id.gv_hotcity);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void handleData(CityModel.CityBean.CityListBean cityListBean, int position) {
        tv_name.setText(cityListBean.getName());
        tv_phone.setText(cityListBean.getSimpleSpell());
        String showValue = String.valueOf(cityListBean.getSimpleSpell().charAt(0));
        ll_contact.setVisibility(View.VISIBLE);
        gv_hotcity.setVisibility(View.GONE);
        switch (showValue) {
            case "#": //#

                break;
            case "@": //定位

                break;
            case "%": //热门
                ll_contact.setVisibility(View.GONE);
                gv_hotcity.setVisibility(View.VISIBLE);
                List<String> datas = new ArrayList<>();
                datas.add("11");
                datas.add("11");
                datas.add("11");
                datas.add("11");
                datas.add("11");
                HotCityAdapter mAdapter = new HotCityAdapter(datas,1);
                gv_hotcity.setAdapter(mAdapter);
                break;
            default:
                break;
        }
    }
}
