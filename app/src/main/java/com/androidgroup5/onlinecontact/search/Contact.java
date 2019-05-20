package com.androidgroup5.onlinecontact.search;

import com.androidgroup5.onlinecontact.cn.CN;



public class Contact implements CN {

    public final String name;

    public final int imgUrl;

    public Contact(String name, int imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    @Override
    public String chinese() {
        return name;
    }
}
