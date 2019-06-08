package com.androidgroup5.onlinecontact;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.google.gson.Gson;
import com.androidgroup5.onlinecontact.EntityClass.User;
import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;

import static com.baidu.mapapi.BMapManager.getContext;

public class EditContactDetailActivity extends AppCompatActivity {
    Button btn_backToDetail, btn_update;
    EditText et_contact_number, et_contact_email, et_contact_type;
    User user = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_detail);
        int index = this.getIntent().getExtras().getInt("index");
        user = ((UserParameter) getApplication()).getLocal();
        Contact con = user.getContact().get(index);
        String contact_name = con.getName(),
                contact_id = String.valueOf(con.getID());
        List<ContactInfos> info = con.getContactInfos();
        ContactInfos cin = info.get(0);
        String contact_number = cin.getNumber(),
                contact_type = cin.getType();
        init(contact_name, contact_number, contact_id, index);
    }

    public void init(String contact_name, String contact_number, final String contact_id, final int index) {
        btn_backToDetail = (Button) findViewById(R.id.btn_backToDetail);
        btn_update = (Button) findViewById(R.id.btn_update);
        final EditText et_contact_name = (EditText) findViewById(R.id.et_contact_name);
        et_contact_name.setText(contact_name);
        et_contact_number = (EditText) findViewById(R.id.et_contact_number);
        et_contact_number.setText(contact_number);
        btn_backToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int in1 = index;
                backToDetail(in1);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Toast.makeText(EditContactDetailActivity.this, "修改中...", Toast.LENGTH_LONG).show();
                    String id = contact_id,
                            contact_name = et_contact_name.getText().toString(),
                            contact_number = et_contact_number.getText().toString();
                    UpadteContact(id, contact_name, contact_number,index);
                } else {
                    Toast.makeText(EditContactDetailActivity.this, "修改失败，请检查所填写的信息！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void UpadteContact(String ID, String Name, String Number,int index) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation op1 = ContentProviderOperation
                .newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                        ContactsContract.Data.RAW_CONTACT_ID + "=? and "
                                + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{
                                String.valueOf(ID),
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,// 对应data表中的data1字段
                        Name).build();
        ops.add(op1);

        ContentProviderOperation op2 = ContentProviderOperation
                .newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                        ContactsContract.Data.RAW_CONTACT_ID + "=? and "
                                + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{String.valueOf(ID),
                                ContactsContract.CommonDataKinds.Phone.MIMETYPE})
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,// 对应data表中的data1字段
                        Number).build();
        ops.add(op2);

        ContentProviderOperation op3 = ContentProviderOperation
                .newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                        ContactsContract.Data.RAW_CONTACT_ID + "=? and "
                                + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{String.valueOf(ID),
                                ContactsContract.CommonDataKinds.Phone.MIMETYPE})
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS,// 对应data表中的data1字段
                        "").build();
        ops.add(op3);

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY,
                    ops);
            Toast.makeText(EditContactDetailActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
            backToDetail(index);
        } catch (Exception e) {
            Toast.makeText(EditContactDetailActivity.this, "修改失败。。。", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void backToDetail(int index) {
        startActivity(new Intent().setClass(EditContactDetailActivity.this, ContactDetailActivity.class).putExtra("index", index));
    }

    private boolean checkData() {
        String number = et_contact_number.getText().toString();
        if (number.length() != 11) {
            return false;
        } else {
            return true;
        }
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

