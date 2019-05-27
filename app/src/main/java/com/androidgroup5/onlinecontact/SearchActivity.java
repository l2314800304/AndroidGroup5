package com.androidgroup5.onlinecontact;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.shu_search.ClearEditText;
import com.androidgroup5.onlinecontact.shu_search.ConstactUtil;
import com.androidgroup5.onlinecontact.shu_search.PinyinComparator;
import com.androidgroup5.onlinecontact.shu_search.SortAdapter;
import com.androidgroup5.onlinecontact.sortlist.CharacterParser;
import com.androidgroup5.onlinecontact.sortlist.SideBar;
import com.androidgroup5.onlinecontact.sortlist.SortModel;


/**
 * @Description:联系人显示界面
 * @author http://blog.csdn.net/finddreams
 */
public class SearchActivity extends Activity {

    private View mBaseView;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private Map<String, String> callRecords;

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main_contact);
        initView();
        initData();
    }

    private void initView() {
        sideBar = (SideBar) this.findViewById(R.id.sidrbar);
        dialog = (TextView) this.findViewById(R.id.dialog);

        sortListView = (ListView) this.findViewById(R.id.sortlist);

    }

    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                // Toast.makeText(getApplication(),
                // ((SortModel)adapter.getItem(position)).getName(),
                // Toast.LENGTH_SHORT).show();
                String number = callRecords.get(((SortModel) adapter
                        .getItem(position)).getName());
                Toast.makeText(SearchActivity.this, number, 0).show();
            }
        });

        new ConstactAsyncTask().execute(0);

    }

    private class ConstactAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... arg0) {
            int result = -1;
            callRecords = ConstactUtil.getAllCallRecords(SearchActivity.this);
            result = 1;
            return result;
        }
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                List<String> constact = new ArrayList<String>();
                for (Iterator<String> keys = callRecords.keySet().iterator(); keys
                        .hasNext();) {
                    String key = keys.next();
                    constact.add(key);
                }
                String[] names = new String[] {};
                names = constact.toArray(names);
                SourceDateList = filledData(names);

                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapter(SearchActivity.this, SourceDateList);
                sortListView.setAdapter(adapter);

                mClearEditText = (ClearEditText) SearchActivity.this
                        .findViewById(R.id.filter_edit);
                mClearEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View arg0, boolean arg1) {
                        mClearEditText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

                    }
                });
                // 根据输入框输入值的改变来过滤搜索
                mClearEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                        filterData(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
=======
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.androidgroup5.onlinecontact.adapter.SearchAdapter;
import com.androidgroup5.onlinecontact.cn.CNPinyin;
import com.androidgroup5.onlinecontact.cn.CNPinyinIndex;
import com.androidgroup5.onlinecontact.search.sContact;
import com.androidgroup5.onlinecontact.cn.CNPinyinIndexFactory;
import com.androidgroup5.onlinecontact.search.TextViewChangedOnSubscribe;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    /**
     * activity中的根目录
     */
    private View ll_root;
    /**
     * 窗体控件上一次的高度,用于监听键盘弹起
     */
    private int mLastHeight;

    private EditText et_search;

    private RecyclerView rv_main;
    private SearchAdapter adapter;

    private ArrayList<CNPinyin<sContact>> contactList;
    private Subscription subscription;

    public static void lanuch(Context context, ArrayList<CNPinyin<sContact>> contactList) {
        if (contactList == null) return;
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("contactList", contactList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        contactList = (ArrayList<CNPinyin<sContact>>) getIntent().getSerializableExtra("contactList");

        ll_root = findViewById(R.id.ll_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(manager);
        adapter = new SearchAdapter(new ArrayList<CNPinyinIndex<sContact>>());
        rv_main.setAdapter(adapter);

        final View decorView = this.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        decorView.getWindowVisibleDisplayFrame(r);
                        if (mLastHeight != r.bottom) {
                            mLastHeight = r.bottom;
                            ViewGroup.LayoutParams params = ll_root.getLayoutParams();
                            params.height = r.bottom - ll_root.getTop()/*  - statusHeight*/;
                            ll_root.setLayoutParams(params);
                        }
                    }
                });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionView = getLayoutInflater().inflate(R.layout.actionbar_search, null);
        actionView.findViewById(R.id.iv_return).setOnClickListener(this);
        et_search = (EditText) actionView.findViewById(R.id.et_search);
        ActionBar.LayoutParams actionBarParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        bar.setCustomView(actionView, actionBarParams);
        ConfigUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));

        /**
         * 下面是搜索框智能
         */
        TextViewChangedOnSubscribe plateSubscribe = new TextViewChangedOnSubscribe();
        plateSubscribe.addTextViewWatcher(et_search);
        subscription = Observable.create(plateSubscribe).debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .switchMap(new Func1<String, Observable<ArrayList<CNPinyinIndex<sContact>>>>() {
                    @Override
                    public Observable<ArrayList<CNPinyinIndex<sContact>>> call(final String s) {
                        return createObservable(s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribe(new Subscriber<ArrayList<CNPinyinIndex<sContact>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArrayList<CNPinyinIndex<sContact>> cnPinyinIndices) {
                        adapter.setNewDatas(cnPinyinIndices);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    /**
     * 搜索订阅
     * @return
     */
    private Observable<ArrayList<CNPinyinIndex<sContact>>> createObservable(final String keywork) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<CNPinyinIndex<sContact>>>() {
            @Override
            public void call(Subscriber<? super ArrayList<CNPinyinIndex<sContact>>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(CNPinyinIndexFactory.indexList(contactList, keywork));
                }
            }
        });
>>>>>>> origin/master
    }

}
