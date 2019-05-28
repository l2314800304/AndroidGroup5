package com.androidgroup5.onlinecontact.adapter;

import android.content.Context;

import com.androidgroup5.onlinecontact.R;
import com.androidgroup5.onlinecontact.search.sContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {

    public static List<sContact> contactList(Context context) {
        List<sContact> contactList = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        String[] names = context.getResources().getStringArray(R.array.names);
        for (int i = 0; i < names.length; i++) {
            int urlIndex = random.nextInt(URLS.length - 1);
            int url = URLS[urlIndex];
            contactList.add(new sContact(names[i], url));
        }
        return contactList;
    }


    static int[] URLS = {R.drawable.header0, R.drawable.header1, R.drawable.header2, R.drawable.header3};

}
