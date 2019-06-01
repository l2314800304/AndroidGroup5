package com.androidgroup5.onlinecontact;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;
import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;


public class Insert extends AppCompatActivity {
    private TextView contactName;
    private TextView contactEmail;
    private TextView contactMobilePhone;
    private TextView contactTelPhone;
    private TextView contactCorpPhone;
    private TextView contactCorpEmail;
    private Button insert;
    private Button clear;

    //EmailOrNumber 0,Email;1,电话


    private String name = contactName.getText().toString().trim();
    private String email = contactEmail.getText().toString().trim();
    private String mobilePhone = contactMobilePhone.getText().toString().trim();
    private String telPhone = contactTelPhone.getText().toString().trim();
    private String corpPhone = contactCorpPhone.getText().toString().trim();
    private String corpEmail = contactCorpEmail.getText().toString().trim();

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

        judge();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactName.setText("");
                contactEmail.setText("");
                contactMobilePhone.setText("");
                contactTelPhone.setText("");
                contactCorpPhone.setText("");
                contactCorpEmail.setText("");
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(ContactInfos ci : list) {
                    insert(ci.getEmailOrNumber(), ci.getNumber(), ci.getType());
                }
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
                .withValue(ContactsContract.Data.DATA1, "小明").build();// 得到了一个添加内容的对象
        operations.add(op2);

        ContentProviderOperation op3 = ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.Data.DATA1, "1233232542")
                .withValue(ContactsContract.Data.DATA2, "2")// data2=2即type=2，表示移动电话
                .build();// 得到了一个添加内容的对象
        operations.add(op3);

        ContentProviderOperation op4 = ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.Data.DATA1, "test@email.com")
                .withValue(ContactsContract.Data.DATA2, "2")// data2=2即type=2，表示工作邮箱
                .build();// 得到了一个添加内容的对象
        operations.add(op4);

        try {
            getContext().getContentResolver().applyBatch("com.android.contacts",
                    operations);// 执行批量操作
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
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
            ci.setEmailOrNumber(1);
            ci.setType("私人座机");
            ci.setNumber(telPhone);
            list.add(ci);
        }

        if(!corpEmail.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(0);
            ci.setType("公司邮箱");
            ci.setNumber(corpEmail);
        }

        if(!corpPhone.isEmpty()) {
            ContactInfos ci = new ContactInfos();
            ci.setEmailOrNumber(1);
            ci.setType("公司电话");
            ci.setNumber(corpPhone);
        }
    }
}
