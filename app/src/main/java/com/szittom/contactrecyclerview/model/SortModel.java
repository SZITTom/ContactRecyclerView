package com.szittom.contactrecyclerview.model;

public class SortModel extends Contact {


	public SortModel(String name, String number) {
		super(name, number);
	}

	public String sortLetters; //显示数据拼音的首字母

	public SortToken sortToken = new SortToken();


}
