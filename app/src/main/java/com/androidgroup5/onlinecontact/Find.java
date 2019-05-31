package com.androidgroup5.onlinecontact;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
import com.androidgroup5.onlinecontact.cn.CNPinyinFactory;
import com.androidgroup5.onlinecontact.search.sContact;

public class Find extends Activity {
    private RecyclerView rv_main;
    private ContactAdapter adapter;

    User u;
    private CharIndexView iv_main;
    private TextView tv_index;

    private ArrayList<CNPinyin<sContact>> contactList = new ArrayList<>();
    private Subscription subscription;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    return true;
                case R.id.navigation_record:
                    startActivity(new Intent().setClass(Find.this,CallLogActivity.class));
                    return true;
                case R.id.navigation_sync:
                    startActivity(new Intent().setClass(Find.this,SyncAddressBook.class));
                    return true;
                case R.id.navigation_call:
                    startActivity(new Intent().setClass(Find.this,Phone.class));
                    return true;
                case R.id.navigation_mine:
                    startActivity(new Intent().setClass(Find.this,SkipActivity.class));
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_find);
        u=((UserParameter) getApplication()).getLocal();
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
        ((ImageButton)findViewById(R.id.btn_find_insert)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Find.this, Insert.class));
            }
        });
        ((ImageButton)findViewById(R.id.btn_find_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Find.this, Delete.class));
            }
        });
        ((Button)findViewById(R.id.btn_export)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Find.this, Export.class));
            }
        });
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
                } else {       tv_index.setVisibility(View.VISIBLE);
                    tv_index.setText(currentIndex);
                }
            }
        });
        adapter = new ContactAdapter(contactList,this);
        rv_main.setAdapter(adapter);
        rv_main.addItemDecoration(new StickyHeaderDecoration(adapter));
        getPinyinList();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
    }
    private void getPinyinList() {
        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<sContact>>>() {
            @Override
            public void call(Subscriber<? super List<CNPinyin<sContact>>> subscriber) {

                if (!subscriber.isUnsubscribed()) {
                    List<sContact> contactLists = new ArrayList<>();
                    Random random = new Random(System.currentTimeMillis());
                    int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};
                    for (int i = 0; i < u.getContact().size(); i++) {
                        int urlIndex = random.nextInt(URLS.length - 1);
                        int url = URLS[urlIndex];
                        contactLists.add(new sContact(u.getContact().get(i).getName(),u.getContact().get(i).getContactInfos().get(0).getNumber(), url));
                    }
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
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
