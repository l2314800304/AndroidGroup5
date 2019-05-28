package com.androidgroup5.onlinecontact;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.adapter.ContactAdapter;
import com.androidgroup5.onlinecontact.search.CharIndexView;
import com.androidgroup5.onlinecontact.stickyheader.StickyHeaderDecoration;
import com.androidgroup5.onlinecontact.cn.CNPinyin;
import com.androidgroup5.onlinecontact.search.sContact;
import com.androidgroup5.onlinecontact.cn.CNPinyinFactory;
import com.google.gson.Gson;

public class Find extends AppCompatActivity {
    private RecyclerView rv_main;
    private ContactAdapter adapter;

    private CharIndexView iv_main;
    private TextView tv_index;
    User user=new User();
    List<sContact> contactLists = new ArrayList<>();
    static int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    Random random = new Random(System.currentTimeMillis());
                    for (int i = 0; i < user.getContact().size(); i++) {
                        int urlIndex = random.nextInt(URLS.length - 1);
                        int url = URLS[urlIndex];
                        contactLists.add(new sContact(user.getContact().get(i).getName(), url));
                    }
                    ///在这里进行对user的所有数据处理与展示
                    subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<sContact>>>() {
                        @Override
                        public void call(Subscriber<? super List<CNPinyin<sContact>>> subscriber) {
                            if (!subscriber.isUnsubscribed()) {
                                List<CNPinyin<sContact>> contactList = CNPinyinFactory.createCNPinyinList(contactLists);
                                Collections.sort(contactList);
                                subscriber.onNext(contactList);
                                subscriber.onCompleted();
                            }
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<CNPinyin<sContact>>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(List<CNPinyin<sContact>> cnPinyins) {
                                    contactList.addAll(cnPinyins);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    break;
            }
        }

    };
    private void getPinyinList(String UserName) {
        HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put("UserName", UserName);
        paramsMap.put("Contact", "[]");
        paramsMap.put("Record", "[]");
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1200, TimeUnit.SECONDS)
                .readTimeout(1200, TimeUnit.SECONDS).build();
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url("http://114.116.171.181:80/SyncAllContactByUserName.ashx")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Find.this,"连接失败",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                user=null;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Toast.makeText(Find.this,"连接成功",Toast.LENGTH_LONG).show();
                    String json = response.body().string();
                    Gson gson = new Gson();
                    user = gson.fromJson(json, User.class);
                    Message message = new Message();
                    message.what = 10;
                    handler.sendMessage(message);
                } else {
                    Toast.makeText(Find.this,"连接失败",Toast.LENGTH_LONG).show();
                    user=null;
                }
            }
        });

    }

    private ArrayList<CNPinyin<sContact>> contactList = new ArrayList<>();
    private Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_find);
        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        iv_main = (CharIndexView) findViewById(R.id.iv_main);
        tv_index = (TextView) findViewById(R.id.tv_index);
        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SearchActivity.lanuch(Find.this, contactList);
            }
        });
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(manager);

        iv_main.setOnCharIndexChangedListener(new CharIndexView.OnCharIndexChangedListener() {
            @Override
            public void onCharIndexChanged(char currentIndex) {
                for (int i=0; i<contactList.size(); i++) {
                    if (contactList.get(i).getFirstChar() == currentIndex) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }

            @Override
            public void onCharIndexSelected(String currentIndex) {
                if (currentIndex == null) {
                    tv_index.setVisibility(View.INVISIBLE);
                } else {
                    tv_index.setVisibility(View.VISIBLE);
                    tv_index.setText(currentIndex);
                }
            }
        });


        adapter = new ContactAdapter(contactList);
        rv_main.setAdapter(adapter);
        rv_main.addItemDecoration(new StickyHeaderDecoration(adapter));
        String UserName = this.getIntent().getExtras().getString("UserName");
        getPinyinList(UserName);
    }




    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

}
