package com.androidgroup5.onlinecontact;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgroup5.onlinecontact.EntityClass.Contact;
import com.androidgroup5.onlinecontact.EntityClass.ContactInfos;

import java.util.ArrayList;

public class Insert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ((Button)findViewById(R.id.btn_backToFind)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Insert.this,Find.class));
            }
        });
        ((Button)findViewById(R.id.btn_insert)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=((EditText)findViewById(R.id.et_contact_name)).getText().toString(),
                        number=((EditText)findViewById(R.id.et_contact_number)).getText().toString();
                if(name.isEmpty()||number.isEmpty()){
                    Toast.makeText(Insert.this,"姓名和号码不能为空！",Toast.LENGTH_LONG).show();
                }else {
                    int id=0;
                    ContentValues values = new ContentValues();
                    Uri rawContactUri = Insert.this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                    long rawContactId = ContentUris.parseId(rawContactUri);
                    id=(int)rawContactId;
                    values.clear();
                    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
                    Insert.this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    values.clear();
                    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
                    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                    Insert.this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    values.clear();
                    Contact contact=new Contact();
                    contact.setName(name);
                    ContactInfos contactInfos=new ContactInfos();
                    contactInfos.setNumber(number);
                    contactInfos.setType("2");
                    contactInfos.setEmailOrNumber(5);
                    contactInfos.setID(0);
                    contact.setID(id);
                    ArrayList<ContactInfos> infos=new ArrayList<>();
                    infos.add(contactInfos);
                    contact.setContactInfos(infos);
                    ((UserParameter)getApplication()).getLocal().getContact().add(contact);
                    Toast.makeText(Insert.this,"添加成功，返回主界面...",Toast.LENGTH_LONG).show();
                    startActivity(new Intent().setClass(Insert.this,Find.class));
                }
            }
        });
    }

}
