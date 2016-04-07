package com.szittom.contactrecyclerview;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.szittom.contactrecyclerview.adapter.ContactAdapter;
import com.szittom.contactrecyclerview.adapter.expandRecyclerviewadapter.StickyRecyclerHeadersDecoration;
import com.szittom.contactrecyclerview.model.SortModel;
import com.szittom.contactrecyclerview.model.SortToken;
import com.szittom.contactrecyclerview.utils.CharacterParser;
import com.szittom.contactrecyclerview.utils.PinyinComparator;
import com.szittom.contactrecyclerview.widget.DividerDecoration;
import com.szittom.contactrecyclerview.widget.SideBar;
import com.szittom.contactrecyclerview.widget.SmoothTimeManager;
import com.szittom.contactrecyclerview.widget.TouchableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 *
 * http://blog.csdn.net/tyzlmjj/article/details/49227601
 * Android RecyclerView滚动定位
 * http://www.angeldevil.me/2015/09/08/The-smoothScrollToPosition-duration-of-RecyclerView/
 * RecyclerView smoothScrollToPosition的滚动时间
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    SideBar mSideBar;
    TextView mUserDialog;
     TouchableRecyclerView mRecyclerView;

    private List<SortModel> mAllContactsList;
    private PinyinComparator pinyinComparator;
    private ContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSideBar = (SideBar)findViewById(R.id.contact_sidebar);
        mUserDialog = (TextView)findViewById(R.id.contact_dialog);
        mRecyclerView = (TouchableRecyclerView)findViewById(R.id.contact_member);

        pinyinComparator = new PinyinComparator();

        SmoothTimeManager mLinearLayoutManager = new SmoothTimeManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSideBar.setTextView(mUserDialog);
        loadContacts();

    }

    private void loadContacts(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key" },
                            null,
                            null,
                            "sort_key COLLATE LOCALIZED ASC");
                    if (phoneCursor == null || phoneCursor.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    if (phoneCursor.getCount() > 0) {
                        mAllContactsList = new ArrayList<SortModel>();
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                            if (TextUtils.isEmpty(phoneNumber))
                                continue;
                            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                            //System.out.println(sortKey);
                            SortModel sortModel = new SortModel(contactName, phoneNumber);
                            //优先使用系统sortkey取,取不到再使用工具取
//                            String sortLetters = getSortLetterBySortKey(contactName);
//                            if (sortLetters == null) {
//                            }
                            String  sortLetters = getSortLetter(contactName);
                            sortModel.sortLetters = sortLetters;
                            sortModel.sortToken = parseSortKey(contactName);
                            mAllContactsList.add(sortModel);
                        }
                    }
                    phoneCursor.close();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Collections.sort(mAllContactsList, pinyinComparator);
                            mAdapter = new ContactAdapter(mAllContactsList);
                            mAdapter.setSideBar(mSideBar);
                            mAdapter.setRecyclerView(mRecyclerView);
                            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
                            mRecyclerView.addItemDecoration(headersDecor);
                            mRecyclerView.addItemDecoration(new DividerDecoration(MainActivity.this));
                            mRecyclerView.setAdapter(mAdapter);
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

}
