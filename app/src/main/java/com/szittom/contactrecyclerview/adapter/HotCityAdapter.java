package com.szittom.contactrecyclerview.adapter;

import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.szittom.contactrecyclerview.model.HotCityItem;

import java.util.List;

import kale.adapter.CommonAdapter;
import kale.adapter.item.AdapterItem;

public class HotCityAdapter extends CommonAdapter<String> {

    protected HotCityAdapter(@NonNull ObservableList<String> data, int viewTypeCount) {
        super(data, viewTypeCount);
    }

    public HotCityAdapter(@Nullable List<String> data, int viewTypeCount) {
        super(data, viewTypeCount);
    }

    @NonNull
    @Override
    public AdapterItem createItem(Object type) {
        return new HotCityItem();
    }
}
