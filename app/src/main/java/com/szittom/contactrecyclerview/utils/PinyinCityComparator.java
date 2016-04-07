package com.szittom.contactrecyclerview.utils;

import com.szittom.contactrecyclerview.model.CityModel;
import com.szittom.contactrecyclerview.model.SortModel;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinCityComparator implements Comparator<CityModel.CityBean.CityListBean> {

	public int compare(CityModel.CityBean.CityListBean o1, CityModel.CityBean.CityListBean o2) {
		if (o1.getSimpleSpell().equals("@") || o2.getSimpleSpell().equals("#")) {
			return -1;
		} else if (o1.getSimpleSpell().equals("#") || o2.getSimpleSpell().equals("@")) {
			return 1;
		} else {
			return o1.getSimpleSpell().compareTo(o2.getSimpleSpell());
		}
	}

}
