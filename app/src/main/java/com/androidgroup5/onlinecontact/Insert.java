/**
package com.androidgroup5.onlinecontact;

import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;
import java.util.ArrayList;
import java.util.List;

public class Insert extends AppCompatActivity {
    private TextView contactName;
    private TextView contactEmail;
    private TextView contactMobilePhone;
    private TextView contactTelPhone;
    private TextView contactCorpPhone;
    private TextView contactCorpEmail;
    private Button insert;
    private Button clear;

    private String name;
    private String email;
    private String mobilePhone;
    private String telPhone;
    private String corpPhone;
    private String corpEmail;

    private List<ContactInfos> list = new ArrayList();

    private void init() {
        contactName = (TextView)findViewById(R.id.contactName);
        contactEmail = (TextView)findViewById(R.id.contactEmail);
        contactMobilePhone = (TextView)findViewById(R.id.contactMobilePhone);
        contactTelPhone = (TextView)findViewById(R.id.contactTelPhone);
        contactCorpPhone = (TextView)findViewById(R.id.contactCorpPhone);
        contactCorpEmail = (TextView)findViewById(R.id.contactCorpEmail);
        insert = (Button)findViewById(R.id.btn_insert);
        clear = (Button)findViewById(R.id.btn_clear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        init();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactName.setText("");
                contactEmail.setText("");
                contactMobilePhone.setText("");
                contactTelPhone.setText("");
                contactCorpPhone.setText("");
                contactCorpEmail.setText("");
                showDialog("消息", "清除成功！");

            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = contactName.getText().toString().trim();
                email = contactEmail.getText().toString().trim();
                mobilePhone = contactMobilePhone.getText().toString().trim();
                telPhone = contactTelPhone.getText().toString().trim();
                corpPhone = contactCorpPhone.getText().toString().trim();
                corpEmail = contactCorpEmail.getText().toString().trim();

                judge();

                for(ContactInfos ci : list) {
                    insert(ci.getEmailOrNumber(), ci.getNumber(), ci.getType());
                }
                showDialog("消息","添加成功！");
            }
        });
    }

    private void insert(int emailOrNumber, String number, String type) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation op1 = ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build();// 得到了一个添加内容的对象
        operations.add(op1);

        ContentProviderOperation op2 = ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.Data.DATA1, name).build();// 得到了一个添加内容的对象
        operations.add(op2);

        if(emailOrNumber == 5) {
            ContentProviderOperation op3 = ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, number)
                    .withValue(ContactsContract.Data.DATA2, type)// data2=2即type=2，表示移动电话
                    .build();// 得到了一个添加内容的对象
            operations.add(op3);
        }
        if(emailOrNumber == 7) {
            ContentProviderOperation op4 = ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, number)
                    .withValue(ContactsContract.Data.DATA2, type)// data2=2即type=2，表示工作邮箱
                    .build();// 得到了一个添加内容的对象
            operations.add(op4);
        }

        try {
            getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, operations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化ContactInfos的ArrayList
    private void judge() {

        if(!mobilePhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(5);
            ci.setType("1");
            ci.setNumber(mobilePhone);
            list.add(ci);
        }

        if(!telPhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(5);
            ci.setType("2");
            ci.setNumber(telPhone);
            list.add(ci);
        }

        if(!corpPhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(5);
            ci.setType("3");
            ci.setNumber(corpPhone);
            list.add(ci);
        }

        if(!email.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(7);
            ci.setType("1");
            ci.setNumber(email);
            list.add(ci);
        }

        if(!corpEmail.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(7);
            ci.setType("2");
            ci.setNumber(corpEmail);
            list.add(ci);
        }
    }

    private void showDialog(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}
 **/
