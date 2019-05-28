package com.androidgroup5.onlinecontact;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.androidgroup5.onlinecontact.adapter.ContactAdapter;
import com.androidgroup5.onlinecontact.search.CharIndexView;
import com.androidgroup5.onlinecontact.stickyheader.StickyHeaderDecoration;
import com.androidgroup5.onlinecontact.adapter.TestUtils;
import com.androidgroup5.onlinecontact.cn.CNPinyin;
import com.androidgroup5.onlinecontact.cn.CNPinyinFactory;
import com.androidgroup5.onlinecontact.search.sContact;

public class Find extends AppCompatActivity {
    private RecyclerView rv_main;
    private ContactAdapter adapter;

    private CharIndexView iv_main;
    private TextView tv_index;

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
                SearchActivity.lanuch(Find.this, contactList);
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

        getPinyinList();
    }


    private void getPinyinList() {
        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<sContact>>>() {
            @Override
            public void call(Subscriber<? super List<CNPinyin<sContact>>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<CNPinyin<sContact>> contactList = CNPinyinFactory.createCNPinyinList(TestUtils.contactList(Find.this));
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
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

}
