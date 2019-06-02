package com.androidgroup5.onlinecontact;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Delete extends AppCompatActivity {
    User u;
    private TextView tv_show;
    private ListView lv_data;
    private Button btn_delete;
    private CheckBox che_all;
    private List<Item> data;
    private MyAdapter myAdapter;

    private void initdata() {

        data = new ArrayList<>();
        for (int i = 0; i < u.getContact().size(); i++) {
            Random random = new Random(System.currentTimeMillis());
            int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};
            int urlIndex = random.nextInt(URLS.length - 1);
            int url = URLS[urlIndex];
            data.add(new Item(u.getContact().get(i).getName(), u.getContact().get(i).getContactInfos().get(0).getNumber(), false, url));
        }
    }

    public List<Item> getData() {
        return this.data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        u = ((UserParameter) getApplication()).getLocal();
        setContentView(R.layout.activity_delete);
        //获取listView
        lv_data = (ListView) findViewById(R.id.lv_data);
        //获取控件
        btn_delete = (Button) findViewById(R.id.btn_delete);
        che_all = (CheckBox) findViewById(R.id.che_all);
        //初始化数据源
        initdata();
        //实例化自定义适配器，把listview传到自定义适配器中
        myAdapter = new MyAdapter(this, lv_data);
        //绑定适配器
        lv_data.setAdapter(myAdapter);
        ((ImageButton) findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete.this.finish();
            }
        });
        //初始化事件监听
        initlistener();
    }

    /**
     * 初始化事件监听方法
     */
    private void initlistener() {
        /**
         * 全选复选框设置事件监听
         */
        che_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (data.size() != 0) {//判断列表中是否有数据
                    if (isChecked) {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setChecked(true);
                        }
                        //通知适配器更新UI
                        myAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setChecked(false);
                        }
                        //通知适配器更新UI
                        myAdapter.notifyDataSetChanged();
                    }
                } else {//若列表中没有数据则隐藏全选复选框
                    che_all.setVisibility(View.GONE);
                }
            }
        });
        //删除按钮点击事件
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建一个要删除内容的集合，不能直接在数据源data集合中直接进行操作，否则会报异常
                List<Item> deleSelect = new ArrayList<Item>();
                //把选中的条目要删除的条目放在deleSelect这个集合中
                User u = ((UserParameter) getApplication()).getLocal();
                List<String> ID=new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getChecked()) {
                        deleSelect.add(data.get(i));
                        ID.add(u.getContact().get(i).getID()+"");
                        int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};
                        u.getContact().remove(i);
                        data.remove(i);
                        i--;
                    }
                }
                ((UserParameter) getApplication()).setLocal(u);
                //判断用户是否选中要删除的数据及是否有数据
                if (ID.size() != 0) {
                    //把全选复选框设置为false
                    che_all.setChecked(false);
                    //通知适配器更新UI
                    myAdapter.notifyDataSetChanged();
                    deleteDB(ID);
                    backToInsert();
                } else if (data.size() == 0) {
                    Toast.makeText(Delete.this, "没有要删除的数据", Toast.LENGTH_SHORT).show();
                } else if (deleSelect.size() == 0) {
                    Toast.makeText(Delete.this, "请选中要删除的数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void deleteDB(List<String> ID){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        // 先删除子表Contacts中的数据
        for(int i=0;i<ID.size();i++){
            ops.add(ContentProviderOperation
                    .newDelete(ContactsContract.Contacts.CONTENT_URI)
                    .withSelection(
                            ContactsContract.Contacts._ID + "=?",
                            new String[] { ID.get(i) })
                    .build());
            // 然后删除子表Data中的数据
            ops.add(ContentProviderOperation
                    .newDelete(ContactsContract.Data.CONTENT_URI)
                    .withSelection(
                            ContactsContract.Data.RAW_CONTACT_ID + "=?",
                            new String[] { ID.get(i) })
                    .build());
            // 最后删除父表RawContacts中的数据
            ops.add(ContentProviderOperation
                    .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                    .withSelection(
                            ContactsContract.RawContacts.CONTACT_ID
                                    + "=?",
                            new String[] { ID.get(i) })
                    .build());
        }
        try {
            getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void backToInsert() {
        startActivity(new Intent().setClass(Delete.this, Find.class));
    }
}

