package com.androidgroup5.onlinecontact;

import android.Manifest;
import android.app.Activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.R;
import com.androidgroup5.onlinecontact.contextDetail.adapter.ContactDetailAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

/**
 * Created by 刘祎锦
 * 读取联系人详细信息的Activity
 */

public class ContactDetailActivity extends Activity {

    protected List<Map<String, Object>> contactDisplay = new ArrayList<Map<String, Object>>(); //手机号（手机号类型、归属地等信息）
    protected ListView lt2;
    protected TextView tv1;
    protected String contactName;
    protected String contactNumber;
    protected int contactId;

    protected Button starButton;
    protected TextView starTextView;
    protected boolean isStarred = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        //User u=((UserParameter)getApplication()).getLocal();
        //u.getContact().get(0).getContactInfos().get(0).getEmailOrNumber();
        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_detail, menu);
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        clearData(); //清除缓存数据
        reInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void init() {
        User u=((UserParameter)getApplication()).getLocal();
        //u.getContact().get(0).getContactInfos().get(0).getEmailOrNumber();
        Contact contactIndex = u.getContact().get(0);//Index

        contactId = contactIndex.getID();//ID
        contactName = contactIndex.getName();//姓名
        contactNumber = contactIndex.getContactInfos().get(0).getNumber();//电话
        //Intent intent = getIntent();
        //rawContactId = (int) intent.getLongExtra("rawContactId", 5364);

        System.out.println("contactId " + contactId);

        displayListView(); //显示listView
        displayStarred(); //设置收藏/未收藏的图标
    }
    public void reInit() {
        User u=((UserParameter)getApplication()).getLocal();
        Contact contactIndex = u.getContact().get(0);//Index
        contactId = contactIndex.getID();//ID
        contactName = contactIndex.getName();//姓名
        contactNumber = contactIndex.getContactInfos().get(0).getNumber();//电话
        //Intent intent = getIntent();
        //rawContactId = (int) intent.getLongExtra("rawContactId", 5364);

        System.out.println("contactId " + contactId);

        displayListView(); //显示listView
        displayStarred(); //设置收藏/未收藏的图标
    }

    public void clearData() {
        contactDisplay.clear();
    }


    //设置lisView布局
    public void displayListView() {
        Map<String, Object> phoneNumMap = new HashMap<>();
        String phoneNumber = contactNumber;
        phoneNumMap.put("phone_png", R.drawable.ic_local_phone_black);
        phoneNumMap.put("phone_num", phoneNumber);
        //phoneNumMap.put("phone_type_id", phoneNumberTypeId);
        //phoneNumMap.put("phone_type", phoneNumberType);
        phoneNumMap.put("phone_location", "北京");
        //phoneNumMap.put("phone_label", phoneNumberLabel);
        phoneNumMap.put("message_png", R.drawable.ic_message_black);
        contactDisplay.add(phoneNumMap);


        tv1 = (TextView) findViewById(R.id.contactName);
        tv1.setText(contactName);
        lt2 = (ListView) findViewById(R.id.list_contact_phone_display);
        if (contactDisplay == null) {
            System.out.println("contactDisplay is null");
        }
        ContactDetailAdapter adapter = new ContactDetailAdapter(contactDisplay, this);
        lt2.setAdapter(adapter);
    }

    //设置收藏/未收藏的图标
    public void displayStarred() {
        starButton = (Button) findViewById(R.id.starButton);
        starTextView = (TextView) findViewById(R.id.starText);
        if (isStarred) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//判断版本号是否大于16
                starButton.setBackground(this.getResources().getDrawable(R.drawable.favorite_icon_normal_dark));
            }
            starTextView.setText("取消收藏");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                starButton.setBackground(this.getResources().getDrawable(R.drawable.unfavorite_icon_normal_dark));
            }
            starTextView.setText("收藏");
        }
    }

    //返回主页面按钮
    public void backToMain(View view) {
        this.finish();
        // Intent intent = new Intent(this, LineActivity.class);
        // startActivity(intent);
    }

    //编辑联系人详细信息
    public void editContactDetail(View view) {
        Intent intent = new Intent(this, EditContactDetailActivity.class);
        intent.putExtra("ContactId", contactId);
        //intent.putExtra("RawContactId", rawContactId);
        //intent.putExtra("hasImage", hasImage);
        //intent.setData(photoUri);
        intent.putExtra("ContactDisplay", (Serializable) contactDisplay);
        intent.putExtra("contactName", contactName);
        startActivity(intent);
    }

    //发送联系人详细信息 这里可以换成RQCode
    public void sendContactDetail(View view) {

    }

    //收藏联系人
    public int starContact(View view) {
        Uri rawContactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactId);
        Uri ContactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactId);
        ContentValues values = new ContentValues();
        int count;
        if (isStarred) {
            values.put(ContactsContract.CommonDataKinds.Phone.STARRED, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                starButton.setBackground(this.getResources().getDrawable(R.drawable.unfavorite_icon_normal_dark));
            }
            starTextView.setText("收藏");
            count = 0;//cr.update(rawContactUri, values, null, null);
            //String Where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND   " + ContactsContract.Data.MIMETYPE + " = ?";
            // cr.update(rawContactUri, values, null, null);
            isStarred = false;
        } else {
            values.put(ContactsContract.CommonDataKinds.Phone.STARRED, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                starButton.setBackground(this.getResources().getDrawable(R.drawable.favorite_icon_normal_dark));
            }
            starTextView.setText("取消收藏");
            count = 0;//cr.update(rawContactUri, values, null, null);
            //String Where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND   " + ContactsContract.Data.MIMETYPE + " = ?";
            //cr.update(rawContactUri, values, null, null);
            isStarred = true;
        }
        return count;
    }
}
