package com.szittom.contactrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.szittom.contactrecyclerview.adapter.CityListAdapter;
import com.szittom.contactrecyclerview.adapter.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;
import com.szittom.contactrecyclerview.model.CityModel;
import com.szittom.contactrecyclerview.model.SortToken;
import com.szittom.contactrecyclerview.utils.CharacterParser;
import com.szittom.contactrecyclerview.utils.PinyinCityComparator;
import com.szittom.contactrecyclerview.widget.DividerDecoration;
import com.szittom.contactrecyclerview.widget.SideBar;
import com.szittom.contactrecyclerview.widget.TouchableRecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import kale.adapter.CommonRcvAdapter;
import kale.adapter.ExRcvAdapterWrapper;
import kale.adapter.util.rcv.RcvOnItemClickListener;

public class CityRxActvity extends AppCompatActivity {

    private static final String TAG = CityRxActvity.class.getSimpleName();

    SideBar mSideBar;
    TextView mUserDialog;
    TouchableRecyclerView mRecyclerView;

    private List<CityModel.CityBean.CityListBean> mAllCityList;
    private PinyinCityComparator pinyinComparator;
    private CityListAdapter mAdapter;
    private LayoutInflater layoutInflater;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_rx_actvity);

        mSideBar = (SideBar)findViewById(R.id.contact_sidebar);
        mUserDialog = (TextView)findViewById(R.id.contact_dialog);
        mRecyclerView = (TouchableRecyclerView)findViewById(R.id.contact_member);

        layoutInflater = LayoutInflater.from(this);
        pinyinComparator = new PinyinCityComparator();

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSideBar.setTextView(mUserDialog);
        loadContacts();


    }


    private void loadContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsondata = getString(getApplicationContext(), "citylist.json");
                if (jsondata == null) {
                    Log.i(TAG, "Assets failed!");
                    return;
                }
                try {
                    Gson gson = new Gson();
                    CityModel cityModel = gson.fromJson(jsondata, CityModel.class);
                    CityModel.CityBean cityBean = cityModel.getCity();
                    List<CityModel.CityBean.CityListBean> listBeanList = cityBean.getCityList();
                    for (int i = 0; i < listBeanList.size(); i++) {
                        listBeanList.get(i).setSimpleSpell(CharacterParser.getInstance().getSelling(listBeanList.get(i).getName()));
                        listBeanList.get(i).setSortLetter(getSortLetter(listBeanList.get(i).getName()));
                    }
                    CityModel.CityBean.CityListBean listBean1 = new CityModel.CityBean.CityListBean();
                    CityModel.CityBean.CityListBean listBean2 = new CityModel.CityBean.CityListBean();
                    listBean1.setSortLetter("@");
                    listBean1.setSimpleSpell("@");
                    listBean1.setIsHot("1");
                    listBean1.setName("深圳");
                    listBean1.setShotPY("SZ");

                    listBean2.setSortLetter("%");
                    listBean2.setSimpleSpell("%");
                    listBean2.setIsHot("1");
                    listBean2.setName("北京");
                    listBean2.setShotPY("BJ");
                    listBeanList.add(listBean1);
                    listBeanList.add(listBean2);

                    mAllCityList = listBeanList;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Collections.sort(mAllCityList, pinyinComparator);
                            mAdapter = new CityListAdapter(mAllCityList);
                            mAdapter.setSideBar(mSideBar);
                            mAdapter.setRecyclerView(mRecyclerView);
//                            RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.header);
//                            assert header != null;
//                            header.attachTo(mRecyclerView, true);
//                            View view1 = layoutInflater.inflate(R.layout.item_top, null);
//                            mRecyclerView.addHeaderView(view1);
                            final ExRcvAdapterWrapper<CityListAdapter> wrapper = new ExRcvAdapterWrapper<>(mAdapter, mLinearLayoutManager);
//                            wrapper.setHeaderView(view1);

                            /*mRecyclerView.addOnItemTouchListener(new RcvOnItemClickListener(this, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    position = position - wrapper.getHeaderCount();
                                    if (position >= 0) {
                                        data.remove(position);
                                    }
                                }
                            }));*/

                            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
                            mRecyclerView.addItemDecoration(headersDecor);
                            mRecyclerView.addItemDecoration(new DividerDecoration(CityRxActvity.this));
                            mRecyclerView.setAdapter(wrapper);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }).start();

    }

    /**
     * 名字转拼音,取首字母
     *
     * @param name
     * @return
     */
    private String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        //汉字转换成拼音
        String pinyin = CharacterParser.getInstance().getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    /**
     * 取sort_key的首字母
     *
     * @param sortKey
     * @return
     */
    private String getSortLetterBySortKey(String sortKey) {
        if (sortKey == null || "".equals(sortKey.trim())) {
            return null;
        }
        String letter = "#";
        //汉字转换成拼音
        String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    String chReg = "[\\u4E00-\\u9FA5]+";//中文字符串匹配

    //String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配

    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            //其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (String enStr : enStrs) {
                if (enStr.length() > 0) {
                    //拼接简拼
                    token.simpleSpell += enStr.charAt(0);
                    token.wholeSpell += enStr;
                }
            }
        }
        return token;
    }


    /**
     * 读取给定文件名的文件的内容并转换成字符串
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 读取结果
     */
    public static String getString(Context context, String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int number = -1;
            while ((number = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, number);
            }
            bytes = baos.toByteArray();
            inputStream.close();
            return new String(bytes, 0, bytes.length, Charset.defaultCharset().name());
        } catch (IOException e) {
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
    }

}
